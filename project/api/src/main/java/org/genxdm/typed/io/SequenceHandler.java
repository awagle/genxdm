/*
 * Copyright (c) 2009-2011 TIBCO Software Inc.
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
package org.genxdm.typed.io;

import java.util.List;

import javax.xml.namespace.QName;

import org.genxdm.exceptions.GenXDMException;
import org.genxdm.io.ContentHandler;

/**
 * <p>
 * Event handler for serializing an XQuery Data Model as a stream.
 * </p>
 */
public interface SequenceHandler<A> extends ContentHandler
{
    /**
     * Receive notification of an attribute.
     * 
     * @param namespaceURI
     *            The namespace-uri part of the attribute name.
     * @param localName
     *            The local-name part of the attribute name.
     * @param prefix
     *            The prefix part of the attribute name.
     * @param data
     *            The value of the attribute.
     * @param type
     *            The type annotation for the attribute supplied by validation. 
     *            May be <code>null</code> if not validated.
     */
    void attribute(String namespaceURI, String localName, String prefix, List<? extends A> data, QName type) throws GenXDMException;

    /**
     * Receive notification of the beginning of an element.
     *
     * A callee will invoke this method at the beginning of every element in the 
     * XML document; there will be a corresponding endElement core for every 
     * startElement core (even when the element is empty). All of the element's 
     * content will be reported, in order, before the
     * corresponding endElement core.
     * 
     * @param namespaceURI
     *            The namespace-uri part of the element name.
     * @param localName
     *            The local-name part of the element name.
     * @param prefix
     *            The prefix part of the element name.
     * @param type
     *            The type annotation of the element provided by validation. May be <code>null</code> if not validated.
     */
    void startElement(String namespaceURI, String localName, String prefix, QName type) throws GenXDMException;

    /** Receive notification of all or part of a text node's content.
     * 
     * This method may be called repeatedly, but implementations must insure
     * that two adjacent text nodes are never created.  That is, implementations
     * must accumulate the data from multiple adjacent calls of this method, and
     * create a single text node.
     *
     * @param data the content, as a list of atoms
     */
    void text(List<? extends A> data) throws GenXDMException;

}
