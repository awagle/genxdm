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
package org.genxdm.bridgekit.atoms;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.genxdm.bridgekit.misc.UnaryIterator;
import org.genxdm.xs.types.NativeType;

/**
 * Corresponds to the W3C XML Schema <a href="http://www.w3.org/TR/xmlschema-2/#boolean">boolean</a>.
 */
public enum XmlBoolean implements XmlAtom
{
    FALSE(false), TRUE(true);

    public static XmlBoolean valueOf(final boolean booleanValue)
    {
        return booleanValue ? TRUE : FALSE;
    }

    private final boolean booleanValue;

    private XmlBoolean(final boolean booleanValue)
    {
        this.booleanValue = booleanValue;
    }

    public void add(final int index, final XmlAtom element)
    {
        throw new UnsupportedOperationException();
    }

    public boolean add(final XmlAtom e)
    {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(final Collection<? extends XmlAtom> c)
    {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(final int index, final Collection<? extends XmlAtom> c)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns this value as a boolean.
     */
    public boolean getBooleanValue()
    {
        return booleanValue;
    }

    public void clear()
    {
        throw new UnsupportedOperationException();
    }

    public boolean contains(Object o)
    {
        // TODO: maybe equivalent to equals?
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> c)
    {
        // TODO: if size == 1, equivalent to equals?
        throw new UnsupportedOperationException();
    }

    public XmlBoolean get(final int index)
    {
        if (0 == index)
        {
            return this;
        }
        else
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    public String getC14NForm()
    {
        return booleanValue ? "true" : "false";
    }

    public NativeType getNativeType()
    {
        return NativeType.BOOLEAN;
    }

    public int indexOf(Object o)
    {
        // TODO: if the object is a boolean and it matches our value, return index 0
        // otherwise, return the not-found indicator.
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty()
    {
        return false;
    }

    public boolean isWhiteSpace()
    {
        return false;
    }

    public Iterator<XmlAtom> iterator()
    {
        return new UnaryIterator<XmlAtom>(this);
    }

    public int lastIndexOf(Object o)
    {
        return indexOf(o);
    }

    public ListIterator<XmlAtom> listIterator()
    {
        // TODO: how does one turn this into a single-element list and then an iterator?
        throw new UnsupportedOperationException();
    }

    public ListIterator<XmlAtom> listIterator(int index)
    {
        if (index == 0)
            return listIterator();
        throw new ArrayIndexOutOfBoundsException(index);
    }

    public XmlAtom remove(final int index)
    {
        throw new UnsupportedOperationException();
    }

    public boolean remove(final Object o)
    {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(final Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(final Collection<?> c)
    {
        throw new UnsupportedOperationException();
    }

    public XmlAtom set(final int index, final XmlAtom element)
    {
        throw new UnsupportedOperationException();
    }

    public int size()
    {
        return 1;
    }

    public List<XmlAtom> subList(final int fromIndex, final int toIndex)
    {
        if ( (fromIndex == 0) && (toIndex == 1) )
            return this;
        throw new ArrayIndexOutOfBoundsException(toIndex);
    }

    public Object[] toArray()
    {
        return new Object[] { this };
    }

    @SuppressWarnings("unchecked")
    public final <T> T[] toArray(T[] a)
    {
        final int size = size();
        if (a.length < size)
        {
            a = (T[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(toArray(), 0, a, 0, size);
        if (a.length > size)
        {
            a[size] = null;
        }
        return a;
    }
}
