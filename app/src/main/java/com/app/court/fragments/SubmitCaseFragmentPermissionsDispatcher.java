package com.app.court.fragments;

import java.lang.Override;
import java.lang.String;
import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

public class SubmitCaseFragmentPermissionsDispatcher {
    private static final int REQUEST_GETSTORAGEPERMISSION = 1;

    private static final String[] PERMISSION_GETSTORAGEPERMISSION = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};

    private SubmitCaseFragmentPermissionsDispatcher() {
    }

    static void getStoragePermissionWithPermissionCheck(SubmitCaseFragment target) {
        if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_GETSTORAGEPERMISSION)) {
            target.getStoragePermission();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_GETSTORAGEPERMISSION)) {
                target.showRationaleForStorage(new SubmitCaseFragmentGetStoragePermissionPermissionRequest(target));
            } else {
                target.requestPermissions(PERMISSION_GETSTORAGEPERMISSION, REQUEST_GETSTORAGEPERMISSION);
            }
        }
    }

    static void onRequestPermissionsResult(SubmitCaseFragment target, int requestCode,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_GETSTORAGEPERMISSION:
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.getStoragePermission();
                } else {
                    if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_GETSTORAGEPERMISSION)) {
                        target.showNeverAskForStorage();
                    } else {
                        target.showDeniedForStorage();
                    }
                }
                break;
            default:
                break;
        }
    }

    private static final class SubmitCaseFragmentGetStoragePermissionPermissionRequest implements PermissionRequest {
        private final WeakReference<SubmitCaseFragment> weakTarget;

        private SubmitCaseFragmentGetStoragePermissionPermissionRequest(SubmitCaseFragment target) {
            this.weakTarget = new WeakReference<SubmitCaseFragment>(target);
        }

        @Override
        public void proceed() {
            SubmitCaseFragment target = weakTarget.get();
            if (target == null) return;
            target.requestPermissions(PERMISSION_GETSTORAGEPERMISSION, REQUEST_GETSTORAGEPERMISSION);
        }

        @Override
        public void cancel() {
            SubmitCaseFragment target = weakTarget.get();
            if (target == null) return;
            target.showDeniedForStorage();
        }
    }
}
