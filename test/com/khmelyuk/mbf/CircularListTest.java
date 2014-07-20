package com.khmelyuk.mbf;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CircularListTest {

    @Test
    public void resetHead() throws Exception {
        CircularList<String> list = new CircularList<>(new String[5], String::valueOf);
        assertThat(list.getHead(), is("0"));
        assertThat(list.getTail(), is("4"));

        list.resetHead();
        assertThat(list.getHead(), is("1"));
        assertThat(list.getTail(), is("0"));

        list.resetHead();
        assertThat(list.getHead(), is("2"));
        assertThat(list.getTail(), is("1"));

        list.resetHead();
        assertThat(list.getHead(), is("3"));
        assertThat(list.getTail(), is("2"));

        list.resetHead();
        assertThat(list.getHead(), is("4"));
        assertThat(list.getTail(), is("3"));

        list.resetHead();
        assertThat(list.getHead(), is("0"));
        assertThat(list.getTail(), is("4"));

        list.resetHead();
        assertThat(list.getHead(), is("1"));
        assertThat(list.getTail(), is("0"));
    }
}