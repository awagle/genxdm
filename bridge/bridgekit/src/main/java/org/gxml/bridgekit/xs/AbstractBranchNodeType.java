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

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.enums.SmNodeKind;
import org.genxdm.xs.types.SmNodeType;

abstract class AbstractBranchNodeType<A> extends AbstractPrimeExcludingNoneType<A> implements SmNodeType<A>
{
	protected final SmCache<A> cache;
	private final SmNodeKind nodeKind;

	public AbstractBranchNodeType(final SmNodeKind nodeKind, final SmCache<A> cache)
	{
		this.nodeKind = PreCondition.assertArgumentNotNull(nodeKind);
		this.cache = cache;
	}

	public final SmNodeKind getNodeKind()
	{
		return nodeKind;
	}

	public boolean isNative()
	{
		return false;
	}

	public boolean isChoice()
	{
		return false;
	}
}