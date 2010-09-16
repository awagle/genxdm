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
package org.gxml.processor.w3c.xs.validation.api;

import javax.xml.namespace.QName;

import org.gxml.xs.components.SmAttribute;
import org.gxml.xs.components.SmElement;
import org.gxml.xs.types.SmType;

public interface VxMetaBridge<A>
{
	SmAttribute<A> getAttributeDeclaration(QName attributeName);

	SmElement<A> getElementDeclaration(QName elementName);

	SmType<A> getTypeDefinition(QName typeName);
}