package com.example.wang.retrofittest;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * 作者   wang
 * 时间   2019/3/11 0011 09:39
 * 文件   RetrofitTest
 * 描述
 */
public class MyRxJavaTest {

    private static final String TAG = "test";

    public static void main(String[] args) {
        step();
    }


    /**
     * 创建被观察者
     * 当 Observable 被订阅时，OnSubscribe 的 call() 方法会自动被调用，即事件序列就会依照设定依次被触发
     */
    private static Observable step1() {
        //第一种
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                // 通过 ObservableEmitter类对象产生事件并通知观察者
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });
        //第二种,实际调用的是第三种
        Observable observable2 = Observable.just(1, 2, 3);

        //第三种
        Integer[] words = {1, 2, 3};
        Observable observable3 = Observable.fromArray(words);

        //第四种
        Observable observable4 = Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1;
            }
        });

        //第五种
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        Observable observable5 = Observable.fromIterable(list);

        //第六种,直到被观察者被订阅后才会创建被观察者。
        Observable<Integer> observable6 = Observable.defer(new Callable<ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> call() throws Exception {
                //返回被观察者，被订阅时才调用
                return Observable.just(1, 2, 3);
            }
        });

        //第七种,当到指定时间后就会发送一个 0L 的值给观察者。
        Observable observable7 = Observable.timer(2, TimeUnit.SECONDS);

        //第八种,每隔一段时间就会发送一个事件，这个事件是从0开始，不断增1的数字。
        Observable observable8 = Observable.interval(2, TimeUnit.SECONDS);
        //可以指定发送事件的开始值和数量，延迟监听时间和间隔。
        observable8 = Observable.intervalRange(1, 3, 2, 1, TimeUnit.SECONDS);

        //第九种,同时发送一定范围的事件序列。和intervalRange不同，没有延迟和间隔
        Observable observable9 = Observable.range(1, 3);
        observable9 = Observable.rangeLong(1, 3);

        //第十种,empty():直接发送 onComplete() 事件 ; never():不发送任何事件 ; error():发送 onError() 事件
        Observable observable10 = Observable.empty();
        observable10 = Observable.never();
        observable10 = Observable.error(new NullPointerException("test"));

        return observable1;
    }

    /**
     * 转换操作符
     */
    private static Observable step2(Observable<Integer> observable) {
        Observable<String> result;

        //第一种：map() 可以将被观察者发送的数据类型转变成其他的类型
        result = observable.map(new Function <Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                return "233" + integer;
            }

        });

        //第二种：flatMap() 可以将事件序列中的元素进行整合加工，返回一个新的被观察者。
        // flatMap() 其实与 map() 类似，但是 flatMap() 返回的是一个 Observerable。
        result = observable.flatMap(new Function<Integer, ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> apply(Integer integer) throws Exception {//将每个发送数据又变为新的被观察者
                List<Integer> list = new ArrayList<>();
                list.add(2);
                list.add(3);
                list.add(3);
                list.add(integer);
                if (integer.equals(2)) {
                    return Observable.fromIterable(list).delay(2, TimeUnit.SECONDS);    //延迟2秒发动信息
                }
                return Observable.fromIterable(list);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integers) throws Exception {
                return Observable.just("233" + integers);
            }
        });

        //第三种： concatMap()  concatMap() 转发出来的事件是有序的，而 flatMap() 是无序的。
        //只需把flatMap()替换掉就行了。观察者收到的消息，会是有序的，延迟消息不会最后发送。

        //第四种：buffer()  从需要发送的事件当中获取一定数量的事件，并将这些事件放到缓冲区当中一并发出。
        //例如下面：1，变为输出1,2; 3,变为输出3,4;跳过了2 skip不能小于1  将每个int对象，变为List对象发送
        Observable<List<Integer>> observable1 = Observable.just(1,2,3,4).buffer(2,2);

        //第五种：groupBy() 将发送的数据进行分组，每个分组都会返回一个被观察者。
        //下面这个例子：分组为：1,3；2,4
        Observable<GroupedObservable<Integer, Integer>> observable2 = observable.
                groupBy(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        return integer % 2;
                    }
                });

        //第六种： scan()   将数据以一定的逻辑聚合起来。
        //下面这个例子，顺序发送3,6,10；也就是累加结果
        Observable observable3 = observable.scan(new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer integer, Integer integer2) throws Exception {
                return integer + integer2;
            }
        });

        //第七种：window()  发送指定数量的事件时，就将这些事件分为一组。
        // window 中的 count 的参数就是代表指定的数量，例如将 count 指定为2，那么每发2个数据就会将这2个数据分成一组。
        Observable<Observable<Integer>> observable4 = observable.window(2);

        return result;
    }

    /**
     * 组合操作符
     */
    private static Observable step3(Observable observable) {
        Observable result = null;
        //第一种:concat()  可以将多个观察者组合在一起，然后按照之前发送顺序发送事件。需要注意的是，concat() 最多只可以发送4个事件。
        //concatArray() 与 concat() 作用一样，不过 concatArray() 可以发送多于 4 个被观察者。
        // concatArrayDelayError()  onError() 事件延迟到所有被观察者都发送完事件后再执行
        result = Observable.concat(observable, Observable.just(5, 6), Observable.just(7, 8));

        //第二种：merge()   这个方法月 concat() 作用基本一样，知识 concat() 是串行发送事件，而 merge() 并行发送事件。
        //mergeArray() 与 merge() 的作用是一样的，只是它可以发送4个以上的被观察者。
        //mergeArrayDelayError() 事件延迟到所有被观察者都发送完事件后再执行
        result = Observable.merge(Observable.interval(1, TimeUnit.SECONDS).map(new Function<Long, String>() {
            @Override
            public String apply(Long aLong) throws Exception {
                return "a" + aLong;
            }
        }), Observable.interval(1, TimeUnit.SECONDS).map(new Function<Long, String>() {
                    @Override
                    public String apply(Long aLong) throws Exception {
                        return "b" + aLong;
                    }
                })
        );

        //第三种：zip() 会将多个被观察者合并，根据各个被观察者发送事件的顺序一个个结合起来，
        // 最终发送的事件数量会与源 Observable 中最少事件的数量一样。

        //第四种：combineLatest() & combineLatestDelayError()
        // combineLatest() 的作用与 zip() 类似，但是 combineLatest() 发送事件的序列是与发送的时间线有关的，最近时间的两个数据结合

        //第五种：reduce()  scan() 每处理一次数据就会将事件发送给观察者，而 reduce() 会将所有数据聚合在一起才会发送事件给观察者。

        //第六种：startWith() & startWithArray()    在发送事件之前追加事件，startWith() 追加一个事件，startWithArray() 可以追加多个事件。追加的事件会先发出。

        //第七种：count()   返回被观察者发送事件的数量。
        Single<Long> observable1 = Observable.just(1, 2, 3, 4).count();

        return result;
    }

    /**
     * 功能操作符
     */
    private static Observable stpe4(Observable<Integer> observable) {
        //第一种：delay()   延迟一段事件发送事件。
        observable = observable.delay(1, TimeUnit.SECONDS);

        //第二种：doOnEach()    Observable 每发送一件事件之前都会先回调这个方法。
        observable = observable.doOnEach(new Consumer<Notification>() {
            @Override
            public void accept(Notification notification) throws Exception {

            }
        });

        //第三种：doOnNext()    Observable 每发送 onNext() 之前都会先回调这个方法。

        //第四种： doAfterNext()    Observable 每发送 onNext() 之后都会回调这个方法。

        //第五种：doOnComplete()    Observable 每发送 onComplete() 之前都会回调这个方法。

        //第六种：doOnError()   Observable 每发送 onError() 之前都会回调这个方法。

        //第七种：doOnSubscribe()   Observable 每发送 onSubscribe() 之前都会回调这个方法。

        //第八种：doOnDispose()     当调用 Disposable 的 dispose() 之后回调该方法。

        //第九种：doOnLifecycle()   在回调 onSubscribe 之前回调该方法的第一个参数的回调方法，可以使用该回调方法决定是否取消订阅。
        observable = observable.doOnLifecycle(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                //在回调 onSubscribe 之前回调该方法的第一个参数的回调方法
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //非accept里取消订阅，这里会被回调
            }
        });

        //第十种：doOnTerminate() & doAfterTerminate()
        //doOnTerminate 是在 onError 或者 onComplete 发送之前回调，而 doAfterTerminate 则是 onError 或者 onComplete 发送之后回调。

        //第十一种：doFinally()  在所有事件发送完毕之后回调该方法。
        //如果取消订阅之后 doAfterTerminate() 就不会被回调，而 doFinally() 无论怎么样都会被回调，且都会在事件序列的最后。

        //12：onErrorReturn()    当接受到一个 onError() 事件之后回调，返回的值会回调 onNext() 方法，并正常结束该事件序列。 相当于异常捕获

        //13:onErrorResumeNext()    当接收到 onError() 事件时，返回一个新的 Observable，并正常结束事件序列

        //14:onExceptionResumeNext()    与 onErrorResumeNext() 作用基本一致，但是这个方法只能捕捉 Exception。不能捕捉 Error 事件。

        //15:retry()    如果出现错误事件，则会重新发送所有事件序列。times 是代表重新发的次数。

        //16:retryUntil()   出现错误事件之后，可以通过此方法判断是否继续发送事件。

        //17:retryWhen()    当被观察者接收到异常或者错误事件时会回调该方法，这个方法会返回一个新的被观察者。
        //如果返回的被观察者发送 Error 事件则之前的被观察者不会继续发送事件，如果发送正常事件则之前的被观察者会继续不断重试发送事件。
        Observable observable1 = observable.retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        if ("java.lang.Exception: 404".equals(throwable.toString())) {
                            return Observable.just("可以忽略的异常");
                        } else {
                            return Observable.error(new Throwable("终止"));
                        }
                    }
                });
            }
        });

        //18: repeat()  重复发送被观察者的事件，times 为发送次数。

        //19:repeatWhen()   这个方法可以会返回一个新的被观察者设定一定逻辑来决定是否重复发送事件。
        //这里分三种情况，如果新的被观察者返回 onComplete 或者 onError 事件，则旧的被观察者不会继续发送事件。如果被观察者返回其他事件，则会重复发送事件。

        //20:subscribeOn()  指定被观察者的线程，要注意的时，如果多次调用此方法，只有第一次有效。

        //21:observeOn()    指定观察者的线程，每指定一次就会生效一次。
        observable = observable.observeOn(Schedulers.newThread());

        return observable;
    }

    /**
     * 过滤操作符
     */
    private static Observable<Integer> step5(Observable<Integer> observable) {
        //1:filter()  通过一定逻辑来过滤被观察者发送的事件，如果返回 true 则会发送事件，否则不会发送。
        observable = observable.filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer < 2;
            }
        });

        //2:ofType()    可以过滤不符合该类型事件
        observable = observable.ofType(Integer.class);

        //3:skip()  跳过正序某些事件，count 代表跳过事件的数量
        //skipLast() 作用也是跳过某些事件，不过它是用来跳过正序的后面的事件

        //4:distinct()  过滤事件序列中的重复事件。

        //5:distinctUntilChanged()  过滤掉连续重复的事件

        //6:take()  控制观察者接收的事件的数量。
        //takeLast() 的作用就是控制观察者只能接受事件序列的后面几件事情
        observable = observable.take(3);

        //7:debounce()  如果两件事件发送的时间间隔小于设定的时间间隔则前一件事件就不会发送给观察者。

        //8:firstElement() && lastElement()     firstElement() 取事件序列的第一个元素，lastElement() 取事件序列的最后一个元素。

        //9:elementAt() & elementAtOrError()    elementAt() 可以指定取出事件序列中事件，
        // 但是输入的 index 超出事件序列的总数的话就不会出现任何结果。这种情况下，你想发出异常信息的话就用 elementAtOrError() 。

        return observable;
    }

    /**
     * 条件操作符
     */
    private static Observable<Integer> step6(Observable<Integer> observable) {
        //1:all()   判断事件序列是否全部满足某个事件，如果都满足则返回 true，反之则返回 false。
        Single<Boolean> observable1 = observable.all(new Predicate<Integer>() {
            @Override
            public boolean test(Integer integer) throws Exception {
                return integer < 5;
            }
        });

        //2:takeWhile() 可以设置条件，当某个数据满足条件时就会发送该数据，反之则不发送。

        //3:skipWhile() 可以设置条件，当某个数据满足条件时不发送该数据，反之则发送。

        //4:takeUntil() 可以设置条件，当事件满足此条件时，下一次的事件就不会被发送了。

        //5：skipUntil() 当 skipUntil() 中的 Observable 发送事件了，原来的 Observable 才会发送事件给观察者。
        observable = observable.skipUntil(Observable.just(5, 6));       //5, 6不会再发送给observable的观察者。消息没有合并

        //6:sequenceEqual()     判断两个 Observable 发送的事件是否相同。
        observable1 = Observable.sequenceEqual(observable, Observable.just(1,2,3));

        //7:contains()      判断事件序列中是否含有某个元素，如果有则返回 true，如果没有则返回 false。
        observable1 = observable.contains(3);

        //8:isEmpty()   判断事件序列是否为空。

        //9:amb()   amb() 要传入一个 Observable 集合，但是只会发送最先发送事件的 Observable 中的事件，其余 Observable 将会被丢弃。

        //10:defaultIfEmpty()   如果观察者只发送一个 onComplete() 事件，则可以利用这个方法发送一个值。
observable.subscribe(new Observer<Integer>() {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Integer integer) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
});
        return observable;
    }

    private static Observer step7() {
        //第一种
        Observer<Integer> observer1 = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        //第二种

        return observer1;
    }

    private static void step() {
        Observable.fromCallable(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "================accept " + integer);
            }
        });
    }
}
