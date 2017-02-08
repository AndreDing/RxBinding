package com.jakewharton.rxbinding2.widget;

import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import com.jakewharton.rxbinding2.InitialValueObservable;
import io.reactivex.Observer;
import io.reactivex.android.MainThreadDisposable;

final class RadioGroupCheckedChangeObservable extends InitialValueObservable<Integer> {
  private final RadioGroup view;

  RadioGroupCheckedChangeObservable(RadioGroup view) {
    this.view = view;
  }

  @Override protected void subscribeListener(Observer<? super Integer> observer) {
    Listener listener = new Listener(view, observer);
    view.setOnCheckedChangeListener(listener);
    observer.onSubscribe(listener);
  }

  @Override protected Integer getInitialValue() {
    return view.getCheckedRadioButtonId();
  }

  static final class Listener extends MainThreadDisposable implements OnCheckedChangeListener {
    private final RadioGroup view;
    private final Observer<? super Integer> observer;
    private int lastChecked = -1;

    Listener(RadioGroup view, Observer<? super Integer> observer) {
      this.view = view;
      this.observer = observer;
    }

    @Override public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
      if (!isDisposed() && checkedId != lastChecked) {
        lastChecked = checkedId;
        observer.onNext(checkedId);
      }
    }

    @Override protected void onDispose() {
      view.setOnCheckedChangeListener(null);
    }
  }
}
