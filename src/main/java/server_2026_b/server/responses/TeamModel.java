package server_2026_b.server.responses;

import server_2026_b.server.entities.TeamEntity;

public class TeamModel {
    private int id;
    private String name;
    private int workersCount;

    public TeamModel () {

    }

    public TeamModel (TeamEntity teamEntity) {
        this.id = teamEntity.getId();
        this.name = teamEntity.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWorkersCount() {
        return workersCount;
    }

    public void setWorkersCount(int workersCount) {
        this.workersCount = workersCount;
    }

    public void incrementWorkersCount () {
        this.workersCount++;
    }
}
