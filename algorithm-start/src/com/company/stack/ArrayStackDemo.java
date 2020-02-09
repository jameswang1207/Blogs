package com.company.stack;

import sun.lwawt.macosx.CSystemTray;

import java.util.Arrays;

public class ArrayStackDemo {


}

class ArrayStack {
	private int arr[];
	private int maxSize;
	private int top = -1;

	public ArrayStack(int maxSize) {
		this.maxSize = maxSize;
		this.arr = new int[maxSize - 1];
	}

	void push(int ele) {
		if (isFull()) {
			System.out.print("Stack is full");
			return;
		}
		top++;
		arr[top] = ele;
	}

	boolean isFull() {
		return this.top == maxSize - 1;
	}

	boolean isEmpty() {
		return this.top == -1;
	}

	void pop() {
		if (isEmpty()) {
			System.out.print("Stack is empty");
			return;
		}
		int value = arr[top];
		top--;
		System.out.println(value);
	}

	void list() {
		if (isEmpty()) {
			System.out.print("Stack is empty");
			return;
		}
		Arrays.stream(arr).forEach(ele -> {
			System.out.print(ele);
		});
	}

	//数字越大，则优先级就越高.
	public int priority(int operation) {
		if (operation == '*' || operation == '/') {
			return 1;
		} else if (operation == '+' || operation == '-') {
			return 0;
		} else {
			return -1; // 假定目前的表达式只有 +, - , * , /
		}
	}

	//判断是不是一个运算符
	public boolean isOper(char val) {
		return val == '+' || val == '-' || val == '*' || val == '/';
	}

	public int cal(int num1, int num2, int oper) {
		int res = 0; // res 用于存放计算的结果
		switch (oper) {
			case '+':
				res = num1 + num2;
				break;
			case '-':
				res = num2 - num1;
				break;
			case '*':
				res = num1 * num2;
				break;
			case '/':
				res = num2 / num1;
				break;
			default:
				break;
		}
		return res;
	}
}
