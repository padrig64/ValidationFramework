package com.google.code.validationframework.experimental.base.property;

import java.util.Map;

public interface MapListener<K, V> {

    void entriesAdded(Map<K, V> added);

    void entriesChanged(Map<K, V> changed);

    void entriesRemoved(Map<K, V> removed);
}
