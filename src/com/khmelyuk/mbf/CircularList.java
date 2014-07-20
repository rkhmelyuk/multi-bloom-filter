package com.khmelyuk.mbf;

import java.util.StringJoiner;
import java.util.function.Function;

/** Represents a looped list, where the next value after a tail element is a head element. */
public class CircularList<T> {

    private final T[] elems;
    private final Function<Integer, T> creator;

    private int head;

    public CircularList(T[] elems, Function<Integer, T> creator) {
        this.elems = elems;
        this.creator = creator;

        for (int i = 0; i < elems.length; i++) {
            this.elems[i] = creator.apply(i);
        }
    }

    /** Updates the head with new element, and moves head forward. */
    public void resetHead() {
        // TODO - lock multiple updates..
        elems[head] = this.creator.apply(head);
        head = head != elems.length - 1 ? head + 1 : 0;
    }

    /** Finds the element that satisfies the test condition */
    public T find(Function<T, Boolean> testFn) {
        // TODO - binary search?
        for (int i = head; i < elems.length; i++) {
            if (testFn.apply(elems[i])) {
                return elems[i];
            }
        }
        if (tail() < head) {
            for (int i = 0; i <= tail(); i++) {
                if (testFn.apply(elems[i])) {
                    return elems[i];
                }
            }
        }
        return null;
    }

    public T getHead() { return elems[head];}

    public T getTail() { return elems[tail()];}

    private int tail() { return head == 0 ? elems.length - 1 : head - 1; }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < elems.length; i++) {
            T elem = elems[i];
            boolean head = i == this.head;
            boolean tail = i == this.tail();
            joiner.add((head ? "H" : tail ? "T" : "") + String.valueOf(elem));
        }
        return joiner.toString();
    }

}
