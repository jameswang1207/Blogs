package com.company.queue;

import java.util.Arrays;

/**
 * 单向队列
 */
public class ArrayQueueVersion1 {
	private int maxSize;
	private int tail;// 添加数据
	private int header; // 获取数据
	private int[] arr;

	ArrayQueueVersion1(int size) {
		this.maxSize = size;
		this.tail = -1;
		this.header = -1;
		this.arr = new int[size];
	}

	boolean addQueue(int ele) {
		if (isFull()) {
			System.out.println("This queue is full");
			return false;
		}
		this.tail++;
		arr[tail] = ele;
		return true;
	}

	int getEle() {
		if (isEmpty()) {
			System.out.println("This queue is empty");
			throw new RuntimeException("Queue is empty，no data");
		}
		this.header++;
		return arr[header];
	}

	boolean isFull() {
		return this.tail == maxSize - 1;
	}

	boolean isEmpty() {
		return this.header == this.tail;
	}

	public void showQueue() {
		if (isEmpty()) {
			System.out.println("This queue is empty");
			return;
		}
		Arrays.stream(arr).forEach(ele -> {
			System.out.println(ele);
		});
	}
}
