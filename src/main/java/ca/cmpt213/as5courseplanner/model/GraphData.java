package ca.cmpt213.as5courseplanner.model;

/**
 * GraphData class holds information about student enrollment for the graph on the server to display
 */

public class GraphData {
    private int semesterCode;
    private int totalCoursesTaken;

    public GraphData(int totalCoursesTaken, int semesterCode) {
        this.totalCoursesTaken = totalCoursesTaken;
        this.semesterCode = semesterCode;
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
