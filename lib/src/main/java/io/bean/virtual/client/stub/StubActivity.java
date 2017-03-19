package io.bean.virtual.client.stub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import io.bean.virtual.client.core.PatchManager;
import io.bean.virtual.client.env.VirtualRuntime;
import io.bean.virtual.client.hook.patchs.am.HCallbackHook;
import io.bean.virtual.client.ipc.VActivityManager;
import io.bean.virtual.remote.StubActivityRecord;
import io.bean.virtual.os.VUserHandle;

public abstract class StubActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// The savedInstanceState's classLoader is not exist.
		super.onCreate(null);
		finish();
        // It seems that we have conflict with the other Android-Plugin-Framework.
		Intent stubIntent = getIntent();
        // Try to acquire the actually component information.
		StubActivityRecord r = new StubActivityRecord(stubIntent);
		if (r.intent != null) {
			if (TextUtils.equals(r.info.processName, VirtualRuntime.getProcessName()) && r.userId == VUserHandle.myUserId()) {
                // Retry to inject the HCallback to instead of the exist one.
				PatchManager.getInstance().checkEnv(HCallbackHook.class);
				Intent intent = r.intent;
				startActivity(intent);
			} else {
                // Start the target Activity in other process.
				VActivityManager.get().startActivity(r.intent, r.userId);
			}
		}
	}

	public static class S0 extends StubActivity {
	}

	public static class S1 extends StubActivity {
	}

	public static class S2 extends StubActivity {
	}

	public static class S3 extends StubActivity {
	}

	public static class S4 extends StubActivity {
	}

	public static class S5 extends StubActivity {
	}

	public static class S6 extends StubActivity {
	}

	public static class S7 extends StubActivity {
	}

	public static class S8 extends StubActivity {
	}

	public static class S9 extends StubActivity {
	}

	public static class S10 extends StubActivity {
	}

	public static class S11 extends StubActivity {
	}

	public static class S12 extends StubActivity {
	}

	public static class S13 extends StubActivity {
	}

	public static class S14 extends StubActivity {
	}

	public static class S15 extends StubActivity {
	}

	public static class S16 extends StubActivity {
	}

	public static class S17 extends StubActivity {
	}

	public static class S18 extends StubActivity {
	}

	public static class S19 extends StubActivity {
	}

	public static class S20 extends StubActivity {
	}

	public static class S21 extends StubActivity {
	}

	public static class S22 extends StubActivity {
	}

	public static class S23 extends StubActivity {
	}

	public static class S24 extends StubActivity {
	}

	public static class S25 extends StubActivity {
	}

	public static class S26 extends StubActivity {
	}

	public static class S27 extends StubActivity {
	}

	public static class S28 extends StubActivity {
	}

	public static class S29 extends StubActivity {
	}
	
}
