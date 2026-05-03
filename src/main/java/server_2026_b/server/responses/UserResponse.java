package server_2026_b.server.responses;

import server_2026_b.server.entities.User;

public class UserResponse extends BasicResponse{
    private User user;

    public UserResponse(boolean success, Integer errorCode, User user) {
        super(success, errorCode);
        this.user = user;
    }
    public User getUser() {
        return user;
    }

    public UserResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
