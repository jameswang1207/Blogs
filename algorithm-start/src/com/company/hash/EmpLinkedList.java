package com.company.hash;

public class EmpLinkedList {

	private Emp head;

	public void add(Emp emp) {
		if (head == null) {
			head = emp;
			return;
		}
		Emp currentEmp = head;
		while (true) {
			if (currentEmp.next == null) {
				break;
			}
			currentEmp = currentEmp.next;

		}
		currentEmp.next = emp;
	}

	public Emp findEmpById(int id) {
		if (head == null) {
			System.out.println("链表为空");
			return null;
		}
		Emp curEmp = head;
		while (true) {
			if (curEmp.id == id) {
				break;
			}
			if (curEmp.next == null) {
				curEmp = null;
				break;
			}
			curEmp = curEmp.next;
		}
		return curEmp;
	}
}
