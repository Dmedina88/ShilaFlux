package com.grayherring.shiala.flux.dispatch;

import com.grayherring.shiala.flux.actions.BaseAction;
import com.grayherring.shiala.flux.util.RxUtil;
import com.jakewharton.rxrelay.PublishRelay;
import java.util.HashMap;
import java.util.Map;

import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by davidmedina on 4/22/16.
 */
public class DefaultDispatcher<t extends BaseAction> implements Dispatcher<Action1<t>, t> {


  final PublishRelay<t> storeRelay;
  final Map<Action1<t>, Subscription> subscriptionHashMap;

  public DefaultDispatcher() {
    storeRelay = PublishRelay.create();
    subscriptionHashMap = new HashMap<>();
  }


  @Override public void registerStore(Action1 s) {
    subscriptionHashMap.put(s, storeRelay.subscribe(s));
  }

  @Override public void sendAction(t action) {
    storeRelay.call(action);
  }

  @Override public void unregisterstore(Action1 s) {
    RxUtil.unSubscribeIfNeeded(subscriptionHashMap.get(s));
  }
}
