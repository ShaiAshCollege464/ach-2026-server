package server_2026_b.server.responses;
import com.social.Entity.User;

public class UserResponse extends BasicResponse{
    private User user;

    public UserResponse(boolean success, Integer errorCode, User user) {
        super(success, errorCode);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }

    public UserResponse(boolean success, Integer errorCode, User user) {
        super(success, errorCode);
        this.user = user;
    }
    public UserResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
