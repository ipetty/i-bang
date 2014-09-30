package net.ipetty.ibang.android.sdk.util;

import java.io.File;

import retrofit.mime.TypedFile;
import android.webkit.MimeTypeMap;

/**
 * FileUtils
 * 
 * @author luocanfeng
 * @date 2014年9月29日
 */
public class FileUtils {

	public static TypedFile typedFile(String filePath) {
		File file = new File(filePath);
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
				MimeTypeMap.getFileExtensionFromUrl(filePath));
		return new TypedFile(mimeType, file);
	}

}
