package com.marcel.guitarixdroid.presenter;

import java.util.List;

/**
 * Created by marcel on 31/01/17.
 */

public interface Presenter {

    void onCreate();
    void onPause();
    void onResume();
    void onDestroy();
    void setPreferenceBankAndPreset(int imageViewIndex, int bankIndex, int presetIndex);
    List<String> getBankList();
    List<String> getPresetList(String bank);
    void onImageViewClicked(int imageViewPosition);
    void onConnectMenuClicked ();
    void clearAllPreset();
}
