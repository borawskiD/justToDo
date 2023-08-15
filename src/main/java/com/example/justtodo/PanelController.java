package com.example.justtodo;

import com.example.justtodo.Panel.TaskDTO;
import com.example.justtodo.Panel.TaskEntity;
import com.example.justtodo.Panel.TaskService;
import com.example.justtodo.UserAuthorization.UserService;
import org.springframework.scheduling.config.Task;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PanelController {
    TaskService taskService;
    UserService userService;
    private final int TASK_LIMIT = 12;
    private final int TASKS_PER_ROW = 4;

    public PanelController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String home(){
        return "loginForm.html";
    }
    @GetMapping("/")
    public String panel(Model model, Authentication authentication){
        Optional<List<TaskEntity>> tasks = taskService.generateTasks(authentication.getName());
        if (tasks.isPresent() && tasks.get().size() > 0){
            int length = tasks.get().size();
            List<TaskEntity> firstRow = new ArrayList<>();
            List<TaskEntity> middleRow = new ArrayList<>();
            List<TaskEntity> lastRow = new ArrayList<>();

            for (int i = 0; i < length; i++) {
                tasks.get().get(i).setCategory(tasks.get().get(i).getCategory().toUpperCase());
                if (i < TASKS_PER_ROW){
                    firstRow.add(tasks.get().get(i));
                    continue;
                }
                if (i < 2 * TASKS_PER_ROW){
                    middleRow.add(tasks.get().get(i));
                    continue;
                }
                if (i < 3 * TASKS_PER_ROW){
                    lastRow.add(tasks.get().get(i));
                }
            }

            model.addAttribute("firstRow", firstRow);
            model.addAttribute("middleRow", middleRow);
            model.addAttribute("lastRow", lastRow);
        }

        return "index.html";
    }
    @PostMapping("/addNewTask")
    public ModelAndView addNew(String title, String category, String description, Authentication authentication, ModelMap model){
        TaskDTO taskDTO = new TaskDTO(title,category,description,false);
        boolean isAdded = taskService.addNewTask(taskDTO, authentication.getName());
        model.addAttribute("addedSuccessfully",String.valueOf(isAdded));
        return new ModelAndView("redirect:/", model);
    }
    @PostMapping("/editTask")
    public ModelAndView testing(String action, String taskId, ModelMap model, RedirectAttributes redirectAttributes){
        switch (action){
            case "done":
                taskService.setTaskToDone(taskId);
                break;
            case "remove":
                taskService.removeTask(taskId);
                break;
            case "edit":
                Optional<TaskDTO> task = taskService.findTask(taskId);
                if(task.isEmpty()) return new ModelAndView("redirect:/", model);
                TaskDTO taskDTO = task.get();
                redirectAttributes.addFlashAttribute("taskCategory", taskDTO.getCategory());
                redirectAttributes.addFlashAttribute("taskTitle", taskDTO.getTitle());
                redirectAttributes.addFlashAttribute("taskDescription", taskDTO.getDescription());
                redirectAttributes.addFlashAttribute("taskId", taskId);

                return new ModelAndView("redirect:/replaceTask", model);
        }
        return new ModelAndView("redirect:/", model);
    }
    @GetMapping("/replaceTask")
    public String editTask(Model model){
        String category = (String) model.asMap().get("taskCategory");
        return "editTaskForm.html";
    }
    @PostMapping("/replaceTask")
    public ModelAndView finishEditing(String title, String category, String description, String task_id, ModelMap model){
        TaskDTO taskDTO = new TaskDTO(title,category, description, false);
        taskService.editTask(task_id, taskDTO);
        return new ModelAndView("redirect:/", model);
    }

}
