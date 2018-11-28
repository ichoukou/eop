/**
 * Feb 27, 20125:14:44 PM
 * wangyong
 */
package net.ytoec.kernel.action.home;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.dataobject.JsonResponse;
import net.ytoec.kernel.dataobject.MailTendency;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.MailTendencyService;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * @author wangyong Feb 21, 2012
 */
@Controller
@Scope("prototype")
@SuppressWarnings("all")
public class TendencyAction extends AbstractActionSupport {

	private static final long serialVersionUID = 1935592746823978475L;

	@Inject
	private MailTendencyService<MailTendency> mailTendencyService;

	private int timeLimit;
	private JFreeChart chart;
	private String lineData;

	public String index() {
		return "index";
	}
	/**
	 * 创建折线图数据集  改版新页面
	 */
	public String getLineDataJson(){
		User currentUser = super.readCookieUser();
		String text = "近"+timeLimit+"天运单量走势";
		
		List<Map<String, Integer>> dataList = mailTendencyService.createTimeSeries(currentUser.getId(), timeLimit);
		StringBuilder date = new StringBuilder();
		StringBuilder mailNum = new StringBuilder();
		int num=1;
		for (Map<String, Integer> map : dataList) {
					
					String time = map.get("day").toString()+"-"+map.get("month").toString()+"月";
					 date.append("'"+time+"',");
					 mailNum.append("'"+map.get("mailNum").toString()+"',");
		}
//		System.out.println(date);
//		System.out.println(mailNum);
//		System.out.println(num);
		StringBuilder jsonStrBuilder = new StringBuilder("{");
		jsonStrBuilder.append("'type': 'line','data':{");
		jsonStrBuilder.append("'text':'"+text+"',");
		jsonStrBuilder.append("'x':["+date.substring(0, date.length()-1)+"],");
		jsonStrBuilder.append("'y':["+mailNum.substring(0, mailNum.length()-1)+"],"+"'z':'"+num+"'}}");
		
//		System.out.println(jsonStrBuilder.toString());
		
	    putMsg(JsonResponse.INFO_TYPE_SUCCESS, num>0, jsonStrBuilder.toString(), "");
		return "jsonRes";
	}
	
	/**
	 * 创建折线图数据集
	 */
	public TimeSeriesCollection createTimeSeries() {
		// 访问量统计时间线
		TimeSeries timeSeries = new TimeSeries("近" + timeLimit + "天运单量走势");
		// 时间曲线数据集合
		TimeSeriesCollection lineDataset = new TimeSeriesCollection();
		// 构建数据集
		User currentUser = super.readCookieUser();
		List<Map<String, Integer>> dataList = mailTendencyService
				.createTimeSeries(currentUser.getId(), timeLimit);
		for (Map<String, Integer> map : dataList) {
			timeSeries.add(
					new Day(map.get("day"), map.get("month"), map.get("year")),
					map.get("mailNum"));
		}
		lineDataset.addSeries(timeSeries);
		return lineDataset;

	}

	public JFreeChart getChart() {
		chart = ChartFactory.createTimeSeriesChart("近" + timeLimit + "天运单量走势",
				"天", "运单量", this.createTimeSeries(), true, true, true);

		Font xfont = new Font("黑体", Font.ITALIC, 0); // X轴
		Font x_tipsfont = new Font("宋体", Font.PLAIN, 12); // X轴坐标
		Font yfont = new Font("黑体", Font.ITALIC, 17); // Y轴
		Font y_tipsfont = new Font("宋体", Font.PLAIN, 12); // Y轴坐标
		Font kfont = new Font("宋体", Font.ITALIC, 16); // 底部
		Font titleFont = new Font("隶书", Font.BOLD, 22); // 图片标题

		XYPlot plot = (XYPlot) chart.getPlot();
		XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) plot
				.getRenderer();
		// 设置网格背景颜色
		plot.setBackgroundPaint(Color.white);
		// 设置网格竖线颜色
		plot.setDomainGridlinePaint(Color.blue);
		// 设置网格横线颜色
		plot.setRangeGridlinePaint(Color.blue);
		// 设置曲线图与xy轴的距离
		plot.setAxisOffset(new RectangleInsets(0D, 0D, 0D, 10D));

		// 设置曲线是否显示数据点
		xylineandshaperenderer.setBaseShapesVisible(true);

		// 设置曲线显示各数据点的值
		XYItemRenderer xyitem = plot.getRenderer();
		xyitem.setBaseItemLabelsVisible(true);
		xyitem.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_RIGHT));
		xyitem.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		xyitem.setBaseItemLabelFont(new Font("宋体", 1, 13));
		xyitem.setSeriesPaint(0, Color.blue);
		plot.setRenderer(xyitem);

		XYPlot xyPlot = chart.getXYPlot();
		xyPlot.getDomainAxis().setLabelFont(xfont);
		xyPlot.getDomainAxis().setTickLabelFont(x_tipsfont);
		xyPlot.getDomainAxis().setAxisLinePaint(Color.black);
		xyPlot.getDomainAxis().setPositiveArrowVisible(true);
		DateAxis dateAxis = (DateAxis) xyPlot.getDomainAxis();
		dateAxis.setDateFormatOverride(new SimpleDateFormat("yy-MM-dd"));
		xyPlot.setDomainAxis(dateAxis);

		xyPlot.getRangeAxis().setLabelFont(yfont);
		xyPlot.getRangeAxis().setTickLabelFont(y_tipsfont);
		xyPlot.getRangeAxis().setAutoRange(true);
		xyPlot.getRangeAxis().setAutoRangeMinimumSize(25000);
		xyPlot.getRangeAxis().setLowerBound(0);
		xyPlot.getRangeAxis().setAxisLinePaint(Color.black);
		xyPlot.getRangeAxis().setPositiveArrowVisible(true);
		chart.getLegend().setItemFont(kfont);

		// 设置子标题
		// TextTitle subtitle = new TextTitle("（2012年度）", new Font("黑体",
		// Font.BOLD, 12));
		// chart.addSubtitle(subtitle);
		// 设置主标题
		chart.setTitle(new TextTitle("近" + timeLimit + "天运单量走势", new Font("黑体",
				Font.ITALIC, 15)));
		chart.setAntiAlias(true);
		chart.clearSubtitles();

		return chart;
	}

	public String getLineData() {
		return lineData;
	}

	public void setLineData(String lineData) {
		this.lineData = lineData;
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

}
