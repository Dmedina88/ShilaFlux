package com.grayherring.shiala.simplesample.flux;

import com.grayherring.shiala.flux.event.ChangeEvent;

public class SimpleChangeEvent extends ChangeEvent<AppState,SimpleAction> {
  public SimpleChangeEvent(AppState state, SimpleAction action) {
    super(state, action);
  }
}
