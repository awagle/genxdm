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
package org.genxdm.processor.w3c.xs.validation.impl;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.genxdm.typed.types.AtomBridge;
import org.genxdm.xs.components.ElementDefinition;
import org.genxdm.xs.constraints.SmIdentityConstraint;
import org.genxdm.xs.exceptions.AbortException;
import org.genxdm.xs.exceptions.SchemaExceptionHandler;
import org.genxdm.xs.types.SmSimpleType;

/**
 * This manager has overall resposibility for checking identity constraints. <br/>
 * It is tightly coupled to the stack of validation items maintained by the core validator, but plays a key role by
 * keeping track of the number of active scopes in order to optimize performance.
 */
final class IdentityConstraintManager<A>
{
	/**
	 * Identity Constraint Validation is very CPU and memory intensive so we want to avoid paying the cost if it is not
	 * actually used. This member variable keeps track of the *total* number of scopes in progress.
	 */
	private int m_totalScopes = 0;

	public void reset()
	{
		m_totalScopes = 0;
	}

	public void startElement(final ModelPSVI<A> elementPSVI, final ValidationItem<A> elementItem, final SchemaExceptionHandler errors) throws AbortException
	{
		// Notify existing scopes of the current event (start of element)
		if (m_totalScopes > 0)
		{
			for (final ValidationItem<A> currentItem : getAncestorOrSelf(elementItem))
			{
				for (final IdentityScope<A> scope : currentItem.m_identityScopes)
				{
					scope.startElement(elementPSVI.getName(), elementItem.getElementIndex(), elementPSVI.getType(), elementItem);
				}
			}
		}

		// This code looks at the element decl to see if it specifies any
		// identity constraints, and if so creates an IdentityScope object for
		// it...
		PreCondition.assertArgumentNotNull(elementPSVI, "elementPSVI");
		final ElementDefinition<A> declaration = elementPSVI.getDeclaration();
		if (null != declaration && declaration.hasIdentityConstraints())
		{
			// Handle xs:unique, xs:key and xs:keyref in order so that
			// xs:keyref scopes can be fixed up to xs:key scopes.
			for (final SmIdentityConstraint<A> constraint : declaration.getIdentityConstraints())
			{
				switch (constraint.getCategory())
				{
					case Key:
					case Unique:
					{
						final IdentityScopeKey<A> scope = new IdentityScopeKey<A>(elementItem.getElementIndex(), constraint, errors, elementItem.getLocation());

						elementItem.m_keyScopes.put(constraint.getName(), scope);
						elementItem.m_refScopes.put(constraint.getName(), new ArrayList<IdentityScopeRef<A>>());
						elementItem.m_identityScopes.add(scope);
						m_totalScopes++;
					}
					break;
					case KeyRef:
					{
						// Ignore
					}
					break;
					default:
					{
						throw new RuntimeException(constraint.getCategory().name());
					}
				}
			}
			for (final SmIdentityConstraint<A> constraint : declaration.getIdentityConstraints())
			{
				switch (constraint.getCategory())
				{
					case Key:
					{
						// Ignore
					}
					break;
					case Unique:
					{
						// Ignore
					}
					break;
					case KeyRef:
					{
						final QName keyName = constraint.getKeyConstraint().getName();
						final ValidationItem<A> referencedItem = ValidationItem.findItemWithKeyConstraint(elementItem, keyName);
						final IdentityScopeKey<A> keyScope = ValidationItem.getKeyIdentityScope(referencedItem, keyName);
						final IdentityScopeRef<A> scope = new IdentityScopeRef<A>(elementItem.getElementIndex(), keyScope, constraint, errors, elementItem.getLocation());
						referencedItem.m_refScopes.get(keyName).add(scope);
						elementItem.m_identityScopes.add(scope);
						m_totalScopes++;
					}
					break;
					default:
					{
						throw new RuntimeException(constraint.getCategory().name());
					}
				}
			}
		}
	}

	public void attribute(final List<? extends A> actualValue, final SmSimpleType<A> attributeType, final ValidationItem<A> elementItem, final QName attributeName, final int attributeIndex, final AtomBridge<A> atomBridge) throws AbortException
	{
		if (m_totalScopes > 0)
		{
			for (final ValidationItem<A> currentItem : getAncestorOrSelf(elementItem))
			{
				for (final IdentityScope<A> scope : currentItem.m_identityScopes)
				{
					scope.attribute(attributeName, actualValue, attributeIndex, attributeType, elementItem, atomBridge);
				}
			}
		}
	}

	public void text(final List<? extends A> actualValue, final SmSimpleType<A> actualType, final ValidationItem<A> elementItem, final int textIndex, final AtomBridge<A> atomBridge) throws AbortException
	{
		if (m_totalScopes > 0)
		{
			for (final ValidationItem<A> currentItem : getAncestorOrSelf(elementItem))
			{
				for (final IdentityScope<A> scope : currentItem.m_identityScopes)
				{
					scope.text(actualValue, actualType, textIndex, elementItem, atomBridge);
				}
			}
		}
	}

	public void endElement(final ModelPSVI<A> elementPSVI, final ValidationItem<A> elementItem) throws AbortException
	{
		if (m_totalScopes > 0)
		{
			for (final QName key : elementItem.m_keyScopes.keySet())
			{
				for (final IdentityScopeRef<A> scope : elementItem.m_refScopes.get(key))
				{
					scope.reportUnmatchedRefs();
				}
			}
			m_totalScopes = m_totalScopes - elementItem.m_identityScopes.size();
			elementItem.m_identityScopes.clear();
			elementItem.m_keyScopes.clear();
			elementItem.m_refScopes.clear();

			final QName elementName = elementPSVI.getName();
			final int elementIndex = elementItem.getElementIndex();
			for (final ValidationItem<A> currentItem : getAncestorOrSelf(elementItem))
			{
				for (final IdentityScope<A> scope : currentItem.m_identityScopes)
				{
					scope.endElement(elementName, elementIndex, elementItem);
				}
			}
		}
	}

	/**
	 * Get an iterable of the links from the origin link to the document link.
	 */
	private Iterable<ValidationItem<A>> getAncestorOrSelf(final ValidationItem<A> origin)
	{
		return new ValidationItemIterable<A>(origin);
	}
}