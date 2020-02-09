package com.company.list;

public class SingleLinkedListDemo {
	public static void main(String[] args) {
		SingleLinkedList list = new SingleLinkedList();
		PeopleNode one = new PeopleNode(1, "james", 12);
		PeopleNode two = new PeopleNode(2, "amos", 13);
		PeopleNode three = new PeopleNode(3, "alan", 14);
		PeopleNode four = new PeopleNode(4, "tom", 15);

		list.addNdeByOrder(one);
		list.addNdeByOrder(three);
		list.addNdeByOrder(two);
		list.addNdeByOrder(four);
		list.listNode();

		System.out.println("===========length=========");
		System.out.println(getLength(list.getHeader()));
	}

	/**
	 * Get linked list size
	 *
	 * @param header
	 * @return
	 */
	public static int getLength(PeopleNode header) {
		if (header.next == null) {
			return 0;
		}
		int length = 0;
		PeopleNode cur = header.next;
		while (cur != null) {
			length++;
			cur = cur.next;
		}
		return length;
	}


}

class SingleLinkedList {

	/**
	 * This is header
	 */
	PeopleNode header = new PeopleNode(0, "", 0);

	public PeopleNode getHeader() {
		return header;
	}


	/**
	 * 原来节点进行反转
	 */
	void reversalList() {
		if (header.next == null || header.next.next == null) {
			return;
		}

		PeopleNode cur = header.next;
		PeopleNode next = null;
		PeopleNode reverseHead = new PeopleNode(0, "", 0);
		while (cur != null) {
			/**
			 * 保存当前的节点的下一个节点
			 */
			next = cur.next;

			/**
			 * 保存新链表的下一个节点
			 */
			PeopleNode reverseHeadNext = reverseHead.next;
			/**
			 * 当前节点的下一个节点为新链表的头节点的下一个节点
			 */
			cur.next = reverseHeadNext;
			/**
			 * 一定新链表的头节点的下一个节点
			 */
			reverseHead.next = cur;
			/**
			 * 循环下一个节点
			 */
			cur = next;
		}
		/**
		 * 最后替换原来节点
		 */
		header.next = reverseHead.next;
	}

	void addNode(PeopleNode node) {
		/**
		 * one: No order, we search list tail
		 */
		PeopleNode temp = header;
		while (true) {
			if (temp.next == null) {
				break;
			}
			temp = temp.next;
		}
		temp.next = node;
	}

	void addNdeByOrder(PeopleNode node) {
		/**
		 * one: No order, we search list tail
		 */
		PeopleNode temp = header;
		boolean flag = false;
		while (true) {
			if (temp.next == null) {
				break;
			}
			if (temp.next.userId > node.userId) {
				break;
			}
			if (temp.next.userId == node.userId) {
				flag = true;
				break;
			}
			temp = temp.next;
		}
		if (flag) {
			System.out.println("Node has exist, dont insert");
		} else {
			node.next = temp.next;
			temp.next = node;
		}
	}

	void update(PeopleNode node) {
		if (header.next == null) {
			System.out.println("Linked list is null");
			return;
		}
		PeopleNode temp = header.next;
		boolean flag = false;
		while (true) {
			if (temp == null) {
				System.out.println("The node is not exist");
				break;
			}
			if (temp.userId == node.userId) {
				flag = true;
				break;
			}
			temp = temp.next;
		}

		if (flag) {
			temp.name = node.name;
			temp.age = node.age;
		} else {
			System.out.println("The node is not exist");
		}
	}

	void del(int userId) {
		if (header.next == null) {
			System.out.println("Linked list is null");
			return;
		}
		PeopleNode temp = header.next;
		boolean flag = false;
		while (true) {
			if (temp == null) {
				System.out.println("The node is not exist");
				break;
			}
			if (temp.next.userId == userId) {
				flag = true;
				break;
			}
			temp = temp.next;
		}

		if (flag) {
			temp.next = temp.next.next;
		} else {
			System.out.println("The node is not exist");
		}

	}

	/**
	 * Show node list
	 */
	void listNode() {
		if (header.next == null) {
			System.out.println("Linked list is null");
			return;
		}

		PeopleNode temp = header.next;
		while (true) {
			if (temp == null) {
				break;
			}
			System.out.println(temp);
			temp = temp.next;
		}
	}
}

class PeopleNode {
	public int userId;
	public String name;
	public int age;
	public PeopleNode next;

	PeopleNode(int userId, String name, int age) {
		this.age = age;
		this.name = name;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "PeopleNode{" +
				"userId=" + userId +
				", name='" + name + '\'' +
				", age=" + age +
				'}';
	}
}
