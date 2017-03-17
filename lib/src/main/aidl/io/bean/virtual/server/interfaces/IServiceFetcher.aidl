// IServiceFetcher.aidl
package io.bean.virtual.server.interfaces;

interface IServiceFetcher {
    IBinder getService(String name);
    void addService(String name,in IBinder service);
    void removeService(String name);
}