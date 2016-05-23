package grayherring.shila.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import com.grayherring.shiala.flux.view.FluxView;
import grayherring.shila.R;
import grayherring.shila.flux.SwagAction;
import grayherring.shila.flux.SwagActionCreator;
import grayherring.shila.flux.SwagChangeEvent;
import grayherring.shila.flux.SwagChangeEventListener;

public abstract class BaseActivity extends AppCompatActivity
    implements SwagChangeEventListener, FluxView {

  protected ProgressDialog progressDialog;
  SwagActionCreator swagActionCreator = SwagActionCreator.getInstance();


  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    progressDialog = new ProgressDialog(this);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    progressDialog.setMessage("Loading");
    // restoreViewFromState();
    // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)      //
  }

  public void networkFailMessage(View view) {
    Snackbar.make(view, getString(R.string.network_error), Snackbar.LENGTH_LONG)
        .setAction("SwagAction", null).show();
  }

  public String sourceId() {
    return this.getClass().getSimpleName();
  }

  @Override public void call(SwagChangeEvent swagChangeEvent) {
    SwagAction action = swagChangeEvent.lastAction;
    if (action.name.equals(SwagActionCreator.START_ACTIVITY) && action.source.equals(
        this.sourceId())) {
      startActivity(action.payload.intent);
    } else if (action.name.equals(SwagActionCreator.BACK_PRESS) && action.source.equals(
        this.sourceId())) {
      super.onBackPressed();
    }
  }

  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      //// TODO: 5/12/16  should have its own action to record
      case android.R.id.home:
        swagActionCreator.backPress(sourceId());
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override public void onBackPressed() {
    swagActionCreator.backPress(sourceId());
    super.onBackPressed();
  }
}
