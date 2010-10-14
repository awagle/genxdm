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
package org.gxml.bridge.axiom.enhanced;

import java.net.URI;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMFactory;
import org.genxdm.DtdAttributeKind;
import org.genxdm.exceptions.GxmlException;
import org.genxdm.typed.io.SequenceBuilder;
import org.genxdm.typed.types.AtomBridge;
import org.gxml.bridgekit.atoms.XmlAtom;
import org.gxml.bridge.axiom.AxiomFragmentBuilder;

final class AxiomSequenceBuilder 
    implements SequenceBuilder<Object, XmlAtom>
{
	public AxiomSequenceBuilder(final AxiomSAProcessingContext pcx, final OMFactory factory, final boolean ignoreComments)
	{
		this.pcx = pcx;
		this.base = pcx.getProcessingContext().newFragmentBuilder();
	}
	
	public void attribute(final String namespaceURI, final String localName, final String prefix, final List<? extends XmlAtom> value, final QName type)
	{
        final AtomBridge<XmlAtom> atomBridge = pcx.getAtomBridge();
	    attribute(namespaceURI, localName, prefix, atomBridge.getC14NString(value), /*map from schema to dtd?*/ DtdAttributeKind.CDATA);
	}
	
	public void attribute(final String namespaceURI, final String localName, final String prefix, final String value, DtdAttributeKind type) throws GxmlException
	{
	    base.attribute(namespaceURI, localName, prefix, value, type); 
	}
	
	public void close()
	{
	    base.close();
	}
	
	public void comment(final String value)
	{
	    base.comment(value);
	}
	
	public void endDocument()
	{
		base.endDocument();
	}

	public void endElement()
	{
	    base.endElement();
	}

	public void flush()
	{
	    base.flush();
	}

	public List<Object> getNodes()
	{
	    return base.getNodes();
	}
	
	public Object getNode()
	{
	    return base.getNode();
	}

	public void namespace(final String prefix, final String namespaceURI)
	{
	    base.namespace(prefix, namespaceURI);
	}

	public void processingInstruction(final String target, final String data)
	{
	    base.processingInstruction(target, data);
	}

	public void reset()
	{
		base.reset();
	}

	public void startDocument(final URI documentURI, final String docTypeDecl)
	{
	    base.startDocument(documentURI, docTypeDecl);
	}

	public void startElement(final String namespaceURI, final String localName, final String prefix)
    {
	    base.startElement(namespaceURI, localName, prefix);
    }

	public void startElement(final String namespaceURI, final String localName, final String prefix, final QName type)
	{
	    startElement(namespaceURI, localName, prefix);
	}

	public void text(final List<? extends XmlAtom> value)
	{
	    base.text(pcx.getAtomBridge().getC14NString(value));
	}

	public void text(final String value) throws GxmlException
	{
	    base.text(value);
	}

	public void atom(XmlAtom atom)
        throws GxmlException
    {
        // TODO Auto-generated method stub
        
    }

    public void endSequence()
        throws GxmlException
    {
        // TODO Auto-generated method stub
        
    }

    public void startSequence()
        throws GxmlException
    {
        // TODO Auto-generated method stub
        
    }

    private final AxiomSAProcessingContext pcx;
	private final AxiomFragmentBuilder base;
}