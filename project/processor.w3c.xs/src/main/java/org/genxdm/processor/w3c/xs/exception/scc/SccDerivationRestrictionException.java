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
package org.genxdm.processor.w3c.xs.exception.scc;

import javax.xml.namespace.QName;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.exceptions.ComponentConstraintException;
import org.genxdm.xs.exceptions.SchemaException;


@SuppressWarnings("serial")
public abstract class SccDerivationRestrictionException extends ComponentConstraintException
{
    private final QName m_complexTypeName;

    public static final String PART_BASE_TYPE_MUST_BE_COMPLEX_TYPE_AND_ALLOWED_BY_FINAL = "1";
    public static final String PART_CONTENT_TYPE = "5";

    public SccDerivationRestrictionException(final String partNumber, final QName complexTypeName)
    {
        super(ValidationOutcome.SCC_Derivation_Valid_Extension, partNumber);
        m_complexTypeName = PreCondition.assertArgumentNotNull(complexTypeName, "complexTypeName");
    }

    public SccDerivationRestrictionException(final String partNumber, final QName complexTypeName, final SchemaException cause)
    {
        super(ValidationOutcome.SCC_Derivation_Valid_Extension, partNumber, PreCondition.assertArgumentNotNull(cause, "cause"));
        m_complexTypeName = PreCondition.assertArgumentNotNull(complexTypeName, "complexTypeName");
    }

    public QName getComplexTypeName()
    {
        return m_complexTypeName;
    }
}
