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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.Order;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.OrderService;
import net.ytoec.kernel.service.SMSBuyersService;

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
public class PieUserAction extends AbstractActionSupport {
	
	@Inject
	private OrderService<Order> orderService;
	
	@Inject
	private SMSBuyersService smsBuyersService;
	
	private JFreeChart chart;
	
	private String someDay;
	
    /** 
     * 创建饼图数据集 
     */  
    private PieDataset getDataset() {  
    	DefaultPieDataset piedataset = new DefaultPieDataset();
    	User currentUser = super.readCookieUser();
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("userIds", Resource.getBindedUserIdList(currentUser));
    	params.put("userGrade", "0");
    	piedataset.setValue("普通会员(" + smsBuyersService.getCountByUserGrade(params) + ")", smsBuyersService.getCountByUserGrade(params));
    	params.put("userGrade", "1");
    	piedataset.setValue("高级会员(" + smsBuyersService.getCountByUserGrade(params) + ")", smsBuyersService.getCountByUserGrade(params));
    	params.put("userGrade", "2");
    	piedataset.setValue("VIP会员(" + smsBuyersService.getCountByUserGrade(params) + ")", smsBuyersService.getCountByUserGrade(params));
    	params.put("userGrade", "3");
    	piedataset.setValue("至尊VIP会员(" + smsBuyersService.getCountByUserGrade(params) + ")", smsBuyersService.getCountByUserGrade(params));
        return piedataset;  
    }
	      
    public JFreeChart getChart() {
    	// 配置字体（解决中文乱码的通用方法）  
        Font kfont = new Font("宋体", Font.PLAIN, 14); // 图表显示对象
        Font legendfont = new Font("宋体", Font.PLAIN, 15); // 图例
        Font titleFont = new Font("隶书", Font.BOLD, 20); // 图片标题
    	//定义图表对象
		chart = ChartFactory.createPieChart3D(null,getDataset(),false,false,false);
		//chart.getTitle().setFont(titleFont);
		chart.setTitle("会员等级分布");
		//获得图表显示对象
		PiePlot3D pieplot = (PiePlot3D)chart.getPlot();
		pieplot.setNoDataMessage("没有数据");
		pieplot.setCircular(true);
		pieplot.setForegroundAlpha(0.65f);
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
		pieplot.setSectionPaint("普通会员", Color.blue);   
		pieplot.setSectionPaint("高级会员", Color.red);        
		pieplot.setSectionPaint("VIP会员", Color.green);
		pieplot.setSectionPaint("至尊VIP会员)", Color.decode("#8F4586"));
		
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
	      
}
