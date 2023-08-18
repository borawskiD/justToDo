package com.example.justtodo.Panel;

import com.example.justtodo.UserAuthorization.User;
import com.example.justtodo.UserAuthorization.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    TaskRepository taskRepository;
    UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Optional<List<TaskEntity>> generateTasks(String username, boolean isDone){
        Optional<User> currentUser = userRepository.findByEmail(username);
        if (currentUser.isPresent()){
            List<TaskEntity> userTasks;
            User user = currentUser.get();
            if (isDone) userTasks = user.getTasks().stream()
                    .filter(TaskEntity::isDone)
                    .collect(Collectors.toList());
            else userTasks = user.getTasks().stream()
                    .filter(task -> !task.isDone())
                    .collect(Collectors.toList());
            userTasks.sort((a, b) -> (-1) *  a.getCreatedAt().compareTo(b.getCreatedAt()));
            return Optional.of(userTasks);
        }
        return Optional.empty();
    }

    public Optional<List<TaskEntity>> filterTasksByCategory(String category, String username){
        Optional<User> currentUser = userRepository.findByEmail(username);
        if (currentUser.isPresent()) {
            User user = currentUser.get();
            List<TaskEntity> userTasks = user.getTasks().stream()
                    .filter(task -> task.getCategory().equals(category))
                    .sorted((a, b) -> (-1) * a.getCreatedAt().compareTo(b.getCreatedAt()))
                    .collect(Collectors.toList());
            return Optional.of(userTasks);
        }
        return Optional.empty();
    }

    public boolean addNewTask(TaskDTO task, String username){
        Optional<User> user = userRepository.findByEmail(username);
        if (user.isEmpty()) return false;
        task.setUser(user.get());
        TaskEntity taskEntity = TaskDtoWrapper.convertDTO(task);
        taskRepository.save(taskEntity);
        return true;
    }
    public boolean removeTask(String rawTaskId){
        try{
            Long taskId = Long.valueOf(rawTaskId);
            Optional<TaskEntity> taskEntity = taskRepository.findById(taskId);
            if (taskEntity.isEmpty()) return false;
            taskRepository.delete(taskEntity.get());
        }catch (Exception e){
            System.out.println("Error during parsing data, check it out!");
            return false;
        }
        return false;
    }
    public Optional<TaskDTO> findTask(String rawTaskId, boolean isDone){
        try{
            Long taskId = Long.valueOf(rawTaskId);
            Optional<TaskEntity> taskEntity = taskRepository.findById(taskId);
            if (taskEntity.isEmpty()) return Optional.empty();
            TaskEntity task = taskEntity.get();
            TaskDTO taskDTO = new TaskDTO(task.getTitle(), task.getCategory(), task.getDescription(), isDone);
            System.out.println(taskDTO.getTitle());
            return Optional.of(taskDTO);
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public boolean setTaskToDone(String rawTaskId){
        try{
            Long taskId = Long.valueOf(rawTaskId);
            Optional<TaskEntity> taskEntity = taskRepository.findById(taskId);
            if (taskEntity.isEmpty()) return false;
            TaskEntity task = taskEntity.get();
            taskRepository.delete(task);
            task.setDone(true);
            taskRepository.save(task);
        }catch (Exception e){
            System.out.println("Error during parsing data, check it out!");
            return false;
        }
        return false;
    }
    public boolean editTask(String rawTaskId, TaskDTO editedTask){
        try{
            Optional<TaskEntity> taskEntity = taskRepository.findById(Long.valueOf(rawTaskId));
            if (taskEntity.isEmpty()) return false;
            TaskEntity task = taskEntity.get();
            editedTask.setUser(task.getUser());
            editedTask.setCreatedAt(task.getCreatedAt());
            editedTask.setId(task.getId());
            taskRepository.delete(taskEntity.get());
            TaskEntity newTask = TaskDtoWrapper.convertDTO(editedTask);
            taskRepository.save(newTask);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public void restoreTask(String taskId){
        try{
            Long id = Long.valueOf(taskId);

            Optional<TaskEntity> optionalTaskEntity = taskRepository.findById(id);
            if (optionalTaskEntity.isPresent()){
                TaskEntity taskBeforeEdit = optionalTaskEntity.get();
                taskRepository.delete(taskBeforeEdit);
                taskBeforeEdit.setDone(false);
                taskRepository.save(taskBeforeEdit);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
