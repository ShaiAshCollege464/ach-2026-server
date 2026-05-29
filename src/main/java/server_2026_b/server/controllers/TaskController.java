package server_2026_b.server.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server_2026_b.server.entities.TaskEntity;
import server_2026_b.server.entities.TeamEntity;
import server_2026_b.server.entities.WorkerEntity;
import server_2026_b.server.responses.TaskModel;
import server_2026_b.server.service.Persist;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class TaskController extends BasicController {
    private final Persist persist;

    public TaskController(Persist persist) {
        this.persist = persist;
    }

    @PostConstruct
    public void init() {

    }

    @RequestMapping("/add-task")
    public ResponseEntity<String> addTask(@CookieValue(value = "token") String token,
                                          String title, String description,
                                          @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date start,
                                          int duration, int teamId) {
        WorkerEntity manager = persist.getWorkerByToken(token);
        if (manager == null) {
            return ResponseEntity.status(403).body("Not authenticated");
        }

        boolean isRelatedTeam = persist.getWorkersByManager(manager.getId()).stream()
                .anyMatch(worker -> worker.getTeamEntity() != null && worker.getTeamEntity().getId() == teamId);
        if (!isRelatedTeam) {
            return ResponseEntity.status(403).body("Team is not related to this user");
        }

        TeamEntity teamEntity = persist.loadObject(TeamEntity.class, teamId);
        if (teamEntity == null) {
            return ResponseEntity.badRequest().body("Team not found");
        }

        TaskEntity task = new TaskEntity(title, description, start, duration);
        task.setTeamEntity(teamEntity);
        this.persist.addTask(task);
        return ResponseEntity.ok("Task saved successfully");
    }
    @RequestMapping("/get-tasks")
    public List<TaskModel> getTasks (@CookieValue(value = "token") String token) {
        List<TaskEntity> allEntities = persist.loadList(TaskEntity.class);

        List<TaskModel> allTasks = allEntities.stream()
                .map(entity -> new TaskModel(entity))
                .toList();

        return allTasks;
    }
}
