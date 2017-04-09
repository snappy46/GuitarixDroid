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

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.TextView;
import com.github.anastr.speedviewlib.ImageSpeedometer;
import com.marcel.guitarixdroid.R;
import com.marcel.guitarixdroid.presenter.GuitarixPresenter;
import com.marcel.guitarixdroid.presenter.Presenter;

/**
 * Tuner activity
 */
public class TunerActivity extends AppCompatActivity implements TunerActivityView {

    TextView tunerTextView;
    TextView noteTextView;
    ImageSpeedometer gaugeView;

    /* instanciate presenter  */
    private final Presenter presenter = new GuitarixPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            // store the state of the view action bar menu

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.tuner_title);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }
        /* Find views   */
        tunerTextView = (TextView) findViewById(R.id.tuner_textView);
        noteTextView = (TextView) findViewById(R.id.note_textview);
        gaugeView = (ImageSpeedometer) findViewById(R.id.imageSpeedometer);

        presenter.tunerPreferences();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // start tuner on activity start
        presenter.tunerStart(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // stop tuner on activity stop
        presenter.tunerStart(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void updateTunerValue(String tunerParam, String note, int cents) {
        tunerTextView.setText(tunerParam);
        gaugeView.speedPercentTo(cents + 50);
        noteTextView.setText(note);
        if (note.equals("E2") || note.equals("A2") || note.equals("D3") || note.equals("G3")
                || note.equals("B3") || note.equals("E4") && cents <= 5 && cents >= -5)
            noteTextView.setTextColor(getResources().getColor(R.color.gaugeMediumSection));
        else
            noteTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public Context getContext(){
        return this;
    }

}
