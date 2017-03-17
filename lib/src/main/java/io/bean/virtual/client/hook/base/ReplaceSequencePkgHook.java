package io.bean.virtual.client.hook.base;

import java.lang.reflect.Method;

import io.bean.virtual.client.hook.utils.HookUtils;

/**
 * @author Lody
 */

public class ReplaceSequencePkgHook extends StaticHook {

	private int sequence;

	public ReplaceSequencePkgHook(String name, int sequence) {
		super(name);
		this.sequence = sequence;
	}

	@Override
	public boolean beforeCall(Object who, Method method, Object... args) {
		HookUtils.replaceSequenceAppPkg(args, sequence);
		return super.beforeCall(who, method, args);
	}
}
