package net.ipetty.ibang.android.publish;

import java.util.List;

import net.ipetty.ibang.vo.SeekWithLocationVO;

/**
 * 求助/帮忙，带地理位置与图片信息
 * 
 * @author luocanfeng
 * @date 2014年11月5日
 */
public class SeekWithLocationForm {

	private SeekWithLocationVO seek;
	private List<String> images;

	public SeekWithLocationForm(SeekWithLocationVO seek, List<String> images) {
		super();
		this.seek = seek;
		this.images = images;
	}

	public SeekWithLocationVO getSeek() {
		return seek;
	}

	public void setSeek(SeekWithLocationVO seek) {
		this.seek = seek;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

}
