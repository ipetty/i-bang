package net.ipetty.ibang.vo;

/**
 * Constants
 * 
 * @author luocanfeng
 * @date 2014年9月19日
 */
public interface Constants {

	/* seek status */
	public static final String SEEK_STATUS_CREATED = "created"; // 已创建（求助中）
	public static final String SEEK_STATUS_OFFERED = "offered"; // 应征中（已有应征者应征，甚至已有部分应征已达成委托协议）
	public static final String SEEK_STATUS_DELEGATED = "delegated"; // 已委托（达到委托数上限时才会成为此状态）
	public static final String SEEK_STATUS_FINISHED = "finished"; // 已完成（所有委托都已结束（双方已评价）或已关闭）
	public static final String SEEK_STATUS_CLOSED = "closed"; // 已关闭（已中止求助）

	/* offer status */
	public static final String OFFER_STATUS_OFFERED = "offered"; // 应征中（已发出应征）
	public static final String OFFER_STATUS_DELEGATED = "delegated"; // 已委托（求助者已接受应征）
	public static final String OFFER_STATUS_FINISHED = "finished"; // 已完成（已完成委托）
	public static final String OFFER_STATUS_CLOSED = "closed"; // 已关闭（已中止委托）

	/* delegate status */
	public static final String DELEGATE_STATUS_DELEGATED = "delegated"; // 已委托（求助者已接受应征）
	public static final String DELEGATE_STATUS_FINISHED = "finished"; // 已完成（已完成委托）
	public static final String DELEGATE_STATUS_SEEKER_EVALUATED = "seeker_evaluated"; // 求助者已评价
	public static final String DELEGATE_STATUS_OFFERER_EVALUATED = "offerer_evaluated"; // 帮助者已评价
	public static final String DELEGATE_STATUS_BI_EVALUATED = "bi_evaluated"; // 双方已评价
	public static final String DELEGATE_STATUS_CLOSED = "closed"; // 已关闭（已中止委托）

	/* evaluation type */
	public static final String EVALUATION_TYPE_SEEKER_TO_OFFERER = "seeker_to_offerer"; // 求助者对帮助者的评价
	public static final String EVALUATION_TYPE_OFFERER_TO_SEEKER = "offerer_to_seeker"; // 帮助者对求助者的评价

}
