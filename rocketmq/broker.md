# Broker source code  analysis

###### Broken process message 

Notice: CompletableFuture 使用

```java
// 1. process sync request
SendMessageProcessor.processRequest
// 2. asyncSendMessage
SendMessageProcessor.asyncSendMessage
  // new message ext broker
	MessageExtBrokerInner
  // Message store
  this.brokerController.getMessageStore().putMessage(msgInner);
      // Check store status
			PutMessageStatus checkStoreStatus = this.checkStoreStatus();
			// put message for commit log
			PutMessageResult result = this.commitLog.putMessage(msg);
// 3. handle put message result
SendMessageProcessor.handlePutMessageResult
```

###### CommitLog

commitLog重要数据映射关系

```java
CommitLog:MappedFileQueue:MappedFile = 1:1:n
// MappedFile: 存储在/Users/james/store/commitlog下的文件类似：00000000000000000000
// MappedFileQueue：MappedFile 所在的文件夹，对 MappedFile 进行封装成文件队列
// CommitLog存储在MappedFile中的数据结构是：MessageExt
```

DefaultMessageStore开始存储消息：notice：Spin lock , ReentrantLock ,assert， ReentrantReadWriteLock

```java
// 1. put message
DefaultMessageStore.putMessage();
// 2. commitLog asyncPutMessage
CommitLog.asyncPutMessage();
// 3. append Message: 存储配置 MessageStoreConfig
MappedFile.appendMessage
// 4. appendMessagesInner
MappedFile.appendMessagesInner
// 5. doAppend 
DefaultAppendMessageCallback.doAppend();
// 6. data into disk(sync and async flush)
CommitLog.submitFlushRequest()
// 7. 通过同步刷盘或异步刷盘进入磁盘
```

###### FlushCommitLogService: 数据落盘

```java
// FlushCommitLogService
	//CommitRealTimeService: async flush and enable cache byteBuffer
  //FlushRealTimeService: async flush and disable cache byteBuffer 
  //GroupCommitService: sync flush
// CountDownLatch2
```

notic: Broker start：start flushCommitLogService thread

notic: CountDownLatch2 实现

```java
    public void start() {
        // FlushRealTimeService and GroupCommitService 二选一
        this.flushCommitLogService.start();
        // 如果开启事物消息，则开启刷新
        if (defaultMessageStore.getMessageStoreConfig().isTransientStorePoolEnable()) {
            this.commitLogService.start();
        }
    }
```

###### GroupCommitService: 同步落盘

```java
    class GroupCommitService extends FlushCommitLogService {
        private volatile List<GroupCommitRequest> requestsWrite = new ArrayList<GroupCommitRequest>();
        private volatile List<GroupCommitRequest> requestsRead = new ArrayList<GroupCommitRequest>();

        public synchronized void putRequest(final GroupCommitRequest request) {
            // 请求写入
            synchronized (this.requestsWrite) {
                this.requestsWrite.add(request);
            }
            // 切换读写队列队列
            if (hasNotified.compareAndSet(false, true)) {
                waitPoint.countDown(); // notify
            }
        }

        // 数据交换
        private void swapRequests() {
            List<GroupCommitRequest> tmp = this.requestsWrite;
            this.requestsWrite = this.requestsRead;
            this.requestsRead = tmp;
        }

        private void doCommit() {
            synchronized (this.requestsRead) {
                if (!this.requestsRead.isEmpty()) {
                    for (GroupCommitRequest req : this.requestsRead) {
                        // There may be a message in the next file, so a maximum of
                        // two times the flush
                        boolean flushOK = false;
                        for (int i = 0; i < 2 && !flushOK; i++) {
                            flushOK = CommitLog.this.mappedFileQueue.getFlushedWhere() >= req.getNextOffset();

                            if (!flushOK) {
                                CommitLog.this.mappedFileQueue.flush(0);
                            }
                        }
                        req.wakeupCustomer(flushOK);
                    }
long storeTimestamp = CommitLog.this.mappedFileQueue.getStoreTimestamp();
                    if (storeTimestamp > 0) {          CommitLog.this.defaultMessageStore.getStoreCheckpoint().setPhysicMsgTimestamp(storeTimestamp);
                    }

                    this.requestsRead.clear();
                } else {
                    // Because of individual messages is set to not sync flush, it
                    // will come to this process
                    CommitLog.this.mappedFileQueue.flush(0);
                }
            }
        }

        // 存储线程
        public void run() {
            CommitLog.log.info(this.getServiceName() + " service started");

            while (!this.isStopped()) {
                try {
                    // 切换读写
                    this.waitForRunning(10);
                    this.doCommit();
                } catch (Exception e) {
                 CommitLog.log.warn(this.getServiceName() + " service has exception. ", e);
                }
            }
            // Under normal circumstances shutdown, wait for the arrival of the
            // request, and then flush
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                CommitLog.log.warn("GroupCommitService Exception, ", e);
            }

            synchronized (this) {
                this.swapRequests();
            }

            this.doCommit();

            CommitLog.log.info(this.getServiceName() + " service end");
        }

        @Override
        protected void onWaitEnd() {
            this.swapRequests();
        }

        @Override
        public String getServiceName() {
            return GroupCommitService.class.getSimpleName();
        }

        @Override
        public long getJointime() {
            return 1000 * 60 * 5;
        }
    }
```

