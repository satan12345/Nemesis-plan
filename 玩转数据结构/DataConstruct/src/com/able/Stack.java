package com.able;

/**
 * @author jipeng
 * @date 2019-03-21 19:43
 * @description
 */
public interface Stack<E> {

    void push(E e);

    E pop();

    E peek();

    int getSize();

    boolean isEmpty();
}
