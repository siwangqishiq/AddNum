package com.xinlan.addnum;

public class Native {
	static{
		System.loadLibrary("addlib");
	}
	
	public native static String addNum(String num1,String num2);
}//end class
