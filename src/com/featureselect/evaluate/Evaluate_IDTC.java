package com.featureselect.evaluate;

import com.classmanage.ClassManager;
import com.featureselect.FeatureSelector;

/**
 * 特征选择方法IDTC的实现类
 * 
 * @author Administrator
 * 
 */
public class Evaluate_IDTC implements Evaluate {

	@Override
	public double mark(String feature, ClassManager classManager,
			FeatureSelector featureItemManager) {
		String[] classNames = classManager.getClassNames();
		double result = 0;
		double totalFileCountOfFeature = featureItemManager
				.getTotalFileCountOfFeature(feature);
		
		
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
						result += featurePro * Math.abs(asociate1 - asociate2);
					}
				}
		}
		return result ;
	}
}
