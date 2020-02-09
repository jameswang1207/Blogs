package com.company.array;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 稀疏数组的实现
 */
public class SparseArray {
	public static void main(String[] args) {
		/**
		 * 1. Create chess, 1 is white, 2 is black
		 */
		int[][] chess = new int[11][11];
		chess[1][3] = 1;
		chess[2][5] = 2;
		chess[3][6] = 2;

		/**
		 * 2. Print chess
		 */
		Map<String, Integer> values = new HashMap<>();

		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 11; j++) {
				int value = chess[i][j];
				System.out.printf("%d\t", value);
				if (value != 0) {
					values.put(String.format("%s,%s", i, j), value);
				}
			}
			System.out.println();
		}

		/**
		 * 3. Array change sparse array
		 */

		int[][] sparseChess = new int[values.size() + 1][3];
		sparseChess[0][0] = 11;
		sparseChess[0][1] = 11;
		sparseChess[0][2] = values.size();

		Set<Map.Entry<String, Integer>> entrys = values.entrySet();

		int i = 1;
		for (Map.Entry<String, Integer> entry : entrys) {

			Integer val = entry.getValue();
			String keys = entry.getKey();
			String[] value = keys.split(",");
			Integer row = Integer.valueOf(value[0]);
			Integer column = Integer.valueOf(value[1]);
			sparseChess[i][0] = row;
			sparseChess[i][1] = column;
			sparseChess[i][2] = val;
			i++;
		}

		System.out.println("==================================");

		Arrays.stream(sparseChess).forEach(sparseChessPrints -> {
			Arrays.stream(sparseChessPrints).forEach(sparseChessPrint -> {
				System.out.printf("%d\t", sparseChessPrint);
			});
			System.out.println();
		});

		/**
		 * 4. sparse array change chess
		 */
	}
}
