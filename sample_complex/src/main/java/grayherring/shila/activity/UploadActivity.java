package grayherring.shila.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import grayherring.shila.R;
import grayherring.shila.model.Book;
import grayherring.shila.util.Constants;
import grayherring.shila.util.Util;
import java.util.HashMap;

/**
 * Created by David on 12/5/2015.
 */
public abstract class UploadActivity extends BaseActivity {

  @BindView(R.id.book_title_textEdit)
  public EditText titleEditText;
  @BindView(R.id.author_edittext)
  public EditText authorEditText;
  @BindView(R.id.publisher_edittext)
  public EditText publisherEditText;
  @BindView(R.id.categories_edittext)
  public EditText categoriesEditText;
  @BindView(R.id.root)
  View view;
  @BindView(R.id.toolbar)
  Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_book);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.upload_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case android.R.id.home:
        if (Util.checkFieldFull(titleEditText, authorEditText, publisherEditText,
            categoriesEditText)) {
          showUnsavedDataDialog();
        } else {
          NavUtils.navigateUpFromSameTask(this);
        }
        break;
      case R.id.action_done:
        this.onFinish();
        break;
    }

    return true;
  }

  HashMap<String, String> getParams() {
    HashMap<String, String> params = new HashMap<>();
    params.put(Constants.AUTHOR_FIELD, authorEditText.getText().toString());
    params.put(Constants.CATEGORIES_FIELD, categoriesEditText.getText().toString());
    params.put(Constants.PUBLISHER_FIELD, publisherEditText.getText().toString());
    params.put(Constants.TITLE_FIELD, titleEditText.getText().toString());
    return params;
  }

  Book newBook(Book oldBook) {
    Book book = new Book();
    book.setAuthor(authorEditText.getText().toString());
    book.setCategories(categoriesEditText.getText().toString());
    book.setPublisher(publisherEditText.getText().toString());
    book.setTitle(titleEditText.getText().toString());
    if(oldBook!=null) {
      book.setId(oldBook.getId());
      book.setLastCheckedOut(oldBook.getLastCheckedOut());
      book.setLastCheckedOutBy(oldBook.getLastCheckedOutBy());
    }
    return book;
  }

  public void onFinish() {
    if (Util.checkFieldFull(titleEditText, authorEditText, publisherEditText, categoriesEditText)) {
      showUnsavedDataDialog();
    } else {
      finish();
    }
  }

  protected void showUnsavedDataDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
    builder.setTitle(getString(R.string.unsaved));
    builder.setMessage("Are you sure you want to leave with unsaved changes.");
    builder.setPositiveButton("OK", (dialog, which) -> {
      finish();
    });
    builder.setNegativeButton("Cancel", null);
    builder.show();
  }

  protected void showMissingDataDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialog);
    builder.setTitle(getString(R.string.missing_data));
    builder.setMessage(getString(R.string.missing_data_dialog));
    builder.setNegativeButton("Ok", null);
    builder.show();
  }
}