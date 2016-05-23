package grayherring.shila.flux;

import android.content.Intent;
import grayherring.shila.model.Book;
import java.util.ArrayList;
import java.util.List;

//todo should make this a value object and make a builder
public class Payload {

  public List<Book> books = new ArrayList();
  public Intent intent;
  public int pos = 0;
  public String filter;

  public Book getBook() {
    return books.get(0);
  }
}
