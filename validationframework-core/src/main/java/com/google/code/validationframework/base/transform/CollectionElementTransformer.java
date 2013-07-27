package com.google.code.validationframework.base.transform;

import com.google.code.validationframework.api.common.Disposable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Transformer transformer each element of a collection using a wrapped transformer.
 * <p/>
 * The input of the transformer is a collection of elements of one type, and the output is a collection of elements of
 * a same type. Both input and output collections have the same size.
 * <p/>
 * Note the output collection built is not a {@link java.util.Set}. So the transformation may lead to duplicates in the
 * output collection.
 *
 * @param <I> Type of collection element to be transformed.
 * @param <O> Type of transformed collection element.
 */
public class CollectionElementTransformer<I, O> implements Transformer<Collection<I>, Collection<O>>, Disposable {

    /**
     * Transformer to be used to transform each element of the collection given in {@link #transform(Collection)}
     * method.
     */
    private final Transformer<I, O> elementTransformer;

    /**
     * Default constructor using the {@link CastTransformer} to transform each element of the collection given in {@link
     * #transform(Collection)} method.
     */
    public CollectionElementTransformer() {
        this(new CastTransformer<I, O>());
    }

    /**
     * Constructor specifying the transformer to be used to transform each element of the collection given in {@link
     * #transform(Collection)} method.
     *
     * @param elementTransformer Transformer to be used to transform each element of the collection given in {@link
     *                           #transform(Collection)} method.
     */
    public CollectionElementTransformer(Transformer<I, O> elementTransformer) {
        this.elementTransformer = elementTransformer;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @Override
    public Collection<O> transform(Collection<I> input) {
        List<O> transformedCollection = null;

        if (input != null) {
            transformedCollection = new ArrayList<O>();
            for (I element : input) {
                transformedCollection.add(elementTransformer.transform(element));
            }
        }

        return transformedCollection;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (elementTransformer instanceof Disposable) {
            ((Disposable) elementTransformer).dispose();
        }
    }
}
