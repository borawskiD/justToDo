package com.example.justtodo.Panel;

import com.example.justtodo.UserAuthorization.User;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class TaskDTO {

    private Long id;
    private String title;
    private String category;
    private String description;
    private boolean isDone;
    private LocalDateTime createdAt;
    private User user;

    public TaskDTO(Long id, String title, String category, String description, boolean isDone, User user) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.isDone = isDone;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public TaskDTO(TaskEntity task) {
        this.id = task.getId();
        this.title = task.getTitle();
        this.category = task.getCategory();
        this.description = task.getDescription();
        this.isDone = task.isDone();
    }

    public static List<TaskDTO> convertTaskList(List<TaskEntity> taskList){
        List<TaskDTO> taskDtoList = new LinkedList<>();
        taskList.forEach(task -> taskDtoList.add(new TaskDTO(task)));
        return taskDtoList;
    }

    public TaskDTO(String title, String category, String description, boolean isDone) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.isDone = isDone;
        this.createdAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", isDone=" + isDone +
                ", createdAt=" + createdAt +
                ", user=" + user +
                '}';
    }
}

