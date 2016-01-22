package com.finalproject.tools;

import java.io.*;
import java.util.Scanner;
/**
 * 文档读取工具
 * @author Administrator
 *
 */
public class DocumentReader {
	/**
	 * 读取文档
	 * @param path
	 * @return 表示文档的字符串
	 */
	public static String readFile(String path) {
		Scanner scanner;
		String content = new String();
		try {
			scanner = new Scanner(new File(path));

			while (scanner.hasNextLine())
				content += scanner.nextLine();
			scanner.close();
		} catch (FileNotFoundException e) {
			System.out.println("File Not Found ["+path+"]");
		}
		
		return content;
	}

	public static void main(String[] args) {
		System.out.println(DocumentReader.readFile("c:/test.epf"));
	}
}
