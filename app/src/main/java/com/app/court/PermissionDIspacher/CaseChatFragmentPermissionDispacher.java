package com.app.court.PermissionDIspacher;

import com.app.court.fragments.CaseMessagesFragment;

import java.lang.ref.WeakReference;

import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;


public class CaseChatFragmentPermissionDispacher {
    private static final int REQUEST_GETSTORAGEPERMISSION = 1;

    private static final String[] PERMISSION_GETSTORAGEPERMISSION = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};

    private CaseChatFragmentPermissionDispacher() {
    }

    public static void getStoragePermissionWithPermissionCheck(CaseMessagesFragment target) {
        if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_GETSTORAGEPERMISSION)) {
            target.getStoragePermission();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_GETSTORAGEPERMISSION)) {
                target.showRationaleForStorage(new CaseChatFragmentPermissionDispacher.SubmitCaseFragmentGetStoragePermissionPermissionRequest(target));
            } else {
                target.requestPermissions(PERMISSION_GETSTORAGEPERMISSION, REQUEST_GETSTORAGEPERMISSION);
            }
        }
    }

    public static void onRequestPermissionsResult(CaseMessagesFragment target, int requestCode,
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
        private final WeakReference<CaseMessagesFragment> weakTarget;

        private SubmitCaseFragmentGetStoragePermissionPermissionRequest(CaseMessagesFragment target) {
            this.weakTarget = new WeakReference<CaseMessagesFragment>(target);
        }

        @Override
        public void proceed() {
            CaseMessagesFragment target = weakTarget.get();
            if (target == null) return;
            target.requestPermissions(PERMISSION_GETSTORAGEPERMISSION, REQUEST_GETSTORAGEPERMISSION);
        }

        @Override
        public void cancel() {
            CaseMessagesFragment target = weakTarget.get();
            if (target == null) return;
            target.showDeniedForStorage();
        }
    }
}



