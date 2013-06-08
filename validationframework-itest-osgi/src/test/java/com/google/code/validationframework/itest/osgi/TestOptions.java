package com.google.code.validationframework.itest.osgi;

import org.ops4j.pax.exam.MavenUtils;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.options.DefaultCompositeOption;

import static org.ops4j.pax.exam.CoreOptions.*;

public class TestOptions {
    public static Option junitAndMockitoBundles() {
        return new DefaultCompositeOption(
            mavenBundle("org.mockito", "mockito-core").versionAsInProject(),
            wrappedBundle(mavenBundle("org.objenesis", "objenesis").versionAsInProject())
                    .exports("*;version=" + MavenUtils.getArtifactVersion("org.objenesis", "objenesis")),
            junitBundles()
        );
    }
}
