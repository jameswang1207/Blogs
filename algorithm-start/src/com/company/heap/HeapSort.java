package com.company.heap;

import java.util.Arrays;

public class HeapSort {

	public static void main(String[] args) {
		int arr[] = {4, 6, 8, 5, 9, 14, 67, 56};
		heapSortMain(arr);
	}

	public static void heapSortMain(int[] arr) {
		int temp = 0;
		//  形成大顶堆
		for (int i = arr.length / 2 - 1; i >= 0; i--) {
			heapSort(arr, i, arr.length);
		}

		// 交换位置，继续寻找到顶对
		for (int j = arr.length - 1; j > 0; j--) {
			temp = arr[j];
			arr[j] = arr[0];
			arr[0] = temp;
			heapSort(arr, 0, j);
		}
		System.out.println(Arrays.toString(arr));
	}


	public static void heapSort(int[] arr, int i, int length) {
		int temp = arr[i];
		for (int k = i * 2 + 1; k < length; k = k * 2 + 1) {
			if (k + 1 < length && arr[k] < arr[k + 1]) {
				k++;
			}
			if (arr[k] > temp) {
				arr[i] = arr[k];
				i = k;
			} else {
				break;
			}
		}
		arr[i] = temp;
	}


}
