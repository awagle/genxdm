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
package org.genxdm.processor.w3c.xs.validation.api;

import java.io.IOException;
import java.util.List;

import javax.xml.namespace.QName;

import org.genxdm.xs.types.SimpleType;
import org.genxdm.xs.types.Type;


public interface VxOutputHandler<A>
{
	void attribute(QName name, String value) throws IOException;

	void attribute(QName name, List<? extends A> value, SimpleType type) throws IOException;

	void endDocument() throws IOException;

	void endElement() throws IOException;

	void namespace(String prefix, String namespaceURI) throws IOException;

	void startDocument() throws IOException;

	void startElement(QName name, Type type) throws IOException;

	void text(List<? extends A> value) throws IOException;

	void text(String value) throws IOException;
}
