package server_2026_b.server.controllers;

import com.github.javafaker.Faker;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import server_2026_b.server.entities.TeamEntity;
import server_2026_b.server.entities.WorkerEntity;
import server_2026_b.server.responses.TeamDetailsResponse;
import server_2026_b.server.responses.TeamModel;
import server_2026_b.server.responses.WorkerModel;
import server_2026_b.server.service.Persist;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.http.ResponseEntity;
import server_2026_b.server.entities.TaskEntity;
import server_2026_b.server.responses.TaskModel;
@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class WorkerController extends BasicController {

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
                WorkerEntity manager = null;
                if (!allWorkers.isEmpty()) {
                    manager = allWorkers.get(random.nextInt(allWorkers.size()));
                }
                workerEntity.setManagerEntity(manager);
                TeamEntity teamEntity = teamEntities.get(random.nextInt(teamEntities.size()));
                workerEntity.setTeamEntity(teamEntity);
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
    public List<WorkerModel> getWorkersByManager (@CookieValue(value = "token") String token) {
        if (token != null) {
            WorkerEntity manager = this.persist.getWorkerByToken(token);
            if (manager != null) {
                int managerId = manager.getId();
                List<WorkerEntity> workerEntities = this.persist.loadList(WorkerEntity.class);
                List<WorkerModel> relevant = new ArrayList<>();
                for (WorkerEntity workerEntity : workerEntities) {
                    if (workerEntity.getManagerEntity() != null && workerEntity.getManagerEntity().getId() == managerId) {
                        relevant.add(new WorkerModel(null, workerEntity));
                    }
                }

                return relevant;
            } else {
                System.out.println("Invalid token");
            }
        } else {
            System.out.println("No token");
        }
        return null;
    }

    @RequestMapping("get-worker-details")
    public WorkerModel getWorkerDetails (int workerId) {
        WorkerEntity workerEntity = persist.loadObject(WorkerEntity.class, workerId);
        return new WorkerModel(null, workerEntity);
    }

    @RequestMapping("get-teams")
    public List<TeamModel> getTeams (@CookieValue(value = "token") String token) {
        List<TeamEntity> allTeams = persist.loadList(TeamEntity.class);
        WorkerEntity manager = persist.getWorkerByToken(token);
        List<TeamModel> teamModels = allTeams.stream().map(TeamModel::new).toList();
        List<WorkerEntity> allWorkers = this.persist.loadList(WorkerEntity.class);
        for (WorkerEntity workerEntity : allWorkers) {
            if (workerEntity.getManagerEntity() != null && workerEntity.getManagerEntity().getId() == manager.getId()) {
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
        }
        return teamModels.stream().filter((item) -> item.getWorkersCount() > 0).toList();
    }



    @RequestMapping("/team-details")
    public TeamDetailsResponse teamDetails (@CookieValue(value = "token") String token,  int id) {
        WorkerEntity manager = persist.getWorkerByToken(token);
        TeamEntity teamEntity = persist.loadObject(TeamEntity.class, id);
        List<WorkerEntity> workerEntities = persist.getWorkersByTeam(id);
        return new TeamDetailsResponse(true, null, teamEntity, workerEntities, manager);

    }

    @RequestMapping ("/update-team")
    public TeamDetailsResponse updateTeam (@CookieValue(value = "token") String token, String name, int id) {
        TeamEntity teamEntity = persist.loadObject(TeamEntity.class, id);
        teamEntity.setName(name);
        persist.save(teamEntity);
        return teamDetails(token, id);
    }

    @RequestMapping("get-all-teams")
    public List<TeamModel> getAllTeams() {
        List<TeamEntity> allTeams = persist.loadList(TeamEntity.class);
        return allTeams.stream().map(TeamModel::new).toList();
    }

    @RequestMapping("change-team")
    public ResponseEntity<String> changeTeam(@CookieValue(value = "token") String token,
                                             int workerId, int teamId) {
        WorkerEntity requester = persist.getWorkerByToken(token);
        if (requester == null)
            return ResponseEntity.status(403).body("Not authenticated");

        WorkerEntity workerToTransfer = persist.loadObject(WorkerEntity.class, workerId);
        boolean isManager = workerToTransfer.getManagerEntity() != null
                && workerToTransfer.getManagerEntity().getId() == requester.getId();
        if (!isManager)
            return ResponseEntity.status(403).body("Only the worker's manager can transfer them");

        TeamEntity teamEntity = persist.loadObject(TeamEntity.class, teamId);
        workerToTransfer.setTeamEntity(teamEntity);
        persist.save(workerToTransfer);
        return ResponseEntity.ok("Transfer successful");
    }

    @RequestMapping("get-team-tasks")
    public List<TaskModel> getTeamTasks(@CookieValue(value = "token") String token) {
        WorkerEntity worker = persist.getWorkerByToken(token);
        if (worker == null || worker.getTeamEntity() == null) return new ArrayList<>();
        List<TaskEntity> tasks = persist.getTasksByTeam(worker.getTeamEntity().getId());
        return tasks.stream().map(TaskModel::new).toList();
    }

    @RequestMapping("is-manager")
    public java.util.Map<String, Boolean> isManager(@CookieValue(value = "token") String token) {
        WorkerEntity worker = persist.getWorkerByToken(token);
        boolean hasSubordinates = worker != null &&
                !persist.getWorkersByManager(worker.getId()).isEmpty();
        return java.util.Map.of("isManager", hasSubordinates);
    }
}
