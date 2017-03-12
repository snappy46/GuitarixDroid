/*
 * Copyright (C) 2017 Marcel Levesque
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.marcel.guitarixdroid.presenter;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.marcel.guitarixdroid.model.Guitarix;
import com.marcel.guitarixdroid.model.ResponseBankPresetList;
import com.marcel.guitarixdroid.model.ResultBankPresetList;
import com.marcel.guitarixdroid.view.MainActivityView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Presenter to handle the logic and control of the application.
 */
public class GuitarixPresenter implements Presenter {

    private static final int MAX_NUMBER_PRESET = 12;
    private static final String PREFS_BANK_PRESET = "MyPrefsBankPreset";

    // constant for the bank key of sharedpreferences
    private static final String[] BANK_KEY = {"bank1", "bank2", "bank3", "bank4", "bank5",
            "bank6", "bank7", "bank8", "bank9", "bank10", "bank11", "bank12"};

    // constant for the preset key of sharedpreferences
    private static final String[] PRESET_KEY = {"preset1", "preset2", "preset3", "preset4",
            "preset5", "preset6", "preset7", "preset8", "preset9", "preset10", "preset11",
            "preset12"};

    // string array variable to store the bank and preset values
    private String[][] bankAndPresetValue = new String[MAX_NUMBER_PRESET][2];

    // Hash map to store all the banks and presets currently available from guitarix
    private Map<String, List<String>> mapBank = new HashMap<>();

    // List holding the banklist
    private List<String> bankList = new ArrayList<>();

    // instanciate mainview interface
    private MainActivityView view;

    // instanciate guitarix API
    private final Guitarix guitarix = new Guitarix();


    // hold current bank and preset string
    private String currentBankPreset;

    // integer that represent the position of the previous ImageView that was clicked
    private int previousImageClicked = -1;

    // socket connection status True: connected; False: not connected
    private boolean socketConnected = false;

    private final String TAG = "GuitarixPresenter";

    /**
     * presenter constructor
     *
     * @param view view that instantiated the presenter
     */
    public GuitarixPresenter(MainActivityView view) {
        this.view = view;
    }

    @Override
    public void onCreate() {
        guitarix.getPreferences(view.getContext());
        new GuitarixPresenter.checkGuitarixConnection().execute();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
        restoreBankPresetPreferences();
        guitarix.getPreferences(view.getContext());
        setAllPresetText();
    }

    @Override
    public void onDestroy() {

    }


    /**
     * Retrieve values to be stored in shared preference
     *
     * @param imageViewIndex index of the imageview being process
     * @param bankIndex      bank index for banklist array
     * @param presetIndex    preset index for the mapbank hash# preset array
     */
    @Override
    public void setPreferenceBankAndPreset(int imageViewIndex, int bankIndex, int presetIndex) {
        String bank = bankList.get(bankIndex);
        String preset = mapBank.get(bank).get(presetIndex);
        storeBankPresetPreferences(BANK_KEY[imageViewIndex], PRESET_KEY[imageViewIndex]
                , bank, preset);
        bankAndPresetValue[imageViewIndex][0] = bank;
        bankAndPresetValue[imageViewIndex][1] = preset;
        setPresetText(imageViewIndex);
    }

    /**
     * retrieve bank list
     *
     * @return list of string representing the banklist
     */
    @Override
    public List<String> getBankList() {
        return bankList;
    }

    /**
     * Retrieve preset list that match the bank parameter
     *
     * @param bank bank as a string
     * @return list of string representing the preset that match bank parameter
     */
    @Override
    public List<String> getPresetList(String bank) {
        return mapBank.get(bank);
    }


    /**
     * Called when an imageview is clicked in the activity.
     *
     * @param imageViewPosition integer indicating the position of the clicked ImageView
     */
    @Override
    public void onImageViewClicked(int imageViewPosition) {
        if (socketConnected) {
            if (previousImageClicked == -1 || previousImageClicked != imageViewPosition) {
                if (setBankAndPreset(imageViewPosition)) {
                    getCurrentBankAndPreset();
                    if (previousImageClicked != -1) {
                        view.setImageView(previousImageClicked, false);
                    }
                    view.setImageView(imageViewPosition, true);
                    previousImageClicked = imageViewPosition;
                }
            }
        }
    }

    /**
     * Called when the connect menu action bar icon is clicked
     */
    @Override
    public void onConnectMenuClicked() {
        if (socketConnected) view.showGuitarixConnectedToast();
        else new checkGuitarixConnection().execute();
    }

    /**
     * This method will clear all the preset store in the preferences
     */
    @Override
    public void clearAllPreset() {
        SharedPreferences settings = view.getContext().getSharedPreferences(PREFS_BANK_PRESET, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.apply();
        restoreBankPresetPreferences();
        setAllPresetText();
        if (previousImageClicked != -1) view.setImageView(previousImageClicked, false);
    }

    /**
     * set bank and preset based on the param given and the bankAndPresetValue array
     *
     * @param bankPresetIndex Integer that identify the element within the bankAndPresetValue
     * @return return true if bankAndPresetValue for the param exist, else return false.
     */
    private boolean setBankAndPreset(int bankPresetIndex) {
        String bank = bankAndPresetValue[bankPresetIndex][0];
        String preset = bankAndPresetValue[bankPresetIndex][1];
        if (bank.equals("") || preset.equals("")) {
            return false;
        } else {
            new SetBankAndPreset(bank, preset).execute();
        }
        return true;
    }

    /**
     * Retrive guitarix current bank and preset
     */
    private void getCurrentBankAndPreset() {
        new RetrieveCurrentBankPreset().execute();
    }

    /**
     * Set app initial condition and required info from guitarix
     */
    private void initialSetup() {
        new GuitarixPresenter.RetrieveCurrentBankPreset().execute();
        new GuitarixPresenter.RetrieveBankPresetList().execute();
    }


    /**
     * Retrieve all the saved guitarix bank/preset and store in array
     */
    private void restoreBankPresetPreferences() {
        SharedPreferences settings = view.getContext().getSharedPreferences(PREFS_BANK_PRESET, 0);
        for (int i = 0; i < MAX_NUMBER_PRESET; i++) {
            bankAndPresetValue[i][0] = settings.getString(BANK_KEY[i], "");
            bankAndPresetValue[i][1] = settings.getString(PRESET_KEY[i], "");
        }
    }

    /**
     * Store guitarix bank/preset in sharepreferences
     *
     * @param bankKey   String that represent bank Key to store in pref
     * @param presetKey String that represent preset key to store in pref
     * @param bank      bank name to be stored in pref
     * @param preset    preset name to be store in pref
     */
    private void storeBankPresetPreferences(String bankKey, String presetKey, String bank, String preset) {
        SharedPreferences settings = view.getContext().getSharedPreferences(PREFS_BANK_PRESET, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(bankKey, bank);
        editor.putString(presetKey, preset);
        editor.apply();
    }


    /**
     * set all the textview preset text
     */
    private void setAllPresetText() {
        for (int i = 0; i < bankAndPresetValue.length; i++) {
            setPresetText(i);
        }
    }

    /**
     * set imageview preset text based on textview position
     *
     * @param index integer representing a specific textview position
     */
    private void setPresetText(int index) {
        view.setImagePresetTextView(index, bankAndPresetValue[index][1]);
    }


    /**
     * build bank and preset list based on the response from guitarix
     *
     * @param responseBankList class representing bank and preset list
     */
    private void buildBankAndPresetList(ResponseBankPresetList responseBankList) {
        if (responseBankList != null) {
            for (ResultBankPresetList response : responseBankList.getResult()) {
                List<String> presetList = response.getPresets();
                mapBank.put(response.getName(), presetList);
            }
            for (Map.Entry<String, List<String>> entry : mapBank.entrySet()) {
                bankList.add(entry.getKey());
            }
        }
    }


    /**
     * Check to see if bank and preset currently set on guitarix is available as a selection.  If found
     * Imageview is set to selected.
     */
    private void currentBankPresetExist() {
        String tmpBankPreset;
        int i;
        for (i = 0; i < bankAndPresetValue.length; i++) {
            tmpBankPreset = bankAndPresetValue[i][0] + " : " + bankAndPresetValue[i][1];
            if (tmpBankPreset.equals(currentBankPreset)) {
                previousImageClicked = i;
                view.setImageView(i, true);
            }
        }
    }


    /**
     * AsyncTask class to check if connection to guitarix can be establish and display correct dialog based on
     * whether or not a connection can be establish
     */
    private class checkGuitarixConnection extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
                view.showConnectingDialog();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return guitarix.isGuitarixReacheable();
        }

        @Override
        protected void onPostExecute(Boolean isConnected) {
            if (isConnected) {
                socketConnected = true;
                initialSetup();
                view.showGuitarixConnectedToast();
            } else {
                socketConnected = false;
                view.showConnectionErrorDialog();
                }

            view.dismissConnectingDialog();
        }

    }


    /**
     * AsyncTask class to set guitarix bank and preset
     */
    private class SetBankAndPreset extends AsyncTask<Void, Void, Void> {

        private String bank;
        private String preset;

        /**
         * Set guitarix bank and preset to the specified parameters
         *
         * @param bank   string that specify the bank
         * @param preset string that specify the preset
         */
        SetBankAndPreset(String bank, String preset) {
            super();
            this.bank = bank;
            this.preset = preset;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            guitarix.setBankAndPreset(bank, preset);
            return null;
        }

    }

    /**
     * AsyncTask class to retrieve the bank and preset list. If successful the bank and preset list
     * is build. If unsuccessful a connection error dialog is shown.
     */
    private class RetrieveBankPresetList extends AsyncTask<Void, Void, ResponseBankPresetList> {

        @Override
        protected ResponseBankPresetList doInBackground(Void... voids) {
            try {
                return guitarix.getBankAndPresetList();
            } catch (IOException e) {
                Log.e(TAG, "Unable to retrieve bank preset list", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(ResponseBankPresetList response) {
            if (response != null) {
                buildBankAndPresetList(response);
            } else {
                socketConnected = false;
                view.showConnectionErrorDialog();
            }
        }
    }

    /**
     * AsyncTask class to retrieve current selected guitarix bank and preset . If successful activity view
     * bank and preset textview is set to the current bank:preset. If unsuccessful a connection error
     * dialog is shown.
     */
    private class RetrieveCurrentBankPreset extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return guitarix.getCurrentBankAndPreset();
            } catch (IOException e) {
                Log.e(TAG, "Unable to retrieve current bank and preset", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                currentBankPreset = s;
                view.setCurrentBankPreset(s);
                currentBankPresetExist();
            } else {
                socketConnected = false;
                view.showConnectionErrorDialog();
            }
        }
    }

}
