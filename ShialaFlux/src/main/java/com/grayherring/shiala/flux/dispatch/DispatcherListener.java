package com.grayherring.shiala.flux.dispatch;

import com.grayherring.shiala.flux.actions.Action;
import rx.functions.Action1;

/**
 * Created by davidmedina on 4/22/16.
 */
public interface DispatcherListener extends Action1<Action> {

}
