package ca.cmpt213.as5courseplanner.model;

/**
 * GraphData class holds information about student enrollment for the graph on the server to display
 */

public class GraphData implements Comparable<GraphData>{
    private int semesterCode;
    private int totalCoursesTaken;

    @Override
    public int compareTo( GraphData other) {
        return semesterCode - other.semesterCode;
    }

    public GraphData(int semesterCode, Department department) {
        this.semesterCode = semesterCode;

        for (Course eachCourse : department) {
            for (Offering eachOffering: eachCourse) {
                if (eachOffering.getSemesterCode() == semesterCode) {
                    totalCoursesTaken += getEnrolledToLec(eachOffering);
                }
            }
        }
    }

    public int getEnrolledToLec(Offering offering) {
        int enrollmentTotal = 0;

        for (Section eachSection : offering) {
            if (eachSection.getType().equals("LEC")) {
                enrollmentTotal += eachSection.getEnrollmentTotal();
            }
        }

        return enrollmentTotal;
    }

    public int getSemesterCode() {
        return semesterCode;
    }

    public void setSemesterCode(int semesterCode) {
        this.semesterCode = semesterCode;
    }

    public int getTotalCoursesTaken() {
        return totalCoursesTaken;
    }

    public void setTotalCoursesTaken(int totalCoursesTaken) {
        this.totalCoursesTaken = totalCoursesTaken;
    }
}
