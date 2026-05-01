package server_2026_b.server.controllers;

import com.github.javafaker.Faker;
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
import java.util.Random;

@RestController
public class WorkerController {

    private final Persist persist;

    public WorkerController(Persist persist) {
        this.persist = persist;
    }


    @PostConstruct
    public void init () {
        List<WorkerEntity> allWorkers = this.persist.loadList(WorkerEntity.class);

        if (allWorkers.size() < 200) {
            Faker faker = new Faker();
            Random random = new Random();
            List<TeamEntity> teamEntities = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                TeamEntity teamEntity = new TeamEntity();
                teamEntity.setName(faker.funnyName().name());
                this.persist.save(teamEntity);
                teamEntities.add(teamEntity);
            }
            for (int i = 0; i < 200; i++) {
                WorkerEntity workerEntity = new WorkerEntity();
                workerEntity.setFirstName(faker.name().firstName());
                workerEntity.setLastName(faker.name().lastName());
                workerEntity.setWorkerId(String.valueOf(random.nextInt(1000000, 9000000)));
                workerEntity.setRole(faker.job().position());
                WorkerEntity manager = allWorkers.get(random.nextInt(allWorkers.size()));
                workerEntity.setManagerEntity(manager);
                TeamEntity teamEntity = teamEntities.get(random.nextInt(teamEntities.size()));
                workerEntity.setTeamEntity(teamEntity);
                workerEntity.setToken(String.valueOf(random.nextInt(1000000, 9000000)));
                allWorkers.add(workerEntity);
                this.persist.save(workerEntity);
            }
        }

    }


    @RequestMapping("get-all-workers")
    public List<WorkerEntity> getAllWorkers () {
        System.out.println("This is from the my branch");
        return this.persist.loadList(WorkerEntity.class);
    }

    @RequestMapping("get-workers-by-manager")
    public List<WorkerModel> getWorkersByManager (String token) {
        if (token != null) {
            WorkerEntity manager = this.persist.getWorkerByToken(token);
            if (manager != null) {
                int managerId = manager.getId();
                List<WorkerEntity> workerEntities = this.persist.loadList(WorkerEntity.class);
                List<WorkerModel> relevant = new ArrayList<>();
                for (WorkerEntity workerEntity : workerEntities) {
                    if (workerEntity.getManagerEntity() != null && workerEntity.getManagerEntity().getId() == managerId) {
                        relevant.add(new WorkerModel(workerEntity));
                    }
                }

                System.out.println("This is new line by Dror");
                return relevant;
            } else {
                System.out.println("Invaliad token");
            }
        } else {
            System.out.println("No token");
        }
        return null;
    }

    @RequestMapping("get-worker-details")
    public WorkerModel getWorkerDetails (int workerId) {
        WorkerEntity workerEntity = persist.loadObject(WorkerEntity.class, workerId);
        return new WorkerModel(workerEntity);
    }

    @RequestMapping("get-teams")
    public List<TeamModel> getTeams () {
        List<TeamEntity> allTeams = persist.loadList(TeamEntity.class);
        List<TeamModel> teamModels = allTeams.stream().map(TeamModel::new).toList();
        List<WorkerEntity> allWorkers = this.persist.loadList(WorkerEntity.class);
        for (WorkerEntity workerEntity : allWorkers) {
            int teamId = workerEntity.getTeamEntity().getId();
            TeamModel teamModel = null;
            for (int i = 0; i < teamModels.size(); i++) {
                if (teamModels.get(i).getId() == teamId) {
                    teamModel = teamModels.get(i);
                    break;
                }
            }
            if (teamModel != null) {
                teamModel.incrementWorkersCount();
            }
        }
        return teamModels;
    }

    @RequestMapping("change-team")
    public void changeTeam (int workerId, int teamId) {
        WorkerEntity workerEntity = persist.loadObject(WorkerEntity.class, workerId);
        TeamEntity teamEntity = persist.loadObject(TeamEntity.class, teamId);
        workerEntity.setTeamEntity(teamEntity);
        persist.save(workerEntity);
    }


}
