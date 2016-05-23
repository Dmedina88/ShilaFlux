package grayherring.shila.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import grayherring.shila.R;
import grayherring.shila.flux.AppStore;
import grayherring.shila.flux.SwagAction;
import grayherring.shila.flux.SwagActionCreator;
import grayherring.shila.flux.SwagChangeEvent;
import grayherring.shila.flux.SwagChangeEventListener;
import grayherring.shila.model.Book;

public class DetailsActivity extends BaseActivity implements SwagChangeEventListener {

  @BindView(R.id.book_title)
  public TextView bookTextView;
  @BindView(R.id.book_authors)
  public TextView authorsTextView;
  @BindView(R.id.book_categories)
  public TextView categoriesTextView;
  @BindView(R.id.book_publisher)
  public TextView publisherTextView;
  @BindView(R.id.book_checkout_info)
  public TextView checkOutInfoTextView;
  @BindView(R.id.LastOut)
  public TextView lastOutTextView;
  @BindView(R.id.root)
  public View root;
  ShareActionProvider shareActionProvider;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  SwagActionCreator actionCenter = SwagActionCreator.getInstance();

  private Intent shareIntent;
  private int position;
  private Book book;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    restoreViewFromState();
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    AppStore.getInstance().register(this);
  }

  @Override
  protected void onDestroy() {
    AppStore.getInstance().unregister(this);
    super.onDestroy();
  }

  @Override
  protected void onResume() {
    shareIntent = new Intent();
    shareIntent.setAction(Intent.ACTION_SEND);
    shareIntent.setType("text/plain");
    shareIntent.putExtra(Intent.EXTRA_TEXT, book.getTitle());
    super.onResume();
  }

  @OnClick(R.id.button_checkout)
  public void checkOut(View view) {
    actionCenter.checkOut(position, book, sourceId());
  }

  @SuppressLint("SetTextI18n")
  private void updateTextViews() {
    Resources resources = this.getResources();
    if (TextUtils.isEmpty(book.getTitle())) {
      bookTextView.setVisibility(View.GONE);
    } else {
      bookTextView.setVisibility(View.VISIBLE);
      bookTextView.setText(book.getTitle());
    }

    if (TextUtils.isEmpty(book.getAuthor())) {
      authorsTextView.setVisibility(View.GONE);
    } else {
      authorsTextView.setVisibility(View.VISIBLE);
      authorsTextView.setText(book.getAuthor());
    }
    if (TextUtils.isEmpty(book.getCategories())) {
      categoriesTextView.setVisibility(View.GONE);
    } else {
      categoriesTextView.setVisibility(View.VISIBLE);
      categoriesTextView.setText(
          resources.getString(R.string.categories) + " " + book.getCategories());
    }
    if (TextUtils.isEmpty(book.getPublisher())) {
      publisherTextView.setVisibility(View.GONE);
    } else {
      publisherTextView.setVisibility(View.VISIBLE);
      publisherTextView.setText(
          resources.getString(R.string.publisher) + " " + book.getPublisher());
    }

    if (TextUtils.isEmpty(book.getLastCheckedOutBy()) || TextUtils.isEmpty(
        book.getLastCheckedOut())) {
      checkOutInfoTextView.setVisibility(View.GONE);
      lastOutTextView.setVisibility(View.GONE);
    } else {
      checkOutInfoTextView.setVisibility(View.VISIBLE);
      lastOutTextView.setVisibility(View.VISIBLE);
      checkOutInfoTextView.setText(
          book.getLastCheckedOutBy() + " " + "@" + " " + book.getLastCheckedOut());
    }
  }

  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();
    switch (id) {

      case R.id.action_delete:
        deleteBook();
        break;
      case R.id.action_update:
        Intent i = new Intent(this, UpdateBookActivity.class);
        //  i.putExtra(Constants.INDEX, position);
        // i.putExtra(Constants.BOOK_KEY, book);
        actionCenter.startActivity(i, this.sourceId());
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_detail, menu);
    MenuItem item = menu.findItem(R.id.action_share);
    shareActionProvider = new ShareActionProvider(this);
    MenuItemCompat.setActionProvider(item, shareActionProvider);
    shareActionProvider.setShareIntent(shareIntent);
    return true;
  }

  private void deleteBook() {
    progressDialog.setMessage(getString(R.string.deleting));
    progressDialog.show();
    actionCenter.remove(position, book, sourceId());
  }

  @Override
  public void call(final SwagChangeEvent changeEvent) {
    SwagAction action = changeEvent.lastAction;
    switch (action.name) {
      case SwagActionCreator.UPDATE_BOOK:
        book = action.payload.books.get(0);
        // getIntent().putExtra(Constants.BOOK_KEY, book);
        updateTextViews();
        break;
      case SwagActionCreator.DELETE_BOOK:
        if (action.source.equals(DetailsActivity.class.getSimpleName())) {
          progressDialog.dismiss();
          finish();
        }
      case SwagActionCreator.DATA_FAIL:
        if (action.source.equals(DetailsActivity.class.getSimpleName())) {
          progressDialog.dismiss();
        }
        break;
      default:
        super.call(changeEvent);
        break;
    }
  }

  @Override public void restoreViewFromState() {
    position = AppStore.getInstance().getState().getSelectedBookIndex();
    book = AppStore.getInstance().getState().getSelectedBook();
    updateTextViews();
  }
}
