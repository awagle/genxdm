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
package org.gxml.processor.xpath.v10;

import java.util.HashMap;

import org.gxml.xpath.v10.Function;
import org.gxml.xpath.v10.XPathCompiler;
import org.gxml.xpath.v10.XPathToolkit;
import org.gxml.xpath.v10.expressions.ExprContextDynamicArgs;
import org.gxml.xpath.v10.expressions.ExprContextStaticArgs;
import org.gxml.processor.xpath.v10.expressions.AncestorAxisExpr;
import org.gxml.processor.xpath.v10.expressions.AncestorOrSelfAxisExpr;
import org.gxml.processor.xpath.v10.expressions.AttributeAxisExpr;
import org.gxml.processor.xpath.v10.expressions.AxisExpr;
import org.gxml.processor.xpath.v10.expressions.ChildAxisExpr;
import org.gxml.processor.xpath.v10.expressions.DescendantAxisExpr;
import org.gxml.processor.xpath.v10.expressions.DescendantOrSelfAxisExpr;
import org.gxml.processor.xpath.v10.expressions.ExprContextDynamicArgsImpl;
import org.gxml.processor.xpath.v10.expressions.ExprContextStaticArgsImpl;
import org.gxml.processor.xpath.v10.expressions.FollowingAxisExpr;
import org.gxml.processor.xpath.v10.expressions.FollowingSiblingAxisExpr;
import org.gxml.processor.xpath.v10.expressions.NamespaceAxisExpr;
import org.gxml.processor.xpath.v10.expressions.ParentAxisExpr;
import org.gxml.processor.xpath.v10.expressions.PrecedingAxisExpr;
import org.gxml.processor.xpath.v10.expressions.PrecedingSiblingAxisExpr;
import org.gxml.processor.xpath.v10.expressions.SelfAxisExpr;
import org.gxml.processor.xpath.v10.expressions.XPathCompilerImpl;
import org.gxml.processor.xpath.v10.functions.BooleanFunction;
import org.gxml.processor.xpath.v10.functions.CeilingFunction;
import org.gxml.processor.xpath.v10.functions.ConcatFunction;
import org.gxml.processor.xpath.v10.functions.ContainsFunction;
import org.gxml.processor.xpath.v10.functions.CountFunction;
import org.gxml.processor.xpath.v10.functions.FalseFunction;
import org.gxml.processor.xpath.v10.functions.FloorFunction;
import org.gxml.processor.xpath.v10.functions.FormatNumberFunction;
import org.gxml.processor.xpath.v10.functions.IdFunction;
import org.gxml.processor.xpath.v10.functions.LangFunction;
import org.gxml.processor.xpath.v10.functions.LastFunction;
import org.gxml.processor.xpath.v10.functions.LocalNameFunction;
import org.gxml.processor.xpath.v10.functions.NameFunction;
import org.gxml.processor.xpath.v10.functions.NamespaceUriFunction;
import org.gxml.processor.xpath.v10.functions.NormalizeSpaceFunction;
import org.gxml.processor.xpath.v10.functions.NotFunction;
import org.gxml.processor.xpath.v10.functions.NumberFunction;
import org.gxml.processor.xpath.v10.functions.PositionFunction;
import org.gxml.processor.xpath.v10.functions.RoundFunction;
import org.gxml.processor.xpath.v10.functions.StartsWithFunction;
import org.gxml.processor.xpath.v10.functions.StringFunction;
import org.gxml.processor.xpath.v10.functions.StringLengthFunction;
import org.gxml.processor.xpath.v10.functions.SubstringAfterFunction;
import org.gxml.processor.xpath.v10.functions.SubstringBeforeFunction;
import org.gxml.processor.xpath.v10.functions.SubstringFunction;
import org.gxml.processor.xpath.v10.functions.SumFunction;
import org.gxml.processor.xpath.v10.functions.SystemPropertyFunction;
import org.gxml.processor.xpath.v10.functions.TranslateFunction;
import org.gxml.processor.xpath.v10.functions.TrueFunction;
import org.gxml.processor.xpath.v10.relations.EqualsRelation;
import org.gxml.processor.xpath.v10.relations.GreaterThanEqualsRelation;
import org.gxml.processor.xpath.v10.relations.GreaterThanRelation;
import org.gxml.processor.xpath.v10.relations.NotEqualsRelation;
import org.gxml.processor.xpath.v10.relations.Relation;

final class XPathToolkitImpl
    implements XPathToolkit
{
	/**
	 * Initialize these once at this toolkit level.
	 */
	private final HashMap<String, AxisExpr> axisTable = new HashMap<String, AxisExpr>();
	private final HashMap<String, Function> functionTable = new HashMap<String, Function>();
	private final HashMap<String, Relation> relationTable = new HashMap<String, Relation>();

	public XPathToolkitImpl(final boolean inheritAttributes, final boolean inheritNamespaces)
	{
		axisTable.put("child", new ChildAxisExpr());
		axisTable.put("parent", new ParentAxisExpr());
		axisTable.put("self", new SelfAxisExpr());
		axisTable.put("attribute", new AttributeAxisExpr(inheritAttributes));
		axisTable.put("namespace", new NamespaceAxisExpr(inheritNamespaces));
		axisTable.put("descendant-or-self", new DescendantOrSelfAxisExpr());
		axisTable.put("descendant", new DescendantAxisExpr());
		axisTable.put("ancestor-or-self", new AncestorOrSelfAxisExpr());
		axisTable.put("ancestor", new AncestorAxisExpr());
		axisTable.put("following-sibling", new FollowingSiblingAxisExpr());
		axisTable.put("preceding-sibling", new PrecedingSiblingAxisExpr());
		axisTable.put("following", new FollowingAxisExpr());
		axisTable.put("preceding", new PrecedingAxisExpr());

		// TODO - the function table is exactly the same, regardless of parameters, so it should
		// be created in a static.
		functionTable.put("boolean", new BooleanFunction());
		functionTable.put("ceiling", new CeilingFunction());
		functionTable.put("concat", new ConcatFunction());
		functionTable.put("contains", new ContainsFunction());
		functionTable.put("count", new CountFunction());
		functionTable.put("false", new FalseFunction());
		functionTable.put("floor", new FloorFunction());
		functionTable.put("format-number", new FormatNumberFunction());
		functionTable.put("id", new IdFunction());
		functionTable.put("lang", new LangFunction());
		functionTable.put("last", new LastFunction());
		functionTable.put("local-name", new LocalNameFunction());
		functionTable.put("namespace-uri", new NamespaceUriFunction());
		functionTable.put("normalize-space", new NormalizeSpaceFunction());
		functionTable.put("not", new NotFunction());
		functionTable.put("number", new NumberFunction());
		functionTable.put("position", new PositionFunction());
		functionTable.put("name", new NameFunction());
		functionTable.put("round", new RoundFunction());
		functionTable.put("starts-with", new StartsWithFunction());
		functionTable.put("string", new StringFunction());
		functionTable.put("string-length", new StringLengthFunction());
		functionTable.put("substring", new SubstringFunction());
		functionTable.put("substring-after", new SubstringAfterFunction());
		functionTable.put("substring-before", new SubstringBeforeFunction());
		functionTable.put("sum", new SumFunction());
		functionTable.put("system-property", new SystemPropertyFunction());
		functionTable.put("translate", new TranslateFunction());
		functionTable.put("true", new TrueFunction());

		relationTable.put("=", new EqualsRelation());
		relationTable.put("!=", new NotEqualsRelation());
		relationTable.put("==", new EqualsRelation());
		relationTable.put(">=", new GreaterThanEqualsRelation());
		relationTable.put(">", new GreaterThanRelation());
	}

	public XPathCompiler newXPathCompiler()
	{
		return new XPathCompilerImpl(axisTable, functionTable, relationTable);
	}

	public <N> ExprContextDynamicArgs<N> newExprContextDynamicArgs()
	{
		return new ExprContextDynamicArgsImpl<N>();
	}

	public ExprContextStaticArgs newExprContextStaticArgs()
	{
		return new ExprContextStaticArgsImpl();
	}

	public Function declareFunction(String name, Function newFunction) {
		return functionTable.put(name, newFunction);
	}
}
