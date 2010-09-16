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
package org.gxml.processor.w3c.xs.validation.regex.impl.restricted;

import java.util.List;

import org.gxml.processor.w3c.xs.validation.regex.api.RegExBridge;
import org.gxml.processor.w3c.xs.validation.regex.api.RegExMachine;
import org.gxml.processor.w3c.xs.validation.regex.api.RegExPattern;


final class FiniteRepetitionPattern<E, T> extends RestrictedBase<E, T> implements RegExPattern<E, T>
{
	public FiniteRepetitionPattern(final E term, final int minOccurs, final int maxOccurs, final RegExBridge<E, T> bridge)
	{
		m_term = term;
		m_minOccurs = minOccurs;
		m_maxOccurs = maxOccurs;
		m_bridge = PreCondition.assertArgumentNotNull(bridge);
	}

	public RegExMachine<E, T> createRegExMachine(final List<E> followerTerms)
	{
		return new FiniteRepetitionRegExMachine<E, T>(m_term, m_minOccurs, m_maxOccurs, m_bridge);
	}

	public String toString()
	{
		return m_term.toString() + m_minOccurs + m_maxOccurs;
	}

	private final E m_term;
	private final int m_minOccurs;
	private final int m_maxOccurs;
	private final RegExBridge<E, T> m_bridge;
}