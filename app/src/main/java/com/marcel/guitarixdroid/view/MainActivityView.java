package com.marcel.guitarixdroid.view;

import android.content.Context;

/**
 * Created by marcel on 31/01/17.
 */

public interface MainActivityView {
    void setImageView(int imagePosition, boolean selected);
    void setCurrentBankPreset(String bankPresetText);
    void showConnectingDialog();
    void dismissConnectingDialog();
    void showConnectionErrorDialog();
    void showGuitarixConnectedToast();
    Context getContext();
    void setImagePresetTextView(int index, String text);
}
