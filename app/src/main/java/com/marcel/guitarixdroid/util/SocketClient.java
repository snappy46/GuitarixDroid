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

package com.marcel.guitarixdroid.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Helper class to handle socket communication.
 */
public class SocketClient {

    private String dstAddress;
    private int dstPort;
    private JSONArray params = new JSONArray();
    private String mMethod;
    private int id = -1;
    private Socket socket;
    private final String TAG = "SocketClient";

    /**
     * Default constructor
     * @param addr IP address or hostname as a string
     * @param port Port number as an integer.
     */
    public SocketClient(String addr, int port) {
        dstAddress = addr;
        dstPort = port;
    }

    /**
     * Open socket and throw IO exception if socket communication cannot be established.
     * @return true is connection is established; false otherwise.
     */
    public boolean openSocket() {
        try {
            socket = new Socket(dstAddress, dstPort);
        } catch (IOException e) {
            Log.e(TAG, "Cannot open socket", e);
            return false;
        }
        return true;
    }

    /**
     * Close socket and throw IO exception if socket cannot be closed.
     * @return true is connection socket has been closed; false otherwise.
     */
    public boolean closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Cannot close socket", e);
            return false;
        }
        return true;
    }


    /**
     * Receive and process JSON Object received from server
     * @return string that represent JSON object
     */
    public String receiveJSON() {
        if (socket != null) {
            String response = "";
            try {
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                response = inFromServer.readLine();

            } catch (UnknownHostException e) {
                response = "UnknownHostException: " + e.toString();
                Log.e(TAG, response);
            } catch (IOException e) {
                response = "IOException: " + e.toString();
                Log.e(TAG, response);
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        response = "IOException: " + e.toString();
                        Log.e(TAG, response);
                    }
                }
            }
            return response;
        }
        return null;
    }


    /**
     * Sends JSON object to the socket server
     * @return true if communication is successful; false otherwise.
     */
    public boolean sendJSON() {
        if (socket != null && buildParam() != null) {
            try {
                String jsonrpcRequest = buildParam().toString();
                DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                outToServer.writeBytes(jsonrpcRequest + '\n');
                return true;
            } catch (UnknownHostException e) {
                Log.e(TAG, "Unable to send JSON string", e);
               return false;
            } catch (IOException e) {
                Log.e(TAG, "unable to send JSON string", e);
                return false;
            }
        }
        return false;
    }


    /**
     * Set JSON Object method
     * @param method String that represent the method
     */
    public void setMethod(String method) {
        mMethod = method;
    }

    /**
     * Set JSON Object parameters
     * @param mStrparams  String that represent the parameter(s)
     */
    public void addParam(String mStrparams) {
        params.put(mStrparams);
    }

    /**
     * Set JSON Object id
     * @param id Integer identifiying the id
     */
    public void setId(int id) {
        this.id = id;
    }


    /**
     * Method to build the JSONObject; Throw exception if JSONObject cannot be build.
     * @return Build JSONObject or null on exception error.
     */
    private JSONObject buildParam() {
        JSONObject object = new JSONObject();

        try {

            object.put("params", params);

            object.put("jsonrpc", "2.0");

            object.put("method", mMethod);

            if (id != -1) {
                object.put("id", id);
            }

        } catch (Exception e) {
            Log.e(TAG, "Error building JSON object string", e);
            object = null;
        }

        return object;
    }

}