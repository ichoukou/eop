/**
 * RedirectAction.java
 * Wangyong
 * 2011-8-4 下午03:09:20
 */
package net.ytoec.kernel.action.login;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
/**
 * 跳转Action类
 * @author Wangyong
 * @2011-8-8
 * net.ytoec.kernel.action.login
 */
@Controller
@Scope("prototype")
public class RedirectAction extends AbstractActionSupport {

	/**
	 * 跳转到登录页面
	 */
	@Override
	public String execute() throws Exception {
		return super.execute();
	}
	
	
}
