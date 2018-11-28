package net.ytoec.kernel.action.orderCommand;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class OrderCommandAction {
	public String list(){
		return "success";
	}
}
