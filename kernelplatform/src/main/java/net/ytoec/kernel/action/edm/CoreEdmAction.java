package net.ytoec.kernel.action.edm;

import net.ytoec.kernel.action.remote.AbstractInterfaceAction;
import net.ytoec.kernel.dataobject.CoreEdm;
import net.ytoec.kernel.service.CoreEdmService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class CoreEdmAction extends AbstractInterfaceAction {

    private static Logger           logger      = LoggerFactory.getLogger(CoreEdmAction.class);

    private static final String     SOURCE_TYPE = "type";
    private static final String     IP          = "ip";
    private static final String     EMAIL       = "email";
    private String                  result      = "email";

    @Autowired
    private CoreEdmService<CoreEdm> coreEdmService;

    public String addCoreEdm() {
    	
		String ip = null;	
		if (request.getHeader("x-forwarded-for") == null) {
			ip = request.getRemoteAddr();
		}else {
			ip = request.getHeader("x-forwarded-for");
		}

        String sourceType = request.getParameter(SOURCE_TYPE);
        String email = request.getParameter(EMAIL);

        CoreEdm coreEdm = new CoreEdm();
        coreEdm.setSourceType(sourceType);
        coreEdm.setEmail(email);
        coreEdm.setIp(ip);

        boolean flag = false;

        flag = coreEdmService.addCoreEdm(coreEdm);
        result = String.valueOf(flag);

        return "success";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
