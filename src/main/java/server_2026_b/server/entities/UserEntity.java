package server_2026_b.server.entities;

public class UserEntity {
    private Long id;
    private String username;
    private String password;
    private String roleType;
    private String token;
    private WorkerEntity workerEntity;

    public UserEntity() {
    }

    public UserEntity(String username, String password, String roleType) {
        this.username = username;
        this.password = password;
        this.roleType = roleType;
    }

    public UserEntity(String username, String roleType) {
        this.username = username;
        this.roleType = roleType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public WorkerEntity getWorkerEntity() {
        return workerEntity;
    }

    public void setWorkerEntity(WorkerEntity workerEntity) {
        this.workerEntity = workerEntity;
    }
}
