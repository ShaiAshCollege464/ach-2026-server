package server_2026_b.server.responses;

import server_2026_b.server.entities.WorkerEntity;

public class BasicResponse {
    private boolean success;
    private Integer errorCode;
    private Integer myId;

    public BasicResponse(boolean success, Integer errorCode, WorkerEntity workerEntity) {
        this.success = success;
        this.errorCode = errorCode;
        if (workerEntity != null) {
            this.myId = workerEntity.getId();
        }
    }

    public BasicResponse() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Integer getMyId() {
        return myId;
    }

    public void setMyId(Integer myId) {
        this.myId = myId;
    }
}
