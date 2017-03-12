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

package com.marcel.guitarixdroid.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.marcel.guitarixdroid.R;
import com.marcel.guitarixdroid.presenter.GuitarixPresenter;
import com.marcel.guitarixdroid.presenter.Presenter;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;


/**
 * Main activity class launched on start up.
 */
public class MainActivity extends AppCompatActivity implements MainActivityView, View.OnClickListener,
        View.OnLongClickListener {

    private TextView bankPresetText;
    private int screenHeight;
    private boolean lockPreset;
    private Context context;
    private Animation animFadein;
    private Menu menu;
    private MaterialDialog connectProgressDialog;

    /* instanciate presenter  */
    private final Presenter presenter = new GuitarixPresenter(this);

    /* Arrays to hold Imageview and TextView  */
    private ArrayList<ImageView> myPresetImages = new ArrayList<>();
    private ArrayList<TextView> myPresetText = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenHeight = getScreenHeightInPixels();
        context = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
    // store the state of the view action bar menu

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.app_name);
                getSupportActionBar().setIcon(R.drawable.ic_guitar_icon);
            }
        }
        /* initialize views  */
        initializeViews();
        /* ImageView animation on click */
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        presenter.onCreate();
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        lockPreset = SP.getBoolean("lock_preset", false);
        screenState(SP.getBoolean("screen_on", false));
        lockScreenOrientation(SP.getBoolean("lock_screen_rotation", false));
        presenter.onResume();
    }



    /**
     * set screen state to always remoin on or to allow to turn off.
     * @param alwaysOn true if you want the screen to stay on; false otherwise
     */
    private void screenState (boolean alwaysOn) {
        if (alwaysOn)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        else
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * Lock screen orientation to the current screen orientation
     * @param lock true to lock screen in the device default orientation (normally portrait
     *             on phones and landscape on tablets.
     */
    private void lockScreenOrientation(boolean lock) {
        if (lock)
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    }

    /**
     * Control the views initialization process.
     */
    private void initializeViews() {
        bankPresetText = (TextView) findViewById(R.id.current_bank_preset_text);
        initializeImageViews();
        initializePresetTextView();
        initializeFrameLayout();
    }

    /**
     * Initialize and set click listeners for the imageviews
     */
    private void initializeImageViews() {

        myPresetImages.add((ImageView) findViewById(R.id.imagePreset1));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset2));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset3));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset4));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset5));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset6));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset7));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset8));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset9));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset10));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset11));
        myPresetImages.add((ImageView) findViewById(R.id.imagePreset12));

        for (int i = 0; i < myPresetImages.size(); i++) {
            myPresetImages.get(i).setOnClickListener(this);
            myPresetImages.get(i).setOnLongClickListener(this);
        }
    }


    /**
     * Initialize all preset TextView
     */
    private void initializePresetTextView() {
        myPresetText.add((TextView) findViewById(R.id.preset1Text));
        myPresetText.add((TextView) findViewById(R.id.preset2Text));
        myPresetText.add((TextView) findViewById(R.id.preset3Text));
        myPresetText.add((TextView) findViewById(R.id.preset4Text));
        myPresetText.add((TextView) findViewById(R.id.preset5Text));
        myPresetText.add((TextView) findViewById(R.id.preset6Text));
        myPresetText.add((TextView) findViewById(R.id.preset7Text));
        myPresetText.add((TextView) findViewById(R.id.preset8Text));
        myPresetText.add((TextView) findViewById(R.id.preset9Text));
        myPresetText.add((TextView) findViewById(R.id.preset10Text));
        myPresetText.add((TextView) findViewById(R.id.preset11Text));
        myPresetText.add((TextView) findViewById(R.id.preset12Text));
    }


    /**
     * Intialize frame layout size
     */
    private void initializeFrameLayout() {
        setFrameLayoutHeight(R.id.framePreset1);
        setFrameLayoutHeight(R.id.framePreset2);
        setFrameLayoutHeight(R.id.framePreset3);
        setFrameLayoutHeight(R.id.framePreset4);
        setFrameLayoutHeight(R.id.framePreset5);
        setFrameLayoutHeight(R.id.framePreset6);
        setFrameLayoutHeight(R.id.framePreset7);
        setFrameLayoutHeight(R.id.framePreset8);
        setFrameLayoutHeight(R.id.framePreset9);
        setFrameLayoutHeight(R.id.framePreset10);
        setFrameLayoutHeight(R.id.framePreset11);
        setFrameLayoutHeight(R.id.framePreset12);
    }

    /**
     * Establish framelayout size based on screen height
     *
     * @param frameId Framelayout id
     */
    private void setFrameLayoutHeight(int frameId) {
        FrameLayout layout = (FrameLayout) findViewById(frameId);
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height = (screenHeight - convertDipToPixels(180)) / getResources().getInteger(R.integer.frame_layout_divider);
        layout.setLayoutParams(params);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // setting menu activity is launched
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        // about activity is launched
        if (id == R.id.action_about) {
            AppCompatDialogFragment newFragment = AboutDialogFragment.newInstance();
            newFragment.show(getSupportFragmentManager(), "dialog");
            return true;
        }

        // connect action
        if (id == R.id.action_connect) {
            presenter.onConnectMenuClicked();
            return true;
        }

        // clear all preset
        if (id == R.id.action_clear_preset) {
            new MaterialDialog.Builder(this)
                    .title("Clear all the presets?")
                    .content("This will clear all the guitarix presets stored on the device. Guitarix presets will remain intact.  Do you wish to continue?")
                    .positiveText(R.string.agree)
                    .negativeText("Cancel")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            presenter.clearAllPreset();
                        }
                    })
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Determine the screen height.
     * @return screen height in pixels
     */
    private int getScreenHeightInPixels() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }


    /**
     * convert dp to pixel
     *
     * @param dp density
     * @return dimension in pixels
     */
    private int convertDipToPixels(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * Imageview event click listeners
     *
     * @param view Imageview that was clicked
     */
    @Override
    public void onClick(View view) {
        ImageView image = (ImageView) view;
        image.startAnimation(animFadein);
        presenter.onImageViewClicked(Integer.parseInt(view.getTag().toString()));
    }


    /**
     * Imageview event onlong click listeners. Only active when lockpreset is false(switch off)
     *
     * @param view Imageview
     * @return boolean
     */
    @Override
    public boolean onLongClick(View view) {

        if (!lockPreset) {
            final int selectedImage = Integer.parseInt(view.getTag().toString());
            new MaterialDialog.Builder(context)
                    .title("Select Bank")
                    .items(presenter.getBankList())
                    .itemsCallback(new MaterialDialog.ListCallback() {
                        @Override
                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            final int selectedBank = which;
                            List<String> presetList = presenter.getPresetList((String) text);
                            new MaterialDialog.Builder(context)
                                    .title("Select Preset")
                                    .items(presetList)
                                    .itemsCallback(new MaterialDialog.ListCallback() {
                                        @Override
                                        public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                            int selectedPreset = which;
                                            presenter.setPreferenceBankAndPreset(selectedImage, selectedBank, selectedPreset);
                                        }
                                    })
                                    .show();
                        }
                    })
                    .show();

            return false;
        }
        return true;
    }

    public void setMenuIcon(boolean connected) {
        if (menu != null) {
            if (connected) menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_action_connected));
            else menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_action_not_connected));
        }
    }

    /**
     * Display proper imageview based on whether or not it is selected
     * @param imagePosition imageview position in the ImageView arraylist
     * @param selected true when imageview is selected
     */
    @Override
    public void setImageView(int imagePosition, boolean selected) {
        ImageView imageSelected = myPresetImages.get(imagePosition);
        if (selected) {
            imageSelected.setImageDrawable(getResources().getDrawable(R.drawable.red_guitar_icon_selected));
        } else {
            imageSelected.setImageDrawable(getResources().getDrawable(R.drawable.red_guitar_icon));
        }
    }

    /**
     * Set bankPreset text view to bank preset string
     * @param bankPreset  string that represent the bank and preset selected.
     */
    @Override
    public void setCurrentBankPreset(String bankPreset) {
        bankPresetText.setText(bankPreset);
    }

    /**
     * Display connecting dialog
     */
    @Override
    public void showConnectingDialog() {
        connectProgressDialog = new MaterialDialog.Builder(this)
                .title(R.string.waiting)
                .content(R.string.connecting)
                .progress(true, 0)
                .show();
    }

    /**
     * Dismiss connecting dialog display
     */
    @Override
    public void dismissConnectingDialog() {
        connectProgressDialog.dismiss();
    }


    /**
     * Display connection error dialog
     */
    @Override
    public void showConnectionErrorDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.title_connection_error)
                .content(R.string.content)
                .positiveText(R.string.agree)
                .show();
        setMenuIcon(false);
        for (int i = 0; i < myPresetImages.size(); i++) {
            setImageView(i, false);
            setCurrentBankPreset("");
        }
    }

    /**
     * Display guitarix connected toast
     */
    @Override
    public void showGuitarixConnectedToast() {
        Toasty.success(this, getString(R.string.connected), Toast.LENGTH_SHORT, true).show();
        setMenuIcon(true);
    }

    /**
     *
     * @return view context
     */
    @Override
    public Context getContext() {
        return this;
    }


    /**
     * Set presetText
     * @param index integer representing specific preset in the mypresetText array.
     * @param text string representing the preset value
     */
    @Override
    public void setImagePresetTextView(int index, String text) {
        myPresetText.get(index).setText(text);
    }


}
