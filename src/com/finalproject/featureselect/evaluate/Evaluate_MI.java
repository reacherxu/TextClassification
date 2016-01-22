package com.finalproject.featureselect.evaluate;

import com.finalproject.classmanage.ClassManager;
import com.finalproject.featureselect.FeatureSelector;
/**
 * 特征选择方法MI的实现类
 * @author Administrator
 *
 */
public class Evaluate_MI implements Evaluate{

	@Override
	public double mark(String feature, ClassManager classManager,FeatureSelector featureItemManager) {
		// TODO Auto-generated method stub
		double totalFileCount=classManager.getTotalFileCount();
		double totalFileCountOfFeature=featureItemManager.getTotalFileCountOfFeature(feature);
		String[] classNames = classManager.getClassNames();
//		double []classPros=new double[classNames.length];
		double result = 0;
		for(int i=0;i<classNames.length;i++){
			int classID = classManager.getClassID(classNames[i]);
			
			double count1=classManager.getClassFileCount(classID);
			double part1=count1/totalFileCount;
			
			double count2=featureItemManager.getClassFileCountOfFeature(classID, feature);
			double part2=(count2*totalFileCount)/(totalFileCountOfFeature*count1)+0.01;			
	        result+=part1*Math.log10(part2);
		}
		return result;
	}
}
