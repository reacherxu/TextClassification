package com.finalproject.featureselect.evaluate;

import com.finalproject.classmanage.ClassManager;
import com.finalproject.featureselect.FeatureSelector;
/**
 * 特征选择方法DF的实现类
 * @author Administrator
 *
 */
public class Evaluate_CE implements Evaluate {

	@Override
	public double mark(String feature, ClassManager classManager,
			FeatureSelector featureItemManager) {
		// TODO Auto-generated method stub
		double totalFileCount = classManager.getTotalFileCount();
		double totalFileCountOfFeature = featureItemManager
				.getTotalFileCountOfFeature(feature);
		String[] classNames = classManager.getClassNames();

		double part1 = totalFileCountOfFeature / totalFileCount;

		double result = 0;
		double part2 = 0;

		for (int i = 0; i < classNames.length; i++) {
			int classID = classManager.getClassID(classNames[i]);

			double count1 = featureItemManager.getClassFileCountOfFeature(
					classID, feature);
			double part21 = count1 / totalFileCountOfFeature;

			double count2 = classManager.getClassFileCount(classID);
			double part22 = count2 / totalFileCount;
			if (part21 != 0)
				part2 += part21 * Math.log10(part21 / part22);
			else {
				part2 += 0;
			}
		}
		result = part1 * part2;
//		System.out.println(result);
		return result;
	}
}
