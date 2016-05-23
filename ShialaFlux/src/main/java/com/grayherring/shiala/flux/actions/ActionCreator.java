package com.grayherring.shiala.flux.actions;

import com.grayherring.shiala.flux.dispatch.Dispatcher;

/**
 * Created by davidmedina on 4/22/16.
 */
public class ActionCreator {

  static protected Dispatcher dispatcher;

  public static void init(Dispatcher dispatcher) {
    ActionCreator.dispatcher = dispatcher;
  }



  public static void registerStore(Object store){
    dispatcher.registerStore(store);
  }

  public static void unregisterStore(Object store){
    dispatcher.unregisterstore(store);

  }

}
