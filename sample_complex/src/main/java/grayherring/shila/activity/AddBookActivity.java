package grayherring.shila.activity;

import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;
import grayherring.shila.R;
import grayherring.shila.flux.AppStore;
import grayherring.shila.flux.SwagAction;
import grayherring.shila.flux.SwagActionCreator;
import grayherring.shila.flux.SwagChangeEvent;
import grayherring.shila.flux.SwagChangeEventListener;
import grayherring.shila.util.Util;

/**
 * Created by David on 12/5/2015.
 */
public class AddBookActivity extends UploadActivity implements SwagChangeEventListener {

  SwagActionCreator swagActionCreator = SwagActionCreator.getInstance();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AppStore.getInstance().register(this);
  }

  @OnClick(R.id.submit_button)
  public void submit(View view) {

    if (Util.checkFieldsEmpty(titleEditText, authorEditText, publisherEditText,
        categoriesEditText)) {
      showMissingDataDialog();
    } else {
      progressDialog.setMessage(getString(R.string.adding));
      progressDialog.show();
      swagActionCreator.add(newBook(null),sourceId());
    }
  }

  @Override protected void onDestroy() {
    AppStore.getInstance().unregister(this);
    super.onDestroy();
  }

  @Override
  public void call(SwagChangeEvent swagChangeEvent) {
    SwagAction action = swagChangeEvent.lastAction;
    if (action.name.equals(SwagActionCreator.ADD_BOOK)) {
      progressDialog.hide();
      finish();
    }
  }

  @Override public void restoreViewFromState() {

  }
}
