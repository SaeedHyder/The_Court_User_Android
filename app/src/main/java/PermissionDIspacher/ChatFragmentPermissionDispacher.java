package PermissionDIspacher;

import com.app.court.fragments.ChatFragment;
import com.app.court.fragments.SubmitCaseFragment;
import com.app.court.fragments.SubmitCaseFragmentPermissionsDispatcher;

import java.lang.ref.WeakReference;

import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;


public class ChatFragmentPermissionDispacher {
    private static final int REQUEST_GETSTORAGEPERMISSION = 1;

    private static final String[] PERMISSION_GETSTORAGEPERMISSION = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"};

    private ChatFragmentPermissionDispacher() {
    }

    public static void getStoragePermissionWithPermissionCheck(ChatFragment target) {
        if (PermissionUtils.hasSelfPermissions(target.getActivity(), PERMISSION_GETSTORAGEPERMISSION)) {
            target.getStoragePermission();
        } else {
            if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_GETSTORAGEPERMISSION)) {
                target.showRationaleForStorage(new ChatFragmentPermissionDispacher.SubmitCaseFragmentGetStoragePermissionPermissionRequest(target));
            } else {
                target.requestPermissions(PERMISSION_GETSTORAGEPERMISSION, REQUEST_GETSTORAGEPERMISSION);
            }
        }
    }

    public static void onRequestPermissionsResult(ChatFragment target, int requestCode,
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
        private final WeakReference<ChatFragment> weakTarget;

        private SubmitCaseFragmentGetStoragePermissionPermissionRequest(ChatFragment target) {
            this.weakTarget = new WeakReference<ChatFragment>(target);
        }

        @Override
        public void proceed() {
            ChatFragment target = weakTarget.get();
            if (target == null) return;
            target.requestPermissions(PERMISSION_GETSTORAGEPERMISSION, REQUEST_GETSTORAGEPERMISSION);
        }

        @Override
        public void cancel() {
            ChatFragment target = weakTarget.get();
            if (target == null) return;
            target.showDeniedForStorage();
        }
    }
}



