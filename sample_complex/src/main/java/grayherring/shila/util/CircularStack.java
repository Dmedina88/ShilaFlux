package grayherring.shila.util;

import android.annotation.SuppressLint;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class CircularStack<E> implements Collection<E> {
  private final Object[] buffer;
  private final int size;
  private int top;
  private int bottom;
  private int count;

  private CircularStack(CircularStack cs) {       // used for iterator only
    this.size = cs.buffer.length;
    this.buffer = Arrays.copyOf(cs.buffer, cs.buffer.length);
    this.top = cs.top;
    this.bottom = cs.bottom;
    this.count = cs.count;
  }

  public CircularStack(int size) {
    this.size = size;
    this.buffer = new Object[size];
    this.top = this.bottom = this.count = 0;
  }

  public void push(E o) {
    this.add(o);
  }

  public E pop() {
    this.count--;
    return (E) this.buffer[this.top = (this.top - 1 + this.size) % this.size];
  }

  @Override
  public int size() {
    return this.count;
  }

  @Override
  public boolean isEmpty() {
    return this.count == 0;
  }

  @SuppressLint("NewApi")
  @Override
  public boolean contains(Object o) {
    for (int i = this.count - 1; i >= 0; i--) {
      if (Objects.equals(this.buffer[(this.top - i + this.size) % this.size], o)) { return true; }
    }
    return false;
  }

  @Override
  public Iterator<E> iterator() {
    final CircularStack<E> me = this;

    Iterator<E> iterator = new Iterator<E>() {
      CircularStack<E> copy = new CircularStack<>(me);

      @Override
      public boolean hasNext() {
        return copy.size() > 0;
      }

      @Override
      public E next() {
        return copy.pop();
      }

      @Override
      public void remove() {
        copy.pop();
      }
    };

    return iterator;
  }

  @Override
  public Object[] toArray() {
    E[] inOrder = (E[]) new Object[this.count];
    int index = 0;
    for (E o : this)
      inOrder[index++] = o;
    return inOrder;
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return (T[]) this.toArray();
  }

  @Override
  public boolean add(E e) {
    this.buffer[this.top] = e;
    this.top = (this.top + 1) % this.size;
    if (this.count == this.size) { this.bottom = (this.bottom + 1) % this.size; } else {
      this.count++;
    }
    return true;
  }

  @Override
  public boolean remove(Object o) {
    if (!this.contains(o)) { return false; }

    for (int i = this.count - 1; i >= 0; i--) {
      if (Objects.equals(this.buffer[(this.top - i + this.size) % this.size], o)) {
        for (int index = i; index > 0; index--) {
          this.buffer[(this.top - index + this.size) % this.size] =
              this.buffer[(this.top - index + 1 + this.size) % this.size];
        }
        this.top = (this.top - 1 + this.size) % this.size;
        this.count--;
        break;
      }
    }

    return true;
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    for (Object o : c)
      if (!this.contains(o)) { return false; }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    for (E o : c)
      if (this.count < this.size) { this.add(o); } else { return false; }
    return true;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean flag = true;
    for (Object o : c) {
      flag &= this.remove(o);
    }
    return flag;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    List<E> toRemove = new ArrayList<>();

    for (E o : this)
      if (!c.contains(o)) { toRemove.add(o); }

    for (E o : toRemove)
      this.remove(o);

    return !toRemove.isEmpty();
  }

  @Override
  public void clear() {
    Arrays.fill(this.buffer, null);
    this.top = this.bottom = this.count = 0;
  }
}
