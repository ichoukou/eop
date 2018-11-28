package net.ytoec.kernel.client.edi;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the net.ytoec.kernel.client.edi package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	private final static QName _AnyURI_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
	private final static QName _Char_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "char");
	private final static QName _UnsignedByte_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/",
			"unsignedByte");
	private final static QName _DateTime_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
	private final static QName _AnyType_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
	private final static QName _UnsignedInt_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/",
			"unsignedInt");
	private final static QName _Int_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "int");
	private final static QName _QName_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "QName");
	private final static QName _UnsignedShort_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/",
			"unsignedShort");
	private final static QName _Decimal_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
	private final static QName _Float_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "float");
	private final static QName _Double_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "double");
	private final static QName _Long_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "long");
	private final static QName _Short_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "short");
	private final static QName _Guid_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "guid");
	private final static QName _Base64Binary_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/",
			"base64Binary");
	private final static QName _Duration_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "duration");
	private final static QName _Byte_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "byte");
	private final static QName _String_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "string");
	private final static QName _UnsignedLong_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/",
			"unsignedLong");
	private final static QName _Boolean_QNAME = new QName(
			"http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
	private final static QName _CommitBackOrderLcid_QNAME = new QName(
			"http://tempuri.org/", "lcid");
	private final static QName _CommitBackOrderOrders_QNAME = new QName(
			"http://tempuri.org/", "orders");
	private final static QName _SynUserInfoInfo_QNAME = new QName(
			"http://tempuri.org/", "info");
	private final static QName _SynUserInfoLcId_QNAME = new QName(
			"http://tempuri.org/", "lcId");
	private final static QName _GetCityProvince_QNAME = new QName(
			"http://tempuri.org/", "province");
	private final static QName _CommitBackOrderStatusResponseCommitBackOrderStatusResult_QNAME = new QName(
			"http://tempuri.org/", "CommitBackOrderStatusResult");
	private final static QName _CommitDeliverInfoResponseCommitDeliverInfoResult_QNAME = new QName(
			"http://tempuri.org/", "CommitDeliverInfoResult");
	private final static QName _CommitBackOrderResponseCommitBackOrderResult_QNAME = new QName(
			"http://tempuri.org/", "CommitBackOrderResult");
	private final static QName _CommitDeliverInfoDeliverInfo_QNAME = new QName(
			"http://tempuri.org/", "deliverInfo");
	private final static QName _SuccessOrdersWaybills_QNAME = new QName(
			"http://tempuri.org/", "waybills");
	private final static QName _GetAreaCity_QNAME = new QName(
			"http://tempuri.org/", "city");
	private final static QName _GetProvinceResponseGetProvinceResult_QNAME = new QName(
			"http://tempuri.org/", "GetProvinceResult");
	private final static QName _GetAreaResponseGetAreaResult_QNAME = new QName(
			"http://tempuri.org/", "GetAreaResult");
	private final static QName _GetOrdersV2ResponseGetOrdersV2Result_QNAME = new QName(
			"http://tempuri.org/", "GetOrdersV2Result");
	private final static QName _GetOrdersResponseGetOrdersResult_QNAME = new QName(
			"http://tempuri.org/", "GetOrdersResult");
	private final static QName _SynUserInfoResponseSynUserInfoResult_QNAME = new QName(
			"http://tempuri.org/", "SynUserInfoResult");
	private final static QName _GetCityResponseGetCityResult_QNAME = new QName(
			"http://tempuri.org/", "GetCityResult");
	private final static QName _GetInBoundOrderResponseGetInBoundOrderResult_QNAME = new QName(
			"http://tempuri.org/", "GetInBoundOrderResult");

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: net.ytoec.kernel.client.edi
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link SuccessOrdersResponse }
	 * 
	 */
	public SuccessOrdersResponse createSuccessOrdersResponse() {
		return new SuccessOrdersResponse();
	}

	/**
	 * Create an instance of {@link SynUserInfo }
	 * 
	 */
	public SynUserInfo createSynUserInfo() {
		return new SynUserInfo();
	}

	/**
	 * Create an instance of {@link CommitBackOrder }
	 * 
	 */
	public CommitBackOrder createCommitBackOrder() {
		return new CommitBackOrder();
	}

	/**
	 * Create an instance of {@link GetOrdersV2 }
	 * 
	 */
	public GetOrdersV2 createGetOrdersV2() {
		return new GetOrdersV2();
	}

	/**
	 * Create an instance of {@link GetCity }
	 * 
	 */
	public GetCity createGetCity() {
		return new GetCity();
	}

	/**
	 * Create an instance of {@link CommitBackOrderStatusResponse }
	 * 
	 */
	public CommitBackOrderStatusResponse createCommitBackOrderStatusResponse() {
		return new CommitBackOrderStatusResponse();
	}

	/**
	 * Create an instance of {@link CommitBackOrderStatus }
	 * 
	 */
	public CommitBackOrderStatus createCommitBackOrderStatus() {
		return new CommitBackOrderStatus();
	}

	/**
	 * Create an instance of {@link CommitDeliverInfoResponse }
	 * 
	 */
	public CommitDeliverInfoResponse createCommitDeliverInfoResponse() {
		return new CommitDeliverInfoResponse();
	}

	/**
	 * Create an instance of {@link CommitBackOrderResponse }
	 * 
	 */
	public CommitBackOrderResponse createCommitBackOrderResponse() {
		return new CommitBackOrderResponse();
	}

	/**
	 * Create an instance of {@link CommitDeliverInfo }
	 * 
	 */
	public CommitDeliverInfo createCommitDeliverInfo() {
		return new CommitDeliverInfo();
	}

	/**
	 * Create an instance of {@link SuccessOrders }
	 * 
	 */
	public SuccessOrders createSuccessOrders() {
		return new SuccessOrders();
	}

	/**
	 * Create an instance of {@link GetArea }
	 * 
	 */
	public GetArea createGetArea() {
		return new GetArea();
	}

	/**
	 * Create an instance of {@link GetProvinceResponse }
	 * 
	 */
	public GetProvinceResponse createGetProvinceResponse() {
		return new GetProvinceResponse();
	}

	/**
	 * Create an instance of {@link GetAreaResponse }
	 * 
	 */
	public GetAreaResponse createGetAreaResponse() {
		return new GetAreaResponse();
	}

	/**
	 * Create an instance of {@link GetOrdersV2Response }
	 * 
	 */
	public GetOrdersV2Response createGetOrdersV2Response() {
		return new GetOrdersV2Response();
	}

	/**
	 * Create an instance of {@link GetInBoundOrder }
	 * 
	 */
	public GetInBoundOrder createGetInBoundOrder() {
		return new GetInBoundOrder();
	}

	/**
	 * Create an instance of {@link GetOrdersResponse }
	 * 
	 */
	public GetOrdersResponse createGetOrdersResponse() {
		return new GetOrdersResponse();
	}

	/**
	 * Create an instance of {@link GetProvince }
	 * 
	 */
	public GetProvince createGetProvince() {
		return new GetProvince();
	}

	/**
	 * Create an instance of {@link SynUserInfoResponse }
	 * 
	 */
	public SynUserInfoResponse createSynUserInfoResponse() {
		return new SynUserInfoResponse();
	}

	/**
	 * Create an instance of {@link GetCityResponse }
	 * 
	 */
	public GetCityResponse createGetCityResponse() {
		return new GetCityResponse();
	}

	/**
	 * Create an instance of {@link GetInBoundOrderResponse }
	 * 
	 */
	public GetInBoundOrderResponse createGetInBoundOrderResponse() {
		return new GetInBoundOrderResponse();
	}

	/**
	 * Create an instance of {@link GetOrders }
	 * 
	 */
	public GetOrders createGetOrders() {
		return new GetOrders();
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
	public JAXBElement<String> createAnyURI(String value) {
		return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
	public JAXBElement<Integer> createChar(Integer value) {
		return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
	public JAXBElement<Short> createUnsignedByte(Short value) {
		return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}
	 * {@link XMLGregorianCalendar }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
	public JAXBElement<XMLGregorianCalendar> createDateTime(
			XMLGregorianCalendar value) {
		return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME,
				XMLGregorianCalendar.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
	public JAXBElement<Object> createAnyType(Object value) {
		return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
	public JAXBElement<Long> createUnsignedInt(Long value) {
		return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
	public JAXBElement<Integer> createInt(Integer value) {
		return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
	public JAXBElement<QName> createQName(QName value) {
		return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
	public JAXBElement<Integer> createUnsignedShort(Integer value) {
		return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
	public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
		return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class,
				null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
	public JAXBElement<Float> createFloat(Float value) {
		return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
	public JAXBElement<Double> createDouble(Double value) {
		return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
	public JAXBElement<Long> createLong(Long value) {
		return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
	public JAXBElement<Short> createShort(Short value) {
		return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
	public JAXBElement<String> createGuid(String value) {
		return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
	public JAXBElement<byte[]> createBase64Binary(byte[] value) {
		return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null,
				((byte[]) value));
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Duration }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
	public JAXBElement<Duration> createDuration(Duration value) {
		return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
	public JAXBElement<Byte> createByte(Byte value) {
		return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
	public JAXBElement<String> createString(String value) {
		return new JAXBElement<String>(_String_QNAME, String.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }
	 * {@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
	public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
		return new JAXBElement<BigInteger>(_UnsignedLong_QNAME,
				BigInteger.class, null, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
	public JAXBElement<Boolean> createBoolean(Boolean value) {
		return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null,
				value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcid", scope = CommitBackOrder.class)
	public JAXBElement<String> createCommitBackOrderLcid(String value) {
		return new JAXBElement<String>(_CommitBackOrderLcid_QNAME,
				String.class, CommitBackOrder.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "orders", scope = CommitBackOrder.class)
	public JAXBElement<String> createCommitBackOrderOrders(String value) {
		return new JAXBElement<String>(_CommitBackOrderOrders_QNAME,
				String.class, CommitBackOrder.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "info", scope = SynUserInfo.class)
	public JAXBElement<String> createSynUserInfoInfo(String value) {
		return new JAXBElement<String>(_SynUserInfoInfo_QNAME, String.class,
				SynUserInfo.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcId", scope = SynUserInfo.class)
	public JAXBElement<String> createSynUserInfoLcId(String value) {
		return new JAXBElement<String>(_SynUserInfoLcId_QNAME, String.class,
				SynUserInfo.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcId", scope = GetOrdersV2.class)
	public JAXBElement<String> createGetOrdersV2LcId(String value) {
		return new JAXBElement<String>(_SynUserInfoLcId_QNAME, String.class,
				GetOrdersV2.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcId", scope = GetCity.class)
	public JAXBElement<String> createGetCityLcId(String value) {
		return new JAXBElement<String>(_SynUserInfoLcId_QNAME, String.class,
				GetCity.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "province", scope = GetCity.class)
	public JAXBElement<String> createGetCityProvince(String value) {
		return new JAXBElement<String>(_GetCityProvince_QNAME, String.class,
				GetCity.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "CommitBackOrderStatusResult", scope = CommitBackOrderStatusResponse.class)
	public JAXBElement<String> createCommitBackOrderStatusResponseCommitBackOrderStatusResult(
			String value) {
		return new JAXBElement<String>(
				_CommitBackOrderStatusResponseCommitBackOrderStatusResult_QNAME,
				String.class, CommitBackOrderStatusResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcid", scope = CommitBackOrderStatus.class)
	public JAXBElement<String> createCommitBackOrderStatusLcid(String value) {
		return new JAXBElement<String>(_CommitBackOrderLcid_QNAME,
				String.class, CommitBackOrderStatus.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "orders", scope = CommitBackOrderStatus.class)
	public JAXBElement<String> createCommitBackOrderStatusOrders(String value) {
		return new JAXBElement<String>(_CommitBackOrderOrders_QNAME,
				String.class, CommitBackOrderStatus.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "CommitDeliverInfoResult", scope = CommitDeliverInfoResponse.class)
	public JAXBElement<String> createCommitDeliverInfoResponseCommitDeliverInfoResult(
			String value) {
		return new JAXBElement<String>(
				_CommitDeliverInfoResponseCommitDeliverInfoResult_QNAME,
				String.class, CommitDeliverInfoResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "CommitBackOrderResult", scope = CommitBackOrderResponse.class)
	public JAXBElement<String> createCommitBackOrderResponseCommitBackOrderResult(
			String value) {
		return new JAXBElement<String>(
				_CommitBackOrderResponseCommitBackOrderResult_QNAME,
				String.class, CommitBackOrderResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcId", scope = CommitDeliverInfo.class)
	public JAXBElement<String> createCommitDeliverInfoLcId(String value) {
		return new JAXBElement<String>(_SynUserInfoLcId_QNAME, String.class,
				CommitDeliverInfo.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "deliverInfo", scope = CommitDeliverInfo.class)
	public JAXBElement<String> createCommitDeliverInfoDeliverInfo(String value) {
		return new JAXBElement<String>(_CommitDeliverInfoDeliverInfo_QNAME,
				String.class, CommitDeliverInfo.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "waybills", scope = SuccessOrders.class)
	public JAXBElement<String> createSuccessOrdersWaybills(String value) {
		return new JAXBElement<String>(_SuccessOrdersWaybills_QNAME,
				String.class, SuccessOrders.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcId", scope = SuccessOrders.class)
	public JAXBElement<String> createSuccessOrdersLcId(String value) {
		return new JAXBElement<String>(_SynUserInfoLcId_QNAME, String.class,
				SuccessOrders.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "city", scope = GetArea.class)
	public JAXBElement<String> createGetAreaCity(String value) {
		return new JAXBElement<String>(_GetAreaCity_QNAME, String.class,
				GetArea.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcId", scope = GetArea.class)
	public JAXBElement<String> createGetAreaLcId(String value) {
		return new JAXBElement<String>(_SynUserInfoLcId_QNAME, String.class,
				GetArea.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "province", scope = GetArea.class)
	public JAXBElement<String> createGetAreaProvince(String value) {
		return new JAXBElement<String>(_GetCityProvince_QNAME, String.class,
				GetArea.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "GetProvinceResult", scope = GetProvinceResponse.class)
	public JAXBElement<String> createGetProvinceResponseGetProvinceResult(
			String value) {
		return new JAXBElement<String>(
				_GetProvinceResponseGetProvinceResult_QNAME, String.class,
				GetProvinceResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "GetAreaResult", scope = GetAreaResponse.class)
	public JAXBElement<String> createGetAreaResponseGetAreaResult(String value) {
		return new JAXBElement<String>(_GetAreaResponseGetAreaResult_QNAME,
				String.class, GetAreaResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "GetOrdersV2Result", scope = GetOrdersV2Response.class)
	public JAXBElement<String> createGetOrdersV2ResponseGetOrdersV2Result(
			String value) {
		return new JAXBElement<String>(
				_GetOrdersV2ResponseGetOrdersV2Result_QNAME, String.class,
				GetOrdersV2Response.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcid", scope = GetInBoundOrder.class)
	public JAXBElement<String> createGetInBoundOrderLcid(String value) {
		return new JAXBElement<String>(_CommitBackOrderLcid_QNAME,
				String.class, GetInBoundOrder.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "GetOrdersResult", scope = GetOrdersResponse.class)
	public JAXBElement<String> createGetOrdersResponseGetOrdersResult(
			String value) {
		return new JAXBElement<String>(_GetOrdersResponseGetOrdersResult_QNAME,
				String.class, GetOrdersResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcId", scope = GetProvince.class)
	public JAXBElement<String> createGetProvinceLcId(String value) {
		return new JAXBElement<String>(_SynUserInfoLcId_QNAME, String.class,
				GetProvince.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "SynUserInfoResult", scope = SynUserInfoResponse.class)
	public JAXBElement<String> createSynUserInfoResponseSynUserInfoResult(
			String value) {
		return new JAXBElement<String>(
				_SynUserInfoResponseSynUserInfoResult_QNAME, String.class,
				SynUserInfoResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "GetCityResult", scope = GetCityResponse.class)
	public JAXBElement<String> createGetCityResponseGetCityResult(String value) {
		return new JAXBElement<String>(_GetCityResponseGetCityResult_QNAME,
				String.class, GetCityResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "GetInBoundOrderResult", scope = GetInBoundOrderResponse.class)
	public JAXBElement<String> createGetInBoundOrderResponseGetInBoundOrderResult(
			String value) {
		return new JAXBElement<String>(
				_GetInBoundOrderResponseGetInBoundOrderResult_QNAME,
				String.class, GetInBoundOrderResponse.class, value);
	}

	/**
	 * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}
	 * 
	 */
	@XmlElementDecl(namespace = "http://tempuri.org/", name = "lcId", scope = GetOrders.class)
	public JAXBElement<String> createGetOrdersLcId(String value) {
		return new JAXBElement<String>(_SynUserInfoLcId_QNAME, String.class,
				GetOrders.class, value);
	}

}
