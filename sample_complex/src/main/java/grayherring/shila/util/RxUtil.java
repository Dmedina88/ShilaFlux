package grayherring.shila.util;

import rx.Subscription;

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
}