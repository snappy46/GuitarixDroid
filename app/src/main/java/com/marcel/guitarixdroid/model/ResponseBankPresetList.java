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


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "jsonrpc",
        "id",
        "result"
})

/**
 * Wrapper class to handle Guitarix bank and preset list JSON response
 */
public class ResponseBankPresetList {

    @JsonProperty("jsonrpc")
    private String jsonrpc;
    @JsonProperty("id")
    private String id;
    @JsonProperty("result")
    private List<ResultBankPresetList> result = null;

    @JsonProperty("jsonrpc")
    public String getJsonrpc() {
        return jsonrpc;
    }

    @JsonProperty("jsonrpc")
    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("result")
    public List<ResultBankPresetList> getResult() {
        return result;
    }

    @JsonProperty("result")
    public void setResult(List<ResultBankPresetList> result) {
        this.result = result;
    }


}
