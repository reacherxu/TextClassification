package com.finalproject.classifier;

import java.util.Iterator;

import com.finalproject.classmanage.ClassManager;
import com.finalproject.featureselect.FeatureManager;
import com.finalproject.featureselect.FeatureSelector;
import com.finalproject.featureselect.FeatureVectorSpace;
/**
 * KNN分类器的功能实现类
 * @author Administrator
 *
 */
public class KNNClassifierCore {
	private String rootPath;
	private FileSet trainSet;
	private ClassManager classManager;
	private FeatureSelector featureSelector;
	private FeatureVectorSpace featureVectorSpace;
	private TrainingSetForKNN trainingSetForKNN;
	
	private class Simulation{
		int classID;
		double simValue;
		
		public Simulation(int classID,double simValue){
			this.classID=classID;
			this.simValue=simValue;
		}
		
		public int getClassID() {
			return classID;
		}
		
		public double getSimValue() {
			return simValue;
		}				
		public String toString(){
			return String.valueOf(classID)+"   "+simValue;
		}
		
	}
	
	public KNNClassifierCore(String path){
		this.rootPath=path;
		classManager=new ClassManager(path);
		featureSelector=FeatureSelector.getInstance(path, classManager);		
	}
	
	public KNNClassifierCore(FileSet trainSet,String function,int limen){
		this.trainSet=trainSet;
		classManager=new ClassManager(trainSet);
		featureSelector=FeatureSelector.getInstance(trainSet, classManager);
		FeatureManager featureManager=featureSelector.doFeatureSelect(function, limen);
		featureVectorSpace=featureManager.getFeatureVectorSpace();
		trainingSetForKNN=new TrainingSetForKNN(trainSet,featureVectorSpace,classManager);
	}
	
	public KNNClassifierCore(FileSet trainSet) {
		// TODO Auto-generated constructor stub
		this.trainSet=trainSet;
		classManager=new ClassManager(trainSet);
		featureSelector=FeatureSelector.getInstance(trainSet, classManager);
	}

	public void refresh(String function,int limen){
		FeatureManager featureManager=featureSelector.doFeatureSelect(function, limen);
		featureVectorSpace=featureManager.getFeatureVectorSpace();
		trainingSetForKNN=new TrainingSetForKNN(trainSet,featureVectorSpace,classManager);
	}
	
	public int classifyByID(Document document) {
		DocumentVector documentVector=new DocumentVector(featureVectorSpace,document,classManager);
		int classID=classify(documentVector);		
		return classID;
	}

	
	public int classifyByID(String path){
		DocumentVector documentVector=new DocumentVector(featureVectorSpace,-1,path);
		int classID=classify(documentVector);		
		return classID;
	}
	
	public String classifyByName(String path){
		DocumentVector documentVector=new DocumentVector(featureVectorSpace,-1,path);
		int classID=classify(documentVector);
		String className=classManager.getClassName(classID);
		return className;
	}
	
	public  int classify(DocumentVector documentVector){
		int count=0;
		int []values=new int[classManager.getClassCount()];
		Simulation []simulations=new Simulation[trainingSetForKNN.size()];
		Iterator<DocumentVector> iterator=trainingSetForKNN.iterator();
		while(iterator.hasNext()){
			DocumentVector tempDocumentVector=iterator.next();
			int classID=tempDocumentVector.getClassID();
			double simValue=simulate(tempDocumentVector, documentVector);
			simulations[count++]= new Simulation(classID,simValue);
			
		}
		sort(simulations);
		int lemon=3*classManager.getClassCount()+1;
		for(int i=0;i<lemon;i++){
			int classID=simulations[i].getClassID();
			values[classID]++;
		}
		int classID=getMaxValueID(values);
		return classID;
	}	
	
	private double simulate(DocumentVector first,DocumentVector second){
		double part1=0,part2=0,part3=0;
		double []firstVector=first.getFeatureCount();
		double []secondVector=second.getFeatureCount();
		for(int i=0;i<firstVector.length;i++){
			part1+=firstVector[i]*secondVector[i];
			part2+=firstVector[i]*firstVector[i];
			part3+=secondVector[i]*secondVector[i];
			
		}
		double result=0;
		if(part2!=0&&part3!=0)
			result=part1/Math.sqrt(part2*part3);
		return result;
	}
	
	private void sort(Simulation []simulations){		
		quickSort(simulations, 0, simulations.length-1);	
	}
	
	
	private void quickSort(Simulation []simulations,int low,int high){
		
		if(low<high){
			int pivotloc=quickSort_Patition(simulations, low, high);
			quickSort(simulations, low, pivotloc-1);
			quickSort(simulations, pivotloc+1, high);
		}
	}
	
	
	private int quickSort_Patition(Simulation []simulations,int low,int high){
		
		Simulation  tempSimulation=simulations[low];
		double piovotkey=simulations[low].getSimValue();
		while(low<high){
			while(low<high&&simulations[high].getSimValue()<=piovotkey)
				--high;
			simulations[low]=simulations[high];
			while(low<high&&simulations[low].getSimValue()>=piovotkey)
				++low;			
			simulations[high]=simulations[low];
		}
		simulations[low]=tempSimulation;
		return low;
	}
	
	
	private int getMaxValueID(int []values){
		int maxValue=values[0];
		int maxValueID=0;
		for(int i=1;i<values.length;i++){
			if(maxValue<values[i]){
				maxValue=values[i];
				maxValueID=i;
			}
		}
		return maxValueID;
	}
	
	public ClassManager getClassManager() {
		return classManager;
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}	
}
