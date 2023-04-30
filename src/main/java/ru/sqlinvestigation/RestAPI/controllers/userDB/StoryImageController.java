package ru.sqlinvestigation.RestAPI.controllers.userDB;

import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import ru.sqlinvestigation.RestAPI.models.userDB.StoryImage;
import ru.sqlinvestigation.RestAPI.services.userDB.StoryImageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/userDB/stories_images")
public class StoryImageController {
    private final StoryImageService storyImageService;

    @Autowired
    public StoryImageController(StoryImageService storyImageService) {
        this.storyImageService = storyImageService;
    }

    @GetMapping("/listID")
    public List<Long> findListId(){
        return storyImageService.findListId();
    }

    @GetMapping("/findByStoryId/{id}")
    public ResponseEntity<InputStreamResource> findByStoryId(@PathVariable long id) throws Exception {
        StoryImage storyImage = storyImageService.findByStoryId(id);
        return ResponseEntity.ok()
                .header("fileName", storyImage.getOriginal_file_name())
                .contentType(MediaType.valueOf(storyImage.getContent_type()))
                .contentLength(storyImage.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(storyImage.getBytes())));
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> create (@RequestBody MultipartFile file, long story_id) throws IOException {
        storyImageService.create(file, story_id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<HttpStatus> update(@RequestBody MultipartFile file, long story_id) throws IOException {
        storyImageService.update(file, story_id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable long id) {
        storyImageService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(ex.getMessage()));
    }
}