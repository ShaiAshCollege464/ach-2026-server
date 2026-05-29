package server_2026_b.server.responses;

import server_2026_b.server.entities.UserEntity;

public class UserResponse extends BasicResponse{
    private UserModel user;

    public UserResponse(boolean success, Integer errorCode, UserEntity userEntity) {
        super(success, errorCode, userEntity != null ? userEntity.getWorkerEntity() : null);
        if (userEntity != null) {
            this.user = new UserModel(userEntity);
        }
    }

    public UserResponse(boolean success, Integer errorCode) {
        super(success, errorCode, null);
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
