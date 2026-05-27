package server_2026_b.server.entities;

import java.util.Date;

public class TaskEntity extends BasicEntity {
    private String title;
    private String details;
    private Date start;
    private Integer hoursEstimation;
    private TeamEntity teamEntity;
    private boolean isCompleted;

    public TaskEntity(String title, String details, Date start, int hoursEstimation) {
        this.hoursEstimation = hoursEstimation;
        this.start = start;
        this.details = details;
        this.title = title;
        isCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public int getHoursEstimation() {
        return hoursEstimation;
    }

    public void setHoursEstimation(int hoursEstimation) {
        this.hoursEstimation = hoursEstimation;
    }

    public TeamEntity getTeamEntity() {
        return teamEntity;
    }

    public void setTeamEntity(TeamEntity teamEntity) {
        this.teamEntity = teamEntity;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = !isCompleted;
    }

}