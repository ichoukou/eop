package net.ytoec.kernel.action.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class HttpChannel   {
	private Logger logger = LoggerFactory.getLogger(HttpChannel.class);
    private URL url;
    private String content;

    public HttpChannel(String url,String conString) throws Exception {
        this.url = new URL(url);
        this.content=conString;
    }

    public String sendPostRequest( ) throws Exception {
        
        HttpURLConnection conn = (HttpURLConnection) this.url.openConnection();
        if (conn==null) {
		}
       
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        
        DataOutputStream out = new DataOutputStream(conn
                .getOutputStream());
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
       
        out.writeBytes(this.content); 
        out.flush();
        out.close(); // flush and close 
        
        
       // logger.debug("try to get input stream");
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn
                .getInputStream()));
        String line;
        StringBuffer buffer = new StringBuffer(1024);
        
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        
        String value = buffer.toString();
        //logger.debug("get resposne: '" + value + "'");
        
        value = URLDecoder.decode(value, "UTF-8");
        return value;
    }

}