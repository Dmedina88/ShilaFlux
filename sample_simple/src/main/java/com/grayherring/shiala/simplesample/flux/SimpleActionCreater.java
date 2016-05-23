package com.grayherring.shiala.simplesample.flux;

import com.grayherring.shiala.flux.actions.ActionCreator;

public class SimpleActionCreater extends ActionCreator {

  public final static String VALUE_ACTION ="VALUE_ACTION";
  public final static String CLEAR_ACTION ="CLEAR_ACTION";
  public final static String EVALUATE_ACTION ="EVALUATE_ACTION";
  public static final String UNDO_ACTION = "undo";

  public static void sendDataValue(String value,String source) {
    Payload payload = new Payload();
    payload.value =value;
    SimpleAction action = new SimpleAction(VALUE_ACTION);
    action.source = source;
    action.payload = payload;
    dispatcher.sendAction(action);
  }

  public static void evaluate(String source) {
    SimpleAction action = new SimpleAction(EVALUATE_ACTION);
    action.source = source;
    dispatcher.sendAction(action);
  }

  public static void clear(String source) {
    SimpleAction action = new SimpleAction(CLEAR_ACTION);
    action.source = source;
    dispatcher.sendAction(action);
  }

  public static void undo(String source) {
    SimpleAction action = new SimpleAction(UNDO_ACTION);
    action.source = source;
    dispatcher.sendAction(action);
  }
}
