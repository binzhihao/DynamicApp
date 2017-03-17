package io.bean.virtual.client.hook.patchs.content;

import io.bean.virtual.client.hook.base.PatchBinderDelegate;

import mirror.android.content.IContentService;

/**
 * @author Lody
 *
 * @see IContentService
 */

public class ContentServicePatch extends PatchBinderDelegate {

    public ContentServicePatch() {
        super(IContentService.Stub.TYPE, "content");
    }
}
