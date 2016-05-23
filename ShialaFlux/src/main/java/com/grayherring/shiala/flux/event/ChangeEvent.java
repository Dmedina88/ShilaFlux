package com.grayherring.shiala.flux.event;

import com.grayherring.shiala.flux.actions.BaseAction;
import com.grayherring.shiala.flux.store.BaseState;

/**
 * Created by davidmedina on 5/9/16.
 */
public class ChangeEvent<State extends BaseState,Action extends BaseAction> {

  public State state;
  public Action lastAction;

  public ChangeEvent(State state, Action action) {
    this.state = state;
    this.lastAction = action;
  }

  @Override
  public String toString() {
    return "ChangeEvent{" +
        "state=" + state +
        ", lastAction=" + lastAction +
        '}';
  }
}
