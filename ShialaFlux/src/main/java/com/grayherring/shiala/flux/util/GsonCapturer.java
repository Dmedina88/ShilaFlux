package com.grayherring.shiala.flux.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonCapturer<T extends Object> extends BaseCapturer<T> {

  protected static final String CAPTURER_PREF = "FLUX_pref_FLux_mess";

  protected  final String key;

  Type type;

  protected Gson gson;
  protected SharedPreferences sharedPreferences;
  protected SharedPreferences.Editor editer;

  public GsonCapturer(Gson gson, Type type, Context context,  String key) {
    this.key = key;
    if (gson == null) {
      this.gson = new Gson();
    } else {
      this.gson = gson;
    }
    this.type = type;
    sharedPreferences = context.getSharedPreferences(CAPTURER_PREF, Context.MODE_PRIVATE);
    editer = sharedPreferences.edit();
  }

  //// TODO: 5/11/16 I hate this hotmess
  @Override public void save(T t) {

    String jsonEventList = sharedPreferences.getString(key, "");
    List<T> events = new ArrayList<>();
    if (!jsonEventList.isEmpty()) {
      //// TODO: 5/16/16 why cant i use type here  UHG 
      Type listOfItems = Types.newParameterizedType(List.class, new TypeToken<T>() {}.getRawType());
      events = gson.fromJson(jsonEventList, listOfItems);
    }
    events.add(t);
    editer.putString(key, gson.toJson(events));
    editer.apply();
  }

  @Override public T itemAt(int index) {
    List<T> events = getItems();
    if (events != null) {
      return events.get(index);
    }
    return null;
  }


  @Override public List<T> getItems() {
    String jsonEventList = sharedPreferences.getString(key, "");
    List<T> events;
    if (!jsonEventList.isEmpty()) {
      Type listOfItems = Types.newParameterizedType(List.class, type);
      events = gson.fromJson(jsonEventList, listOfItems);

      return events;
    }
    return null;
  }

  @Override public void clear() {
    editer.clear();
    editer.apply();
  }

  public String getGsonString(){
    return sharedPreferences.getString(key, "");
  }

}
