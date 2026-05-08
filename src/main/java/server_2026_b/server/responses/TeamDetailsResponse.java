package server_2026_b.server.responses;

import server_2026_b.server.entities.TeamEntity;
import server_2026_b.server.entities.UserEntity;
import server_2026_b.server.entities.WorkerEntity;

import java.util.List;

public class TeamDetailsResponse extends BasicResponse {
    private TeamExtendedModel team;


    public TeamDetailsResponse(boolean success, Integer errorCode, TeamEntity teamEntity,
                               List<WorkerEntity> workerEntityList, WorkerEntity manager) {
        super(success, errorCode, manager);
        this.team = new TeamExtendedModel();
        this.team.setId(teamEntity.getId());
        this.team.setName(teamEntity.getName());
        this.team.setWorkers(workerEntityList.stream().map(item -> new WorkerModel(manager, item)).toList());
    }


    public TeamExtendedModel getTeam() {
        return team;
    }

    public void setTeam(TeamExtendedModel team) {
        this.team = team;
    }
}
