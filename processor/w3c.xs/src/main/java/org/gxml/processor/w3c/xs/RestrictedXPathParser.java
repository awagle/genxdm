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
package org.gxml.processor.w3c.xs;

import java.util.List;
import java.util.StringTokenizer;

import javax.xml.XMLConstants;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.names.NameSource;
import org.genxdm.typed.types.AtomBridge;
import org.genxdm.xs.components.SmComponentProvider;
import org.genxdm.xs.constraints.SmRestrictedXPath;
import org.genxdm.xs.exceptions.SmDatatypeException;
import org.genxdm.xs.exceptions.SmSimpleTypeException;
import org.genxdm.xs.resolve.SmPrefixResolver;
import org.genxdm.xs.types.SmNativeType;
import org.genxdm.xs.types.SmSimpleType;
import org.gxml.processor.w3c.xs.exception.SrcPrefixNotFoundException;


final class RestrictedXPathParser<A> implements SmRestrictedXPathParser
{
	private final SmComponentProvider<A> bootstrap;
	private final AtomBridge<A> atomBridge;
	private final NameSource nameBridge;

	public RestrictedXPathParser(final SmComponentProvider<A> bootstrap, final AtomBridge<A> atomBridge)
	{
		this.bootstrap = bootstrap;
		this.atomBridge = atomBridge;
		this.nameBridge = atomBridge.getNameBridge();
	}

	/**
	 * Parse the given xpath expression. The syntax of <a
	 * href="http://www.w3.org/TR/xmlschema-1/#c-selector-xpath">http://www.w3.org/TR/xmlschema-1/#c-selector-xpath</a>
	 * is accepted and no extensive error checks are performed.
	 * 
	 * @return the parsed model implementing {@link com.tibco.xml.schema.SmIdentityConstraint.Path}
	 */
	public SmRestrictedXPath parseXPath(final String xpath, final SmPrefixResolver prefixes) throws SmSimpleTypeException
	{
		PreCondition.assertArgumentNotNull(xpath, "xpath");
		PreCondition.assertArgumentNotNull(prefixes, "prefixes");

		if (xpath.startsWith("|"))
		{
			final SmDatatypeException dte = new SmDatatypeException(xpath, null);
			throw new SmSimpleTypeException(xpath, null, dte);
		}
		final StringTokenizer terms = new StringTokenizer(xpath, "|");
		if (terms.hasMoreTokens())
		{
			final RestrictedXPathImpl result = parseAlternate(terms.nextToken(), prefixes, nameBridge, xpath, bootstrap, atomBridge);
			RestrictedXPathImpl lastTerm = result;
			while (terms.hasMoreTokens())
			{
				lastTerm.setAlternate(parseAlternate(terms.nextToken(), prefixes, nameBridge, xpath, bootstrap, atomBridge));
				lastTerm = lastTerm.getAlternate();
			}
			return result;
		}
		else
		{
			final SmDatatypeException dte = new SmDatatypeException(xpath, null);
			throw new SmSimpleTypeException(xpath, null, dte);
		}
	}

	/**
	 * Return the local-name part of the lexical xs:QName. <br/>
	 * The input is assumed to be lexically valid.
	 * 
	 * @param qualifiedName
	 *            The lexical xs:QName.
	 */
	public static String getLocalName(final String qualifiedName)
	{
		return qualifiedName.substring(qualifiedName.indexOf(":") + 1);
	}

	/**
	 * Return the prefix part of the lexical xs:QName. If there is no colon separator, returns the empty string. <br/>
	 * The input is assumed to be lexically valid.
	 * 
	 * @param qualifiedName
	 *            The lexical xs:QName.
	 */
	public static String getPrefix(final String qualifiedName)
	{
		final int index = qualifiedName.indexOf(':');
		if (index == -1)
		{
			return XMLConstants.DEFAULT_NS_PREFIX;
		}
		else
		{
			return qualifiedName.substring(0, index);
		}
	}

	private static <A> RestrictedXPathImpl parseAlternate(final String xpath, final SmPrefixResolver prefixes, NameSource nameBridge, final String original, final SmComponentProvider<A> bootstrap, final AtomBridge<A> atomBridge) throws SmSimpleTypeException
	{
		PreCondition.assertArgumentNotNull(xpath);
		PreCondition.assertArgumentNotNull(prefixes);

		final RestrictedXPathImpl expression = new RestrictedXPathImpl();

		final String trimmed = xpath.trim();

		final Pair<String, Boolean> opening = doubleKeyword(trimmed, ".", "//", original);

		expression.m_relocatable = opening.getSecond();
		final StringTokenizer tokens;
		{
			final String remainder = opening.getFirst();
			if ((remainder.length() == 0) || remainder.startsWith("/") || remainder.contains("//"))
			{
				final SmDatatypeException dte = new SmDatatypeException(original, null);
				throw new SmSimpleTypeException(original, null, dte);
			}
			else
			{
				tokens = new StringTokenizer(remainder, "/");
			}
		}
		while (tokens.hasMoreTokens())
		{
			final String token = tokens.nextToken().trim();
			final String nodeTest;
			{
				final Pair<String, Boolean> attributeAxis = attributeAxis(token, original);

				if (attributeAxis.getSecond())
				{
					expression.m_isAttribute = true;
					nodeTest = attributeAxis.getFirst();
				}
				else
				{
					expression.m_isAttribute = false;
					nodeTest = childAxis(token, original).getLeft();
				}
			}

			if (".".equals(nodeTest))
			{
				expression.addContextNodeStep();
			}
			else
			{
				if ("*".equals(nodeTest))
				{
					expression.addNameStep(null, null);
				}
				else
				{
					final String namespace;
					final String localPart;
					final String prefix = getPrefix(nodeTest);
					if (prefix.length() == 0)
					{
						if (expression.m_isAttribute)
						{
							namespace = XMLConstants.NULL_NS_URI;
						}
						else
						{
							// namespace = getDefaultNamespaceForElementAndTypeNames();
							namespace = XMLConstants.NULL_NS_URI;
						}
						localPart = getLocalPart(nodeTest, original, bootstrap, atomBridge);
					}
					else
					{
						ensureNCName(prefix, bootstrap, atomBridge);
						localPart = getLocalPart(nodeTest, original, bootstrap, atomBridge);

						namespace = prefixes.getNamespaceURI(prefix);
						if (null == namespace)
						{
							final SrcPrefixNotFoundException cause = new SrcPrefixNotFoundException(prefix);
							final SmDatatypeException dte = new SmDatatypeException(original, null, cause);
							throw new SmSimpleTypeException(original, null, dte);
						}
					}
					expression.addNameStep(namespace, localPart);
				}
			}
		}
		return expression;
	}

	private static <A> String ensureNCName(final String initialValue, final SmComponentProvider<A> bootstrap, final AtomBridge<A> atomBridge) throws SmSimpleTypeException
	{
		final SmSimpleType<A> atomicType = bootstrap.getAtomicType(SmNativeType.NCNAME);
		try
		{
			final List<A> atoms = atomicType.validate(initialValue);
			if (atoms.size() > 0)
			{
				return atomBridge.getString(atoms.get(0));
			}
			else
			{
				return null;
			}
		}
		catch (final SmDatatypeException e)
		{
			throw new SmSimpleTypeException(initialValue, atomicType, e);
		}
	}

	private static <A> String getLocalPart(final String step, final String original, final SmComponentProvider<A> bootstrap, final AtomBridge<A> atomBridge) throws SmSimpleTypeException
	{
		final String localName = getLocalName(step);
		if ("*".equals(localName))
		{
			return localName;
		}
		else
		{
			return ensureNCName(localName, bootstrap, atomBridge);
		}
	}

	/**
	 * Parses a string starting with two (optional) keyword literals.
	 * 
	 * @param strval
	 *            The string to be parsed.
	 * @param partOne
	 *            The first keyword literal.
	 * @param partTwo
	 *            The second keyword literal.
	 * @param original
	 *            The entire original string.
	 * @return A pair consisting of what is left and whether the keyword literals were found.
	 */
	private static Pair<String, Boolean> doubleKeyword(final String strval, final String partOne, final String partTwo, final String original) throws SmSimpleTypeException
	{
		final String following;
		final Boolean found;
		if (strval.startsWith(partOne.concat(" ")))
		{
			final String remainder = strval.substring(partOne.length() + 1).trim();
			if (remainder.startsWith(partTwo))
			{
				following = remainder.substring(partTwo.length()).trim();
				found = Boolean.TRUE;
			}
			else
			{
				final SmDatatypeException dte = new SmDatatypeException(original, null);
				throw new SmSimpleTypeException(original, null, dte);
			}
		}
		else if (strval.startsWith(partOne.concat(partTwo)))
		{
			following = strval.substring(partOne.length() + partTwo.length()).trim();
			found = Boolean.TRUE;
		}
		else
		{
			following = strval;
			found = Boolean.FALSE;
		}
		return new Pair<String, Boolean>(following, found);
	}

	private static Pair<String, Boolean> attributeAxis(final String strval, final String original) throws SmSimpleTypeException
	{
		final Pair<String, Boolean> attributeAxis = doubleKeyword(strval, "attribute", "::", original);
		if (attributeAxis.getSecond())
		{
			return attributeAxis;
		}
		else
		{
			if (strval.length() > 0 && strval.charAt(0) == '@')
			{
				return new Pair<String, Boolean>(strval.substring(1).trim(), Boolean.TRUE);
			}
			else
			{
				return new Pair<String, Boolean>(strval, Boolean.FALSE);
			}
		}
	}

	private static Pair<String, Boolean> childAxis(final String strval, final String original) throws SmSimpleTypeException
	{
		return doubleKeyword(strval, "child", "::", original);
	}
}