package ca.cmpt213.as5courseplanner.model;

import java.util.List;

/**
 *  Section class represents the each course's section
 *  Section class allows all offerings of a course to be grouped together
 */

public class Section {
    private int enrollmentCapacity;
    private int enrollmentTotal;
    private String type;

    public Section(List<String> fieldList) {
        enrollmentCapacity = Integer.parseInt(fieldList.get(0));
        enrollmentTotal = Integer.parseInt(fieldList.get(1));
        type = fieldList.get(2).trim();
    }

    public int getEnrollmentCapacity() {
        return enrollmentCapacity;
    }

    public void setEnrollmentCapacity(int enrollmentCapacity) {
        this.enrollmentCapacity = enrollmentCapacity;
    }

    public void combineEnrollmentCapacity(int otherEnrollmentCapacity) {
        this.enrollmentCapacity += otherEnrollmentCapacity;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public void setEnrollmentTotal(int enrollmentTotal) {
        this.enrollmentTotal = enrollmentTotal;
    }

    public void combineEnrollmentTotal(int otherEnrollmentTotal) {
        this.enrollmentTotal += otherEnrollmentTotal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
