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
        "name",
        "mutable",
        "type",
        "presets"
})

/**
 * Wrapper class to handle Guitarix bank and preset list JSON response.
 */
public class ResultBankPresetList {

    @JsonProperty("name")
    private String name;
    @JsonProperty("mutable")
    private Integer mutable;
    @JsonProperty("type")
    private String type;
    @JsonProperty("presets")
    private List<String> presets = null;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("mutable")
    public Integer getMutable() {
        return mutable;
    }

    @JsonProperty("mutable")
    public void setMutable(Integer mutable) {
        this.mutable = mutable;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("presets")
    public List<String> getPresets() {
        return presets;
    }

    @JsonProperty("presets")
    public void setPresets(List<String> presets) {
        this.presets = presets;
    }


}