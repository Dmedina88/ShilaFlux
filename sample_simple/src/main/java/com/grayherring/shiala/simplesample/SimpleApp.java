package com.grayherring.shiala.simplesample;

import android.app.Application;
import com.grayherring.shiala.flux.actions.ActionCreator;
import com.grayherring.shiala.flux.dispatch.DefaultDispatcher;
import com.grayherring.shiala.flux.util.GsonActionCapturer;
import com.grayherring.shiala.flux.util.RxUtil;
import com.grayherring.shiala.simplesample.flux.AppStore;
import com.grayherring.shiala.simplesample.flux.SimpleAction;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class SimpleApp extends Application {

  @Override public void onCreate() {
    super.onCreate();

    GsonActionCapturer gsonActionCapturer =
        new GsonActionCapturer(null, SimpleAction.class, getApplicationContext());
    if (false) {
      final DefaultDispatcher<SimpleAction> defaultDispatcher = new DefaultDispatcher();
      Observable zippedObservable =
          RxUtil.timedObservable(300, TimeUnit.MILLISECONDS,
              Observable.from(gsonActionCapturer.getItems()))
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(AndroidSchedulers.mainThread())
              .doOnNext(new Action1() {
                @Override public void call(Object o) {
                  defaultDispatcher.sendAction((SimpleAction) o);
                }
              });
      ActionCreator.init(defaultDispatcher);
      defaultDispatcher.registerStore(AppStore.getInstance());
      zippedObservable.subscribe();
    } else {
      gsonActionCapturer.clear();
      ActionCreator.init(new DefaultDispatcher<SimpleAction>());
      ActionCreator.registerStore(gsonActionCapturer);
      ActionCreator.registerStore(AppStore.getInstance());
    }
  }
}
