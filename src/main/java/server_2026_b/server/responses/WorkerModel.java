package server_2026_b.server.responses;

import server_2026_b.server.entities.WorkerEntity;

public class WorkerModel {
    private int id;
    private String firstName;
    private String lastName;
    private String teamName;
    private int teamId;

    public WorkerModel () {
    }

    public WorkerModel (WorkerEntity workerEntity) {
        this.id = workerEntity.getId();
        this.firstName = workerEntity.getFirstName();
        this.lastName = workerEntity.getLastName();
        this.teamName = workerEntity.getTeamEntity().getName();
        this.teamId = workerEntity.getTeamEntity().getId();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }
}
