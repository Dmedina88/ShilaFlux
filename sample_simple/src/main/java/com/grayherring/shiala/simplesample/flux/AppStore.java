package com.grayherring.shiala.simplesample.flux;

import android.support.v4.util.CircularArray;
import android.util.Log;
import com.grayherring.shiala.flux.event.ChangeEventListener;
import com.grayherring.shiala.flux.store.ActionStore;
import com.grayherring.shiala.flux.util.RxUtil;
import com.grayherring.shiala.simplesample.util.Eval;
import com.jakewharton.rxrelay.PublishRelay;
import java.util.HashMap;
import java.util.Map;
import rx.Subscription;

import static com.grayherring.shiala.simplesample.flux.SimpleActionCreater.CLEAR_ACTION;
import static com.grayherring.shiala.simplesample.flux.SimpleActionCreater.EVALUATE_ACTION;
import static com.grayherring.shiala.simplesample.flux.SimpleActionCreater.UNDO_ACTION;
import static com.grayherring.shiala.simplesample.flux.SimpleActionCreater.VALUE_ACTION;

public class AppStore extends ActionStore<AppState, SimpleAction> {

  static AppStore instance;
  AppState appState = new AppState("", "");
  AppState.AppStateBuilder appBuilder = new AppState.AppStateBuilder();
  PublishRelay<SimpleChangeEvent> dataRelay;
  Map<ChangeEventListener, Subscription> subscriptionHashMap;
  CircularArray<AppState> appStateCircularArray = new CircularArray(10);

  public AppStore(PublishRelay publishRelay) {
    dataRelay = publishRelay;
    subscriptionHashMap = new HashMap<>();
    appStateCircularArray.addLast(appState);
  }

  @Override public AppState getState() {
    return appState;
  }

  @Override
  public void register(ChangeEventListener changeEventListener) {
    subscriptionHashMap.put(changeEventListener, dataRelay.subscribe(changeEventListener));
  }

  @Override
  public void unregister(ChangeEventListener changeEventListener) {
    RxUtil.unSubscribeIfNeeded(subscriptionHashMap.get(changeEventListener));
  }

  public static AppStore getInstance(PublishRelay publishRelay) {
    if (instance == null) {
      instance = new AppStore(publishRelay);
    }
    return instance;
  }

  public static AppStore getInstance() {
    if (instance == null) {
      instance = new AppStore(PublishRelay.create());
    }
    return instance;
  }

  @Override public void call(SimpleAction simpleAction) {

    appBuilder.setErrorMessage("");
    switch (simpleAction.name) {
      case VALUE_ACTION: {
        appState =
            appBuilder.setDisplayResult(appState.displayResult() + simpleAction.payload.value)
                .createAppState();
        appStateCircularArray.addLast(appState);

        dataRelay.call(new SimpleChangeEvent(appState, simpleAction));
        break;
      }
      case EVALUATE_ACTION:
        try {
          String result = String.valueOf(Eval.eval(appState.displayResult()));
          appState = appBuilder.setDisplayResult(result).createAppState();
          appStateCircularArray.addLast(appState);
          dataRelay.call(new SimpleChangeEvent(appState, simpleAction));
        } catch (Exception e) {
          appState = appBuilder.setErrorMessage(e.toString()).createAppState();
          dataRelay.call(new SimpleChangeEvent(appState, simpleAction));

          Log.d("appStore", e.getMessage());
        }
        break;
      case CLEAR_ACTION:
        appState = appBuilder.setDisplayResult("").createAppState();
        dataRelay.call(new SimpleChangeEvent(appState, simpleAction));
        break;
      case UNDO_ACTION:
        if (appStateCircularArray.size() > 0 && appStateCircularArray.getLast() != null
            && appStateCircularArray.getLast().equals(appState)) {
          appStateCircularArray.popLast();
        }
        if (appStateCircularArray.size() > 0 && appStateCircularArray.getLast() != null) {
          appState = appStateCircularArray.popLast();
          // appBuilder.setDisplayResult(appState.displayResult());
          dataRelay.call(new SimpleChangeEvent(appState, simpleAction));
        } else {
          appState = appBuilder.setErrorMessage("can not undo past this point").createAppState();
          dataRelay.call(new SimpleChangeEvent(appState, simpleAction));
          // send no undo event
        }
        break;
    }
  }
}
