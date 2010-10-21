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
package org.genxdm.bridgekit.xs;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.enums.KeeneQuantifier;
import org.genxdm.xs.types.SmChoiceType;
import org.genxdm.xs.types.SmPrimeType;
import org.genxdm.xs.types.SmSequenceType;
import org.genxdm.xs.types.SmSequenceTypeVisitor;

final class ZChoiceType<A> implements SmChoiceType<A>
{
	public static <A> SmSequenceType<A> choice(final SmSequenceType<A> lhs, final SmSequenceType<A> rhs)
	{
		// Make an attempt to simplify, but not approximate the type expression by detecting
		// "none" type subtrees which are the identity element for choice. However we must use
		// the prime() and quantifier() approximations to detect the "none" type in a subtree.
		if (lhs.prime().isNone() && lhs.quantifier().isExactlyOne())
		{
			return rhs;
		}
		else if (rhs.prime().isNone() && rhs.quantifier().isExactlyOne())
		{
			return lhs;
		}
		else if (lhs.prime().isNone() && lhs.quantifier().isOptional())
		{
			return ZMultiplyType.multiply(rhs, KeeneQuantifier.OPTIONAL);
		}
		else if (rhs.prime().isNone() && rhs.quantifier().isOptional())
		{
			return ZMultiplyType.multiply(lhs, KeeneQuantifier.OPTIONAL);
		}
		else if (SmSupportImpl.subtype(lhs, rhs))
		{
			return rhs;
		}
		else if (SmSupportImpl.subtype(rhs, lhs))
		{
			return lhs;
		}
		else
		{
			return new ZChoiceType<A>(lhs, rhs);
		}
	}

	private final SmSequenceType<A> m_lhs;

	private final SmSequenceType<A> m_rhs;

	private ZChoiceType(final SmSequenceType<A> lhs, final SmSequenceType<A> rhs)
	{
		m_lhs = PreCondition.assertArgumentNotNull(lhs);
		m_rhs = PreCondition.assertArgumentNotNull(rhs);
	}

	public void accept(final SmSequenceTypeVisitor<A> visitor)
	{
		visitor.visit(this);
	}

	public SmSequenceType<A> getLHS()
	{
		return m_lhs;
	}

	public SmSequenceType<A> getRHS()
	{
		return m_rhs;
	}

	public boolean isChoice()
	{
		return true;
	}

	public SmPrimeType<A> prime()
	{
		return ZPrimeChoiceType.choice(m_lhs.prime(), m_rhs.prime());
	}

	public KeeneQuantifier quantifier()
	{
		return m_lhs.quantifier().choice(m_rhs.quantifier());
	}

	@Override
	public String toString()
	{
		return "(" + m_lhs + " | " + m_rhs + ")";
	}
}
