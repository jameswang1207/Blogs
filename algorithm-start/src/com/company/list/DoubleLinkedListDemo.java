package com.company.list;

public class DoubleLinkedListDemo {

	public static void main(String[] args) {

	}
}

class DoubleLinkedList {

	DoublePeopleNode header = new DoublePeopleNode(0, "", 0);


	void list() {

		if (header.next == null) {
			System.out.println("Double list is null");
			return;
		}

		DoublePeopleNode temp = header.next;
		while (true) {
			if (temp == null) {
				System.out.println("List is end");
				break;
			}

			System.out.println(temp);
			temp = temp.next;
		}
	}

	void insert(DoublePeopleNode node) {
		DoublePeopleNode temp = header;
		while (true) {
			if (temp.next == null) {
				break;
			}
			temp = temp.next;
		}

		/**
		 * linked list is double list
		 */
		temp.next = node;
		node.pro = temp;
	}

	void update(DoublePeopleNode node) {
		if (header.next == null) {
			System.out.println("Double list is null");
			return;
		}

		DoublePeopleNode temp = header.next;
		boolean flag = false;
		while (true) {

			if (temp == null) {
				break;
			}

			if (temp.userId == node.userId) {
				flag = true;
				break;
			}
			temp = temp.next;
		}
		if (flag) {
			temp.age = node.age;
			temp.name = node.name;
		} else {
			System.out.println("no have node");
		}
	}

	void del(int userId) {
		if (header.next == null) {
			System.out.println("Linked list is null");
			return;
		}
		DoublePeopleNode temp = header.next;
		boolean flag = false;
		while (true) {
			if (temp == null) {
				System.out.println("The node is not exist");
				break;
			}
			if (temp.userId == userId) {
				flag = true;
				break;
			}
			temp = temp.next;
		}

		if (flag) {
			temp.pro.next = temp.next;
			if (temp.next != null) {
				temp.next.pro = temp.pro;
			}
		} else {
			System.out.println("The node is not exist");
		}

	}
}

class DoublePeopleNode {
	public int userId;
	public String name;
	public int age;
	public DoublePeopleNode next;
	public DoublePeopleNode pro;

	public DoublePeopleNode() {

	}

	public DoublePeopleNode(int userId, String name, int age) {
		this.userId = userId;
		this.name = name;
		this.age = age;
	}

	@Override
	public String toString() {
		return "DoublePeopleNode{" +
				"userId=" + userId +
				", name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}