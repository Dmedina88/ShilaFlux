package com.grayherring.shiala.flux.util;

import java.util.List;

public abstract class BaseCapturer<T extends Object>  {

  public abstract void save(T t);

  public abstract T itemAt(int index);

  public abstract List<T> getItems();

  public abstract void clear();
}
