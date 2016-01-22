package com.finalproject.featureselect.evaluate;

import com.finalproject.classmanage.ClassManager;
import com.finalproject.featureselect.FeatureSelector;
/**
 * 特征选择方法CHI的实现类
 * @author Administrator
 *
 */
public class Evaluate_X2 implements Evaluate {

	@Override
	public double mark(String feature, ClassManager classManager,
			FeatureSelector featureItemManager) {
		// TODO Auto-generated method stub
		double totalFileCount = classManager.getTotalFileCount();
	    double totalFileCountOfFeature = featureItemManager
				.getTotalFileCountOfFeature(feature);
		String[] classNames = classManager.getClassNames();
		double result = 0;
		for (int i = 0; i < classNames.length; i++) {
			int classID = classManager.getClassID(classNames[i]);

			double count1 = classManager.getClassFileCount(classID);
			double part1 = count1 / totalFileCount;

			double count_a = featureItemManager.getClassFileCountOfFeature(
					classID, feature);
			double count_b = totalFileCountOfFeature - count_a;
			double count_c = count1 - count_a;
			double count_d = totalFileCount - totalFileCountOfFeature - count_c;
			double part2 = (totalFileCount * Math.pow(
					(count_a * count_d - count_b * count_c), 2))
					/ ((count_a + count_b) * (count_a + count_c)
							* (count_b + count_d) * (count_c + count_d));
			result += part1 * part2;
		}
//		System.out.println(result);
		return result;
	}
}
