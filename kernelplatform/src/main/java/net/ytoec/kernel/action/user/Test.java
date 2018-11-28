package net.ytoec.kernel.action.user;

import java.util.Random;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Random random = new Random();  
		int x = random.nextInt(89999999);  
		int x1 = x+100000; 
		String zz="K"+Integer.toString(x1);
		System.out.println(zz);
		

	}

}
