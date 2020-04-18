package com.company.tree;

public class HeroNode {
	private int no;
	private String name;
	private HeroNode left; //默认 null
	private HeroNode right; //默认 null


	public HeroNode(int no, String name) {
		this.no = no;
		this.name = name;
	}

	public void preOrder() {
		System.out.println(this);
		if (this.left != null) {
			this.left.preOrder();
		}

		if (this.right != null) {
			this.right.preOrder();
		}
	}

	public void infixOrder() {
		if (this.left != null) {
			this.left.infixOrder();
		}
		System.out.println(this);
		if (this.right != null) {
			this.right.infixOrder();
		}
	}

	public void postOrder() {
		if (this.left != null) {
			this.left.postOrder();
		}
		if (this.right != null) {
			this.right.postOrder();
		}
		System.out.println(this);
	}

	public int getNo() {
		return no;
	}

	public String getName() {
		return name;
	}

	public HeroNode getLeft() {
		return left;
	}

	public HeroNode getRight() {
		return right;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLeft(HeroNode left) {
		this.left = left;
	}

	public void setRight(HeroNode right) {
		this.right = right;
	}

	@Override
	public String toString() {
		return "HeroNode{" +
				"no=" + no +
				", name='" + name + '\'' +
				", left=" + left +
				", right=" + right +
				'}';
	}
}
