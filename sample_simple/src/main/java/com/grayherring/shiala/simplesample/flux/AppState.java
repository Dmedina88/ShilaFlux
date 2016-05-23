package com.grayherring.shiala.simplesample.flux;

import com.grayherring.shiala.flux.store.BaseState;

public class AppState implements BaseState {

  private String displayResult = "";
  private String errorMessage;

  AppState(String displayResult, String errorMessage) {
    this.displayResult = displayResult;
    this.errorMessage = errorMessage;
  }

  public String errorMessage() {
    return errorMessage;
  }

  public String displayResult() {
    return displayResult;
  }

  @Override public boolean equals(Object o) {
    if (this == o) { return true; }
    if (o == null || getClass() != o.getClass()) { return false; }

    AppState appState = (AppState) o;

    return displayResult != null ? displayResult.equals(appState.displayResult)
                                 : appState.displayResult == null;
  }

  @Override public int hashCode() {
    return displayResult != null ? displayResult.hashCode() : 0;
  }

  public static class AppStateBuilder {
    private String displayResult;
    private String errorMessage;


    public AppStateBuilder setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
      return this;
    }


    public AppStateBuilder setDisplayResult(String displayResult) {
      this.displayResult = displayResult;
      return this;
    }
    public AppState createAppState() {
      if(errorMessage == null){
        errorMessage ="";
      }
      return new AppState(displayResult,errorMessage);
    }
  }
}
