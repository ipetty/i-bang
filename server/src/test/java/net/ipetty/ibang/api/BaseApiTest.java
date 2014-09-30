package net.ipetty.ibang.api;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import net.ipetty.ibang.api.exception.ApiException;
import net.ipetty.ibang.api.util.FileUtils;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import retrofit.mime.TypedFile;

/**
 * BaseSDKTest
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-api-test.xml")
public class BaseApiTest {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	protected TypedFile getTestPhoto() {
		return FileUtils.typedFile(getTestPhotoPath());
	}

	protected String getTestPhotoPath() {
		return getPhotoPath("test.png");
	}

	protected String getPhotoPath(String photoFilename) {
		try {
			URL url = ClassLoader.getSystemResource(photoFilename);
			logger.debug("--photoPathURL={}", url);
			String photoPath = URLDecoder.decode(url.getPath(), "UTF-8");
			logger.debug("--photoPath={}", photoPath);
			return photoPath;
		} catch (UnsupportedEncodingException e) {
			throw new ApiException(e);
		}
	}

}
