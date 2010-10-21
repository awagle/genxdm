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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.genxdm.typed.types.AtomBridge;
import org.genxdm.xs.components.EnumerationDefinition;
import org.genxdm.xs.enums.DerivationMethod;
import org.genxdm.xs.enums.ScopeExtent;
import org.genxdm.xs.enums.WhiteSpacePolicy;
import org.genxdm.xs.exceptions.DatatypeException;
import org.genxdm.xs.facets.SmFacet;
import org.genxdm.xs.facets.SmFacetKind;
import org.genxdm.xs.facets.SmPattern;
import org.genxdm.xs.resolve.PrefixResolver;
import org.genxdm.xs.types.SmNativeType;
import org.genxdm.xs.types.SmSequenceTypeVisitor;
import org.genxdm.xs.types.SmSimpleType;

final class FloatType<A> extends AbstractAtomType<A>
{
	private static final String LEGACY_NEGATIVE_INFINITY_LITERAL = "-Infinity";
	private static final int LEGACY_NEGATIVE_INFINITY_LITERAL_LENGTH = 9;
	private static final String LEGACY_POSITIVE_INFINITY_LITERAL = "Infinity";
	private static final int LEGACY_POSITIVE_INFINITY_LITERAL_LENGTH = 8;
	private static final String MODERN_NEGATIVE_INFINITY_LITERAL = "-INF";
	private static final int MODERN_NEGATIVE_INFINITY_LITERAL_LENGTH = 4;
	private static final String MODERN_POSITIVE_INFINITY_LITERAL = "INF";
	private static final int MODERN_POSITIVE_INFINITY_LITERAL_LENGTH = 3;
	private static final String NAN_LITERAL = "NaN";
	private static final int NAN_LITERAL_LENGTH = 3;

	public FloatType(final QName name, final SmSimpleType<A> baseType, final AtomBridge<A> atomBridge)
	{
		super(name, baseType, atomBridge);
	}

	public void accept(SmSequenceTypeVisitor<A> visitor)
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public boolean derivedFrom(String namespace, String name, Set<DerivationMethod> derivationMethods)
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public Iterable<EnumerationDefinition<A>> getEnumerations()
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public SmFacet<A> getFacetOfKind(SmFacetKind facetKind)
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public Iterable<SmFacet<A>> getFacets()
	{
		return Collections.emptyList();
	}

	public Set<DerivationMethod> getFinal()
	{
		return Collections.emptySet();
	}

	public SmNativeType getNativeType()
	{
		return SmNativeType.FLOAT;
	}

	public Iterable<SmPattern> getPatterns()
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public ScopeExtent getScopeExtent()
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}

	public WhiteSpacePolicy getWhiteSpacePolicy()
	{
		return WhiteSpacePolicy.COLLAPSE;
	}

	public boolean hasEnumerations()
	{
		return false;
	}

	public boolean hasFacetOfKind(final SmFacetKind facetKind)
	{
		return false;
	}

	public boolean hasFacets()
	{
		return false;
	}

	public boolean hasPatterns()
	{
		return false;
	}

	public boolean isAbstract()
	{
		return false;
	}

	public boolean isID()
	{
		return false;
	}

	public boolean isIDREF()
	{
		return false;
	}

	public List<A> validate(final String strval) throws DatatypeException
	{
		try
		{
			final String trimmed = trim(strval);

			final int trimmedLength = trimmed.length();

			if (trimmedLength == 0)
			{
				throw new DatatypeException(strval, this);
			}

			if (trimmedLength == NAN_LITERAL_LENGTH && trimmed.equals(NAN_LITERAL))
			{
				return atomBridge.wrapAtom(atomBridge.createFloat(Float.NaN));
			}

			if (trimmedLength == MODERN_POSITIVE_INFINITY_LITERAL_LENGTH && trimmed.equals(MODERN_POSITIVE_INFINITY_LITERAL))
			{
				return atomBridge.wrapAtom(atomBridge.createFloat(Float.POSITIVE_INFINITY));
			}

			if (trimmedLength == MODERN_NEGATIVE_INFINITY_LITERAL_LENGTH && trimmed.equals(MODERN_NEGATIVE_INFINITY_LITERAL))
			{
				return atomBridge.wrapAtom(atomBridge.createFloat(Float.NEGATIVE_INFINITY));
			}

			if (trimmedLength == LEGACY_POSITIVE_INFINITY_LITERAL_LENGTH && trimmed.equals(LEGACY_POSITIVE_INFINITY_LITERAL))
			{
				throw new DatatypeException(strval, this);
			}
			if (trimmedLength == LEGACY_NEGATIVE_INFINITY_LITERAL_LENGTH && trimmed.equals(LEGACY_NEGATIVE_INFINITY_LITERAL))
			{
				throw new DatatypeException(strval, this);
			}
			// Java Wrapper parseFloat method is more lenient than XML.
			if (trimmed.endsWith("f") || trimmed.endsWith("F"))
			{
				throw new DatatypeException(strval, this);
			}
			else
			{
				return atomBridge.wrapAtom(atomBridge.createFloat(Float.parseFloat(trimmed)));
			}
		}
		catch (final NumberFormatException e)
		{
			// The unchecked exception is generally useless noise so we drop it.
			throw new DatatypeException(strval, this);
		}
	}

	public List<A> validate(String initialValue, PrefixResolver resolver) throws DatatypeException
	{
		// TODO Auto-generated method stub
		throw new AssertionError("TODO");
	}
}