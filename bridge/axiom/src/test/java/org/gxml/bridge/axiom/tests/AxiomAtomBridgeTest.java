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
package org.gxml.bridge.axiom.tests;

import org.apache.axiom.om.impl.llom.factory.OMLinkedListImplFactory;
import org.genxdm.base.ProcessingContext;
import org.gxml.bridgekit.atoms.XmlAtom;
import org.gxml.bridgetest.typed.AtomBridgeTestBase;
import org.gxml.bridge.axiom.AxiomProcessingContext;

public final class AxiomAtomBridgeTest
    extends AtomBridgeTestBase<Object, XmlAtom>
{
    public ProcessingContext<Object> newProcessingContext()
    {
        return new AxiomProcessingContext(new OMLinkedListImplFactory());
    }
}