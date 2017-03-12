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

package com.marcel.guitarixdroid.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcel.guitarixdroid.util.SocketClient;

import java.io.IOException;

/**
 * Class to handle Guitarix activities/task.
 */
public class Guitarix {

    // Mapper used to map API responses
    private ObjectMapper mapper = new ObjectMapper();

    private String address; // Holds Guitarix server IP address;
    private int port;  // holds Guitarix server Port number;

    /**
     * Retrieves IP address and Port number from settings
     * @param context application context
     */
    public void getPreferences(Context context){
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(context);
        address = SP.getString("ip_host", "");
        port = Integer.parseInt(SP.getString("port", "7000"));
    }

    /**
     * Retrieve Guitarix current bank value
     * @return Guitarix current bank as a string
     * @throws IOException
     */
    public String getCurrentBank() throws IOException {
        final SocketClient mySocket = new SocketClient(address, port);
        if (mySocket.openSocket()) {
            mySocket.setId(1);
            mySocket.addParam("system.current_bank");
            mySocket.setMethod("get");
            mySocket.sendJSON();
            ResponseBankPreset response = mapper.readValue(mySocket.receiveJSON(), ResponseBankPreset.class);
            mySocket.closeSocket();
            return response.getResult().getSystemCurrentBank();
        }
        return null;
    }

    /**
     * Retrieve Guitarix current preset value
     * @return Guitarix current preset as string
     * @throws IOException
     */
    public String getCurrentPreset() throws IOException {
        final SocketClient mySocket = new SocketClient(address, port);
        if (mySocket.openSocket()) {
            mySocket.setId(2);
            mySocket.addParam("system.current_preset");
            mySocket.setMethod("get");
            mySocket.sendJSON();
            ResponseBankPreset response = mapper.readValue(mySocket.receiveJSON(), ResponseBankPreset.class);
            mySocket.closeSocket();
            return response.getResult().getSystemCurrentPreset();
        }
        return null;
    }

    /**
     * Retrieve Guitarix current bank and preset value
     * @return Guitarix current bank and preset as a concatenated string with bank and preset separated with
     * a colon (:)
     * @throws IOException
     */
    public String getCurrentBankAndPreset () throws IOException {
        final SocketClient mySocket = new SocketClient(address, port);
        if (mySocket.openSocket()) {
            StringBuilder formattedResponse = new StringBuilder();
            mySocket.setId(3);
            mySocket.addParam("system.current_bank");
            mySocket.addParam("system.current_preset");
            mySocket.setMethod("get");
            mySocket.sendJSON();
            ResponseBankPreset response = mapper.readValue(mySocket.receiveJSON(), ResponseBankPreset.class);
            formattedResponse.append(response.getResult().getSystemCurrentBank());
            formattedResponse.append(" : ");
            formattedResponse.append(response.getResult().getSystemCurrentPreset());
            mySocket.closeSocket();
            return formattedResponse.toString();
        }
        return null;
    }

    /**
     * Retrieve Guitarix full bank and preset list currently available.
     * @return RepknseBankPresetList class holding the bank and preset list.
     * @throws IOException
     */
    public ResponseBankPresetList getBankAndPresetList () throws IOException {
        final SocketClient mySocket = new SocketClient(address, port);
        if (mySocket.openSocket()) {
            mySocket.setId(1);
            mySocket.setMethod("banks");
            mySocket.sendJSON();
            return mapper.readValue(mySocket.receiveJSON(), ResponseBankPresetList.class);
        }
        return null;
    }

    /**
     * Set Guitarix bank and preset. Preset name must be present in the bank name.
     * @param bank string representing the bank name
     * @param preset string representing the preset name
     * @return true if operation was successful; false otherwise.
     */
    public boolean setBankAndPreset(String bank, String preset) {
        final SocketClient mySocket = new SocketClient(address, port);
        if (mySocket.openSocket()) {
            mySocket.addParam(bank);
            mySocket.addParam(preset);
            mySocket.setMethod("setpreset");
            return mySocket.sendJSON();
        }
        return false;
    }

    /**
     * Check to see if Guitarix server is reacheable
     * @return true if reacheable otherwise returns false.
     */
    public boolean isGuitarixReacheable() {
        final SocketClient mySocket = new SocketClient(address, port);
            if (mySocket.openSocket()) {
                mySocket.closeSocket();
                 return true;
            }
        return false;
    }

}
