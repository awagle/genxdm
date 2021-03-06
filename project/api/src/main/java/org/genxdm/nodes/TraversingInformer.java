/*
 * Copyright (c) 2012 TIBCO Software Inc.
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
package org.genxdm.nodes;

import org.genxdm.Cursor;
import org.genxdm.axes.AxisTraverser;

/**
 * The base interface for looking at an underlying data structure using a "cursor"
 * approach, this interface captures all the ways to inspect the underlying data,
 * as well as the ability to get a new (and separate) {@link Cursor} to navigate to a new
 * location. 
 */
public interface TraversingInformer extends AxisTraverser, Informer, Comparable<TraversingInformer> {

    /**
     * Allow navigation of the tree where this informer is positioned.
     * 
     * @return a Precursor positioned at the contained node.
     */
    Cursor newCursor();
}
