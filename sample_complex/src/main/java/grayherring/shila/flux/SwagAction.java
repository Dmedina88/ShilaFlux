package grayherring.shila.flux;

import com.grayherring.shiala.flux.actions.BaseAction;

public class SwagAction extends BaseAction<Payload, String> {
  public SwagAction(String name, String s, Payload payload) {
    super(name, s, payload);
    payload = new Payload();
  }

  public SwagAction(String name) {
    super(name);
    payload = new Payload();
  }
}
