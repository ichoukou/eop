package net.ytoec.kernel.action.remote.process;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.ytoec.kernel.action.remote.xml.UpdateWaybillInfo;
import net.ytoec.kernel.common.Resource;
import net.ytoec.kernel.dataobject.OrderFormInfo;
import net.ytoec.kernel.dataobject.Product;
import net.ytoec.kernel.dataobject.SpecialType;
import net.ytoec.kernel.dataobject.TraderInfo;
import net.ytoec.kernel.dto.JGOrderDTO;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单json对象转换 create by hufei create time 2013-04-22
 */
public class OrderProcessor {

	private static Logger logger = LoggerFactory.getLogger(OrderProcessor.class);

	/**
	 * description 将json转换成OrderFormInfo对象 created by hufei@2013-04-22
	 */
	public OrderFormInfo createToObject(JGOrderDTO jGOrder) {
		// 将json字符串转换为JSONObject对象
		JSONObject jsonObject = JSONObject.fromObject(jGOrder
				.getCommendContent());
		// 取出json字符串中的order对象
		JSONObject jsonObjectOrder = jsonObject.getJSONObject("order");
		// 取出货品数组
		JSONArray jsonArray = jsonObjectOrder.getJSONArray("ordGoodsDetails");
		// 根据jGOrder构造order
		OrderFormInfo orderFormInfoder = new OrderFormInfo();
		if (jsonObject.getString("orderChannelCode").equals("TAOBAO_STD")) {
			orderFormInfoder.setClientId("Taobao");
		}
		// 物流公司
		orderFormInfoder.setLogisticProviderId("YTO");
		// 物流号
		orderFormInfoder.setTxLogisticId(jsonObject.getString("logisticCode"));
		// 客户ID
		orderFormInfoder.setCustomerId(jsonObjectOrder
				.getString("senderEncryptionCode"));
		// 交易号
		orderFormInfoder.setTradeNo(jsonObjectOrder.getString("tradeNo"));
		// 运单号
		orderFormInfoder.setMailNo(jsonObjectOrder.getString("waybillNo"));
		// 发货单号 没有
		// orderFormInfoder.setDeliverNo(deliverNo);
		// 发货单 代收款 没有
		// orderFormInfoder.setAgencyFundEle(agencyFundEle);

		orderFormInfoder.setFlag(jsonObjectOrder.getString("orderFlag"));
		// 寄件人
		TraderInfo sender = new TraderInfo();
		// 发件人姓名
		sender.setName(jsonObjectOrder.getString("senderName"));
		// 发件人邮编
		sender.setPostCode(jsonObjectOrder.getString("senderPostalCode"));
		// 发件人手机
		sender.setPhone(jsonObjectOrder.getString("senderPhone"));
		// 发件人固定电话
		sender.setMobile(jsonObjectOrder.getString("senderMobile"));
		// 发件人省份
		sender.setProv(jsonObjectOrder.getString("senderProvName"));

		// 发件人省份code
		if (StringUtils.isBlank(jsonObjectOrder.getString("senderProvCode"))) {
			String senderProv = jsonObjectOrder.getString(
					"senderProvName").trim();

			if (StringUtils.isNotBlank(senderProv)) {

				senderProv = senderProv.indexOf("省") > -1 ? senderProv
						.replace("省", "") : senderProv;
						sender.setNumProv(Resource.getCodeByName(senderProv));
			} else {
				sender.setNumProv(0);
			}
		} else {
			sender.setNumProv(Integer.valueOf(jsonObjectOrder
					.getString("senderProvCode")));
		}
		// 发件人市
		sender.setCity(jsonObjectOrder.getString("senderCityName"));
		// 发件人市code
		// sender.setNumCity(jsonObjectOrder.getString("senderCityCode"));
		// 收件人区
		sender.setDistrict(jsonObjectOrder.getString("senderCountyName"));
		// 收件人区code
		// sender.setNumDistrict(jsonObjectOrder.getString("senderCountyCode"));
		// 详细地址
		
		String adress = jsonObjectOrder.getString("senderAddress");
		if(StringUtils.isNotBlank(adress)){
			adress = adress.length() > 200 ? adress.substring(0, 200) : adress;
		}
		sender.setAddress(adress);
		
		orderFormInfoder.setSender(sender);

		// 收件人
		TraderInfo receiver = new TraderInfo();
		// 收件人姓名
		receiver.setName(jsonObjectOrder.getString("recipientName"));
		// 收件人邮编
		receiver.setPostCode(jsonObjectOrder.getString("recipientPostalCode"));
		// 收件人固定电话
		receiver.setPhone(jsonObjectOrder.getString("recipientPhone"));
		// 收件人手机
		receiver.setMobile(jsonObjectOrder.getString("recipientMobile"));
		// 收件人省份
		receiver.setProv(jsonObjectOrder.getString("recipientProvName"));

		// 收件人省份code
		if (StringUtils.isBlank(jsonObjectOrder.getString("recipientProvCode"))) {
			String recipientProv = jsonObjectOrder.getString(
					"recipientProvName").trim();

			if (StringUtils.isNotBlank(recipientProv)) {

				recipientProv = recipientProv.indexOf("省") > -1 ? recipientProv
						.replace("省", "") : recipientProv;
				receiver.setNumProv(Resource.getCodeByName(recipientProv));
			} else {
				receiver.setNumProv(0);
			}
		} else {
			receiver.setNumProv(Integer.valueOf(jsonObjectOrder
					.getString("recipientProvCode")));
		}

		// 收件人市
		receiver.setCity(jsonObjectOrder.getString("recipientCityName"));
		// 收件人市code
		// receiver.setNumCity(jsonObjectOrder.getString("recipientCityCode"));
		// 收件人区
		receiver.setDistrict(jsonObjectOrder.getString("recipientCountyName"));
		// 收件人区code
		// receiver.setNumDistrict(jsonObjectOrder
		// .getString("recipientCountyCode"));
		
		// 详细地址
		String reAdress = jsonObjectOrder.getString("recipientAddress");
		if(StringUtils.isNotBlank(reAdress)){
			reAdress = reAdress.length() > 200 ? reAdress.substring(0, 200) : reAdress;
		}
		
		receiver.setAddress(reAdress);
		orderFormInfoder.setReceiver(receiver);

		orderFormInfoder.setSendStartTime(jsonObjectOrder
				.getString("startCanvassTime"));
		orderFormInfoder.setSendEndTime(jsonObjectOrder
				.getString("endCanvassTime"));
		// 货物信息
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObjectProduct = jsonArray.getJSONObject(i);
			Product product = new Product();
			// 货物名称
			product.setItemName(jsonObjectProduct.getString("goodsName"));
			// 货物数量
			if (!jsonObjectProduct.getString("goodsQty").isEmpty()) {
				product.setItemNumber(Integer.parseInt(jsonObjectProduct
						.getString("goodsQty")));
			}
			// 备注
			product.setRemark(jsonObjectProduct.getString("remark"));
			// 货物价格
			if (!jsonObjectProduct.getString("goodsFee").isEmpty()
					&& !jsonObjectProduct.getString("goodsFee")
							.equalsIgnoreCase("null")) {
				product.setItemValue(Double.parseDouble(jsonObjectProduct
						.getString("goodsFee")));
			}
			orderFormInfoder.addItem(product);
		}
		// 保价
		// orderFormInfoder.setInsuranceValue(jsonObjectOrder.getDouble("offerFee"));
		// 是否打包
		orderFormInfoder.setPackageOrNot(jsonObjectOrder
				.getBoolean("isPackage"));
		// 特殊商品性质
		SpecialType result = SpecialType.valueOfByCode(jsonObjectOrder
				.getString("specialGoodsType"));
		orderFormInfoder.setSpecial(result);
		orderFormInfoder.setRemark(jsonObjectOrder.getString("remark"));
		orderFormInfoder.setOrderType(jsonObjectOrder
				.getString("orderTypeCode"));
		// orderFormInfoder.setServiceType(serviceType);
		orderFormInfoder.setGoodsValue(jsonObjectOrder.getString("offerFee"));
		orderFormInfoder.setItemsValue(jsonObjectOrder
				.getString("goodsTotalFee"));
		orderFormInfoder.setTotalServiceFee(jsonObjectOrder
				.getString("totalServiceFee"));
		orderFormInfoder.setBuyServiceFee(jsonObjectOrder
				.getString("buyServiceFee"));
		orderFormInfoder.setCodSplitFee(jsonObjectOrder
				.getString("codSplitFee"));
		// orderFormInfoder.setType(type);
		orderFormInfoder.setItemsWeight(jsonObjectOrder.getString("weight"));
		return orderFormInfoder;
	}

	public UpdateWaybillInfo cancelOrWayMergerToObject(JGOrderDTO jGOrder,
			Integer status) {

		// 将json字符串转换为JSONObject对象
		JSONObject jsonObject = JSONObject.fromObject(jGOrder
				.getCommendContent());
		// 取出货品数组
		UpdateWaybillInfo updateWaybillInfo = new UpdateWaybillInfo();
		// 物流号
		updateWaybillInfo.setTxLogisticId(jsonObject.getString("logisticCode"));
		if (jsonObject.getString("orderChannelCode").equals("TAOBAO_STD")) {
			updateWaybillInfo.setClientId("Taobao");
		}
		if (status == 1) { // 订单取消
			updateWaybillInfo.setInfoContent("WITHDRAW");
		} else if (status == 2) {
			updateWaybillInfo.setMailNo(jsonObject.getString("wayBillNo"));
		}
		return updateWaybillInfo;
	}
}
