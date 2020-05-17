package ca.cmpt213.as5courseplanner.model;

/**
 * Observer Interface allows our Watcher to receive updates about Course objects
 */

public interface Observer {
    void addUpdate(Offering newOffering, Section section);
}
