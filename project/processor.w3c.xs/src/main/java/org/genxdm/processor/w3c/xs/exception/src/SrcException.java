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
package org.genxdm.processor.w3c.xs.exception.src;

import org.genxdm.processor.w3c.xs.exception.sm.SmLocationException;
import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.resolve.LocationInSchema;

@SuppressWarnings("serial")
public class SrcException extends SmLocationException
{
    /**
     * @param outcome
     *            The section reference in the specification.
     */
    public SrcException(final ValidationOutcome outcome, final LocationInSchema location)
    {
        super(outcome, "?", location);
    }

    public String getMessage()
    {
        return getOutcome().getSection();
    }
}
