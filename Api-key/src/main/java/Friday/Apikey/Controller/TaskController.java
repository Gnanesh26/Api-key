package Friday.Apikey.Controller;


import Friday.Apikey.Entity.Task;
import Friday.Apikey.Entity.UserInfo;
import Friday.Apikey.Service.ApiKeyService;
import Friday.Apikey.Service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;



import java.util.List;


//@RestController
@RestController
@Slf4j
public class TaskController {

//public class TaskController {
    @Autowired
    private TaskService taskService;
    private final ApiKeyService apiKeyService;

    @Autowired
    public TaskController(TaskService taskService, ApiKeyService apiKeyService) {
        this.taskService = taskService;
        this.apiKeyService = apiKeyService;
    }
    @PreAuthorize(" hasAuthority('developer')")
    @GetMapping("/tasks")
    public List<Task> getAllTasks(HttpServletRequest request) {
        String apiKey = request.getHeader("API-Key");
        if (apiKey == null || !apiKeyService.isValidApiKey(apiKey)) {
            throw new UnauthorizedException("Invalid API key");
        }
        return taskService.getAllTasks();
    }



    @PreAuthorize("hasAuthority('support')")
    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id);
    }
    @PostMapping("/add")
    public String addNewUser(@RequestBody UserInfo userInfo){
        return taskService.addUser(userInfo);
    }

}


