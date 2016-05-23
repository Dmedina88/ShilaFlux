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
import grayherring.shila.model.Book;
import grayherring.shila.util.Util;

public class UpdateBookActivity extends UploadActivity implements SwagChangeEventListener {

  SwagActionCreator actionCenter = SwagActionCreator.getInstance();
  private Book book;
  private int position;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null) {
      restoreViewFromState();
    } else {
      book = AppStore.getInstance().getState().getSelectedBook();
      position = AppStore.getInstance().getState().getSelectedBookIndex();
    }
    AppStore.getInstance().register(this);
  }

  @OnClick(R.id.submit_button)
  public void submit(View view) {
    if (Util.checkFieldsEmpty(titleEditText, authorEditText, publisherEditText,
        categoriesEditText)) {
      showMissingDataDialog();
    } else {
      //  progressDialog.setMessage(getString(R.string.updating));
      //  progressDialog.show();
      actionCenter.update(position, newBook(book),sourceId());
    }
  }

  @Override
  protected void onDestroy() {
    AppStore.getInstance().unregister(this);
    super.onDestroy();
  }

  @Override
  public void call(final SwagChangeEvent changeEvent) {
    SwagAction action = changeEvent.lastAction;
    if (action.name.equals(SwagActionCreator.UPDATE_BOOK)) {
      finish();
      return;
    }
    super.call(changeEvent);
  }

  @Override public void restoreViewFromState() {
    book = AppStore.getInstance().getState().getSelectedBook();
    position = AppStore.getInstance().getState().getSelectedBookIndex();
    authorEditText.setText(book.getAuthor());
    categoriesEditText.setText(book.getCategories());
    publisherEditText.setText(book.getPublisher());
    titleEditText.setText(book.getTitle());
  }
}
