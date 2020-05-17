package ca.cmpt213.as5courseplanner.model;

import java.util.List;

import static java.lang.Integer.parseInt;

/**
 *  Section class represents the each course's section
 *  Section class allows all offerings of a course to be grouped together
 */

public class Section implements Comparable<Section>{
    private int enrollmentCap;
    private int enrollmentTotal;
    private String component;

    @Override
    public int compareTo(Section other) {
        return component.compareTo(other.component);
    }

    public Section(List<String> fields) {
        enrollmentCap = parseInt(fields.get(0));
        enrollmentTotal = parseInt(fields.get(1));
        component = fields.get(2).trim();
    }

    public String getType() {
        return component;
    }

    public void setType(String component) {
        this.component = component;
    }

    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public void setEnrollmentCap(int enrollmentCap) {
        this.enrollmentCap = enrollmentCap;
    }

    public void addEnrollmentCap(int aggregateCap) {
        enrollmentCap += aggregateCap;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public void setEnrollmentTotal(int enrollmentTotal) {
        this.enrollmentTotal = enrollmentTotal;
    }

    public void addEnrollmentTotal(int aggregateTotal) {
        enrollmentTotal += aggregateTotal;
    }
}
