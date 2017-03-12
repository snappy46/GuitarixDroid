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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Wrapper class to handle the bank and preset list JSON object.
 */
public class ResultBankPreset {

    @JsonProperty("system.current_bank")
    private String systemCurrentBank;

    @JsonProperty("system.current_preset")
    private String systemCurrentPreset;

    public String getSystemCurrentBank() {
        return systemCurrentBank;
    }

    public void setSystemCurrentBank(String systemCurrentBank) {
        this.systemCurrentBank = systemCurrentBank;
    }

    public String getSystemCurrentPreset() {
        return systemCurrentPreset;
    }

    public void setSystemCurrentPreset(String systemCurrentPreset) {
        this.systemCurrentPreset = systemCurrentPreset;
    }
}
