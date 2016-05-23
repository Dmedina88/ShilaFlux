package grayherring.shila.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import grayherring.shila.R;
import grayherring.shila.adapter.BookAdapter;
import grayherring.shila.flux.AppStore;
import grayherring.shila.flux.SwagAction;
import grayherring.shila.flux.SwagActionCreator;
import grayherring.shila.flux.SwagChangeEvent;
import grayherring.shila.flux.SwagChangeEventListener;
import grayherring.shila.model.Book;
import java.util.List;
import timber.log.Timber;
import ui.DividerItemDecoration;

public class MainActivity extends BaseActivity
    implements SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener,
    SwagChangeEventListener {

  @BindView(R.id.home_recycler)
  RecyclerView recyclerView;
  @BindView(R.id.toolbar)
  Toolbar toolbar;
  @BindView(R.id.swiperefresh)
  SwipeRefreshLayout swipeRefreshLayout;
  MenuItem searchMenuItem;
  private BookAdapter bookAdapter;
  SwagActionCreator actionCenter = SwagActionCreator.getInstance();
  private AppStore store = AppStore.getInstance();
  private SearchView searchView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);
    store.register(this);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    recyclerView.addItemDecoration(
        new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    bookAdapter = new BookAdapter(null, this);
    recyclerView.setAdapter(bookAdapter);

    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setProgressViewOffset(false, 100, 100);

    if (store.getCachedData() != null && store.getCachedData().size() > 0) {
      newData(store.getCachedData());
      Snackbar.make(recyclerView, "hi from the store", Snackbar.LENGTH_LONG)
          .setAction("SwagAction", null)
          .show();
    }

  }

  @Override
  protected void onDestroy() {
    store.unregister(this);
    super.onDestroy();
  }

  @OnClick(R.id.fab)
  public void fabClicked(View view) {
    startAddActivity();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    searchMenuItem = menu.findItem(R.id.search);
    searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
    searchView.setOnQueryTextListener(this);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();
    switch (id) {
      case R.id.action_add:
        startAddActivity();
        break;
      case R.id.action_seed:
        actionCenter.seed();
        break;
      case R.id.action_delete_all:
        actionCenter.deleteAllData();
        break;
    }
    return true;
  }

  public void startAddActivity() {
    Intent intent = new Intent(this, AddBookActivity.class);
    actionCenter.startActivity(intent, this.sourceId());
  }

  @Override
  public void onRefresh() {
    actionCenter.getAllData();
  }

  @Override
  public boolean onQueryTextSubmit(String query) {
    actionCenter.filter(query);
    searchMenuItem.collapseActionView();
    return false;
  }

  @Override
  public boolean onQueryTextChange(String newText) {
    return false;
  }

  public void newData(List<Book> books) {
    bookAdapter.changeData(books);
    progressDialog.dismiss();
  }

  @Override
  public void call(final SwagChangeEvent changeEvent) {
    SwagAction action = changeEvent.lastAction;
    Timber.d(changeEvent.toString());
    switch (action.name) {
      case SwagActionCreator.DATA_CHANGE_NEW:
        newData(action.payload.books);
        progressDialog.dismiss();
        if (swipeRefreshLayout.isRefreshing()) {
          swipeRefreshLayout.setRefreshing(false);
        }
        if (action.payload.filter != null) {
          setTitle(action.payload.filter);
        }
        break;
      case SwagActionCreator.CHANGE_EVENT_NETWORK_ERROR:
        progressDialog.dismiss();
        networkFailMessage(recyclerView);
        if (swipeRefreshLayout.isRefreshing()) {
          swipeRefreshLayout.setRefreshing(false);
        }
        break;
      case SwagActionCreator.ADD_BOOK:
        bookAdapter.addBook(action.payload.getBook());
        break;
      case SwagActionCreator.DELETE_BOOK:
        bookAdapter.deleteBook(action.payload.pos);
        break;
      case SwagActionCreator.DELETE_ALL:
        bookAdapter.deleteAllBooks();
        break;
      case SwagActionCreator.UPDATE_BOOK:
        bookAdapter.updateBook(action.payload.pos,
            action.payload.getBook());
        break;
      default:
        super.call(changeEvent);
        break;
    }
  }

  @Override
  public void onBackPressed() {
    if (store.canUndo()) {
      actionCenter.undoFilter();
    } else if (!getTitle().equals(getString(R.string.app_name))) {
      setTitle(R.string.app_name);
      if (store.getCachedData() != null && store.getCachedData().size() > 0) {
        newData(store.getCachedData());
      } else {
        actionCenter.getAllData();
      }
    } else {
      super.onBackPressed();
    }
  }

  @OnClick(R.id.get_all)
  public void getAllClicked(){
    progressDialog.show();
    actionCenter.getAllData();
  }

  @Override public void restoreViewFromState() {
    if (store.getCachedData() != null && store.getCachedData().size() > 0) {
      newData(store.getCachedData());
      Snackbar.make(recyclerView, "hi from the store", Snackbar.LENGTH_LONG)
          .setAction("SwagAction", null)
          .show();
    }
  }
}
