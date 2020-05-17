package ca.cmpt213.as5courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Course class represents each unique course and its entire list of offerings
 */

public class Course implements Comparable<Course>, Iterable<Offering>, Subject {
    private long courseId;
    private String catalogNumber;
    private List<Offering> offeringList = new ArrayList<>();
    private List<Observer> observerList = new ArrayList<>();

    @Override
    public void addObserver(Observer observer) {
        observerList.add(observer);
    }

    @Override
    public void deleteObserver(Observer observer) {
        observerList.remove(observer);
    }

    @Override
    public void notifyObservers(Offering newOffering, Section newSection) {
        for (Observer eachObserver : observerList) {
            eachObserver.addUpdate(newOffering, newSection);
        }
    }

    @Override
    public int compareTo(Course otherCourse) {
        return catalogNumber.compareTo(otherCourse.catalogNumber);
    }

    @Override
    public Iterator<Offering> iterator() {
        return offeringList.iterator();
    }

    public Course() { }

    public Course(String catalogNumber, long courseId) {
        this.catalogNumber = catalogNumber.trim();
        this.courseId = courseId;
    }

    public Offering getOffering(long courseOfferingId) {
        for (Offering eachOffering : offeringList) {
            if (eachOffering.getCourseOfferingId() == courseOfferingId) {
                return eachOffering;
            }
        }
        return null;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    @JsonIgnore
    public List<Offering> getOfferingList() {
        return offeringList;
    }

    public void setOfferingList(List<Offering> offeringList) {
        this.offeringList = offeringList;
    }

    public void addToOfferingList(Offering newOffering, Section newSection) {
        // check for duplicate course; if there is, let Offering check for duplicates
        for (Offering eachOffering : offeringList) {
            if (eachOffering.getLocation().equals(newOffering.getLocation())
                    && eachOffering.getSemesterCode() == newOffering.getSemesterCode()) {
                // check to see if only instructors were missing; if so, add missing instructors to list of instructors
                if (!eachOffering.getInstructorList().equals(newOffering.getInstructorList())) {
                    eachOffering.addInstructorToList(newOffering.getInstructorList());
                    return;
                } else {
                    eachOffering.addSectionToList((newSection));
                    return;
                }
            }
        }

        // if not duplicate, add to course list
        newOffering.addSectionToList(newSection);
        offeringList.add(newOffering);
    }
}
