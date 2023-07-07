package Friday.Apikey.Controller;


import Friday.Apikey.Entity.Task;
import Friday.Apikey.Entity.UserInfo;
import Friday.Apikey.Service.ApiKeyService;
import Friday.Apikey.Service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;



import java.util.List;

//@RestController
//public class TaskController {
//    private final TaskService taskService;
//
//    @Autowired
//    public TaskController(TaskService taskService) {
//        this.taskService = taskService;
//    }
//
//
//    @GetMapping("/tasks")
//    public List<Task> getAllTasks() {
//        return taskService.getAllTasks();
//    }
//
//    @GetMapping("/{id}")
//    public Task getTaskById(@PathVariable int id) {
//        return taskService.getTaskById(id);
//
//    }

@RestController
//@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    private final ApiKeyService apiKeyService;

    @Autowired
    public TaskController(TaskService taskService, ApiKeyService apiKeyService) {
        this.taskService = taskService;
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/tasks")
    public List<Task> getAllTasks(HttpServletRequest request) {
        String apiKey = request.getHeader("API-Key");
        if (apiKey == null || !apiKeyService.isValidApiKey(apiKey)) {
            throw new UnauthorizedException("Invalid API key");
        }
        return taskService.getAllTasks();
    }

//    @GetMapping("/{id}")
//    public Task getTaskById(@PathVariable int id, HttpServletRequest request) {
//        String apiKey = request.getHeader("API-Key");
//        if (apiKey == null || !apiKeyService.isValidApiKey(apiKey)) {
//            throw new UnauthorizedException("Invalid API key");
//        }
//        return taskService.getTaskById(id);
//    }


        @PreAuthorize("hasAuthority('developer')")
//    @PreAuthorize("hasRole('Tester')")

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable int id) {
        return taskService.getTaskById(id);
    }
    @PostMapping("/add")
    public String addNewUser(@RequestBody UserInfo userInfo){
        return taskService.addUser(userInfo);
    }

}


