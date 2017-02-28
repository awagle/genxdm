package org.genxdm.bridgekit.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.genxdm.creation.Attrib;
import org.genxdm.creation.ContentEvent;
import org.genxdm.creation.EventKind;
import org.genxdm.creation.EventQueue;

public class ContentHelperToEventQueue
    extends AbstractContentHelper
    implements EventQueue
{
    public ContentHelperToEventQueue(Map<String, String> bindings)
    {
        if (bindings == null)
            context = new HashMap<String, String>();
        else 
            context = bindings;
    }

    @Override
    public List<ContentEvent> getQueue()
    {
        return queue;
        // throw new GenXDMException("Unbalanced queue! Missing 'end' event for 'start' event");
    }
    
    @Override
    public void start()
    {
        // TODO: this should *probably* throw an exception, because how stupid is this?
        // it's an event queue. we shouldn't have a document in it.
        // *alternately*, just discard the event. do that for now.
        //queue.add(new ContentEventImpl((URI)null, null));
    }

    @Override
    public void startComplex(String ns, String name, Map<String, String> bindings, Iterable<Attrib> attributes)
    {
        // big deal is all in here; queue a start element:
        // TODO: do it right.
        String prefix = null;
        queue.add(new ContentEventImpl(ns, name, prefix));
        // TODO: queue the namespaces
        // TODO: queue the attributes
    }

    @Override
    public void comment(String text)
    {
        queue.add(new ContentEventImpl(EventKind.COMMENT, text));
    }

    @Override
    public void pi(String target, String data)
    {
        queue.add(new ContentEventImpl(EventKind.PROCESSING_INSTRUCTION, target, data));
    }

    @Override
    public void endComplex()
    {
        queue.add(new ContentEventImpl(EventKind.END_ELEMENT));
    }

    @Override
    public void end()
    {
        //TODO: see above, for start(). we should *not* have documents inside
        // an event queue, so we either throw an exception or we ignore it.
        // ignore for now.
        //queue.add(new ContentEventImpl(EventKind.END_DOCUMENT));
    }

    @Override
    public void reset()
    {
        queue.clear();
        // TODO: clear the namespace stuff, too
        // that means we need to have a way to reset context, for reuse!
    }
    
    protected void text(String ns, String name, String value)
    {
        queue.add(new ContentEventImpl(EventKind.TEXT, value));
    }

    private final List<ContentEvent> queue = new ArrayList<ContentEvent>();
    private final Map<String, String> context; // namespace context at start
    private final NamespaceContextStack nsStack = new NamespaceContextStack("cns");
}
