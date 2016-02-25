package com.tools;

import java.util.Date;
/**
 * ÈÕÖ¾¼ÇÂ¼
 * @author Administrator
 *
 */
public class Log {
	public static void log(String msg){
		Date date=new Date();
		System.out.println("["+date+"]"+msg);
	}
}
