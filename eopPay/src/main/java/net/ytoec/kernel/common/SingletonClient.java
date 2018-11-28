package net.ytoec.kernel.common;

import cn.emay.sdk.client.api.Client;
/**
 * 亿美软通[]
 * 短信接口service
 * @author guoliang.wang
 * @param <T>
 */
@Deprecated
public class SingletonClient {
	private static Client client=null;
	private SingletonClient(){
	}
	public synchronized static Client getClient(String softwareSerialNo,String key){
		if(client==null){
			try {
				client=new Client(softwareSerialNo,key);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}
	@Deprecated
	public synchronized static Client getClient(){
		//ResourceBundle bundle=PropertyResourceBundle.getBundle("config");
		if(client==null){
			try {
				client=new Client("0SDK-EAA-6688-JEUTM","799634");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return client;
	}
	public static void main(String str[]){
		try {
			System.out.println(SingletonClient.getClient().getBalance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
