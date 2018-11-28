package net.ytoec.kernel.action.partern;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.ZebraPartern;
import net.ytoec.kernel.service.ZebraParternService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ibm.icu.text.SimpleDateFormat;

/**
 * 密钥管理Action
 * 
 * @date 2013-5-20
 * @author tfhuang
 * 
 */
@Controller
@Scope("prototype")
public class ParternAction<T extends ZebraPartern> extends AbstractActionSupport {

	private static Logger logger = LoggerFactory.getLogger(ParternAction.class);

	private static final long serialVersionUID = -4983298684253497729L;

	@Inject
	private ZebraParternService<ZebraPartern> zebraParternService;

	private List<ZebraPartern> zebraParterns = new ArrayList<ZebraPartern>();

	private ZebraPartern zebraPartern;

	
	private Pagination<ZebraPartern> pagination;

	private Integer currentPage = 1;
	
	private String paginationMessage=null;

	/**
	 * 分页查询密钥
	 * 
	 * @return
	 */
	public String search() {
		if (pagination == null) {
			pagination = new Pagination(currentPage, pageNum);
		}
		Map<String, Object> params = new HashMap<String, Object>();

		pagination = zebraParternService.findPageList(pagination, params);
		zebraParterns = pagination.getRecords();

		return "index";
	}

	/**
	 * 修改密钥管理信息
	 * @throws ParseException 
	 */
	public String update() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Date d=df.parse(df.format(new Date()));
		zebraPartern=new ZebraPartern();
		zebraPartern.setCustomerCode("123456");
		zebraPartern.setParternCode("abcd");
		zebraPartern.setUpdateTime(d);
		if(zebraPartern==null){
			paginationMessage="密钥不可为空!";
			return "updateError";
		}
		zebraParternService.editByCustomerCode(zebraPartern);
		return "edit";
	}
	/**
	 * 删除密钥
	 */
	public String delete(){
		zebraPartern=new ZebraPartern(); 
		zebraPartern.setCustomerCode("123456");
		if(zebraPartern.getCustomerCode()==null){
			paginationMessage="用户编号不可为空!";
			return "delError";
		}
		
		zebraParternService.deleteByCustomerCode(zebraPartern);
		return "delete";
	}

	/**
	 * 添加密钥管理信息
	 * @throws ParseException 
	 */
	public String add() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		Date d=df.parse(df.format(new Date()));
		zebraPartern=new ZebraPartern(); 
		zebraPartern.setCustomerCode("123456");
		zebraPartern.setParternCode("53532");
		zebraPartern.setUpdateTime(d);
		
		
		zebraPartern.setUpdateTime(d);
		if(zebraPartern.getParternCode()==null){
			paginationMessage="密钥不可为空!";
			
			return "addError";
		}
		
		zebraParternService.insertSelective(zebraPartern);
		return "add";
	}

	/**
	 * 弹出新增密钥窗体方法
	 */
	public String padd() {
		return "padd";
	}

	/**
	 * 弹出修改密钥窗体方法
	 */
	public String pupdate() {
		return "pupdate";
	}

	/**
	 * panhailing 2013-03-28 手动同步密钥
	 */
	public void synchroKey() {
		logger.info("同步密钥.....");
		zebraParternService.zebraParternTimer();
	}

	public List<ZebraPartern> getZebraParterns() {
		return zebraParterns;
	}

	public ZebraParternService<ZebraPartern> getZebraParternService() {
		return zebraParternService;
	}

	public void setZebraParternService(
			ZebraParternService<ZebraPartern> zebraParternService) {
		this.zebraParternService = zebraParternService;
	}

	public ZebraPartern getZebraPartern() {
		return zebraPartern;
	}

	public void setZebraPartern(ZebraPartern zebraPartern) {
		this.zebraPartern = zebraPartern;
	}

	public Pagination<ZebraPartern> getPagination() {
		return pagination;
	}

	public void setPagination(Pagination<ZebraPartern> pagination) {
		this.pagination = pagination;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getPaginationMessage() {
		return paginationMessage;
	}

	public void setPaginationMessage(String paginationMessage) {
		this.paginationMessage = paginationMessage;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setZebraParterns(List<ZebraPartern> zebraParterns) {
		this.zebraParterns = zebraParterns;
	}



}
