package com.finalproject.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
/**
 * 文档集实现类，为了多次调用而常驻内存的数据结构
 * @author Administrator
 *
 */
public class FileSet extends ArrayList<Document>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<String,Integer>map;
	private int total;
	
	public FileSet(String filePath){
		map=new HashMap<String, Integer>();
		try {
			Scanner scanner=new Scanner(new File(filePath));
			String line=scanner.nextLine();
			String []temps=line.split(" ");
			String []temps2=temps[0].split(":");
			total=Integer.parseInt(temps2[1]);
			for(int i=1;i<temps.length;i++){
				temps2=temps[i].split(":");
				map.put(temps2[0], Integer.parseInt(temps2[1]));
			}
			
			while(scanner.hasNextLine()){
				line=scanner.nextLine();
				Document doc=new Document(line);
				add(doc);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public String[] getClassNameStrings() {
		String []classNameStrings;
		int count =map.size();
		classNameStrings=new String[count];
		Set<String>set=map.keySet();
		set.toArray(classNameStrings);
		return classNameStrings;
	}
	
	public int  getCount(String className){
		return map.get(className);
	}
	
	public int getTotal(){
		return total;
	}
	public int getClassCount(){
		return map.size();
	}
}
