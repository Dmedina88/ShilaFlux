package com.grayherring.shiala.simplesample.flux;

import com.grayherring.shiala.flux.actions.BaseAction;

public class SimpleAction extends BaseAction<Payload,String> {
  public SimpleAction(String name, String source,
      Payload payload) {
    super(name, source, payload);
  }

  public SimpleAction(String name) {
    super(name);
  }


}
