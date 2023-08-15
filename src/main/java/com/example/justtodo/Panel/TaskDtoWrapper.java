package com.example.justtodo.Panel;

public class TaskDtoWrapper {
    public static TaskEntity convertDTO(TaskDTO dto){
        TaskEntity task = new TaskEntity();
        task.setId(dto.getId());
        task.setUser(dto.getUser());
        task.setTitle(dto.getTitle());
        task.setDone(dto.isDone());
        task.setDescription(dto.getDescription());
        task.setCategory(dto.getCategory());
        task.setCreatedAt(dto.getCreatedAt());
        return task;
    }
}
