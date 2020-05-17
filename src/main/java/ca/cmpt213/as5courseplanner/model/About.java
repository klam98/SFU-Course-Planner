package ca.cmpt213.as5courseplanner.model;

/**
 * About class describes the name and details of this project
 */

public class About {
    private String appName;
    private String authorName;

    public About() {
        appName = "CMPT 213 REST API Course Planner";
        authorName = "Kenrick Lam";
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
