package com.grayherring.shiala.flux.util;

import com.grayherring.shiala.flux.event.ChangeEvent;
import com.grayherring.shiala.flux.event.ChangeEventListener;
import java.util.List;

//// TODO: 5/11/16  cant i jsut use change event and not use a template?
public abstract class BaseEventCapturer<T extends ChangeEvent> implements ChangeEventListener<T> {


  @Override public void call(T t) {
    save(t);
  }

  public abstract void save(T t);

  public abstract T eventAt(int index);
  public abstract List<T> getEvents();

  public abstract void clear();

}
