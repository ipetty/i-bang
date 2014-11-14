package net.ipetty.ibang.vo;

/**
 * Constants
 * @author luocanfeng
 * @date 2014年9月19日
 */
public interface Constants {

	/* seek type */
	public static final String SEEK_TYPE_SEEK = "求助";
	public static final String SEEK_TYPE_ASSISTANCE = "帮忙";

	/* seek status */
	public static final String SEEK_STATUS_CREATED = "求助中"; // 已创建（求助中）
	public static final String SEEK_STATUS_OFFERED = "应征中"; // 应征中（已有应征者应征，甚至已有部分应征已达成委托协议）
	public static final String SEEK_STATUS_DELEGATED = " 已委托"; // 已委托（达到委托数上限时才会成为此状态）
	public static final String SEEK_STATUS_FINISHED = "已完成"; // 已完成（所有委托都已结束（双方已评价）或已关闭）
	public static final String SEEK_STATUS_CLOSED = "已关闭"; // 已关闭（已中止求助）

	/* offer status */
	public static final String OFFER_STATUS_OFFERED = "等待接受帮助"; // 应征中（已发出应征）
	public static final String OFFER_STATUS_DELEGATED = "已委托"; // 已委托（求助者已接受应征）
	public static final String OFFER_STATUS_FINISHED = "已完成"; // 已完成（已完成委托）
	public static final String OFFER_STATUS_CLOSED = "已关闭"; // 已关闭

	/* delegate status */
	public static final String DELEGATE_STATUS_DELEGATED = "已委托"; // 已委托（求助者已接受应征）
	public static final String DELEGATE_STATUS_FINISHED = "已完成"; // 已完成（已完成委托）
	public static final String DELEGATE_STATUS_SEEKER_EVALUATED = "求助者已评价"; // 求助者已评价
	public static final String DELEGATE_STATUS_OFFERER_EVALUATED = "帮助者已评"; // 帮助者已评价
	public static final String DELEGATE_STATUS_BI_EVALUATED = "双方已评价"; // 双方已评价
	public static final String DELEGATE_STATUS_CLOSED = "已关闭"; // 已关闭（已中止委托）

	/* evaluation type */
	public static final String EVALUATION_TYPE_SEEKER_TO_OFFERER = "seeker_to_offerer"; // 求助者对帮助者的评价
	public static final String EVALUATION_TYPE_OFFERER_TO_SEEKER = "offerer_to_seeker"; // 帮助者对求助者的评价

	/* system message type */
	public static final String SYS_MSG_TYPE_NEW_OFFER = "new_offer"; // 您的求助单有了新的应征
	public static final String SYS_MSG_TYPE_OFFER_DISABLED = "offer_disabled"; // 您的应征被管理员屏蔽
	public static final String SYS_MSG_TYPE_NEW_DELEGATION = "new_delegation"; // 您的应征已被求助者接受
	public static final String SYS_MSG_TYPE_DELEGATION_FINISHED = "delegation_finished"; // 您（求助者）的一个委托已被帮助者完成
	public static final String SYS_MSG_TYPE_DELEGATION_CLOSED = "delegation_closed"; // 您（求助者）的一个委托已被帮助者关闭
	public static final String SYS_MSG_TYPE_NEW_EVALUATION = "new_evaluation"; // 您收到了新的评价
	public static final String SYS_MSG_TYPE_SEEK_FINISHED = "seek_finished"; // 您（求助者）的一个求助单已完成
	public static final String SYS_MSG_TYPE_SEEK_CLOSED = "seek_closed"; // 您（帮助者）应征的一个求助单已被求助者关闭
	public static final String SYS_MSG_TYPE_SEEK_DISABLED = "seek_disabled"; // 您的求助被管理员屏蔽
	public static final String SYS_MSG_TYPE_ID_VERIFICATION_APPROVED = "id_verification_approved"; // 您的身份审核通过
	public static final String SYS_MSG_TYPE_ID_VERIFICATION_UNAPPROVED = "id_verification_unapproved"; // 您的身份审核未通过

	/* 身份验证状态 */
	public static final String ID_VERIFICATION_VERIFYING = "待审核";
	public static final String ID_VERIFICATION_APPROVED = "审核通过";
	public static final String ID_VERIFICATION_UNAPPROVED = "审核未通过";

	/* report type */
	public static final String REPORT_TYPE_SEEK = "report_seek";
	public static final String REPORT_TYPE_OFFER = "report_offer";
	public static final String REPORT_TYPE_USER = "report_user";
	/* report behave type */
	public static final String REPORT_BEHAVE_TYPE_GARBAGE = "垃圾营销";
	public static final String REPORT_BEHAVE_TYPE_AD = "小广告";
	public static final String REPORT_BEHAVE_TYPE_FAKE = "虚假信息";
	public static final String REPORT_BEHAVE_TYPE_PIRATE = "抄袭/盗版";
	public static final String REPORT_BEHAVE_TYPE_SENSITIVE = "敏感信息";
	/* report result */
	public static final String REPORT_RESULT_PUNISH = "严惩不贷";
	public static final String REPORT_RESULT_WARNING = "警告";
	public static final String REPORT_RESULT_PENDING = "暂不处理";
	public static final String REPORT_RESULT_FAKE_REPORT = "虚假举报";
	public static final String REPORT_RESULT_EVIL_REPORT = "恶意举报";

}
