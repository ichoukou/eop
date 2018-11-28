package net.ytoec.kernel.dto;

/**
 * 用于问题件更新的实体类，用于把问题件处理结果同步给金刚
 * @author mabo
 *
 */

public class QuestionnaireResultsUpdateDTO {
    
    //问题件Id 金刚中的问题件Id
    private String issueId;
    
    //处理类容，揽收网点对上报网点的回复
    private String dealContent;
    
    //处理结果 :  DEALING处理中，DEALED 处理完毕
    private String status;
    
    //网点编码，和金刚中的组织机构code一致！
    private String createOrgCode;
    
    //创建者姓名,在我们易通的中姓名，与金刚无关
    private String createUserName;
    
    //创建者在易通里的userId，与金刚没有关系
    private String createUserCode;


    //请求是否成功！ true/false
    private String success;
    
    //请求失败原因
    private String reason;

    
    //问题件在金刚的创建时间
    /**
     *    电商尽量使该字段不为空。该字段的值是从金刚同步到电商的，如果电商同步
     *    出错，这个时候我们就无法传递该值，所以不能保证总是不为空
     */
    //电商尽量使该字段不为空。该字段的值是从金刚同步到电商的，
    //如果电商同步出错，这个时候我们就无法传递该值，所以不能保证总是不为空
    private String issueCreateTime;
    
    
    public String getIssueCreateTime() {
        return issueCreateTime;
    }


    
    public void setIssueCreateTime(String issueCreateTime) {
        this.issueCreateTime = issueCreateTime;
    }


    public String getIssueId() {
        return issueId;
    }

    
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    
    public String getDealContent() {
        return dealContent;
    }

    
    public void setDealContent(String dealContent) {
        this.dealContent = dealContent;
    }

    
    public String getStatus() {
        return status;
    }

    
    public void setStatus(String status) {
        this.status = status;
    }

    
    public String getCreateOrgCode() {
        return createOrgCode;
    }

    
    public void setCreateOrgCode(String createOrgCode) {
        this.createOrgCode = createOrgCode;
    }

    
    public String getCreateUserName() {
        return createUserName;
    }

    
    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    
    public String getCreateUserCode() {
        return createUserCode;
    }

    
    public void setCreateUserCode(String createUserCode) {
        this.createUserCode = createUserCode;
    }
    
    public String getSuccess() {
        return success;
    }
    
    public void setSuccess(String success) {
        this.success = success;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
}
