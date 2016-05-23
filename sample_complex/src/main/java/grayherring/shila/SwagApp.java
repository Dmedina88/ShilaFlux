package grayherring.shila;

import android.app.Application;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.grayherring.shiala.flux.actions.ActionCreator;
import com.grayherring.shiala.flux.dispatch.DefaultDispatcher;
import com.grayherring.shiala.flux.util.GsonActionCapturer;
import com.grayherring.shiala.flux.util.GsonEventCapturer;
import com.grayherring.shiala.flux.util.RxUtil;
import com.jakewharton.rxrelay.PublishRelay;
import grayherring.shila.flux.AppStore;
import grayherring.shila.flux.SwagAction;
import grayherring.shila.flux.SwagChangeEvent;
import grayherring.shila.model.Book;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by David on 4/23/2016.
 */
public class SwagApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Timber.plant(new Timber.DebugTree());
    Realm.setDefaultConfiguration(new RealmConfiguration.Builder(getApplicationContext()).build());
    Gson gson = new Gson();
    //not sure i need this any more..
    try {
       gson = new GsonBuilder()
          .setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes f) {
              return f.getDeclaringClass().equals(RealmObject.class);
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
              return false;
            }
          })
          .registerTypeAdapter(Class.forName("io.realm.BookRealmProxy"),
              (JsonSerializer<Book>) (src, typeOfSrc, context) -> {
                final JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("author", src.getAuthor());
                jsonObject.addProperty("category", src.getCategories());
                jsonObject.add("id", context.serialize(src.getId()));
                jsonObject.add("lastCheckedOut", context.serialize(src.getLastCheckedOut()));
                jsonObject.add("lastCheckedOutBy", context.serialize(src.getLastCheckedOutBy()));
                jsonObject.add("publisher", context.serialize(src.getPublisher()));
              //  jsonObject.add("lastCheckedOut", context.serialize(src.()));
                return jsonObject;
              })
          .create();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    GsonActionCapturer gsonActionCapturer;

    if (false) {
      gsonActionCapturer = new GsonActionCapturer(gson, SwagAction.class, getApplicationContext());
      DefaultDispatcher<SwagAction> defaultDispatcher = new DefaultDispatcher();
      Observable<SwagAction> zippedObservable =
          RxUtil.timedObservable(1, TimeUnit.SECONDS,
              Observable.from(gsonActionCapturer.getItems()))
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(AndroidSchedulers.mainThread())
              .doOnNext(o -> defaultDispatcher.sendAction((SwagAction) o))
              .doOnError(e -> Timber.d(e.toString()));
      ActionCreator.init(defaultDispatcher);
      defaultDispatcher.registerStore(AppStore.getInstance());
      //AppStore.getInstance(publishRelay);
      zippedObservable.subscribe();
    } else {
      gsonActionCapturer = new GsonActionCapturer(gson, SwagAction.class, getApplicationContext());
      gsonActionCapturer.clear();
      ActionCreator.init(new DefaultDispatcher<SwagAction>());
      ActionCreator.registerStore(gsonActionCapturer);
      ActionCreator.registerStore(AppStore.getInstance());
    }
  }
}
