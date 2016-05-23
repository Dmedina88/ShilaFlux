package com.grayherring.shiala.flux.event;

import rx.functions.Action1;

/**
 * Created by davidmedina on 5/9/16.
 */
public interface ChangeEventListener<T extends ChangeEvent> extends Action1<T> {
}
