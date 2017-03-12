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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Html;
import android.text.Spanned;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.marcel.guitarixdroid.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Fragment to show about dialog on the screen.
 */
public class AboutDialogFragment extends AppCompatDialogFragment {

    public static AboutDialogFragment newInstance() {
        return new AboutDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View aboutView = View.inflate(getActivity(), R.layout.about, null);

        Activity activity = getActivity();

        String versionName;
        try {
            versionName = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException exc) {
            versionName = null;
        }

        TextView version = (TextView) aboutView.findViewById(R.id.app_version);
        version.setText("v" + versionName);

        TextView tv = (TextView) aboutView.findViewById(R.id.info_text);

        tv.setText(fromHtml(readRawTextFile(R.raw.info, getContext())));

        tv.setLinkTextColor(getResources().getColor(R.color.colorAccent));

        Linkify.addLinks(tv, Linkify.ALL);

        return new AlertDialog.Builder(getActivity())
                .setView(aboutView)
                .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                })
                .create();
    }


    /**
     * Read a textfile and return result as a string
     * @param id  Represent the text file resource id number
     * @param mcontext  fragment context
     * @return string representative of the text file read or null if can't read file (IOException)
     */
    public static String readRawTextFile(int id, Context mcontext) {

        InputStream inputStream = mcontext.getResources().openRawResource(id);

        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader buf = new BufferedReader(in);

        String line;

        StringBuilder text = new StringBuilder();
        try {

            while (( line = buf.readLine()) != null) text.append(line);
        } catch (IOException e) {
            return null;

        }

        return text.toString();

    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

}