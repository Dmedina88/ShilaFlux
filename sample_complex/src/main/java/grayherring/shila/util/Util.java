package grayherring.shila.util;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by David on 12/7/2015.
 */
public class Util {

  public static boolean checkFieldsEmpty(EditText... editTexts) {
    for (EditText editText : editTexts) {
      if (TextUtils.isEmpty(editText.getText())) {
        return true;
      }
    }
    return false;
  }

  public static boolean checkFieldFull(EditText... editTexts) {
    for (EditText editText : editTexts) {
      if (!TextUtils.isEmpty(editText.getText())) {
        return true;
      }
    }
    return false;
  }

  public static boolean isEmpty(@Nullable CharSequence str) {
    if (str == null || str.length() == 0) { return true; } else { return false; }
  }
}
