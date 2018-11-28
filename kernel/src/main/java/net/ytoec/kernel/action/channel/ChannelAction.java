/**
 * ChannelAction.java
 * Wangyong
 * 2011-8-23 上午09:12:45
 */
package net.ytoec.kernel.action.channel;

import java.util.List;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Constants;
import net.ytoec.kernel.common.Pagination;
import net.ytoec.kernel.dataobject.Channel;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.ChannelService;

/**
 * 渠道信息Action
 * @author Wangyong
 * @2011-8-23
 * net.ytoec.kernel.action.channel
 */
@Controller
@Scope("prototype")
public class ChannelAction extends AbstractActionSupport {

	@Inject
	public ChannelService<Channel> channelService;
	
	//private static Integer pageNum = 5;
	private Integer currentPage = 1;
	private Pagination pagination; //分页对象
	
	private List<Channel> channelList;
	private Channel channel;
	private Integer id;
	private String response;
	
	/*
	 * 获取渠道管理员渠道信息列表
	 */
	public String list(){
		User currentUser = super.readCookieUser();
		response = null;
		pagination = new Pagination(currentPage, pageNum);
		if(currentUser.getUserType()!=null && currentUser.getUserType().equals("3")){
			//分页list
			channelList = channelService.getAllChannel(pagination, true);
			if(channelService.getAllChannel(pagination, false)!=null)
				pagination.setTotalRecords(channelService.getAllChannel(pagination, false).size());
			else
				pagination.setTotalRecords(0);
		}else{
			pagination.setTotalRecords(0);
			response = "您没有查询渠道信息的权限!";
		}
		return "list";
	}
	
	/*
	 * 根据Id查找渠道数据
	 */
	public Channel getChannelById(Integer id){
		Channel chan = null;
		if(id!=null){
			chan = channelService.getChannelById(id);
		}
		return chan;
	}
	
	/*
	 * 跳转到增加渠道信息数据界面
	 */
	public String addUI(){
		return "addUI";
	}
	
	/*
	 * 增加渠道信息数据
	 */
	public String add(){
		response = null;
		if(channel!=null){
			if(channelService.addChannel(channel)){
				response = "添加成功";
				channelService.putChannelCache(channel.getClientId(), channel);//加入oscache缓存
			}
			else
				response = "添加失败";
		}
		return "add";
	}
	
	/*
	 * 跳转到修改渠道信息数据界面
	 */
	public String editUI(){
		channel = null;
		if(id!=null){
			channel = channelService.getChannelById(id);
		}
		return "editUI";
	}
	
	/*
	 * 修改渠道信息数据
	 */
	public String edit(){
		response = null;
		if(channel!=null){
			if(channelService.editChannel(channel)){
				response = "修改成功";
				channelService.putChannelCache(channel.getClientId(), channel);//修改oscache缓存
			}
			else
				response = "修改失败";
		}
		return "edit";
	}
	
	/*
	 * 删除渠道信息
	 */
	public String remove(){
		response = null;
		Channel channel = this.getChannelById(id);
		if(channel!=null){
			if(channelService.removeChannel(channel)) {
				response = "删除成功";
				channelService.removeChannelCache(channel.getClientId());//删除oscache缓存
			} else
				response = "删除失败";
		}
		return "remove";
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public List<Channel> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Channel> channelList) {
		this.channelList = channelList;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
