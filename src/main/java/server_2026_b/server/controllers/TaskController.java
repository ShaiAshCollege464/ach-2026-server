package server_2026_b.server.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server_2026_b.server.entities.TaskEntity;
import server_2026_b.server.service.Persist;

import javax.annotation.PostConstruct;
import java.util.Date;

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
    public void addTask(String title, String description,
                        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date start, int duration) {

        System.out.println(start);
        System.out.println(duration);
        TaskEntity task = new TaskEntity(title, description, start, duration);
        this.persist.addTask(task);

    }
}
