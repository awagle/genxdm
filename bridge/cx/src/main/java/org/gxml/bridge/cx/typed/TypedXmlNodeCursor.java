/**
 * Copyright (c) 2010 TIBCO Software Inc.
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
package org.gxml.bridge.cx.typed;

import javax.xml.namespace.QName;

import org.gxml.bridgekit.atoms.XmlAtom;
import org.gxml.bridge.cx.base.XmlNodeCursor;
import org.gxml.bridge.cx.tree.XmlNode;
import org.gxml.exceptions.PreCondition;
import org.gxml.typed.TypedCursor;
import org.gxml.typed.types.AtomBridge;

public class TypedXmlNodeCursor
    extends XmlNodeCursor
    implements TypedCursor<XmlNode, XmlAtom>
{
    TypedXmlNodeCursor(final TypedXmlNodeContext context, final XmlNode node)
    {
        super(node);
        this.context = PreCondition.assertNotNull(context);
    }

    public QName getAttributeTypeName(String namespaceURI, String localName)
    {
        return node.getAttributeTypeName(namespaceURI, localName);
    }

    @SuppressWarnings("unchecked")
    public Iterable<? extends XmlAtom> getAttributeValue(String namespaceURI, String localName)
    {
        return node.getAttributeValue(namespaceURI, localName);
    }

    public QName getTypeName()
    {
        return node.getTypeName();
    }

    @SuppressWarnings("unchecked")
    public Iterable<? extends XmlAtom> getValue()
    {
        AtomBridge<XmlAtom> bridge = context.getAtomBridge();
        switch (node.getNodeKind())
        {
            case TEXT :
            case ATTRIBUTE :
            {
                return node.getValue();
            }
            case NAMESPACE :
            case COMMENT :
            case PROCESSING_INSTRUCTION :
            case DOCUMENT :
            {
                return bridge.wrapAtom(bridge.createString(node.getStringValue()));
            }
            case ELEMENT :
            {
                if (node.hasChildren())
                {
                    XmlNode firstChild = node.getFirstChild();
                    if ( (firstChild == node.getLastChild()) && firstChild.isText() ) // simple content
                        return firstChild.getValue();
                }
                return bridge.wrapAtom(bridge.createString(node.getStringValue())); // complex or empty content
            }
            default :
            {
                throw new AssertionError(node.getNodeKind());
            }
        }
    }
    
    private final TypedXmlNodeContext context;
}
