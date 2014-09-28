package net.ipetty.ibang.api.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * DateTypeAdapter
 * 
 * @author luocanfeng
 * @date 2014年9月28日
 */
public class DateTypeAdapter extends TypeAdapter<Date> {

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public void write(JsonWriter out, Date value) throws IOException {
		if (value == null) {
			out.nullValue();
			return;
		}
		String dateFormatAsString = DATE_FORMAT.format(value);
		out.value(dateFormatAsString);
	}

	@Override
	public Date read(JsonReader in) throws IOException {
		if (in.peek() == JsonToken.NULL) {
			in.nextNull();
			return null;
		}
		return deserializeToDate(in.nextString());
	}

	private synchronized Date deserializeToDate(String json) {
		try {
			return DATE_FORMAT.parse(json);
		} catch (ParseException e) {
			throw new JsonSyntaxException(json, e);
		}
	}

}
