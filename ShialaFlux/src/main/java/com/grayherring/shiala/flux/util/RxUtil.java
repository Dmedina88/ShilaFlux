package com.grayherring.shiala.flux.util;

import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscription;
import rx.functions.Func2;
import rx.schedulers.TimeInterval;

public final class RxUtil {
  /**
   * Returns a boolean indicating whether a subscription is already being made
   */
  public static boolean inFlight(final Subscription subscription) {
    return subscription != null && !subscription.isUnsubscribed();
  }

  /**
   * UnSubscribe if the subscription is in flight
   */
  public static void unSubscribeIfNeeded(final Subscription subscription) {
    if (inFlight(subscription)) {
      subscription.unsubscribe();
    }
  }

  /**
   * UnSubscribe if the subscriptions are in flight
   */
  public static void unSubscribeIfNeeded(final Subscription... subscriptions) {
    for (final Subscription subscription : subscriptions) {
      unSubscribeIfNeeded(subscription);
    }
  }
  /**
   * creates an observable that emits object with time between
   */
  public static Observable timedObservable(int delay,TimeUnit timeUnit, final Observable delayedObservable) {

    Observable<TimeInterval<Long>> timer = Observable.interval(delay, timeUnit).timeInterval();
    return Observable.zip(timer, delayedObservable, new Func2() {
      @Override public Object call(Object o, Object o2) {
        return o2;
      }
    });
  }

}
