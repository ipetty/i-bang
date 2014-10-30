package net.ipetty.ibang.android.boot;

import android.app.Activity;
import net.ipetty.ibang.android.core.Task;
import net.ipetty.ibang.android.update.UpdateUtils;

public class CheckUpdateTask extends Task<Void, Boolean> {

    public CheckUpdateTask(Activity activity) {
        super(activity);
    }

    @Override
    protected Boolean myDoInBackground(Void... args) {
        return UpdateUtils.hasUpdate(activity);
    }
}
