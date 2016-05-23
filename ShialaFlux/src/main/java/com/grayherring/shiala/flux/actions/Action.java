package com.grayherring.shiala.flux.actions;

import java.util.HashMap;

public class Action extends BaseAction<HashMap<String, Object>, String> {

  public Action(String name, String source, HashMap map) {
    super(name, source, map);
  }

  public Action(String name) {
    super(name);
    payload = new HashMap<>();
    source = "";
  }
}
