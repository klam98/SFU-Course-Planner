package ca.cmpt213.as5courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Offering class represents a single offering of a class
 * Offering class also contains a list of sections for the course being offered
 */

public class Offering implements Comparable<Offering>, Iterable<Section> {
    private long courseOfferingId;
    private String location;
    private String term;
    private int semesterCode;
    private int year;

    private List<String> instructorList = new ArrayList<>();
    private List<Section> sectionList = new ArrayList<>();

    @Override
    public int compareTo(Offering other) {
        return semesterCode - other.semesterCode;
    }

    @Override
    public Iterator<Section> iterator() {
        return sectionList.iterator();
    }

    public Offering(List<String> fields, long courseOfferingId) {
        location = fields.get(0).trim();

        // collecting the number of instructors of each course offering
        for (int i = 1; i < fields.size() - 1; i++) {
            instructorList.add(fields.get(i).replace("\"", " ").trim());
        }
        semesterCode = Integer.parseInt(fields.get(fields.size() - 1));

        term = decodeTerm(semesterCode);
        year = decodeYear(semesterCode);

        semesterCode = Integer.parseInt(fields.get(fields.size() - 1));

        this.courseOfferingId = courseOfferingId;
    }

    public long getCourseOfferingId() {
        return courseOfferingId;
    }

    public void setCourseOfferingId(long courseOfferingId) {
        this.courseOfferingId = courseOfferingId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getInstructorList() {
        return instructorList;
    }

    // returns a string of all instructors, separated by commas
    public String getInstructors() {
        if (instructorList.size() == 1) {
            return instructorList.get(0);
        }
        else {
            StringBuilder StringOfInstructors = new StringBuilder();

            for (int i = 0; i < instructorList.size(); i++) {
                if (i == 0) {
                    StringOfInstructors.append(instructorList.get(i));
                }
                else {
                    StringOfInstructors.append(", ").append(instructorList.get(i));
                }
            }
            return StringOfInstructors.toString();
        }
    }

    public void setInstructors(List<String> instructors) {
        this.instructorList = instructors;
    }

    public void addInstructorToList(List<String> instructorList) {
        for (String newInstructor: instructorList) {
            if (!instructorList.contains(newInstructor)) {
                instructorList.add(newInstructor);
            }
        }
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    private String decodeTerm(int semesterCode) {
        String semesterCodeString = String.valueOf(semesterCode);

        if (semesterCodeString.charAt(3) == '1') {
            term = "Spring";
        }
        else if (semesterCodeString.charAt(3) == '4') {
            term = "Summer";
        }
        else if (semesterCodeString.charAt(3) == '7') {
            term = "Fall";
        }

        return term;
    }

    public int getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(int semesterCode) {
        this.semesterCode = semesterCode;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private int decodeYear(int semesterCode) {
        String semesterCodeString = String.valueOf(semesterCode);

        // first digit of semesterCode denotes the century
        int centuryDigit = semesterCodeString.charAt(0);

        // second and third digits of semesterCode denotes the year
        int yearDigits = semesterCodeString.charAt(1) + semesterCodeString.charAt(2);

        int year = 1900 + (centuryDigit * 100) + yearDigits;
        return year;
    }

    @JsonIgnore
    public List<Section> getSectionList() {
        return sectionList;
    }

    public void setSectionList(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    public void addSectionToList(Section newSection) {
        // check for duplicate component, if it is then simply add their enrollment caps and totals together
        for (Section eachSection : sectionList) {
            if (eachSection.getType().equals(newSection.getType())) {
                eachSection.addEnrollmentCap(newSection.getEnrollmentCap());
                eachSection.addEnrollmentTotal(newSection.getEnrollmentTotal());
                return;
            }
        }

        // if not a duplicate component, add to components list
        sectionList.add(newSection);
    }
}
