package net.ytoec.kernel.action.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


/**
 * 此类只用于页面标签读取cookie,
 * @author Administrator
 *
 */
@Controller
@Scope("prototype")
public class CookieAction extends AbstractActionSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    

    
    public static Object getCookie(String name){
        CookieAction cAction=new CookieAction();
        return cAction.readCookie(name);
    }
}
