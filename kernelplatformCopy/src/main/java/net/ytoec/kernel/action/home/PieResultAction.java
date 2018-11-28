/**
 * Feb 27, 20125:14:44 PM
 * wangyong
 */
package net.ytoec.kernel.action.home;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.UserService;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DatasetGroup;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


/**
 * @author wangyong
 * Feb 21, 2012
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class PieResultAction extends AbstractActionSupport {
	
	@Inject
	private OrderService<Order> orderService;
	@Inject
	private UserService<User> userService;
	
	private JFreeChart chart;
	
	private String someDay;
	//分仓账号名
	private Integer vipName;

    /** 
     * 创建饼图数据集 
     */  
    private PieDataset getDataset() {  
    	DefaultPieDataset piedataset = new DefaultPieDataset();
    	User currentUser = super.readCookieUser();
    	List<String> custormerId=Resource.getBindedCustomerIdList(currentUser);
    	Map<String, Number> map = orderService.getPieChart(someDay, custormerId);
    	piedataset.setValue("接单中",map.get("ACCEPT"));
    	piedataset.setValue("已签收",map.get("SIGNED"));
    	piedataset.setValue("走件中",map.get("ONGOING"));
    	piedataset.setValue("正在派送",map.get("SENT_SCAN"));
        return piedataset;  
    }
    
    /**卖家饼图*/
    public String getPieDataJson(){
        User currentUser = super.readCookieUser();
        List<String> custormerId=Resource.getBindedCustomerIdList(currentUser);
        Map<String, Number> map = orderService.getPieChart(someDay, custormerId);
        StringBuilder jsonStrBuilder = new StringBuilder("{");
        int ACCEPT = map.get("ACCEPT") == null ? 0 :  map.get("ACCEPT").intValue();
        int SIGNED = map.get("SIGNED") == null ? 0 :  map.get("SIGNED").intValue();
        int SENT_SCAN =  map.get("SENT_SCAN") == null ? 0 :  map.get("SENT_SCAN").intValue();
        int ONGOING =  map.get("ONGOING") == null ? 0 :  map.get("ONGOING").intValue();
        jsonStrBuilder.append("'type': 'pie','data':[");
        jsonStrBuilder.append("['接单中',"+ACCEPT+"]");
        jsonStrBuilder.append(",['走件中',"+ONGOING+"]");
        jsonStrBuilder.append(",['正在派送',"+SENT_SCAN+"]");
        jsonStrBuilder.append(",['已签收',"+SIGNED+"]");
        jsonStrBuilder.append("]}");
        putMsg(JsonResponse.INFO_TYPE_SUCCESS, ONGOING+SIGNED+SENT_SCAN > 0, jsonStrBuilder.toString(), "");
        return "jsonRes";
    }
    
    /**平台饼图*/
    public String getPieDataJsonIsPintTai(){
        User currentUser = super.readCookieUser();
        List<String> custormerId = new ArrayList<String>();
		//查平台用户下的所有分仓
		if (vipName != null && vipName==0) {
			List<User> pingtailist = userService.pingTaiSelect(currentUser,1);
			for(User user : pingtailist){
				if(user!=null&&user.getTaobaoEncodeKey()!=null&&!user.getTaobaoEncodeKey().equals("")){
					custormerId.add(user.getTaobaoEncodeKey());
				}
			}
		} else if (vipName != null) {
			//如果是单个分仓的时候
			User user = (User) userService.getUserById(vipName);
			if(user!=null&&user.getTaobaoEncodeKey()!=null&&!user.getTaobaoEncodeKey().equals("")){
				custormerId.add(user.getTaobaoEncodeKey());
			}
		}
        Map<String, Number> map = orderService.getPieChart(someDay, custormerId);
        StringBuilder jsonStrBuilder = new StringBuilder("{");
        int ACCEPT = map.get("ACCEPT") == null ? 0 :  map.get("ACCEPT").intValue();
        int SIGNED = map.get("SIGNED") == null ? 0 :  map.get("SIGNED").intValue();
        int SENT_SCAN =  map.get("SENT_SCAN") == null ? 0 : map.get("SENT_SCAN").intValue();
        int ONGOING =  map.get("ONGOING") == null ? 0 :  map.get("ONGOING").intValue();
        jsonStrBuilder.append("'type': 'pie','data':[");
        jsonStrBuilder.append("['接单中',"+ACCEPT+"]");
        jsonStrBuilder.append(",['走件中',"+ONGOING+"]");
        jsonStrBuilder.append(",['正在派送',"+SENT_SCAN+"]");
        jsonStrBuilder.append(",['已签收',"+SIGNED+"]");
        jsonStrBuilder.append("]}");
        putMsg(JsonResponse.INFO_TYPE_SUCCESS, ONGOING+SIGNED+SENT_SCAN > 0, jsonStrBuilder.toString(), "");
        return "jsonRes";
    }
	      
    public JFreeChart getChart() {
    	// 配置字体（解决中文乱码的通用方法）  
        Font kfont = new Font("宋体", Font.PLAIN, 14); // 图表显示对象
        Font legendfont = new Font("宋体", Font.PLAIN, 15); // 图例
        Font titleFont = new Font("隶书", Font.BOLD, 20); // 图片标题
    	//定义图表对象
		chart = ChartFactory.createPieChart3D(someDay+"企业物流状态",getDataset(),false,false,false);
		chart.getTitle().setFont(titleFont);
		//获得图表显示对象
		PiePlot3D pieplot = (PiePlot3D)chart.getPlot();
		pieplot.setNoDataMessage("没有数据");
		pieplot.setCircular(true);
		//pieplot.setForegroundAlpha(0.65f);
		pieplot.setLabelGap(0.01D);//间距
		pieplot.setBackgroundPaint(Color.white);
		pieplot.setOutlinePaint(Color.white);
		
		//设置图表标签字体
		pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0},{2}",NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
		pieplot.setLabelFont(new Font("SansSerif",Font.BOLD,0));
		pieplot.setLabelBackgroundPaint(Color.white);
		pieplot.setLabelLinksVisible(false);
		pieplot.setLabelOutlineStroke(new BasicStroke(0));
		pieplot.setLabelOutlinePaint(Color.white);
		pieplot.setLabelShadowPaint(Color.white);
		pieplot.setSectionPaint("已签收", Color.decode("#68AD45"));        
		pieplot.setSectionPaint("走件中", Color.decode("#1C75BC"));        
		pieplot.setSectionPaint("正在派送", Color.decode("#FFC83B"));
		
		pieplot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator("{0},{2}",NumberFormat.getNumberInstance(), new DecimalFormat("0.00%")));
		LegendTitle legendtitle = new LegendTitle(chart.getPlot());
		legendtitle.setPosition(RectangleEdge.RIGHT);
		legendtitle.setPadding(0, 100D, 0, 0);
		chart.addSubtitle(legendtitle);
		chart.getLegend().setItemFont(legendfont);
		return chart;
    }  
      
    @Override  
    public String execute() throws Exception {  
        return SUCCESS;  
    }

	public String getSomeDay() {
		return someDay;
	}

	public void setSomeDay(String someDay) {
		this.someDay = someDay;
	}

	public Integer getVipName() {
		return vipName;
	}

	public void setVipName(Integer vipName) {
		this.vipName = vipName;
	}  
	      
}
