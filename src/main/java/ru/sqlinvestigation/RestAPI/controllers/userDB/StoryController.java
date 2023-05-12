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
import ru.sqlinvestigation.RestAPI.dto.userDB.Answer;
import ru.sqlinvestigation.RestAPI.dto.userDB.StoryDTO;
import ru.sqlinvestigation.RestAPI.models.userDB.JWT.JwtAuthentication;
import ru.sqlinvestigation.RestAPI.models.userDB.Story;
import ru.sqlinvestigation.RestAPI.services.userDB.StoryService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/userDB/stories")
public class StoryController {
    private final StoryService storyService;

    @Autowired
    public StoryController(StoryService storyService) {
        this.storyService = storyService;
    }

    @GetMapping("/index")
    public List<Story> get() {
        return storyService.findAll();
    }

    @GetMapping("/listStory")
    public List<StoryDTO> getListStoryDTO() {
        return storyService.findAllDTO();
    }

    @GetMapping("/getById/{id}")
    public StoryDTO getById(@PathVariable long id) {
        return storyService.findById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody @Valid Story story, BindingResult bindingResult) {
        long id = storyService.create(story, bindingResult);
        return ResponseEntity.ok(String.format("id: %s",id));
    }

    @PostMapping("/update")
    public ResponseEntity<HttpStatus> update(@RequestBody @Valid Story story, BindingResult bindingResult) {
        storyService.update(story, bindingResult);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/checkAnswer/{storyId}")
    public ResponseEntity<String> checkAnswer(@PathVariable long storyId, @RequestBody Answer answer) {
        if (answer.getAnswer().isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Answer = null");
        // Получаем id авторизованного пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        long userId  = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        if(!storyService.isAnswerCorrect(userId, answer.getAnswer(), storyId))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ответ неправильный!");;
        return ResponseEntity.status(HttpStatus.OK).body("Ответ верный!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable long id) {
        storyService.delete(id);
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