package com.app.court.interfaces;

import com.app.court.fragments.ChatFragment;
import com.app.court.fragments.SubmitCaseFragment;

public interface OnItemCancelClick {
    void itemCrossClick(SubmitCaseFragment.FileType fileItem);
    void itemCrossClickChat(Object fileItem);
}
