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
package org.gxml.bridgekit.xs;

import javax.xml.namespace.QName;

import org.gxml.bridgekit.names.QNameAsSet;
import org.gxml.exceptions.PreCondition;
import org.gxml.xs.enums.SmNodeKind;
import org.gxml.xs.enums.SmScopeExtent;
import org.gxml.xs.types.SmAttributeNodeType;
import org.gxml.xs.types.SmPrimeChoiceType;
import org.gxml.xs.types.SmPrimeType;
import org.gxml.xs.types.SmPrimeTypeKind;
import org.gxml.xs.types.SmSequenceType;
import org.gxml.xs.types.SmSequenceTypeVisitor;

final class AttributeNodeType<A> extends AbstractPrimeExcludingNoneType<A> implements SmAttributeNodeType<A>
{
	private final QName name;
	private final SmSequenceType<A> m_type;

	public AttributeNodeType(final QName name, final SmSequenceType<A> type, final SmCache<A> cache)
	{
		this.name = PreCondition.assertArgumentNotNull(name, "name");
		if (null != type)
		{
			this.m_type = type;
		}
		else
		{
			this.m_type = cache.getSimpleUrType();
		}
	}

	public SmPrimeType<A> prime()
	{
		return this;
	}

	public void accept(final SmSequenceTypeVisitor<A> visitor)
	{
		visitor.visit(this);
	}

	public SmPrimeTypeKind getKind()
	{
		return SmPrimeTypeKind.ATTRIBUTE;
	}

	public String getLocalName()
	{
		if (null != name)
		{
			return name.getLocalPart();
		}
		else
		{
			return null;
		}
	}

	public QName getName()
	{
		return name;
	}

	public SmNodeKind getNodeKind()
	{
		return SmNodeKind.ATTRIBUTE;
	}

	public SmScopeExtent getScopeExtent()
	{
		return SmScopeExtent.Global;
	}

	public String getTargetNamespace()
	{
		if (null != name)
		{
			return name.getNamespaceURI();
		}
		else
		{
			return null;
		}
	}

	public SmSequenceType<A> getType()
	{
		return m_type;
	}

	public boolean isAnonymous()
	{
		return false;
	}

	public boolean isChoice()
	{
		return false;
	}

	public boolean isNative()
	{
		return false;
	}

	public boolean subtype(final SmPrimeType<A> rhs)
	{
		switch (rhs.getKind())
		{
			case CHOICE:
			{
				final SmPrimeChoiceType<A> choiceType = (SmPrimeChoiceType<A>)rhs;
				return subtype(choiceType.getLHS()) || subtype(choiceType.getRHS());
			}
			case ATTRIBUTE:
			{
				final SmAttributeNodeType<A> other = (SmAttributeNodeType<A>)rhs;
				return QNameAsSet.subset(name, other.getName());
			}
			default:
			{
				return false;
			}
		}
	}

	@Override
	public String toString()
	{
		return "attribute " + name + " of type " + m_type;
	}
}