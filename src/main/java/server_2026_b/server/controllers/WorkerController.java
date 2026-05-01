package server_2026_b.server.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import server_2026_b.server.entities.TeamEntity;
import server_2026_b.server.entities.WorkerEntity;
import server_2026_b.server.responses.TeamModel;
import server_2026_b.server.responses.WorkerModel;
import server_2026_b.server.service.Persist;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
public class WorkerController {

    private final Persist persist;

    public WorkerController(Persist persist) {
        this.persist = persist;
    }


    @PostConstruct
    public void init () {
    }


    @RequestMapping("get-all-workers")
    public List<WorkerEntity> getAllWorkers () {
        System.out.println("This is from the my branch");
        return this.persist.loadList(WorkerEntity.class);
    }

    @RequestMapping("get-workers-by-manager")
    public List<WorkerModel> getWorkersByManager (String token) {
        int managerId = 4;//TODO: load id by token
        List<WorkerEntity> workerEntities = this.persist.loadList(WorkerEntity.class);
        List<WorkerModel> relevant = new ArrayList<>();
        for (WorkerEntity workerEntity : workerEntities) {
            if (workerEntity.getManagerEntity() != null && workerEntity.getManagerEntity().getId() == managerId) {
                relevant.add(new WorkerModel(workerEntity));
            }
        }

        System.out.println("This is new line by Dror");
        return relevant;
    }

    @RequestMapping("get-worker-details")
    public WorkerModel getWorkerDetails (int workerId) {
        WorkerEntity workerEntity = persist.loadObject(WorkerEntity.class, workerId);
        return new WorkerModel(workerEntity);
    }

    @RequestMapping("get-teams")
    public List<TeamModel> getTeams () {
        List<TeamEntity> allTeams = persist.loadList(TeamEntity.class);
        return allTeams.stream().map(TeamModel::new).toList();
    }

    @RequestMapping("change-team")
    public void changeTeam (int workerId, int teamId) {
        WorkerEntity workerEntity = persist.loadObject(WorkerEntity.class, workerId);
        TeamEntity teamEntity = persist.loadObject(TeamEntity.class, teamId);
        workerEntity.setTeamEntity(teamEntity);
        persist.save(workerEntity);
    }


}
