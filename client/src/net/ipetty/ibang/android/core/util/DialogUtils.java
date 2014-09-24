package net.ipetty.ibang.android.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.ipetty.ibang.R;
import net.ipetty.ibang.android.core.ui.ModDialogItem;

import org.apache.commons.lang3.StringUtils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogUtils {

	private String TAG = getClass().getSimpleName();

	public static Dialog datePopupDialog(Context context, DatePickerDialog.OnDateSetListener onDateSetListener,
			String value, Dialog d) {
		if (d != null && d.isShowing()) {
			return d;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		if (!StringUtils.isEmpty(value)) {
			Date date;
			try {
				date = format.parse(value);
			} catch (ParseException e) {
				date = new Date();
			}
			c.setTime(date);
		}

		final Dialog dialog = new DatePickerDialog(context, onDateSetListener, c.get(Calendar.YEAR), // 传入年份
				c.get(Calendar.MONTH), // 传入月份
				c.get(Calendar.DAY_OF_MONTH) // 传入天数
		);

		if (dialog != null) {
			dialog.show();
		}
		return dialog;
	}

	public static Dialog modPopupDialog(Context context, List<ModDialogItem> items, Dialog d) {
		if (d != null && d.isShowing()) {
			return d;
		}
		final Dialog dialog = new Dialog(context, R.style.PopDialog);
		dialog.setContentView(R.layout.dialog_mod_pop);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);

		LinearLayout layout = (LinearLayout) dialog.findViewById(R.id.items);

		for (ModDialogItem item : items) {
			View v = dialog.getLayoutInflater().inflate(R.layout.dialog_mod_pop_item, layout, false);
			View line = v.findViewById(R.id.line);
			TextView tx = (TextView) v.findViewById(R.id.text);
			TextView val = (TextView) v.findViewById(R.id.value);
			ImageView img = (ImageView) v.findViewById(R.id.image);
			if (item.getIconId() == null) {
				img.setVisibility(View.GONE);
			}
			if (items.indexOf(item) == (items.size() - 1)) {
				line.setVisibility(View.GONE);
			}

			tx.setText(item.getText());
			val.setText(item.getValue());
			v.setOnClickListener(item.getOnClickListener());
			layout.addView(v);
		}
		if (dialog != null) {
			dialog.show();
		}
		return dialog;
	}

}
