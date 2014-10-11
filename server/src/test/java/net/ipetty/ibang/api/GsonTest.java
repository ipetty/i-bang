package net.ipetty.ibang.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.api.util.DateTypeAdapter;
import net.ipetty.ibang.vo.ImageVO;
import net.ipetty.ibang.vo.SeekVO;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonTest {

	public static void main(String[] args) {
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();

		SeekVO seek = new SeekVO();
		seek.setSeekerId(1);
		seek.setCategoryL1("IT");
		seek.setCategoryL2("软件");
		seek.setContent("求开发一款类似陌陌的手机App");
		List<ImageVO> images = new ArrayList<ImageVO>();
		images.add(new ImageVO(null, null, "small_url", "original_url"));
		images.add(new ImageVO(null, null, "small_url2", "original_url2"));
		seek.setImages(images);
		seek.setRequirement("要求支持短视频分享");
		seek.setReward("一个月内完成，两万；一个半月内完成，一万五。");

		String json = gson.toJson(seek);
		System.out.println(json);

		seek = gson.fromJson(json, SeekVO.class);
		System.out.println(seek);
	}

}
