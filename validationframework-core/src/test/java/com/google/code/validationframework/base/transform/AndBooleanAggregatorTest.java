package com.google.code.validationframework.base.transform;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @see AndBooleanAggregator
 */
public class AndBooleanAggregatorTest {

    @Test
    public void testNonNull() {
        Aggregator<Boolean, Boolean> transformer = new AndBooleanAggregator();

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

        assertFalse(transformer.transform(booleans));
    }

    @Test
    public void testNullCollection() {
        Aggregator<Boolean, Boolean> transformer = new AndBooleanAggregator();
        assertTrue(transformer.transform(null));

        transformer = new AndBooleanAggregator(true, AndBooleanAggregator.DEFAULT_NULL_ELEMENT_VALUE);
        assertTrue(transformer.transform(null));

        transformer = new AndBooleanAggregator(Boolean.FALSE, AndBooleanAggregator.DEFAULT_NULL_ELEMENT_VALUE);
        assertFalse(transformer.transform(null));

        transformer = new AndBooleanAggregator(null, AndBooleanAggregator.DEFAULT_NULL_ELEMENT_VALUE);
        assertEquals(null, transformer.transform(null));
    }

    @Test
    public void testNullElement() {
        List<Boolean> booleans = new ArrayList<Boolean>();
        booleans.add(true);
        booleans.add(true);
        booleans.add(null);

        Aggregator<Boolean, Boolean> transformer = new AndBooleanAggregator();
        assertFalse(transformer.transform(booleans));

        transformer = new AndBooleanAggregator(AndBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALUE, Boolean.TRUE);
        assertTrue(transformer.transform(booleans));

        transformer = new AndBooleanAggregator(AndBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALUE, false);
        assertFalse(transformer.transform(booleans));

        transformer = new OrBooleanAggregator(OrBooleanAggregator.DEFAULT_EMPTY_COLLECTION_VALUE, null);
        assertTrue(transformer.transform(booleans));
    }
}
