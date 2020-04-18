package com.company.huffmancoding;

import java.util.*;

public class HuffManCoding {
	//生成赫夫曼树对应的赫夫曼编码
	//思路:
	//1. 将赫夫曼编码表存放在 Map<Byte,String> 形式
	// 生成的赫夫曼编码表{32=01, 97=100, 100=11000, 117=11001, 101=1110, 118=11011, 105=101, 121=11010, 106=0010, 107=1111, 108=000, 111=0011}
	static Map<Byte, String> huffmanCodes = new HashMap<Byte, String>();
	//2. 在生成赫夫曼编码表示，需要去拼接路径, 定义一个 StringBuilder 存储某个叶子结点的路径
	static StringBuilder stringBuilder = new StringBuilder();

	public static void main(String[] args) {
		String transString = "i like like like java do you like a java";
		byte[] contentBytes = transString.getBytes();
		byte[] resultCodes = huffManZip(contentBytes);

		System.out.println("============");

		byte[] result = unzip(huffmanCodes, resultCodes);
		System.out.println(new String(result));
	}

	private static byte[] unzip(Map<Byte, String> huffmanCodes, byte[] codes) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < codes.length; i++) {
			byte b = codes[i];
			boolean flag = i == codes.length - 1;
			stringBuilder.append(byteToBitString(!flag, b));
		}

		Map<String, Byte> huffManCode = new HashMap<>();
		huffmanCodes.entrySet().stream().forEach(entity -> {
			huffManCode.put(entity.getValue(), entity.getKey());
		});

		List<Byte> lists = new ArrayList<>();
		for (int i = 0; i < stringBuilder.length(); ) {
			int count = 1;
			boolean flag = true;
			Byte b = null;
			while (flag) {
				String key = stringBuilder.substring(i, i + count);
				b = huffManCode.get(key);
				if (b == null) {
					count++;
				} else {
					flag = false;
				}
			}
			lists.add(b);
			i += count;
		}
		byte b[] = new byte[lists.size()];
		for (int i = 0; i < b.length; i++) {
			b[i] = lists.get(i);
		}
		return b;
	}

	/**
	 * 将一个 byte 转成一个二进制的字符串, 如果看不懂，可以参考我讲的 Java 基础 二进制的原码，反码，
	 * 补码
	 *
	 * @param b    传入的 byte
	 * @param flag 标志是否需要补高位如果是 true ，表示需要补高位，如果是 false 表示不补, 如果是最后一 个字节，无需补高位
	 * @return 是该 b 对应的二进制的字符串，(注意是按补码返回)
	 */
	private static String byteToBitString(boolean flag, byte b) {
		//使用变量保存 b
		int temp = b;
		// 将 b 转成 int //如果是正数我们还存在补高位
		if (flag) {
			temp |= 256; //按位与 256
		}
		String str = Integer.toBinaryString(temp); //返回的是 temp 对应的二进制的补码
		if (flag) {
			return str.substring(str.length() - 8);
		} else {
			return str;
		}
	}


	/**
	 * 压缩
	 *
	 * @param contentBytes
	 * @return
	 */
	private static byte[] huffManZip(byte[] contentBytes) {
		List<Node> nodes = getNodes(contentBytes);
		Node root = createHuffmanTree(nodes);
		preOrder(root);
		getCodes(root);
		byte[] huffs = zip(contentBytes, huffmanCodes);
		return huffs;
	}

	private static byte[] zip(byte[] bytes, Map<Byte, String> huffmanCodes) {
		StringBuilder stringBuilder = new StringBuilder();
		for (byte b : bytes) {
			stringBuilder.append(huffmanCodes.get(b));
		}
		int len = 0;
		if (stringBuilder.length() % 8 == 0) {
			len = stringBuilder.length() / 8;
		} else {
			len = stringBuilder.length() / 8 + 1;
		}
		byte[] huffmanCodeBytes = new byte[len];
		int index = 0;
		for (int i = 0; i < stringBuilder.length(); i += 8) {
			String strByte;
			if (i + 8 > stringBuilder.length()) {
				strByte = stringBuilder.substring(i);
			} else {
				strByte = stringBuilder.substring(i, i + 8);
			}
			huffmanCodeBytes[index] = (byte) Integer.parseInt(strByte, 2);
			index++;
		}
		return huffmanCodeBytes;
	}

	//为了调用方便，我们重载 getCodes
	private static Map<Byte, String> getCodes(Node root) {
		if (root == null) {
			return null;
		}
		//处理 root 的左子树
		getCodes(root.left, "0", stringBuilder);
		//处理 root 的右子树
		getCodes(root.right, "1", stringBuilder);
		return huffmanCodes;
	}

	private static void getCodes(Node node, String code, StringBuilder stringBuilder) {
		StringBuilder stringBuilder2 = new StringBuilder(stringBuilder);
		//将 code 加入到 stringBuilder2
		stringBuilder2.append(code);
		if (node != null) { //如果 node == null 不处理 //判断当前 node 是叶子结点还是非叶子结点
			if (node.data == null) { //非叶子结点
				//递归处理
				//向左递归
				getCodes(node.left, "0", stringBuilder2);
				//向右递归
				getCodes(node.right, "1", stringBuilder2);
			} else { //说明是一个叶子结点 //就表示找到某个叶子结点的最后
				huffmanCodes.put(node.data, stringBuilder2.toString());
			}
		}
	}

	private static List<Node> getNodes(byte[] bytes) {
		//1 创建一个 ArrayList
		ArrayList<Node> nodes = new ArrayList<Node>();
		//遍历 bytes , 统计 每一个 byte 出现的次数->map[key,value]
		Map<Byte, Integer> counts = new HashMap<>();
		for (byte b : bytes) {
			System.out.println("this is byte:" + b);
			Integer count = counts.get(b);
			if (count == null) { // Map 还没有这个字符数据,第一次
				counts.put(b, 1);
			} else {
				counts.put(b, count + 1);
			}
		}
		//把每一个键值对转成一个 Node 对象，并加入到 nodes 集合 //遍历 map
		for (Map.Entry<Byte, Integer> entry : counts.entrySet()) {
			nodes.add(new Node(entry.getKey(), entry.getValue()));
		}
		return nodes;
	}

	//前序遍历的方法
	private static void preOrder(Node root) {
		if (root != null) {
			root.preOrder();
		} else {
			System.out.println("赫夫曼树为空");
		}
	}


	private static Node createHuffmanTree(List<Node> nodes) {
		while (nodes.size() > 1) {
			Collections.sort(nodes);
			Node leftNode = nodes.get(0);
			Node rightNode = nodes.get(1);
			Node parent = new Node(null, leftNode.weight + rightNode.weight);
			parent.left = leftNode;
			parent.right = rightNode;
			nodes.remove(leftNode);
			nodes.remove(rightNode);
			nodes.add(parent);
		}
		return nodes.get(0);
	}
}

class Node implements Comparable<Node> {
	Byte data; // 存放数据(字符)本身，比如'a' => 97 ' ' => 32
	int weight; //权值, 表示字符出现的次数
	public Node left;
	public Node right;

	Node(Byte data, int weight) {
		this.data = data;
		this.weight = weight;
	}

	void preOrder() {
		System.out.println(this);
		if (this.left != null) {
			this.left.preOrder();
		}

		if (this.right != null) {
			this.right.preOrder();
		}
	}

	// 从小到大排序
	@Override
	public int compareTo(Node o) {
		return this.weight - o.weight;
	}

	@Override
	public String toString() {
		return "Node{" +
				"data=" + data +
				", weight=" + weight +
				'}';
	}
}
