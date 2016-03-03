package com.featureselect.evaluate;

import com.classmanage.ClassManager;
import com.featureselect.FeatureSelector;
/**
 * 特征选择方法CHI的实现类
 * @author Administrator
 *
 */
public class Evaluate_SCR implements Evaluate {

	@Override
	public double mark(String feature, ClassManager classManager,
			FeatureSelector featureItemManager) {
		double totalFileCount = classManager.getTotalFileCount();
	    double totalFileCountOfFeature = featureItemManager
				.getTotalFileCountOfFeature(feature);
	    
		String[] classNames = classManager.getClassNames();
		double max = 0;
		for (int i = 0; i < classNames.length; i++) {
			int classID = classManager.getClassID(classNames[i]);

			double count1 = classManager.getClassFileCount(classID);
//			double part1 = count1 / totalFileCount;

			double count_a = featureItemManager.getClassFileCountOfFeature(
					classID, feature);
			double count_b = totalFileCountOfFeature - count_a;
			double count_c = count1 - count_a;
			double count_d = totalFileCount - totalFileCountOfFeature - count_c;
			
			double dis_classID = Math.pow(count_a, 2) / (count_b * count_c + 1) - 
					Math.pow(count_b, 2) / (count_a * count_d + 1);
			
			max = max > dis_classID ? max : dis_classID;
		}
		
		return totalFileCountOfFeature / totalFileCount * max;
	}
}
