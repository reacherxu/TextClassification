package com.featureselect.evaluate;

import com.classmanage.ClassManager;
import com.featureselect.FeatureSelector;

/**
 * 特征选择方法IDTC的实现类
 * 
 * @author Administrator
 * 
 */
public class Evaluate_Combined implements Evaluate {

	@Override
	public double mark(String feature, ClassManager classManager,
			FeatureSelector featureItemManager) {
		String[] classNames = classManager.getClassNames();
		double result_X2 = 0;
		double result_MI = 0;
		double totalFileCountOfFeature = featureItemManager
				.getTotalFileCountOfFeature(feature);
		double totalFileCount = classManager.getTotalFileCount();
		
		double lambda = 0.95;		
		//improved X2
		for (int i = 0; i < classNames.length; i++) {
			int classID = classManager.getClassID(classNames[i]);

			double count1 = classManager.getClassFileCount(classID);
			double part1 = count1 / totalFileCount;

			double count_a = featureItemManager.getClassFileCountOfFeature(
					classID, feature);
			double count_b = totalFileCountOfFeature - count_a;
			double count_c = count1 - count_a;
			double count_d = totalFileCount - totalFileCountOfFeature - count_c;
			double part2 = 0;
			
			double FI = featureItemManager.getFeatureWordFreq(feature, classID) / count1;
			double CI = count_a / totalFileCountOfFeature;
			double DI = count_a / count1;
			if(count_a * count_d - count_b * count_c > 0)
				part2 = (totalFileCount * Math.pow(
						(count_a * count_d - count_b * count_c), 2))
						/ ((count_a + count_b) * (count_a + count_c)
								* (count_b + count_d) * (count_c + count_d));
			result_X2 += part1 * part2 * FI * CI * DI ;
		}
		
		//improved MI
		if (totalFileCountOfFeature > classNames.length) {
			for (int i = 0; i < classNames.length - 1; i++)
				for (int j = i + 1; j < classNames.length; j++) {
					double classFileCount1 = classManager.getClassFileCount(i);
					double classFileCount2 = classManager.getClassFileCount(j);
					double classFileCountOfFeature1 = featureItemManager
							.getClassFileCountOfFeature(i, feature);
					double classFileCountOfFeature2 = featureItemManager
							.getClassFileCountOfFeature(j, feature);
					double total = classFileCount1 + classFileCount2;
					double totalOfFeature = classFileCountOfFeature1
							+ classFileCountOfFeature2;
					double featurePro = totalOfFeature / total;
					if (totalOfFeature > 0 && totalOfFeature < total) {
						double tempClassProAtFeature = classFileCountOfFeature1
								/ totalOfFeature;
						double tempClassPro = classFileCount1 / total;
						double asociate1 = Math.log10(tempClassProAtFeature
								/ tempClassPro + 0.01);
						tempClassProAtFeature = classFileCountOfFeature2
								/ totalOfFeature;
						tempClassPro = classFileCount2 / total;
						double asociate2 = Math.log10(tempClassProAtFeature
								/ tempClassPro + 0.01);
						result_MI += featurePro * Math.abs(asociate1 - asociate2);
					}
				}
		}
		return result_X2*lambda + result_MI*(1-lambda) ;
	}
}
