package com.ozj.baby.mvp.model.rx;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * Created by Roger ou on 2016/4/14.
 * <p/>
 * 利用Rxjava实现Eventbus
 */
public class RxBus {
    //Subject是一个可以是订阅者又不是被订阅者的存在
    private final SerializedSubject mSubject;

    public RxBus() {
        mSubject = new SerializedSubject<>(PublishSubject.create());
    }

//    public static RxBus getDefaultInstance() {
//        RxBus rxBus = defaultInstance;
//        if (rxBus == null) {
//            synchronized (RxBus.class) {
//                rxBus = defaultInstance;
//                if (rxBus == null) {
//                    rxBus = new RxBus();
//                    defaultInstance = rxBus;
//                    
//                }
//            }
//        }
//
//        return rxBus;
//    }

    @SuppressWarnings("unchecked")
    public void post(Object o) {
        mSubject.onNext(o);
    }

    @SuppressWarnings("unchecked")
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mSubject.ofType(eventType);

    }

}
