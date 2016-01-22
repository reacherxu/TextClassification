package com.finalproject.featureselect;
/**
 * 数据结构，带打分值的特征
 * @author Administrator
 *
 */
public class MarkedFeature {
	private String name;
	private double mark;
	
	public MarkedFeature(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getMark() {
		return mark;
	}
	public void setMark(double mark) {
		this.mark = mark;
	}
	
}
