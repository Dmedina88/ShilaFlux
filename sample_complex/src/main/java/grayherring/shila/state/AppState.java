package grayherring.shila.state;

import com.grayherring.shiala.flux.store.BaseState;
import grayherring.shila.flux.SwagAction;
import grayherring.shila.model.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class AppState implements BaseState {

  protected Stack<SwagAction> filterActionStack;
  protected List<Book> bookData;
  protected List<Book> filterdBookData;
  protected boolean filter = false;
  protected Book selectedBook;
  protected int selectedBookIndex = 0;
  protected String curFilter = "";

  public AppState(
      Stack<SwagAction> filterActionStack,
      List<Book> bookData, List<Book> filterdBookData, boolean filter,
      Book selectedBook, int selectedBookIndex,
      String curFilter) {
    this.filterActionStack = filterActionStack;
    this.bookData = bookData;
    this.filterdBookData = filterdBookData;
    this.filter = filter;
    this.selectedBook = selectedBook;
    this.selectedBookIndex = selectedBookIndex;
    this.curFilter = curFilter;
  }

  //public AppStateBuilder getBuilder() {
  //  return new AppState.AppStateBuilder();
  //}

  public Stack<SwagAction> getFilterActionStack() {
    return filterActionStack;
  }

  public List<Book> getBookData() {
    return bookData;
  }

  public List<Book> getFilterdBookData() {
    return filterdBookData;
  }

  public boolean isFilter() {
    return filter;
  }

  public Book getSelectedBook() {
    return selectedBook;
  }

  public int getSelectedBookIndex() {
    return selectedBookIndex;
  }

  public String getCurFilter() {
    return curFilter;
  }

  public static class AppStateBuilder {

    private Stack<SwagAction> filterActionStack = new Stack<>();
    private List<Book> bookData = new ArrayList<>();
    private List<Book> filterdBookData = new ArrayList<>();
    private boolean filter;
    private Book selectedBook;
    private int selectedBookIndex;
    private String curFilter;

    public AppState createAppState() {
      return new AppState(filterActionStack, bookData, filterdBookData, filter, selectedBook,
          selectedBookIndex, curFilter);
    }

    public Stack<SwagAction> getFilterActionStack() {
      return filterActionStack;
    }

    public AppStateBuilder setFilterActionStack(Stack<SwagAction> filterActionStack) {
      this.filterActionStack = filterActionStack;
      return this;
    }

    public List<Book> getBookData() {
      return bookData;
    }

    public AppStateBuilder setBookData(List<Book> bookData) {
     ArrayList<Book> books =  new ArrayList<>();
      books.addAll(bookData);
      this.bookData = books;

      return this;
    }

    public List<Book> getFilterdBookData() {
      return filterdBookData;
    }

    public AppStateBuilder setFilterdBookData(List<Book> filterdBookData) {
      this.filterdBookData = filterdBookData;
      return this;
    }

    public boolean isFilter() {
      return filter;
    }

    public AppStateBuilder setFilter(boolean filter) {
      this.filter = filter;
      return this;
    }

    public Book getSelectedBook() {
      return selectedBook;
    }

    public AppStateBuilder setSelectedBook(Book selectedBook) {
      this.selectedBook = selectedBook;
      return this;
    }

    public int getSelectedBookIndex() {
      return selectedBookIndex;
    }

    public void setSelectedBookIndex(int selectedBookIndex) {
      this.selectedBookIndex = selectedBookIndex;
    }

    public String getCurFilter() {
      return curFilter;
    }

    public AppStateBuilder setCurFilter(String curFilter) {
      this.curFilter = curFilter;
      return this;
    }
  }
}

