package com.android.poesiamonstruosa.utils;

import android.graphics.Point;

public class Page {

  private Point resolution;

  public Page(Point res) {

    this.resolution = res;
  }

  public Point getResolution() {
    return resolution;
  }

  public void setResolution(Point resolution) {
    this.resolution = resolution;
  }


}
