package com.digwex.api.model;

import java.io.Serializable;

public class ActivateResponseJson implements Serializable {
    public int id;
    public String access_token;
    public int offset;
    public SettingsJson settings;
}
