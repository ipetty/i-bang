package net.ipetty.ibang.api.util;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

import retrofit.mime.TypedFile;

/**
 * FileUtils
 * 
 * @author luocanfeng
 * @date 2014年9月29日
 */
public class FileUtils {

	private static MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();

	public static TypedFile typedFile(String filePath) {
		File file = new File(filePath);
		String mimeType = mimetypesFileTypeMap.getContentType(file);
		return new TypedFile(mimeType, file);
	}

}
