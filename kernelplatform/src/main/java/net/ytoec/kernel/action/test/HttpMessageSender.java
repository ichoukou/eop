package net.ytoec.kernel.action.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

public class HttpMessageSender {

	@Test
	public void test(){
		 BufferedReader reader =null;
		 File taskFiles = new File("d:/unzip/31278238");
		 try {
			reader =  new BufferedReader(new FileReader(taskFiles));
		
		 String tempString = null;
         while ((tempString = reader.readLine()) != null) {
        	 System.out.print(tempString);
         }
         reader.close();
         System.out.print("sdf");
         if (taskFiles.exists()) {
             if (taskFiles.isFile()) {
                 taskFiles.delete();
                 taskFiles.deleteOnExit();
             } else if (taskFiles.isDirectory()) {
                 File files[] = taskFiles.listFiles();
                 for (int j = 0; j < files.length; j++) {
                     files[j].delete();
                 }
             } else {
                 taskFiles.delete();
             }
         }
		 } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
