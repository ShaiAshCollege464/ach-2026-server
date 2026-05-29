package server_2026_b.server.responses;

import server_2026_b.server.entities.UserEntity;

public class UserModel {
    private long userId;
    private long workerId;
    private String role;
    private String token;

    public UserModel () {
    }

    public UserModel (UserEntity user) {
        this.userId = user.getId();
        this.role=user.getRoleType();
        if (user.getWorkerEntity() != null) {
            this.workerId = user.getWorkerEntity().getId();
        }
        this.token = user.getToken();
    }


    public long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
