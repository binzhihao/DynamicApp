// IBinderDelegateService.aidl
package io.bean.virtual.server;

import android.content.ComponentName;

interface IBinderDelegateService {

   ComponentName getComponent();

   IBinder getService();

}
