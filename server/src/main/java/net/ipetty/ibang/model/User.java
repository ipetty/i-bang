package net.ipetty.ibang.model;

import java.util.Date;

import net.ipetty.ibang.vo.RegisterVO;
import net.ipetty.ibang.vo.UserFormVO;
import net.ipetty.ibang.vo.UserVO;

import org.springframework.beans.BeanUtils;

/**
 * 用户
 * 
 * @author luocanfeng
 * @date 2014年9月17日
 */
public class User extends AbstractEntity {

	/** serialVersionUID */
	private static final long serialVersionUID = -2109373072723898861L;

	private Integer id; // 非业务主键
	private String username; // 用户名
	private String email; // 邮箱
	private String password; // 密码
	private String salt; // 盐值
	private String nickname; // 昵称
	private String gender; // 性别
	private String job; // 职位
	private String phone; // 手机号码
	private String telephone; // 联系电话
	private String avatar; // 头像
	private String signature; // 个性签名
	private String address; // 所在地
	private Date createdOn; // 注册时间

	private SeekerInfo seekerInfo; // 用户作为求助者身份的相应信息
	private OffererInfo offererInfo; // 用户作为帮助者身份的相应信息

	public User() {
		super();
	}

	public UserVO toVO() {
		UserVO vo = new UserVO();
		BeanUtils.copyProperties(this, vo);
		if (seekerInfo != null) {
			vo.setSeekCount(seekerInfo.getSeekCount());
			vo.setSeekerTotalPoint(seekerInfo.getTotalPoint());
			vo.setSeekerTitle(seekerInfo.getTitle());
		}
		if (offererInfo != null) {
			vo.setOfferCount(offererInfo.getOfferCount());
			vo.setOffererTotalPoint(offererInfo.getTotalPoint());
			vo.setOffererTitle(offererInfo.getTitle());
			vo.setOfferRange(offererInfo.getOfferRange());
		}
		return vo;
	}

	public static User fromVO(UserVO vo) {
		User entity = new User();
		BeanUtils.copyProperties(vo, entity);
		SeekerInfo seekerInfo = new SeekerInfo();
		seekerInfo.setSeekCount(vo.getSeekCount());
		seekerInfo.setTotalPoint(vo.getSeekerTotalPoint());
		seekerInfo.setTitle(vo.getSeekerTitle());
		entity.setSeekerInfo(seekerInfo);
		OffererInfo offererInfo = new OffererInfo();
		offererInfo.setOfferCount(vo.getOfferCount());
		offererInfo.setTotalPoint(vo.getOffererTotalPoint());
		offererInfo.setTitle(vo.getOffererTitle());
		offererInfo.setOfferRange(vo.getOfferRange());
		entity.setOffererInfo(offererInfo);
		return entity;
	}

	public static User fromRegisterVO(RegisterVO register) {
		User entity = new User();
		BeanUtils.copyProperties(register, entity);
		return entity;
	}

	public static User fromUserFormVO(UserFormVO userFormVO) {
		User entity = new User();
		BeanUtils.copyProperties(userFormVO, entity);
		return entity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public SeekerInfo getSeekerInfo() {
		return seekerInfo;
	}

	public void setSeekerInfo(SeekerInfo seekerInfo) {
		this.seekerInfo = seekerInfo;
	}

	public OffererInfo getOffererInfo() {
		return offererInfo;
	}

	public void setOffererInfo(OffererInfo offererInfo) {
		this.offererInfo = offererInfo;
	}

}
