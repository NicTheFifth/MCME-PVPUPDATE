package com.mcmiddleearth.mcme.pvpplugin.Handlers;

public interface EventBroadcaster<T> {
    void addListener(EventListener<T> eventListener);

    void removeListener(EventListener<T> eventListener);
}
