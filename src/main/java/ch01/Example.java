/*
 * @(#)Example.class  2014. 9. 12..
 *
 * Copyright 2007 NHN Corp. All rights Reserved. 
 * NHN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package ch01;

import java.util.Arrays;

public class Example {
	public static void main(String[] args) {
		String[] words = {"abcd", "a"};
		Arrays.sort(words, (first, second) -> Integer.compare(first.length(), second.length()));

		System.out.println(words[0]);
	}
}
