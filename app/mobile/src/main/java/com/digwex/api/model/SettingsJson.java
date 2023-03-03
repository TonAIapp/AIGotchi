package com.digwex.api.model;

public class SettingsJson {
  public BrightnessscheduleJson[] brightnessSchedule;
  public int[] rebootSchedule;
  public FillJson fill;
  public WindowJson window;
  public int rotation;


  public class FillJson
  {
    public Boolean image;
    public Boolean video;
  }

  public class WindowJson
  {
    public int x;
    public int y;
    public int width;
    public int height;
    public Boolean fullscreen;
  }

  public class BrightnessscheduleJson
  {
    public int time;
    public double value;
  }
}
