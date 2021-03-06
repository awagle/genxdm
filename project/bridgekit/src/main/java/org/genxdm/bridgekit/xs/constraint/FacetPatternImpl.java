/**
 * Copyright (c) 2009-2010 TIBCO Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.genxdm.bridgekit.xs.constraint;

import org.genxdm.bridgekit.xs.ForeignAttributesImpl;
import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.exceptions.PatternException;
import org.genxdm.xs.facets.Pattern;
import org.genxdm.xs.facets.RegExPattern;

public final class FacetPatternImpl
    extends ForeignAttributesImpl
    implements Pattern
{
    private final RegExPattern pattern;
    private final String regex;

    public FacetPatternImpl(final RegExPattern pattern, final String regex)
    {
        this.pattern = PreCondition.assertArgumentNotNull(pattern, "pattern");
        this.regex = PreCondition.assertArgumentNotNull(regex, "regex");
    }

    public void validate(final String input) throws PatternException
    {
        PreCondition.assertArgumentNotNull(input, "input");

        if (!pattern.matches(input))
        {
            throw new PatternException(this, input);
        }
    }

    public String getValue()
    {
        return regex;
    }
}
