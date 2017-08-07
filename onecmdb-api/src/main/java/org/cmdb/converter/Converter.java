package org.cmdb.converter;

import java.util.List;

/**
 * Created by X on 2017/4/17.
 * DTO与Entiry类型转换
 */
public interface Converter<A, B> {

    A doForward(B b);

    void forwardAfter(final A a);

    B doBackward(A a);

    void backwardAfter(final B b);

    List<A> doForwardList(List<B> list);

    List<B> doBackwardList(List<A> list);
}
