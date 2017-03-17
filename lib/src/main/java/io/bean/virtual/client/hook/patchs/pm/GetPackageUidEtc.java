package io.bean.virtual.client.hook.patchs.pm;

/**
 * @author Lody
 */
public class GetPackageUidEtc extends GetPackageUid {
    @Override
    public String getName() {
        return super.getName() + "Etc";
    }
}
