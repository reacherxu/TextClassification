package com.classifier;

import java.util.HashMap;

import com.tools.DocumentReader;

/**
 * 表示原始文档的数据结构，带有在文档中出现的每个词汇及其出现个数
 * 
 * @author Administrator
 * 
 */
public class Document extends HashMap<String, Integer> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String classNameString;
	
	private String docPosition;

	public Document(String fileContent) {
		String[] wordStrings = fileContent.split(" ");
		
		String[] first = wordStrings[0].split(":");
		classNameString = first[0];
		docPosition = first[1];
		
		for (int i = 1; i < wordStrings.length; i++) {
			String[] tempStrings = wordStrings[i].split(":");
			
			String word = filter(tempStrings[0]);
			if( word != null )
				put(word, Integer.parseInt(tempStrings[1]));
		}
	}
	
	/**
	 * 默认读单个文件
	 */
	public Document() {
		String fileContent = DocumentReader.readFile("d:/document_test.txt");
		String[] wordStrings = fileContent.split(" ");

		for (int i = 0; i < wordStrings.length; i++) {
			String[] tempStrings = wordStrings[i].split(":");

			String word = filter(tempStrings[0]);
			if( word != null )
				put(word, Integer.parseInt(tempStrings[1]));
		}
	}

	private String filter(String str) {
		String wt[] = str.split("/");
		String item = wt[0];
		String ext = wt[1];
		
		//TODO　看看其他词性有没有影响
		if ( (ext.startsWith("n")&& !ext.startsWith("nr")) || ext.startsWith("un")
				|| ext.startsWith("v") || ext.startsWith("a")) {
			return item;
		}
		return null;
	}

	public String getClassNameString() {
		return classNameString;
	}

	public String getDocPosition() {
		return docPosition;
	}
	
}
