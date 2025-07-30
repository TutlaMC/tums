package net.tutla.tums.tusan.nodes.tums;

public enum EventType {
    // client side
    ON_LEFT_CLICK,
    ON_RIGHT_CLICK,
    ON_MIDDLE_CLICK,

    // server side
    ON_SERVER_START,
    ON_SERVER_ENTITY_LOAD, // TODO: event_entity returns entity's name
}
