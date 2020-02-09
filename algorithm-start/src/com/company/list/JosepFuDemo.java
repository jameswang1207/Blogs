package com.company.list;

public class JosepFuDemo {

	public static void main(String[] args) {
		CircleSingleLinkedList list = new CircleSingleLinkedList();
		list.insert(10);
		list.showBody();

	}
}

class CircleSingleLinkedList {
	Boy first = null;
	void insert(int boyNumber) {
		Boy curBoy = null;
		for (int i = 1; i < boyNumber; i++) {
			Boy boy = new Boy(i);
			if (i == 1) {
				first = boy;
				boy.next = first;
				curBoy = first;
			} else {
				curBoy.next = boy;
				boy.next = first;
				curBoy = boy;
			}
		}
	}

	void showBody() {
		if (first == null) {
			System.out.println("No child");
			return;
		}
		Boy curBoy = first;
		while (true) {
			System.out.println(curBoy);
			if (curBoy.next == first) {
				break;
			}
			curBoy = curBoy.next;
		}
	}
}

class Boy {
	public int userId;
	public Boy next;

	public Boy() {

	}

	public Boy(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Boy{" +
				"userId=" + userId +
				'}';
	}
}
