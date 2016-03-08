package com.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

/**
 * 文档集实现类，为了多次调用而常驻内存的数据结构
 * 
 * @author Administrator
 * 
 */
public class FileSet extends ArrayList<Document> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 文件类别以及对应的文件数
	 */
	private HashMap<String, Integer> map;
	/**
	 * fileset中的文档数
	 */
	private int total;  

	/**
	 * 根据map获取类名
	 * @return
	 */
	public String[] getClassNameStrings() {
		String[] classNameStrings;
		int count = map.size();
		classNameStrings = new String[count];
		Set<String> set = map.keySet();
		set.toArray(classNameStrings);
		return classNameStrings;
	}

	public int getCount(String className) {
		if(map.containsKey(className))
			return map.get(className);
		else
			return 0;
	}

	/**
	 * 获得fileset中所有的文件数
	 * @return
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 获得类的个数
	 * @return
	 */
	public int getClassCount() {
		return map.size();
	}
	
	public FileSet(String filePath) {
		map = new HashMap<String, Integer>();
		try {
			Scanner scanner = new Scanner(new File(filePath));
			
			/* 第一行记录 类别以及对应的文件数*/
			String line = scanner.nextLine();
			String[] temps = line.split(" ");
			String[] temps2 = temps[0].split(":");
			total = Integer.parseInt(temps2[1]);
			for (int i = 1; i < temps.length; i++) {
				temps2 = temps[i].split(":");
				map.put(temps2[0], Integer.parseInt(temps2[1]));
			}

			while (scanner.hasNextLine()) {
				line = scanner.nextLine();
				Document doc = new Document(line);
				add(doc);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


}
