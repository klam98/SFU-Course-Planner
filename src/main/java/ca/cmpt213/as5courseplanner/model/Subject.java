package ca.cmpt213.as5courseplanner.model;

/**
 * Subject interface allows creation, deletion, and notification of observers that inquire data about Course objects
 */

public interface Subject {
    void addObserver(Observer observer);
    void deleteObserver(Observer observer);
    void notifyObservers(Offering newOffering, Section newSection);
}
