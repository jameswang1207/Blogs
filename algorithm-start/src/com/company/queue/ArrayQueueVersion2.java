package com.company.queue;

import java.util.Arrays;

/**
 * 环形队列
 */
public class ArrayQueueVersion2 {
	private int maxSize;
	private int tail;// 添加数据
	private int header; // 获取数据
	private int[] arr;

	ArrayQueueVersion2(int size) {
		this.maxSize = size;
		this.tail = 0;
		this.header = 0;
		this.arr = new int[size];
	}

	boolean addQueue(int ele) {
		if (isFull()) {
			System.out.println("This queue is full");
			return false;
		}
		// Use circle array for queue
		arr[tail] = ele;
		tail = (tail + 1) % maxSize;
		return true;
	}

	int getEle() {
		if (isEmpty()) {
			System.out.println("This queue is empty");
			throw new RuntimeException("Queue is empty，no data");
		}
		int value = arr[header];
		header = (header + 1) % maxSize;
		return value;
	}

	/**
	 * 重要
	 *
	 * @return
	 */
	boolean isFull() {
		return (this.tail + 1) % maxSize == header;
	}

	/**
	 * 重要
	 *
	 * @return
	 */
	boolean isEmpty() {
		return this.header == this.tail;
	}
}
