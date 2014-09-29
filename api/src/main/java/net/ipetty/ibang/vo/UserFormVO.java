package net.ipetty.ibang.vo;

/**
 * 用户编辑VO
 * 
 * @author luocanfeng
 * @date 2014年9月29日
 */
public class UserFormVO extends BaseVO {

	/** serialVersionUID */
	private static final long serialVersionUID = 5608078496854686438L;

	private Integer id; // 非业务主键
	private String nickname; // 昵称
	private String gender; // 性别
	private String job; // 职位
	private String phone; // 手机号码
	private String telephone; // 联系电话
	private String signature; // 个性签名
	private String address; // 所在地

	public UserFormVO() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
