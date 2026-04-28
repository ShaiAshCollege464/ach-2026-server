package server_2026_b.server.entities;

public class WorkerEntity extends BasicEntity {
    private String firstName;
    private String lastName;
    private String workerId;
    private String role;
    private WorkerEntity managerEntity;
    private TeamEntity teamEntity;

    public WorkerEntity () {
    }

    public WorkerEntity(String firstName, String lastName, String workerId, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.workerId = workerId;
        this.role = role;
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

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public WorkerEntity getManagerEntity() {
        return managerEntity;
    }

    public void setManagerEntity(WorkerEntity managerEntity) {
        this.managerEntity = managerEntity;
    }

    public TeamEntity getTeamEntity() {
        return teamEntity;
    }

    public void setTeamEntity(TeamEntity teamEntity) {
        this.teamEntity = teamEntity;
    }
}
