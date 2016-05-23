package com.grayherring.shiala.flux.util;

import android.content.Context;
import com.grayherring.shiala.flux.event.ChangeEvent;
import com.grayherring.shiala.flux.event.ChangeEventListener;
import com.google.gson.Gson;
import java.lang.reflect.Type;

public class GsonEventCapturer<T extends ChangeEvent> extends GsonCapturer<T> implements
    ChangeEventListener<T> {

  private static final String DEFAULT_KEY = "EVENTS";

  public GsonEventCapturer(Gson gson, Type type,
      Context context, String key) {
    super(gson, type, context, key);
  }

  public GsonEventCapturer(Gson gson, Type type, Context context) {
    super(gson, type, context, DEFAULT_KEY);
  }

  @Override public void call(T o) {
    super.save(o);
  }

  @Override public String getGsonString() {
    return super.getGsonString();
  }
}