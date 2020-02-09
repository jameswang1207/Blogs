package com.company.sort;

import java.util.Arrays;

public class SortDemo {
	public static void main(String[] args) {
		int[] arr = {1, 3, 2, 7, 6, 9, 0};
//		bubbleSort(arr);
		selectSort(arr);
		insertSort(arr);


		int[] shellSort = {1, 3, 2, 7, 6, 9, 4, 8, 5, 0};
		System.out.println("======");
		shellMoveSort(shellSort);

		int[]  mergeSort = {8, 4, 5, 7, 6, 4, 3, 2};

	}

	public static void mergeSort() {

	}


	public static void quickSort(int[] arr, int left, int right) {
		int l = left;
		int r = right;
		int temp = 0;
		int pivot = arr[(left + right) / 2];
		while (l < r) {
			while (arr[l] < pivot) {
				l += 1;
			}
			while (arr[r] > pivot) {
				r -= 1;
			}

			if (l >= r) {
				break;
			}

			temp = arr[l];
			arr[l] = arr[r];
			arr[r] = temp;
			if (arr[l] == pivot) {
				r -= 1;
			}
			if (arr[r] == pivot) {
				l += 1;
			}
		}

		if (left < r) {
			quickSort(arr, left, r);
		}

		if (right > l) {
			quickSort(arr, l, right);
		}
	}

	public static void shellMoveSort(int[] arr) {
		for (int grep = arr.length / 2; grep > 0; grep /= 2) {
			for (int i = grep; i < arr.length; i++) {
				int j = i;
				int temp = arr[j];
				while (j - grep >= 0 && temp < arr[j - grep]) {
					arr[j] = arr[j - grep];
					j -= grep;
				}
				arr[j] = temp;
			}
		}
		System.out.print(Arrays.toString(arr));
	}

	public static void insertSort(int[] arr) {
		for (int i = 1; i < arr.length; i++) {
			int insertValue = arr[i];
			int insertIndex = i - 1;
			while (insertIndex >= 0 && insertValue < arr[insertIndex]) {
				arr[insertIndex + 1] = arr[insertIndex];
				insertIndex--;
			}
			if (insertIndex + 1 != i) {
				arr[insertIndex + 1] = insertValue;
			}
		}
	}

	/**
	 * 选择排序
	 *
	 * @param arr
	 */
	public static void selectSort(int[] arr) {
		for (int i = 0; i < arr.length - 1; i++) {
			int minValue = arr[i];
			int minIndex = i;
			for (int j = i + 1; j < arr.length; j++) {
				int after = arr[j];
				if (minValue > after) {
					minValue = after;
					minIndex = j;
				}
			}
			if (minIndex != i) {
				arr[minIndex] = arr[i];
				arr[i] = minValue;
			}

		}
		System.out.println(Arrays.toString(arr));
	}

	/**
	 * 冒泡排序
	 *
	 * @param arr
	 */
	public static void bubbleSort(int[] arr) {
		boolean flag = false;
		for (int i = 0; i < arr.length - 1; i++) {
			for (int j = 0; j < arr.length - 1 - i; j++) {
				int current = arr[j];
				int after = arr[j + 1];
				if (current > after) {
					flag = true;
					arr[j] = after;
					arr[j + 1] = current;
				}
			}
			if (!flag) {
				break; // 没有进行过一次交换就直接跳出for循环
			} else {
				flag = false; //将标识位重置
			}
		}
		System.out.println(Arrays.toString(arr));
	}
}
