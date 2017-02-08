package com.jakewharton.rxbinding2;

import android.support.annotation.MainThread;
import io.reactivex.Observable;
import io.reactivex.Observer;

import static com.jakewharton.rxbinding2.internal.Preconditions.checkMainThread;

public abstract class InitialValueObservable<T> extends Observable<T> {
  @Override protected final void subscribeActual(Observer<? super T> observer) {
    if (checkMainThread(observer)) {
      subscribeListener(observer);
      observer.onNext(getInitialValue());
    }
  }

  @MainThread
  protected abstract void subscribeListener(Observer<? super T> observer);

  @MainThread
  protected abstract T getInitialValue();

  public final Observable<T> skipInitialValue() {
    return new Skipped();
  }

  private final class Skipped extends Observable<T> {
    Skipped() {
    }

    @Override protected void subscribeActual(Observer<? super T> observer) {
      if (checkMainThread(observer)) {
        subscribeListener(observer);
      }
    }
  }
}
