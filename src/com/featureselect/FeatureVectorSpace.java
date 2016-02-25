package com.featureselect;

import java.util.Vector;

/**
 * 向量空间模型，含有特征及其在训练集中出现的文档个数
 * 
 * @author Administrator
 * 
 */
public class FeatureVectorSpace {
	// 特征字符串
	private Vector<String> featureVector;
	//TODO 出现特征的文档数
	private int[] fileCountOfFeature;

	public FeatureVectorSpace(Vector<String> stringVector,
			FeatureManager featureManager) {
		this.featureVector = stringVector;
		fileCountOfFeature = new int[featureVector.size()];
		for (int i = 0; i < featureVector.size(); i++) {
			fileCountOfFeature[i] = featureManager
					.getTotalFileCountOfFeature(featureVector.get(i));
		}
	}

	public String getFeatureName(int featureID) {
		return featureVector.get(featureID);
	}

	public int getFeatureID(String featureName) {
		return featureVector.indexOf(featureName);
	}

	public int getFeatureCount() {
		return featureVector.size();
	}

	public int getFileCount(String feature) {
		return fileCountOfFeature[featureVector.indexOf(feature)];
	}

	public boolean isFeature(String featureName) {
		if (featureVector.contains(featureName))
			return true;
		else
			return false;
	}
}
