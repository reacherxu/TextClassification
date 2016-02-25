package com.featureselect.evaluate;

import com.classmanage.ClassManager;
import com.featureselect.FeatureSelector;
/**
 * 特征选择方法IG的实现类
 * @author Administrator
 *
 */
public class Evaluate_IG implements Evaluate {

	@Override
	public double mark(String feature, ClassManager classManager,
			FeatureSelector featureItemManager) {
		double part1 = 0, part2 = 0, part3 = 0;
		double totalFileCount = classManager.getTotalFileCount();
		double totalFileCountOfFeature = featureItemManager
				.getTotalFileCountOfFeature(feature);
		double totalFileCountOfNotFeature = totalFileCount
				- totalFileCountOfFeature;
		String[] classNames = classManager.getClassNames();
		for (int i = 0; i < classNames.length; i++) {
			int classID = classManager.getClassID(classNames[i]);

			double count1 = classManager.getClassFileCount(classID);
			double temp = count1 / totalFileCount;
			if (temp != 0)
				part1 += temp * Math.log10(temp);
			else {
				part1 += 0;
			}

			double count2 = featureItemManager.getClassFileCountOfFeature(
					classID, feature);
			temp = count2 / totalFileCountOfFeature;
			if (temp != 0)
				part2 += temp * Math.log10(temp);
			else {
				part2 += 0;
			}
			double count3 = count1 - count2;
			temp = count3 / totalFileCountOfNotFeature;
			if (temp != 0)
				part3 += temp * Math.log10(temp);
			else {
				part3 += 0;
			}
		}
		part2 = ((double) totalFileCountOfFeature / totalFileCount) * part2;
		part3 = ((double) totalFileCountOfNotFeature / totalFileCount) * part3;
		double result = -part1 + part2 + part3;

//		System.out.println(result);
		return result;
	}
}
