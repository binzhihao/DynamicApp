package com.lody.virtual.client.hook.patchs.notification.compat;

import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.helper.utils.Reflect;
import com.lody.virtual.helper.utils.VLog;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/***
 * 通知栏的包名判断
 */
/* package */ class NotificaitionUtils {
	private static final String TAG = NotificaitionUtils.class.getSimpleName();
	private static Map<Integer, String> sSystemLayoutResIds = new HashMap<Integer, String>(0);

	static {
		init();
	}

	private static void init() {
		try {
			// read all com.android.internal.R
			Class clazz = Class.forName("com.android.internal.R$layout");
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				// public static final
				if (Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers())
						&& Modifier.isFinal(field.getModifiers())) {
					try {
						int id = field.getInt(null);
						sSystemLayoutResIds.put(id, field.getName());
					} catch (IllegalAccessException e) {
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public static boolean isSystemLayoutId(int id) {
		return sSystemLayoutResIds.containsKey(Integer.valueOf(id));
	}

	public static boolean isCustomNotification(Notification notification) {
		if (isCustomNotification(notification.contentView)) {
			return true;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (isCustomNotification(notification.tickerView)) {
				return true;
			}
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			if (isCustomNotification(notification.bigContentView)) {
				return true;
			}
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (isCustomNotification(notification.headsUpContentView)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isCustomNotification(RemoteViews remoteViews) {
		if (remoteViews == null) {
			return false;
		} else if (sSystemLayoutResIds.containsKey(remoteViews.getLayoutId())) {
			return false;
		} else {
			return true;
		}
	}

	//
	public static boolean isPluginNotification(Notification notification) {
		if (notification == null) {
			return false;
		}

		if (notification.contentView != null && !isHostPackageName(notification.contentView.getPackage())) {
			return true;
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (notification.tickerView != null && !isHostPackageName(notification.tickerView.getPackage())) {
				return true;
			}
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			if (notification.bigContentView != null && !isHostPackageName(notification.bigContentView.getPackage())) {
				return true;
			}
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			if (notification.headsUpContentView != null
					&& !isHostPackageName(notification.headsUpContentView.getPackage())) {
				return true;
			}
			if (notification.publicVersion != null && notification.publicVersion.contentView != null
					&& !isHostPackageName(notification.publicVersion.contentView.getPackage())) {
				return true;
			}
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			android.graphics.drawable.Icon icon = notification.getSmallIcon();
			if (icon != null) {
				try {
					Object mString1Obj = Reflect.on(icon).get("mString1");
					if (mString1Obj instanceof String) {
						String mString1 = ((String) mString1Obj);
						if (!isHostPackageName(mString1)) {
							return true;
						}
					}
				} catch (Exception e) {
				}
			}
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			android.graphics.drawable.Icon icon = notification.getLargeIcon();
			if (icon != null) {
				try {
					Object mString1Obj = Reflect.on(icon).get("mString1");
					if (mString1Obj instanceof String) {
						String mString1 = ((String) mString1Obj);
						if (!isHostPackageName(mString1)) {
							return true;
						}
					}
				} catch (Exception e) {
				}
			}
		}

		try {
			Bundle mExtras = Reflect.on(notification).get("extras");
			for (String s : mExtras.keySet()) {
				if (mExtras.get(s) != null && mExtras.get(s) instanceof ApplicationInfo) {
					ApplicationInfo applicationInfo = (ApplicationInfo) mExtras.get(s);
					if (applicationInfo != null) {
						return !isHostPackageName(applicationInfo.packageName);
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	public static Notification clone(Context context, Notification notification) {
		// 插件的icon，绘制完成再替换成自己的
		Notification notification1 = null;
		// TODO 貌似克隆有问题,icon不对，如果不克隆，就得去找出title和contentText
		// try {
		// notification1 = new Notification();
		// Reflect.on(notification).call("cloneInto", notification1, true);
		// } catch (Exception e) {
		// VLog.w("kk", "clone 1 fail " + notification, e);
		// try {
		// notification1 = Reflect.on(notification).call("clone").get();
		// } catch (Exception e1) {
		// notification1 = null;
		// VLog.w("kk", "clone 2 fail " + notification, e1);
		// }
		// }
		// if (notification1 == null) {
		Notification.Builder builder = null;

		try {
			builder = Reflect.on(Notification.Builder.class).create(context, notification).get();
		} catch (Exception e) {
			if (builder == null) {
				builder = createBuilder(context, notification);
			}
			//VLog.w(TAG, "clone" + VLog.getStackTraceString(e));
		}
		fixNotificationIcon(context, notification, builder);
		if (Build.VERSION.SDK_INT >= 16) {
			notification1 = builder.build();
		} else {
			notification1 = builder.getNotification();
		}
		notification1.flags = notification.flags;
		notification1.icon = notification.icon;

        //intent
        if(notification1.contentIntent == null){
            notification1.contentIntent = notification.contentIntent;
        }
        if(notification1.deleteIntent == null){
            notification1.deleteIntent = notification.deleteIntent;
        }
        if(notification1.fullScreenIntent == null){
            notification1.fullScreenIntent = notification.fullScreenIntent;
        }
		///
		if (notification1.contentView == null) {
			notification1.contentView = notification.contentView;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			if (notification1.bigContentView == null) {
				notification1.bigContentView = notification.bigContentView;
			}
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			notification1.publicVersion = notification.publicVersion;
			if (notification1.headsUpContentView == null) {
				notification1.headsUpContentView = notification.headsUpContentView;
			}
		}
		// }
		return notification1;
	}

	private static boolean isHostPackageName(String pkg) {
		return VirtualCore.get().isHostPackageName(pkg);
	}

	private static Notification.Builder createBuilder(Context context, Notification notification) {
		Notification.Builder builder = new Notification.Builder(context);
		if (Build.VERSION.SDK_INT < 23) {
			builder.setSmallIcon(context.getApplicationInfo().icon);
			builder.setLargeIcon(notification.largeIcon);
		} else {
			builder.setSmallIcon(notification.getSmallIcon());
			builder.setLargeIcon(notification.getLargeIcon());
		}
		if (Build.VERSION.SDK_INT >= 21) {
			builder.setCategory(notification.category);
			builder.setColor(notification.color);
		}
		if (Build.VERSION.SDK_INT >= 20) {
			builder.setGroup(notification.getGroup());
//			builder.setGroupSummary(notification.isGroupSummary());
			builder.setPriority(notification.priority);
			builder.setSortKey(notification.getSortKey());
		}
		if (notification.sound != null) {
			if (notification.defaults == 0) {
				builder.setDefaults(Notification.DEFAULT_SOUND);// notification.defaults);
			} else {
				builder.setDefaults(Notification.DEFAULT_ALL);
			}
		}
		builder.setLights(notification.ledARGB, notification.ledOnMS, notification.ledOffMS);
		builder.setNumber(notification.number);
		builder.setTicker(notification.tickerText);
		// intent
		builder.setContentIntent(notification.contentIntent);
		builder.setDeleteIntent(notification.deleteIntent);
		builder.setFullScreenIntent(notification.fullScreenIntent,
				(notification.flags & Notification.FLAG_HIGH_PRIORITY) != 0);
		// 6.0以下是有contentviews
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			builder.setContentTitle(notification.extras.getString(Notification.EXTRA_TITLE));
			builder.setContentText(notification.extras.getString(Notification.EXTRA_TEXT));
			builder.setSubText(notification.extras.getString(Notification.EXTRA_SUB_TEXT));
		}
		return builder;
	}

	public static void fixNotificationIcon(Context context, Notification notification) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			notification.icon = context.getApplicationInfo().icon;
			return;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			android.graphics.drawable.Icon icon = notification.getSmallIcon();
			if (icon != null) {
				Bitmap bitmap = drawableToBitMap(icon.loadDrawable(context));
				if (bitmap != null) {
					android.graphics.drawable.Icon newIcon = android.graphics.drawable.Icon.createWithBitmap(bitmap);
					Reflect.on(notification).set("mSmallIcon", newIcon);
				}
			}
			android.graphics.drawable.Icon icon2 = notification.getLargeIcon();
			if (icon2 != null) {
				Bitmap bitmap = drawableToBitMap(icon2.loadDrawable(context));
				if (bitmap != null) {
					android.graphics.drawable.Icon newIcon = android.graphics.drawable.Icon.createWithBitmap(bitmap);
					Reflect.on(notification).set("mLargeIcon", newIcon);
				}
			}
		}
	}

	public static void fixIconImage(Context pluginContext, RemoteViews remoteViews, Notification notification) {
		if (remoteViews == null)
			return;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			try {
				int iconId = notification.icon;
				if (iconId == 0) {
					iconId = pluginContext.getApplicationInfo().icon;
				}
				int id = Reflect.on("com.android.internal.R$id").get("icon");
				Bitmap bitmap;
				if (Build.VERSION.SDK_INT >= 21) {
					bitmap = drawableToBitMap(
							pluginContext.getResources().getDrawable(iconId, pluginContext.getTheme()));
				} else {
					bitmap = drawableToBitMap(pluginContext.getResources().getDrawable(iconId));
				}
				remoteViews.setImageViewBitmap(id, bitmap);
			} catch (Exception e) {
				VLog.w("notification", "set notification icon " + VLog.getStackTraceString(e));
			}
		} else {
			try {
				int id = Reflect.on("com.android.internal.R$id").get("icon");
				Icon icon = notification.getLargeIcon();
				if (icon == null) {
					icon = notification.getSmallIcon();
				}
				remoteViews.setImageViewIcon(id, icon);
				// Bitmap bitmap =
				// drawableToBitMap(icon.loadDrawable(pluginContext));
				// if (bitmap != null) {
				// remoteViews.setImageViewBitmap(id, bitmap);
				// }
			} catch (Exception e) {
				VLog.e("kk", "set icon 23 " + e);
			}
		}
	}

	public static void fixNotificationIcon(Context context, Notification notification, Notification.Builder builder) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			builder.setSmallIcon(notification.icon);
			builder.setLargeIcon(notification.largeIcon);
		} else {
			android.graphics.drawable.Icon icon = notification.getSmallIcon();
			if (icon != null) {
				Bitmap bitmap = drawableToBitMap(icon.loadDrawable(context));
				if (bitmap != null) {
					android.graphics.drawable.Icon newIcon = android.graphics.drawable.Icon.createWithBitmap(bitmap);
					builder.setSmallIcon(newIcon);
				}
			}
			android.graphics.drawable.Icon icon2 = notification.getLargeIcon();
			if (icon2 != null) {
				Bitmap bitmap = drawableToBitMap(icon2.loadDrawable(context));
				if (bitmap != null) {
					android.graphics.drawable.Icon newIcon = android.graphics.drawable.Icon.createWithBitmap(bitmap);
					builder.setLargeIcon(newIcon);
				}
			}
		}
	}

	private static Bitmap drawableToBitMap(Drawable drawable) {
		if (drawable == null) {
			return null;
		}
		if (drawable instanceof BitmapDrawable) {
			BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
			return bitmapDrawable.getBitmap();
		} else {
			Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
					drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		}
	}
}
