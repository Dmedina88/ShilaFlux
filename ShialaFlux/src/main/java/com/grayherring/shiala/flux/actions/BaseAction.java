package com.grayherring.shiala.flux.actions;

/**
 * Created by davidmedina on 4/22/16.
 */
public class BaseAction<Payload, Source> {

  public String name;
  public Source source;
  public Payload payload;
  //I had a builder for this but then i stopped hating myself
  public BaseAction(String name, Source source, Payload payload) {
    this.name = name;
    this.source = source;
    this.payload = payload;
  }
  public BaseAction(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public Payload getPayload() {
    return payload;
  }

  public boolean sameSorce(Source source) {
    return source.equals(source);
  }
}
