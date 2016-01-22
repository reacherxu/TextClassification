package com.finalproject.featureselect.evaluate;
/**
 * 特征选择方法工厂类，提供各个特征选择方法类的调用接口
 * @author Administrator
 *
 */
public class EvaluateFactory {

	//单例调用
	private static EvaluateFactory instance;
	private EvaluateFactory(){}
	
	public static EvaluateFactory getInstance(){
		if(instance==null){
			instance=new EvaluateFactory();
		}
		return instance;
	}
	
	public Evaluate getEvaluate(String name){
		if(name.equals("DF")){
			return new Evaluate_DF();
		}
		else if(name.equals("IG")){
			return new Evaluate_IG();
		}
		else if(name.equals("MI")){
			return new Evaluate_MI();
		}
		else if(name.equals("CE")){
			return new Evaluate_CE();
		}
		else if(name.equals("X2")){
			return new Evaluate_X2();
		}
		else if(name.equals("IMPROVE")){
			return new Evaluate_IDTC();
		}
		else{
			System.out.println("name not found");
			return null;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
