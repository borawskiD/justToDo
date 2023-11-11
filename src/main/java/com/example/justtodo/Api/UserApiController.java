package com.example.justtodo.Api;

import com.example.justtodo.Panel.TaskDTO;
import com.example.justtodo.Panel.TaskEntity;
import com.example.justtodo.Panel.TaskRepository;
import com.example.justtodo.UserAuthorization.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {
    UserRepository userRepository;
    TaskRepository taskRepository;

    public UserApiController(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/{id}")
    public TaskDTO getTask(@PathVariable Long id){
       try{
           return new TaskDTO(taskRepository.findById(id).get());
       }catch(Exception e){
           throw new TaskNotFoundException(id);
       }
    }

    @GetMapping("/tasks")
    List<TaskDTO> all() {
        List<TaskEntity> taskList = (List<TaskEntity>)taskRepository.findAll();
        return TaskDTO.convertTaskList(taskList);
    }

    @PostMapping("/tasks")
    TaskDTO newTask(@RequestBody TaskEntity newEmployee) {
        return new TaskDTO(taskRepository.save(newEmployee));
    }


    @PutMapping("/tasks/{id}")
    TaskDTO replaceTask(@RequestBody TaskEntity newTask, @PathVariable Long id) {
        if (taskRepository.findById(id).isPresent()) {
            TaskEntity taskToChange = taskRepository.findById(id).get();
            taskToChange.setUser(newTask.getUser());
            taskToChange.setTitle(newTask.getTitle());
            taskToChange.setDone(newTask.isDone());
            taskToChange.setDescription(newTask.getDescription());
            taskToChange.setCategory(newTask.getCategory());
            taskToChange.setCreatedAt(newTask.getCreatedAt());
            taskRepository.delete(taskRepository.findById(id).get());
            return new TaskDTO(taskRepository.save(taskToChange));
        } else {
            return new TaskDTO(taskRepository.save(newTask));
        }

    }
    @DeleteMapping("/tasks/{id}")
    void replaceTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }

}
