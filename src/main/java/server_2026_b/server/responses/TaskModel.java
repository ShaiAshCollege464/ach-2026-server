package server_2026_b.server.responses;


import server_2026_b.server.entities.TaskEntity;

import java.util.Date;

    public class TaskModel {
        private int id;
        private String title;
        private String details;
        private Date start;
        private int hoursEstimation;
        private String teamName;

        public TaskModel() {}

        public TaskModel(TaskEntity task) {
            this.id = task.getId();
            this.title = task.getTitle();
            this.details = task.getDetails();
            this.start = task.getStart();
            this.hoursEstimation = task.getHoursEstimation();
            if (task.getTeamEntity() != null) {
                this.teamName = task.getTeamEntity().getName();
            }
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getDetails() { return details; }
        public void setDetails(String details) { this.details = details; }

        public Date getStart() { return start; }
        public void setStart(Date start) { this.start = start; }

        public int getHoursEstimation() { return hoursEstimation; }
        public void setHoursEstimation(int hoursEstimation) { this.hoursEstimation = hoursEstimation; }

        public String getTeamName() { return teamName; }
        public void setTeamName(String teamName) { this.teamName = teamName; }
    }


