package net.ipetty.ibang.android.publish;

import java.util.List;

import net.ipetty.ibang.vo.SeekVO;

/**
 * SeekForm
 * 
 * @author luocanfeng
 * @date 2014年10月20日
 */
public class SeekForm {

	private SeekVO seek;
	private List<String> images;

	public SeekForm(SeekVO seek, List<String> images) {
		super();
		this.seek = seek;
		this.images = images;
	}

	public SeekVO getSeek() {
		return seek;
	}

	public void setSeek(SeekVO seek) {
		this.seek = seek;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

}
