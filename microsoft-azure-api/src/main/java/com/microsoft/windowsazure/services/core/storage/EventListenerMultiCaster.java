package com.microsoft.windowsazure.services.core.storage;

import java.util.ArrayList;

/**
 * Represents a generic event multi-caster that allows event listeners to be dynamically added and removed.
 * 
 * @param <EVENT_TYPE>
 *            An object that represents the type of the event.
 * @param <EVENT_LISTENTER_TYPE>
 *            An object that represents the type of the event listener.
 * 
 *            Copyright (c)2011 Microsoft. All rights reserved.
 */
public final class EventListenerMultiCaster<EVENT_TYPE, EVENT_LISTENTER_TYPE extends EventListener<EVENT_TYPE>> {

    /**
     * Holds the list of listeners.
     */
    private final ArrayList<EVENT_LISTENTER_TYPE> listeners = new ArrayList<EVENT_LISTENTER_TYPE>();

    /**
     * Adds a listener to the event chain.
     * 
     * @param listener
     *            An <code>EventListenerType</code> object that represents the listener to add.
     */
    public synchronized void addListener(final EVENT_LISTENTER_TYPE listener) {
        this.listeners.add(listener);
    }

    /**
     * Fires the event to all subscribed event listeners.
     * 
     * @param event
     *            An <code>EVENTTYPE</code>object that represents the event being multi-casted.
     */
    public synchronized void fireEvent(final EVENT_TYPE event) {
        for (final EventListener<EVENT_TYPE> listener : this.listeners) {
            listener.eventOccurred(event);
        }
    }

    /**
     * Returns a value that indicates whether any event listeners are registered for events.
     * 
     * @return <code>true</code> if any event listeners are registered; otherwise, <code>false</code>.
     */
    public synchronized boolean hasListeners() {
        return this.listeners.size() > 0;
    }

    /**
     * Removes an event listener from the event chain.
     * 
     * @param listener
     *            An <code>EventListenerType</code> object that represents the listener to remove.
     */
    public synchronized void removeListener(final EVENT_LISTENTER_TYPE listener) {
        this.listeners.remove(listener);
    }
}
