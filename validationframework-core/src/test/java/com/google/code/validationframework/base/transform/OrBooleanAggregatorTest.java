package com.google.code.validationframework.base.transform;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @see OrBooleanAggregator
 */
public class OrBooleanAggregatorTest {

    @Test
    public void testNonNull() {
        Aggregator<Boolean, Boolean> transformer = new OrBooleanAggregator();

        // All true
        List<Boolean> booleans = new ArrayList<Boolean>();
        booleans.add(true);
        booleans.add(true);
        booleans.add(true);

        assertTrue(transformer.transform(booleans));

        // All false
        booleans.clear();
        booleans.add(false);
        booleans.add(false);
        booleans.add(false);

        assertFalse(transformer.transform(booleans));

        // Some true, some false
        booleans.clear();
        booleans.add(true);
        booleans.add(false);
        booleans.add(true);

        assertTrue(transformer.transform(booleans));
    }

    @Test
    public void testNullCollection() {
        Aggregator<Boolean, Boolean> transformer = new OrBooleanAggregator();
        assertTrue(transformer.transform(null));

        transformer = new OrBooleanAggregator(true, OrBooleanAggregator.DEFAULT_NULL_ELEMENT_VALUE);
        assertTrue(transformer.transform(null));

        transformer = new OrBooleanAggregator(Boolean.FALSE, OrBooleanAggregator.DEFAULT_NULL_ELEMENT_VALUE);
        assertFalse(transformer.transform(null));

        transformer = new OrBooleanAggregator(null, OrBooleanAggregator.DEFAULT_NULL_ELEMENT_VALUE);
        assertEquals(null, transformer.transform(null));
    }

    @Test
    public void testNullElement() {
        List<Boolean> booleans = new ArrayList<Boolean>();
        booleans.add(false);
        booleans.add(false);
        booleans.add(null);

        Aggregator<Boolean, Boolean> transformer = new OrBooleanAggregator();
        assertFalse(transformer.transform(booleans));

        transformer = new OrBooleanAggregator(OrBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALUE, Boolean.TRUE);
        assertTrue(transformer.transform(booleans));

        transformer = new OrBooleanAggregator(OrBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALUE, false);
        assertFalse(transformer.transform(booleans));

        transformer = new OrBooleanAggregator(OrBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALUE, null);
        assertFalse(transformer.transform(booleans));
    }
}
