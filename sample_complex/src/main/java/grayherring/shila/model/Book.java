package grayherring.shila.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David on 12/3/2015.
 */

public class Book extends RealmObject {

  private String author;
  private String categories;
  private String lastCheckedOut;
  private String lastCheckedOutBy;
  private String publisher;
  private String title;
  @PrimaryKey
  private Integer id;

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getCategories() {
    return categories;
  }

  public void setCategories(String categories) {
    this.categories = categories;
  }

  public String getLastCheckedOut() {
    return lastCheckedOut;
  }

  public void setLastCheckedOut(String lastCheckedOut) {
    this.lastCheckedOut = lastCheckedOut;
  }

  public String getLastCheckedOutBy() {
    return lastCheckedOutBy;
  }

  public void setLastCheckedOutBy(String lastCheckedOutBy) {
    this.lastCheckedOutBy = lastCheckedOutBy;
  }

  public String getPublisher() {
    return publisher;
  }

  public void setPublisher(String publisher) {
    this.publisher = publisher;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
