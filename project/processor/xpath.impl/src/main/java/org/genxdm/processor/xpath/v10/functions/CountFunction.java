/**
 * Portions copyright (c) 1998-1999, James Clark : see copyingjc.txt for
 * license details
 * Portions copyright (c) 2002, Bill Lindsey : see copying.txt for license
 * details
 * 
 * Portions copyright (c) 2009-2010 TIBCO Software Inc.
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
package org.genxdm.processor.xpath.v10.functions;

import org.genxdm.Model;
import org.genxdm.processor.xpath.v10.expressions.ConvertibleExprImpl;
import org.genxdm.processor.xpath.v10.expressions.ConvertibleNumberExpr;
import org.genxdm.xpath.v10.ExprContextDynamic;
import org.genxdm.xpath.v10.ExprContextStatic;
import org.genxdm.xpath.v10.ExprException;
import org.genxdm.xpath.v10.ExprParseException;
import org.genxdm.xpath.v10.NodeIterator;
import org.genxdm.xpath.v10.NodeSetExpr;
import org.genxdm.xpath.v10.extend.ConvertibleExpr;

/**
 * Represents the XPath Function: number count(node-set)
 * 
 * The count function returns the number of nodes in the argument node-set.
 */
public final class CountFunction 
    extends Function1
{
	private static final <N> int count(final NodeIterator<N> iter) throws ExprException
	{
		int n = 0;
		while (iter.next() != null)
		{
			n++;
		}
		return n;
	}


	ConvertibleExprImpl makeCallExpr(final ConvertibleExpr e, final ExprContextStatic statEnv) throws ExprParseException
	{
		final NodeSetExpr nse = e.makeNodeSetExpr(statEnv);

		return new ConvertibleNumberExpr()
		{
			public <N> double numberFunction(Model<N> model, final N contextNode, final ExprContextDynamic<N> dynEnv) throws ExprException
			{
				return count(nse.nodeIterator(model, contextNode, dynEnv));
			}
		};
	}
}
