# Consumer source code  analysis

###### DefaultMQProducer

- private String produceGroup

  Producer group conceptually. Aggregate all producer, it's import for transactional, For non-transactional messages, it does not matter as long as it's unique per process.

- private int maxMessageSize = 4M

  Maximum allowed message in bytes. Compress message body threshold. The threshold is 4k 

- <!--TraceDispather traceDispather-->

  Interface of asynchronous for data 

###### DefaultMQProducerImpl：核心类

注意：DefaultMQProducer没有继承或实现关系,两个对象存在相互引用的关系

```java
# Applications intending to send message
private final DefaultMQProducer defaultMQProducer;
# All topic into topicPushlishTable
private final ConcurrentMap<String/* topic */, TopicPublishInfo> topicPublishInfoTable;
## TODO
private final ArrayList<SendMessageHook> sendMessageHookList;
private final RPCHook rpcHook;
## TODO: async sender thread pool，异步执行时的线程池队列大小，深度为50000
private final BlockingQueue<Runnable> asyncSenderThreadPoolQueue;
# async sender executor
private final ExecutorService defaultAsyncSenderExecutor;
private ExecutorService asyncSenderExecutor;
## TODO
protected BlockingQueue<Runnable> checkRequestQueue;
## TODO
protected ExecutorService checkExecutor;
## TODO
private ServiceState serviceState = ServiceState.CREATE_JUST;
## TODO
private MQClientInstance mQClientFactory;
private int zipCompressLevel;
## TODO
private MQFaultStrategy mqFaultStrategy = new MQFaultStrategy();
```

###### Client status

说明：Set up rocket client and client has status

```java
    /**
     * Service just created,not start
     */
    CREATE_JUST,
    /**
     * Service Running
     */
    RUNNING,
    /**
     * Service shutdown
     */
    SHUTDOWN_ALREADY,
    /**
     * Service Start failure
     */
    START_FAILED;
```

- Client  start process
  - Set client status is failed
  
  - Check client group has exist
  
  - Create or get client: clientid is ip@pid
  
  - Get ClientFactory
  
  - Register client
  
  - Add default topic
  
  - Start client factory
  
  - <!--Start client-->
  
    MQClientInstance start need sync
  
  - 
  
  - 
  
      
  
    

