package net.ipetty.ibang.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 求助分类帮助类
 * 
 * @author luocanfeng
 * @date 2014年9月18日
 */
public class SeekCategoryUtils {

	private static final Map<String, String[]> CATEGORIES = new LinkedHashMap<String, String[]>();
	private static String[] L1_CATEGORIES;

	static {
		CATEGORIES.put("家政服务", new String[] { "保洁", "搬家", "杂活", "保姆", "开换锁", "预订", "管道", "其他", });
		CATEGORIES.put("IT服务", new String[] { "电脑硬件", "电脑软件", "手机硬件", "手机软件", "网络维护", "IT咨询", "其他", });
		CATEGORIES.put("教育培训", new String[] { "幼儿", "小学", "初中", "高中", "大学", "硕士", "博士", "博士后", "人事行政", "财务", "运营",
				"市场销售", "其他", });
		CATEGORIES.put("健康安全", new String[] { "家庭生活", "医疗", "健康咨询", "工业类", "其他", });
		CATEGORIES.put("家电维修", new String[] { "电视", "空调", "冰箱", "洗衣机", "热水器", "油烟机", "炉灶", "小家电", "其他", });
		CATEGORIES.put("汽车服务", new String[] { "代驾", "陪练", "汽车咨询", "汽车修理", "其他", });
		CATEGORIES.put("旅游休闲", new String[] { "导游", "美食", "其他", "旅游咨询", });
		CATEGORIES.put("物流运输", new String[] { "快递", "顺风车", });

		L1_CATEGORIES = new String[CATEGORIES.size()];
		L1_CATEGORIES = CATEGORIES.keySet().toArray(L1_CATEGORIES);
	}

	/**
	 * 获取所有分类
	 */
	public static final Map<String, String[]> listCategories() {
		return CATEGORIES;
	}

	/**
	 * 获取一级分类列表
	 */
	public static final String[] listL1Categories() {
		return L1_CATEGORIES;
	}

	/**
	 * 获取一级分类下的二级分类列表
	 */
	public static final String[] listL2Categories(String l1Category) {
		return CATEGORIES.get(l1Category);
	}

	public static void main(String[] args) {
		System.out.println(listCategories());

		for (String category : listL1Categories()) {
			System.out.print(category + ", ");
		}
		System.out.println();

		for (String category : listL2Categories("教育培训")) {
			System.out.print(category + ", ");
		}
		System.out.println();
	}

}
