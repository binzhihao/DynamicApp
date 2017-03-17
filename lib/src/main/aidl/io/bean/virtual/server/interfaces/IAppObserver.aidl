// IAppObserver.aidl
package io.bean.virtual.server.interfaces;

interface IAppObserver {
    void onNewApp(String pkg);
    void onRemoveApp(String pkg);
}
