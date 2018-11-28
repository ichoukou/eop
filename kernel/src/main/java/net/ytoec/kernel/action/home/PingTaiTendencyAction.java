/**
 * Feb 27, 20125:14:44 PM
 * wangyong
 */
package net.ytoec.kernel.action.home;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import net.ytoec.kernel.action.common.AbstractActionSupport;
import net.ytoec.kernel.dataobject.MailTendency;
import net.ytoec.kernel.dataobject.User;
import net.ytoec.kernel.service.MailTendencyService;
import net.ytoec.kernel.service.UserService;
import net.ytoec.kernel.util.DateUtil;

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
public class PingTaiTendencyAction extends AbstractActionSupport {

	private static final long serialVersionUID = 1935592746823978475L;
	@Inject
	private UserService<User> userService;
	@Inject
	private MailTendencyService<MailTendency> mailTendencyService;

	private String startDate;
	private String endDate;
	private Integer timeLimit;
	private JFreeChart chart;
	private String title;

	public String index() {
		return "index";
	}

	private Integer vipName;

	public Integer getVipName() {
		return vipName;
	}

	public void setVipName(Integer vipName) {
		this.vipName = vipName;
	}

	/**
	 * 创建折线图数据集
	 */
	public TimeSeriesCollection createTimeSeries() {

		//timeLimit++;
		// 访问量统计时间线
		TimeSeries timeSeries = new TimeSeries("近" + timeLimit + "天运单量走势");



		// 时间曲线数据集合
		TimeSeriesCollection lineDataset = new TimeSeriesCollection();
		// 构建数据集
		User currentUser = super.readCookieUser();
		List<Integer> userId = new ArrayList<Integer>();
		List<Map<String, Integer>> dataList = new ArrayList<Map<String, Integer>>();
		// 查平台用户下的所有分仓
		if (vipName != null && vipName == 0) {
			List<User> pingtailist = userService.pingTaiSelect(currentUser, 1);
			for (User user : pingtailist) {
				userId.add(user.getId());
			}
			dataList = mailTendencyService.pingTaiSeries(userId,
					this.getStartDate(), this.getEndDate(), timeLimit);
		} else if (vipName != null) {
			// 如果是单个分仓的时候
			User user = (User) userService.getUserById(vipName);
			dataList = mailTendencyService.singleSeries(user.getId(),
					this.getStartDate(), this.getEndDate(), timeLimit);
		} else if (vipName == null) {
			// 如果为空的时间，补曲线日期
			List<Map<String, Integer>> list = new ArrayList<Map<String, Integer>>();
			if (timeLimit > 0) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				GregorianCalendar gc = new GregorianCalendar();
				while (timeLimit > 0) {
					Map<String, Integer> map = new HashMap<String, Integer>();
					gc.setTime(DateUtil.valueOfStandard(endDate));
					gc.add(Calendar.DAY_OF_MONTH, -timeLimit);
					int year = gc.get(Calendar.YEAR);
					int month = gc.get(Calendar.MONTH);
					int day = gc.get(Calendar.DAY_OF_MONTH);
					String curDate = sdf.format(gc.getTime());
					map.put("year", year);
					map.put("month", month + 1);
					map.put("day", day);
					map.put("mailNum", 0);
					dataList.add(map);
					timeLimit--;
				}
			}
		}

		for (Map<String, Integer> map : dataList) {
			timeSeries.add(
					new Day(map.get("day"), map.get("month"), map.get("year")),
					map.get("mailNum"));
		}
		lineDataset.addSeries(timeSeries);
		return lineDataset;

	}

	public JFreeChart getChart() {
		if ((startDate == null || startDate.equals(""))
				&& (endDate == null || endDate.equals(""))) {
			startDate = DateUtil.dateArithmetic(new Date(), 6);
			endDate = DateUtil.dateArithmetic(new Date(), 0);
			this.setStartDate(startDate);
			this.setEndDate(endDate);
		}
		double d = DateUtil.dayInterval(DateUtil.valueOfStandard(endDate),
				DateUtil.valueOfStandard(startDate));

		timeLimit = (int) d;
		title = "近" + timeLimit + "天运单量走势";
		chart = ChartFactory.createTimeSeriesChart(title, "天", "运单量",
				this.createTimeSeries(), true, true, true);

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
		chart.setTitle(new TextTitle(title, new Font("黑体", Font.ITALIC, 15)));
		chart.setAntiAlias(true);
		chart.clearSubtitles();

		return chart;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String execute() throws Exception {
		return SUCCESS;
	}



}
