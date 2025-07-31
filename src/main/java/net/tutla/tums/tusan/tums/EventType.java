package net.tutla.tums.tusan.tums;

public enum EventType {
    // client side
    LEFT_CLICK,
    RIGHT_CLICK,
    MIDDLE_CLICK,

    // server side
    SERVER_START,
    SERVER_ENTITY_LOAD, // TODO: event_entity returns entity's name
}
