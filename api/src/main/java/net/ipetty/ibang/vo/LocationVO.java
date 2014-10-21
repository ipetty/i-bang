package net.ipetty.ibang.vo;

/**
 * 位置
 * 
 * @author luocanfeng
 * @date 2014年10月21日
 */
public class LocationVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = -2529988102632820979L;

	private String province; // 省
	private String city; // 市
	private String district; // 区/县

	public LocationVO() {
		super();
	}

	public LocationVO(String province) {
		super();
		this.province = province;
	}

	public LocationVO(String province, String city) {
		super();
		this.province = province;
		this.city = city;
	}

	public LocationVO(String province, String city, String district) {
		super();
		this.province = province;
		this.city = city;
		this.district = district;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

}
