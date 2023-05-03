package ru.sqlinvestigation.RestAPI.controllers.userDB;

import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import ru.sqlinvestigation.RestAPI.models.userDB.JWT.JwtAuthentication;
import ru.sqlinvestigation.RestAPI.models.userDB.UserStats;
import ru.sqlinvestigation.RestAPI.services.userDB.UserStatsService;
import ru.sqlinvestigation.RestAPI.util.BindingResultChecker;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/userDB/user_stats_by_stories")
public class UserStatsController {
    private final UserStatsService userStatsService;

    @Autowired
    public UserStatsController(UserStatsService userStatsService) {
        this.userStatsService = userStatsService;
    }

    @GetMapping("/index")
    public List<UserStats> getDriverLicense() {
        return userStatsService.findAll();
    }

    @GetMapping("/findMyStats")
    public List<UserStats> findMyStats() throws Exception {
        // Получаем id авторизованного пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long userId  = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();

        return userStatsService.findByUserId(userId);
    }

    @GetMapping("/findByUserId/{id}")
    public List<UserStats> findByUserId(@PathVariable long id) throws Exception {
        return userStatsService.findByUserId(id);
    }

    @GetMapping("/findByStoryId/{id}")
    public List<UserStats> findByStoryId(@PathVariable long id) {
        return userStatsService.findAllByStoryId(id);
    }


    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid UserStats userStats, BindingResult bindingResult) {
        userStatsService.create(userStats, bindingResult);
        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @PostMapping("/saveMyStats")
//    public ResponseEntity<HttpStatus> saveMyStats(@RequestBody @Valid UserStats userStats, BindingResult bindingResult) {
//        //Получаем id авторизованного пользователя
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        long userIdAuth  = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
//        long userIdRequest = userStats.getUser_id();
//
//        if (userStats.getUser_id() == 0)
//            throw new RuntimeException("user id = null");
//
//        if (userIdAuth!=userIdRequest){
//            throw new RuntimeException("The given user id does not belong to you.");
//        }
//
//        userStatsService.create(userStats, bindingResult);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }

    @PostMapping("/update")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid UserStats userStats, BindingResult bindingResult) {
        userStatsService.update(userStats, bindingResult);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable long id) {
        userStatsService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
    }
}