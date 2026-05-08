package server_2026_b.server.responses;

import java.util.List;

public class TeamExtendedModel extends TeamModel {
    private List<WorkerModel> workers;

    public TeamExtendedModel () {
    }

    public List<WorkerModel> getWorkers() {
        return workers;
    }

    public void setWorkers(List<WorkerModel> workers) {
        this.workers = workers;
    }
}
