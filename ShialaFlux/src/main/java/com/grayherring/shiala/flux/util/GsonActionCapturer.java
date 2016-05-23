package com.grayherring.shiala.flux.util;

import android.content.Context;
import com.grayherring.shiala.flux.actions.BaseAction;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import rx.functions.Action1;

public class GsonActionCapturer<T extends BaseAction> extends GsonCapturer<T>
    implements Action1<T> {

  private static final String DEFAULT_KEY = "Actions";

  public GsonActionCapturer(Gson gson, Type type,
      Context context, String key) {
    super(gson, type, context, key);
  }

  public GsonActionCapturer(Gson gson, Type type, Context context) {
    super(gson, type, context, DEFAULT_KEY);
  }

  @Override public void call(T o) {
    super.save(o);
  }

  @Override public String getGsonString() {
    return super.getGsonString();
  }
}