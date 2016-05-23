package com.grayherring.shiala.simplesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.grayherring.shiala.flux.event.ChangeEventListener;
import com.grayherring.shiala.flux.view.FluxView;
import com.grayherring.shiala.simplesample.flux.AppStore;
import com.grayherring.shiala.simplesample.flux.SimpleActionCreater;
import com.grayherring.shiala.simplesample.flux.SimpleChangeEvent;

public class MainActivity extends AppCompatActivity  implements FluxView ,ChangeEventListener<SimpleChangeEvent>{

  EditText result;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(com.grayherring.shiala.simplesample.R.layout.activity_main);
    AppStore.getInstance().register(this);
    result = (EditText) findViewById(com.grayherring.shiala.simplesample.R.id.result);
  }

  public void onClick(View v) {
    Button button = (Button) v;
    if ((v.getId() == com.grayherring.shiala.simplesample.R.id.equal)) {
      SimpleActionCreater.evaluate(getLocalClassName());
    }else if ((v.getId() == com.grayherring.shiala.simplesample.R.id.undo)) {
      SimpleActionCreater.undo(getLocalClassName());
    } else if ((v.getId() == com.grayherring.shiala.simplesample.R.id.clear)) {
      SimpleActionCreater.clear(getLocalClassName());
    } else {
      SimpleActionCreater.sendDataValue(button.getText().toString(),getLocalClassName());

    }
  }

  @Override protected void onResume() {
    super.onResume();
    restoreViewFromState();
  }

  @Override public void restoreViewFromState() {
     result.setText(AppStore.getInstance().getState().displayResult());
  }

  @Override public void call(SimpleChangeEvent simpleChangeEvent) {
    if(!TextUtils.isEmpty(simpleChangeEvent.state.errorMessage())){
      if(simpleChangeEvent.lastAction.source.equals(getLocalClassName())) {
        Toast.makeText(this, simpleChangeEvent.state.errorMessage(), Toast.LENGTH_LONG).show();
      }
    }else{
      result.setText(simpleChangeEvent.state.displayResult());
    }

  }
}
