package net.tutla.tums.tusan.tums;

public enum EventType {
    // client side
    LEFT_CLICK,
    LEFT_RELEASE,
    RIGHT_CLICK,
    RIGHT_RELEASE,
    MIDDLE_CLICK,
    MIDDILE_RELEASE,
    SCROLL_UP,
    SCROLL_DOWN,

    ATTACK,

    // server side
    SERVER_START,
    SERVER_ENTITY_LOAD, // TODO: event_entity returns entity's name
}
