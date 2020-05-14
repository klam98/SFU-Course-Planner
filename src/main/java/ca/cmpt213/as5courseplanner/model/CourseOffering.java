package ca.cmpt213.as5courseplanner.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * CourseOffering class represents a single offering of a class
 * CourseOffering class also contains a list of sections for the course being offered
 */

public class CourseOffering implements Iterable<Section> {
    private long courseOfferingId;
    private String location;
    private String term;
    private List<String> instructorList = new ArrayList<>();
    private List<Section> sectionList = new ArrayList<>();
    private int semesterCode;
    private int year;

    @Override
    public Iterator<Section> iterator() {
        return sectionList.iterator();
    }

    public CourseOffering(long courseOfferingId, List<String> fieldList) {
        this.courseOfferingId = courseOfferingId;
        location = fieldList.get(0).trim();

        // collecting the number of instructors of each course offering
        for (int i = 1; i < fieldList.size() - 1; i++) {
            instructorList.add(fieldList.get(i).replace("\"", " ").trim());
        }
        semesterCode = Integer.parseInt(fieldList.get(fieldList.size() - 1));

        term = decodeTerm(semesterCode);
        year = decodeYear(semesterCode);
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

    public List<String> getInstructorList() {
        return instructorList;
    }

    public void setInstructors(List<String> instructorList) {
        this.instructorList = instructorList;
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

    public void addInstructorToList(List<String> instructors) {
        for (String newInstructor : instructors) {
            if (!instructorList.contains(newInstructor)) {
                instructorList.add(newInstructor);
            }
        }
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

    public void setSections(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    @JsonIgnore
    public void sortSectionList() {
        Collections.sort(sectionList, (section1, section2) -> section1.getType().compareTo(section2.getType()));
    }

    public void addSectionToList(Section newSection) {
        // duplicate sections; add their enrollment capacities and totals together
        for (Section eachSection : sectionList) {
            if (eachSection.getType().equals(newSection.getType())) {
                eachSection.combineEnrollmentCapacity(newSection.getEnrollmentCapacity());
                eachSection.combineEnrollmentTotal(newSection.getEnrollmentTotal());
                return;
            }
        }

        // otherwise unique section; add it to sectionList
        sectionList.add(newSection);
        sortSectionList();
    }

    public int getNumEnrolledToLecture() {
        int enrollmentTotal = 0;

        for (Section eachSection : sectionList) {
            if (eachSection.getType().equals("LEC")) {
                enrollmentTotal += eachSection.getEnrollmentTotal();
            }
        }

        return enrollmentTotal;
    }
}
