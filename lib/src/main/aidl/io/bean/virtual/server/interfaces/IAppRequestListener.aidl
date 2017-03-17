// IAppRequestListener.aidl
package io.bean.virtual.server.interfaces;

interface IAppRequestListener {
    void onRequestInstall(in String path);
    void onRequestUninstall(in String pkg);
}
