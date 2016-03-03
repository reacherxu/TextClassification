package com.featureselect.evaluate;

import com.classmanage.ClassManager;
import com.featureselect.FeatureSelector;
/**
 * 特征选择方法CHI 数据集偏斜的实现类
 * @author Administrator
 *
 */
public class Evaluate_X2_Skewed implements Evaluate {

	@Override
	public double mark(String feature, ClassManager classManager,
			FeatureSelector featureItemManager) {
		double totalFileCount = classManager.getTotalFileCount();
	    double totalFileCountOfFeature = featureItemManager
				.getTotalFileCountOfFeature(feature);
	    
	    if(totalFileCountOfFeature <= 1)
	    	return 0;
	    
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
			double part2 = 0;
			
			double FI = featureItemManager.getFeatureWordFreq(feature, classID) / count1;
			double CI = count_a / totalFileCountOfFeature;
			double DI = count_a / count1;
			double IDF = Math.log(totalFileCount / totalFileCountOfFeature);
			
			if(count_a * count_d - count_b * count_c > 0)
				part2 = (totalFileCount * Math.pow(
						(count_a * count_d - count_b * count_c), 2))
						/ ((count_a + count_b) * (count_a + count_c)
								* (count_b + count_d) * (count_c + count_d));
			result += part1 * part2 * FI * CI * DI * IDF;
		}
		return result;
	}
}
