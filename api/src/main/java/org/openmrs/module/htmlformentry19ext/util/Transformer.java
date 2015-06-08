package org.openmrs.module.htmlformentry19ext.util;

import java.util.Collection;

public interface Transformer<T,V> {
    Collection<V>  transform(Collection<T> collection,Predicate<T> predicate);
}
