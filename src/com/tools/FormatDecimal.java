package com.tools;

import java.text.DecimalFormat;

public class FormatDecimal {

	public static String format(double d) {
		DecimalFormat   df   =new DecimalFormat("#.00000");  
		return df.format(d);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(format(2.9998));
	}

}
