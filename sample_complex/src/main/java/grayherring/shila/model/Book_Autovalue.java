package grayherring.shila.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 12/3/2015.
 */
//// TODO: 5/23/16 use this auto value verson of the model some day 
@AutoValue
public abstract class Book_Autovalue implements Parcelable {

  public static TypeAdapter<Book_Autovalue> typeAdapter(Gson gson) {
    return new AutoValue_Book_Autovalue.GsonTypeAdapter(gson);
  }

  @Nullable abstract public String author();

  @Nullable abstract public String categories();

  @Nullable abstract public String lastCheckedOut();

  @Nullable abstract public String lastCheckedOutBy();

  @Nullable abstract public String publisher();

  @Nullable abstract public String title();

  @Nullable abstract public String url();

  abstract public int id();

  public String readableDate() {

    DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    fromFormat.setLenient(false);
    DateFormat toFormat = new SimpleDateFormat("MMMM dd, yyyy HH:mm a");
    toFormat.setLenient(false);
    String dateString = "";
    try {
      Date date = fromFormat.parse(this.lastCheckedOut());
      dateString = toFormat.format(date);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return dateString;
  }

  public String shareString() {
    String string = "Title: " + title() + '\n' +
        "Author: " + author() + '\n';
    if (categories() != null) {
      string = string + "Categories: " + categories() + '\n';
    }
    if (publisher() != null) {
      string = string + "Publisher: " + publisher() + '\n';
    }
    return string;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public String getUrl() {
    return url().replace("books/", "").replace("/", "");
  }

  public static class AutoValueTypeAdapterFactory implements TypeAdapterFactory {
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
      Class<? super T> rawType = type.getRawType();
      if (rawType.equals(Book_Autovalue.class)) {
        return (TypeAdapter<T>) Book_Autovalue.typeAdapter(gson);
      }
      return null;
    }
  }
}
