package ru.tuanviet.javabox;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class SuperStream<T> {
    private  Iterable<T> iterable;

    FunctionImpl function;

    List<SuperStream> streamsList;

    public SuperStream(Iterable<T> iterable) {
        if (iterable == null) {
            throw new IllegalArgumentException("Iterable is null");
        } else {
            this.iterable = iterable;
            streamsList = new ArrayList<>();
            streamsList.add(this);
        }
    }

    private SuperStream(FunctionImpl function, List<SuperStream> ss) {

        this.function = function;
        this.streamsList = ss;
    }

    public <R> SuperStream<R> map(Function<? super T,? extends R> mapper) {

        SuperStream<R> newStream = new SuperStream<>(new FunctionImpl(mapper), new ArrayList<SuperStream>(streamsList));
        newStream.streamsList.add(newStream);
        return newStream;
    }

    public SuperStream<T> filter(Predicate<? super T> predicate) {

        SuperStream<T> newStream = new SuperStream<>(new FunctionImpl(predicate), new ArrayList<SuperStream>(streamsList));
        newStream.streamsList.add(newStream);
        return newStream;
    }

    public List<T> toList() {
        List<T> listResult = (List<T>) elementHandler();
        cleaner();

        return listResult;
    }

    public Set<T> toSet() {
        Set<T> setResult = new HashSet<>();
        setResult.addAll((List<T>)elementHandler());
        cleaner();

        return setResult;
    }

    public String join() {
        StringBuilder resultString = new StringBuilder();
        List<Object> strings = elementHandler();
        strings.forEach(resultString::append);
        return resultString.toString();
    }

    public String join(String divider) {
        if (divider == null)
            throw new IllegalArgumentException("Divider can't be null");
        StringBuilder resultString = new StringBuilder();
        List<Object> strings = elementHandler();
        Iterator<Object> iter = strings.iterator();
        if (iter.hasNext()) {
            resultString.append(iter.next());
        }
        while (iter.hasNext()) {
            resultString.append(divider);
            resultString.append(iter.next());
        }
        return resultString.toString();
    }

    private List<Object> elementHandler() {
        List<Object> resultList = new ArrayList<>();
        boolean shouldAdd;
        Iterable iterable = streamsList.get(0).iterable;

        for (Object elem : iterable) {
            shouldAdd = true;
            for (int i = 1; i < streamsList.size(); i++) {
                if (streamsList.get(i).function.shouldAccept(elem)) {
                    elem = streamsList.get(i).function.getElement();
                }
                else {
                    shouldAdd = false;
                    break;
                }
            }
            if (shouldAdd) {
                resultList.add(elem);
            }
        }
        return resultList;
    }

    private void cleaner() {
        SuperStream newS = streamsList.get(0);
        streamsList.clear();
        streamsList.add(newS);
    }
}


class FunctionImpl<T, R> {
    private Function<? super T,? extends R> function;
    private Predicate<? super T>predicate;
    private Object element;

    public FunctionImpl(Function<? super T,? extends R> func) {
        this.function = func;
    }

    public FunctionImpl(Predicate<? super T> predicate) {
        this.predicate = predicate;
    }

    public boolean shouldAccept(T elem) {
        if (function != null) {
            element = function.apply(elem);
            return true;
        }
        if (predicate != null) {
            element = elem;
            return predicate.test(elem);
        }
        return false;
    }

    public Object getElement() {
        return element;
    }


}
