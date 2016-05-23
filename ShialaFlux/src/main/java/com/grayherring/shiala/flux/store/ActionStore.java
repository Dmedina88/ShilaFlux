package com.grayherring.shiala.flux.store;

import com.grayherring.shiala.flux.actions.BaseAction;
import com.grayherring.shiala.flux.event.ChangeEventListener;
import rx.functions.Action1;

/**
 * Created by davidmedina on 4/22/16.
 */
public abstract class ActionStore<State extends BaseState,Action extends BaseAction> implements Action1<Action> {

  public abstract State getState();

  public abstract void register(ChangeEventListener changeEventListener);

  public abstract void unregister(ChangeEventListener changeEventListener);
}
