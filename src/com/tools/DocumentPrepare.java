package com.tools;

/**
 * 文档预处理工具，比如多空格、标点等处理
 * 
 * @author Administrator
 * 
 */
public class DocumentPrepare {

	public static String prepare(String source) {
		String result = source.replaceAll("[^\\u4e00-\\u9fa5\\w]", " ");
		result = result.replaceAll("\\d", " ");
		result = result.replaceAll("\\s{2,}", " ");
		result = result.replaceAll("&nbsp", "");
		return result;
	}

	public static void print(Object[] o) {
		for (int i = 0; i < o.length; i++) {
			System.out.print(o[i] + " ");
		}
		System.out.println();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sourceString = DocumentReader
				.readFile("D:\\data\\corpus_mini\\test\\旅游\\10.txt");
		ChineseSplitter splitter = ChineseSplitter.getInstance();
		print(splitter.split(DocumentPrepare.prepare(sourceString)));
	}
}
