package com.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * 文档读取工具
 * @author Administrator
 *
 */
public class DocumentReader {
	/**
	 * 读取文档,一个文档是一行
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
		System.out.println(DocumentReader.readFile("file/stop_words_zh.txt"));
	}
}
