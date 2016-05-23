package grayherring.shila.flux;

import com.grayherring.shiala.flux.event.ChangeEvent;
import grayherring.shila.state.AppState;

/**
 * Created by davidmedina on 5/9/16.
 */
public class SwagChangeEvent extends ChangeEvent<AppState, SwagAction> {

  public SwagChangeEvent(AppState state, SwagAction action) {
    super(state, action);
  }
}
