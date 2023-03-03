package com.digwex.network.websockets;

import com.google.gson.JsonElement;

import java.io.Serializable;
import java.util.HashMap;

public class SocketServerJson implements Serializable {
  public Integer commands;
  public Integer id;
  public HashMap<Integer, JsonElement> extra;
}
