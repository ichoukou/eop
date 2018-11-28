
package net.ytoec.kernel.dataobject;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;
/**
 * @作者：罗典
 * @描述：日期时间格式转换类
 * @时间：2013-08-29
 * */
public class Adapter1
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
    	 if (value == null) {
             return null;
         }
         return new Date(value);
    }

    public String marshal(Date value) {
    	  if (value == null) {
              return null;
          }
          Calendar c = Calendar.getInstance();
          c.setTime(value);
          return DatatypeConverter.printDateTime(c);
    }
    
}
