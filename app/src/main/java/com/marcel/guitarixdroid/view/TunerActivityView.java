package com.marcel.guitarixdroid.view;

import android.content.Context;

/**
 * Created by marcel on 08/04/17.
 */

public interface TunerActivityView {
    void updateTunerValue(String tunerParam, String note, int cents);
    Context getContext();
}
