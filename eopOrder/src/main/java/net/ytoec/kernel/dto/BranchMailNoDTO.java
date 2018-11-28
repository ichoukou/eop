package net.ytoec.kernel.dto;

/**
 * 使用mailNo查询结果对象
 * @author Administrator
 *
 */

public class BranchMailNoDTO {

    //运单号
    private String mailNo;
    //组织编码，即网点编码，如果运单号不存在，则返回空
    private String orgCode;
    //如果某运单查不到，请返回原因
    private String failed;
    
    
    public String getFailed() {
        return failed;
    }

    
    public void setFailed(String failed) {
        this.failed = failed;
    }

    public String getMailNo() {
        return mailNo;
    }
    
    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }
    
    public String getOrgCode() {
        return orgCode;
    }
    
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }
    
}
