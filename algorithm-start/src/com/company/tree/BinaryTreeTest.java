package com.company.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BinaryTreeTest {

	public static void main(String[] args) {
		//		BinaryTree binaryTree = new BinaryTree();
		//		HeroNode root = new HeroNode(1, "宋江");
		//		HeroNode node2 = new HeroNode(2, "吴用");
		//		HeroNode node3 = new HeroNode(3, "卢俊义");
		//		HeroNode node4 = new HeroNode(4, "林冲");
		//		HeroNode node5 = new HeroNode(5, "关胜");
		//		root.setLeft(node2);
		//		root.setRight(node3);
		//		node3.setRight(node4);
		//		node3.setLeft(node5);
		//		binaryTree.setRoot(root);
		//		//测试
		//		System.out.println("前序遍历");
		//		binaryTree.preOrder();

		List<Node> nodes = new ArrayList<>();
		Node node1 = new Node("01", "01Name", "0");
		Node node2 = new Node("02", "02Name", "01");
		Node node3 = new Node("03", "03Name", "02");
		Node node4 = new Node("04", "04Name", "01");
		Node node5 = new Node("05", "05Name", "08");
		nodes.add(node1);
		nodes.add(node2);
		nodes.add(node3);
		nodes.add(node4);
		nodes.add(node5);

		Map<String, List<Node>> mapNode = new ConcurrentHashMap<>();
		nodes.stream().parallel().forEach(node -> {
			String parentId = node.getParentId();
			List<Node> children = mapNode.get(parentId);
			if (children == null || children.size() == 0) {
				List<Node> temps = new ArrayList<>();
				temps.add(node);
				mapNode.put(parentId, temps);
			} else {
				children.add(node);
				mapNode.put(parentId, children);
			}
		});

		nodes.stream().parallel().forEach(no -> {
			String code = no.getCode();

			List<Node> children = mapNode.get(code);
			if (children != null && children.size() != 0) {
				no.setChildren(children);
			}
			if (no.getParentId() != "0") {
				mapNode.remove(code);
			}
		});

		Node root = mapNode.get("0").get(0);
		List<Node> syncNode = Collections.synchronizedList(new ArrayList<>());
		syncNode.addAll(root.getChildren());
		mapNode.entrySet().forEach(no -> {
			List<Node> values = no.getValue();
			if (no.getKey() != "01" && values != null && values.size() > 0) {
				syncNode.addAll(no.getValue());
			}
		});
		root.setChildren(syncNode);

		System.out.println(root.toString());
	}
}

class Node {
	private String code;
	private String name;
	private String parentId;

	private List<Node> children;

	Node(String code, String name, String parentId) {
		this.name = name;
		this.code = code;
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public String getParentId() {
		return parentId;
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public List<Node> getChildren() {
		return children;
	}

	@Override public String toString() {
		return "Node{" +
				"code='" + code + '\'' +
				", name='" + name + '\'' +
				", parentId='" + parentId + '\'' +
				'}';
	}
}