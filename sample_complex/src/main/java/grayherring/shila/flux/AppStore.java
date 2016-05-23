package grayherring.shila.flux;

import com.grayherring.shiala.flux.event.ChangeEventListener;
import com.grayherring.shiala.flux.store.ActionStore;
import com.jakewharton.rxrelay.PublishRelay;
import grayherring.shila.model.Book;
import grayherring.shila.state.AppState;
import grayherring.shila.util.RxUtil;
import grayherring.shila.util.Util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AppStore extends ActionStore<AppState, SwagAction> {

  public static final String BOOK_DATA_KEY = "BOOK_DATA_KEY";
  public static final String BOOK_INDEX_KEY = "BOOK_INDEX_KEY";
  public static final String FILTER_KEY = "FILTER_KEY";

  static AppStore instance;
  PublishRelay<SwagChangeEvent> dataRelay;
  Map<ChangeEventListener, Subscription> subscriptionHashMap;
  boolean playback = false;
  AppState.AppStateBuilder appState = new AppState.AppStateBuilder();
  Subscription filterSubscription;

  private AppStore() {
    dataRelay = PublishRelay.create();
    subscriptionHashMap = new HashMap<>();
  }

  private AppStore(PublishRelay publishRelay) {
    playback = true;
    dataRelay = publishRelay;
    //// TODO: 5/16/16  coolcool
    dataRelay.subscribe(swagChangeEvent -> {
      appState.setCurFilter(swagChangeEvent.state.getCurFilter());
      appState.setFilter(swagChangeEvent.state.isFilter());
      appState.setSelectedBook(swagChangeEvent.state.getSelectedBook());
      appState.setSelectedBookIndex(swagChangeEvent.state.getSelectedBookIndex());
    });
    subscriptionHashMap = new HashMap<>();
  }

  public static AppStore getInstance(PublishRelay publishRelay) {
    if (instance == null) {
      instance = new AppStore(publishRelay);
    }
    return instance;
  }

  public static AppStore getInstance() {
    if (instance == null) {
      instance = new AppStore();
    }
    return instance;
  }

  // TODO: 4/28/16  some more pure flux  examples it would just admit a change event and then the whole view will
  // TODO update but that seems wasteful in the android world since we cant do a DOM dif
  @Override
  public void call(SwagAction action) {

    Timber.d(action.getName());
    // if(!playback)
    switch (action.name) {
      case SwagActionCreator.DATA_CHANGE_NEW: {
        appState.setFilter(false);
        appState.getBookData().clear();
        appState.getBookData().addAll(action.payload.books);
        dataRelay.call(new SwagChangeEvent(appState.createAppState(), action));
        break;
      }
      case SwagActionCreator.DATA_FAIL: {

        break;
      }
      case SwagActionCreator.DELETE_ALL: {
        appState.getBookData().clear();
        dataRelay.call(new SwagChangeEvent(appState.createAppState(), action));
        break;
      }
      case SwagActionCreator.ADD_BOOK: {
        appState.getBookData().add(action.payload.getBook());
        dataRelay.call(new SwagChangeEvent(appState.createAppState(), action));
        // actionStack.push(action);
        break;
      }
      case SwagActionCreator.DELETE_BOOK: {
        Book book = action.payload.getBook();
        // int index = (int) action.payload.get(BOOK_INDEX_KEY);
        appState.getBookData().remove(appState.getSelectedBook());
        appState.getBookData().remove(book);
        dataRelay.call(new SwagChangeEvent(appState.createAppState(), action));
        break;
      }
      case SwagActionCreator.FILTER: {
        appState.setCurFilter(action.payload.filter);
        appState.getFilterActionStack().push(action);
        filter(action.payload.filter);
        //   dataRelay.call(new SwagChangeEvent(appState.createAppState(), action));
        break;
      }
      case SwagActionCreator.FILTER_UNDO: {
        if (appState.getFilterActionStack().peek() != null) {
          filter(appState.getFilterActionStack().pop().payload.filter);
        }
        break;
      }
      case SwagActionCreator.UPDATE_BOOK: {
        appState.getBookData().set(action.payload.pos,
            action.payload.getBook());
        dataRelay.call(new SwagChangeEvent(appState.createAppState(), action));
        break;
      }
      case SwagActionCreator.SELECT_BOOK: {
        appState.setSelectedBook(action.payload.getBook());
        appState.setSelectedBookIndex(action.payload.pos);
        dataRelay.call(new SwagChangeEvent(appState.createAppState(), action));
        break;
      }
      case SwagActionCreator.START_ACTIVITY: {
        dataRelay.call(new SwagChangeEvent(appState.createAppState(), action));
        break;
      }
      case SwagActionCreator.BACK_PRESS: {
        dataRelay.call(new SwagChangeEvent(appState.createAppState(), action));
        break;
      }
    }
  }

  public void filter(final String filterOn) {
    appState.setFilter(true);
    appState.setCurFilter(filterOn);
    RxUtil.unSubscribeIfNeeded(filterSubscription);
    filterSubscription = Observable.from(appState.getBookData())
        .filter(book -> {
          if (Util.isEmpty(filterOn)) {
            return true;
          } else {
            return book.getTitle().toLowerCase().contains(filterOn.toLowerCase())
                || book.getAuthor().toLowerCase().contains(filterOn.toLowerCase());
          }
        }).distinct().subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread()).toList().subscribe(books -> {
          SwagAction dataChangeAction = new SwagAction(SwagActionCreator.DATA_CHANGE_NEW);
          dataChangeAction.payload.books = books;
          dataChangeAction.payload.filter = filterOn;
          appState.getFilterdBookData().addAll(books);
          dataRelay.call(new SwagChangeEvent(appState.createAppState(), dataChangeAction));
        });
  }

  public boolean canUndo() {
    if (appState.getFilterActionStack().size() > 0) {
      if (appState.getFilterActionStack().peek().payload.filter
          .equals(appState.getCurFilter())) {
        appState.getFilterActionStack().pop();
        return canUndo();
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  public List<Book> getCachedData() {
    if (appState.isFilter()) {
      return appState.createAppState().getFilterdBookData();
    }
    return appState.createAppState().getBookData();
  }

  @Override
  public AppState getState() {
    return appState.createAppState();
  }

  @Override
  public void register(ChangeEventListener changeEventListener) {
    subscriptionHashMap.put(changeEventListener, dataRelay.subscribe(changeEventListener));
  }

  @Override
  public void unregister(ChangeEventListener changeEventListener) {
    RxUtil.unSubscribeIfNeeded(subscriptionHashMap.get(changeEventListener));
  }
}
