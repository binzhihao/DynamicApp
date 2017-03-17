// IProcessObserver.aidl
package io.bean.virtual.server.interfaces;

interface IProcessObserver {
    void onProcessCreated(in String pkg, in String processName);

    void onProcessDied(in String pkg, in String processName);
}
