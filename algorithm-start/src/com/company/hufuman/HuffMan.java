package com.company.hufuman;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HuffMan {
	public static void main(String[] args) {
		int arr[] = {13, 7, 8, 3, 29, 6, 1};
		Node root = createHuffManTree(arr);
		proOrder(root);
	}

	public static void proOrder(Node root) {
		if (root != null) {
			root.proOrder();
		} else {
			System.out.println("this tree is end");
		}
	}

	public static Node createHuffManTree(int[] arrs) {
		List<Node> nodes = new ArrayList<Node>();
		for (int value : arrs) {
			nodes.add(new Node(value));
		}
		while (nodes.size() > 1) {
			Collections.sort(nodes);
			Node nodeLeft = nodes.get(0);
			Node nodeRight = nodes.get(1);

			Node parentNode = new Node(nodeLeft.value + nodeRight.value);
			parentNode.left = nodeLeft;
			parentNode.right = nodeRight;

			nodes.remove(nodeLeft);
			nodes.remove(nodeRight);
			nodes.add(parentNode);
		}
		return nodes.get(0);
	}
}

class Node implements Comparable<Node> {
	public int value;
	public Node left;
	public Node right;

	Node(int value) {
		this.value = value;
	}

	void proOrder() {
		System.out.println(this);
		if (this.left != null) {
			this.left.proOrder();
		}

		if (this.right != null) {
			this.right.proOrder();
		}
	}

	@Override
	public int compareTo(Node o) {
		return this.value - o.value;
	}

	@Override
	public String toString() {
		return "Node{" +
				"value=" + value +
				'}';
	}
}
