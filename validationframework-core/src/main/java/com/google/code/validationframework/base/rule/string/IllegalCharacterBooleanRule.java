/*
 * Copyright (c) 2013, Patrick Moawad
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.google.code.validationframework.base.rule.string;

import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.base.rule.NegateBooleanRule;

import java.util.regex.Pattern;

/**
 * Rule checking that the input does not contain any characters from  a specified string.<br>This is a simple
 * alternative to the {@link StringRegexRule}.
 *
 * @see StringRegexRule
 */
public class IllegalCharacterBooleanRule extends AbstractStringBooleanRule {

    /**
     * Simple implementation of the rule that will actually do the check.
     */
    private Rule<String, Boolean> delegatePattern;

    /**
     * String containing all illegal characters.
     */
    private String illegalCharacters;

    /**
     * String containing all illegal characters separated by a white space.
     */
    private String illegalCharactersSeparatedBySpaces;

    /**
     * Constructor specifying the list of illegal characters in a string.
     *
     * @param illegalCharacters String containing all illegal characters.
     */
    public IllegalCharacterBooleanRule(final String illegalCharacters) {
        setIllegalCharacters(illegalCharacters);
    }

    /**
     * Gets the list of illegal characters in a string.
     *
     * @return String containing all illegal characters.
     */
    public String getIllegalCharacters() {
        return illegalCharacters;
    }

    /**
     * Sets the list of illegal characters in a string.
     *
     * @param illegalCharacters String containing all illegal characters.
     */
    public void setIllegalCharacters(final String illegalCharacters) {
        // Create a simple sub-rule taking care of the check
        this.illegalCharacters = illegalCharacters;
        delegatePattern = new NegateBooleanRule<String>(new StringRegexRule("[" + Pattern.quote(illegalCharacters) +
                "]"));

        // Create a nicely space-separated list of illegal characters
        final int illegalCharacterCount = illegalCharacters.length();
        final StringBuilder illegalCharactersSeparatedBySpacesStringBuilder = new StringBuilder(illegalCharacterCount
                * 2);
        for (int i = 0; i < illegalCharacterCount; i++) {
            illegalCharactersSeparatedBySpacesStringBuilder.append(illegalCharacters.toCharArray()[i]);
            if (i < (illegalCharacterCount - 1)) {
                illegalCharactersSeparatedBySpacesStringBuilder.append(' ');
            }
        }
        illegalCharactersSeparatedBySpaces = illegalCharactersSeparatedBySpacesStringBuilder.toString();
    }

    /**
     * Gets the list of illegal characters in a string, separated by a white space.<br>This may be useful for error
     * message purposes.
     *
     * @return List of illegal characters in a string, separated by a white space.
     */
    public String getIllegalCharactersSeparatedBySpaces() {
        return illegalCharactersSeparatedBySpaces;
    }

    /**
     * @see AbstractStringBooleanRule#validate(Object)
     */
    @Override
    public Boolean validate(final String text) {
        final String trimmedText = trimIfNeeded(text);
        return delegatePattern.validate(trimmedText);
    }
}
