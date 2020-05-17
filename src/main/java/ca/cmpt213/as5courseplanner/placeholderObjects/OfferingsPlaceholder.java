package ca.cmpt213.as5courseplanner.placeholderObjects;

/**
 * A place holder object to store the fields for when we call @RequestParam
 */

public class OfferingsPlaceholder {
    public int semester;
    public String subjectName;
    public String catalogNumber;
    public String location;
    public int enrollmentCap;
    public String component;
    public int enrollmentTotal;
    public String instructor;

    OfferingsPlaceholder() { }

    public int getSemesterCode() {
        return semester;
    }

    public void setSemesterCode(int semester) {
        this.semester = semester;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(String catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getEnrollmentCap() {
        return enrollmentCap;
    }

    public void setEnrollmentCap(int enrollmentCap) {
        this.enrollmentCap = enrollmentCap;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getEnrollmentTotal() {
        return enrollmentTotal;
    }

    public void setEnrollmentTotal(int enrollmentTotal) {
        this.enrollmentTotal = enrollmentTotal;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
