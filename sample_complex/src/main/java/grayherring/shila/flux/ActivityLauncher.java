package grayherring.shila.flux;

import android.content.Context;
import android.content.Intent;

public class ActivityLauncher implements SwagChangeEventListener {

  Context context;

  public ActivityLauncher(Context context) {
    this.context = context;
  }

  @Override public void call(SwagChangeEvent swagChangeEvent) {
    if (swagChangeEvent.lastAction.name.equals(SwagActionCreator.START_ACTIVITY)) {
      swagChangeEvent.lastAction.payload.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(swagChangeEvent.lastAction.payload.intent);
    }
  }
}
