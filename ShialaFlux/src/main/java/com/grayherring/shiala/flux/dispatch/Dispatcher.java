package com.grayherring.shiala.flux.dispatch;

import com.grayherring.shiala.flux.actions.BaseAction;

/**
 * Created by davidmedina on 4/22/16.
 */
public interface Dispatcher<Subscriber, Action extends BaseAction> {

  void registerStore(Subscriber s);

  void sendAction(Action action);

  void unregisterstore(Subscriber s);
}
