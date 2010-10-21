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

import org.genxdm.NodeKind;
import org.genxdm.xs.types.SmPrimeChoiceType;
import org.genxdm.xs.types.SmPrimeType;
import org.genxdm.xs.types.SmPrimeTypeKind;
import org.genxdm.xs.types.SmProcessingInstructionNodeType;
import org.genxdm.xs.types.SmSequenceTypeVisitor;

final class ProcessingInstructionNodeType<A> extends AbstractLeafNodeType<A> implements SmProcessingInstructionNodeType<A>
{
	private final String m_name;

	public ProcessingInstructionNodeType(final String name, final SmCache<A> cache)
	{
		super(NodeKind.PROCESSING_INSTRUCTION, cache);
		m_name = name;
	}

	public void accept(final SmSequenceTypeVisitor<A> visitor)
	{
		visitor.visit(this);
	}

	public SmPrimeTypeKind getKind()
	{
		return SmPrimeTypeKind.PROCESSING_INSTRUCTION;
	}

	public String getName()
	{
		return m_name;
	}

	public SmProcessingInstructionNodeType<A> prime()
	{
		return this;
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
			case PROCESSING_INSTRUCTION:
			{
				final SmProcessingInstructionNodeType<A> pi = (SmProcessingInstructionNodeType<A>)rhs;
				final String name = pi.getName();
				if (null == name)
				{
					// null is wildcard.
					return true;
				}
				else
				{
					if (null != m_name)
					{
						return m_name.equals(name);
					}
					else
					{
						return false;
					}
				}
			}
			default:
			{
				return false;
			}
		}
	}
}