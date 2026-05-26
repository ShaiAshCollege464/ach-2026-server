package server_2026_b.server.responses;

import server_2026_b.server.entities.WorkerEntity;

public class WorkerModel {
    private int id;
    private String firstName;
    private String lastName;
    private String teamName;
    private int teamId;
    private String managerName;
    private long managerId;
    private boolean myWorker;

    public WorkerModel () {
    }

    public WorkerModel (WorkerEntity meAsManager, WorkerEntity workerEntity) {
        this.id = workerEntity.getId();
        this.firstName = workerEntity.getFirstName();
        this.lastName = workerEntity.getLastName();
        this.teamName = workerEntity.getTeamEntity().getName();
        this.teamId = workerEntity.getTeamEntity().getId();
        if (workerEntity.getManagerEntity() != null) {
            this.managerId = workerEntity.getManagerEntity().getId();
            this.managerName = workerEntity.getManagerEntity().getFirstName() + " " + workerEntity.getManagerEntity().getLastName();
            if (meAsManager != null && this.managerId == meAsManager.getId()) {
                this.myWorker = true;
            }

        }
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

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public long getManagerId() {
        return managerId;
    }

    public void setManagerId(long managerId) {
        this.managerId = managerId;
    }

    public boolean isMyWorker() {
        return myWorker;
    }

    public void setMyWorker(boolean myWorker) {
        this.myWorker = myWorker;
    }
}
