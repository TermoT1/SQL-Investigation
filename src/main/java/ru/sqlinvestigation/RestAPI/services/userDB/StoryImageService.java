package ru.sqlinvestigation.RestAPI.services.userDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.webjars.NotFoundException;
import ru.sqlinvestigation.RestAPI.models.userDB.StoryImage;
import ru.sqlinvestigation.RestAPI.repositories.userDB.StoryImageRepository;
import ru.sqlinvestigation.RestAPI.repositories.userDB.StoryRepository;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StoryImageService {
    private final StoryImageRepository storyImageRepository;

    private final StoryRepository storyRepository;

    @Autowired
    public StoryImageService(StoryImageRepository storyImageRepository, StoryRepository storyRepository) {
        this.storyImageRepository = storyImageRepository;
        this.storyRepository = storyRepository;
    }

    public List<Long> findListId() throws EntityNotFoundException {
        return storyImageRepository.findAll().stream().map(StoryImage::getId_stories).collect(Collectors.toList());
    }

    public StoryImage findByStoryId(long id) throws Exception {
        Optional<StoryImage> storyImage = storyImageRepository.findById(id);
        if (storyImage.isPresent()) {
            return storyImage.get();
        }
        throw new NotFoundException(String.format("Row with id %s was not found", id));
    }
    @Transactional(transactionManager = "userTransactionManager")
    public void create(MultipartFile file, long id) throws IOException {
        if (existsById(id))
            throw new NotFoundException("Row with id %s already exists");
        if (!existsByStoryId(id))
            throw new NotFoundException(String.format("Row with id %s was not found", id));
        StoryImage storyImage = fileToStoryImage(file, id);
        storyImageRepository.save(storyImage);
    }

    @Transactional(transactionManager = "userTransactionManager")
    public void update(MultipartFile file, long id) throws IOException{
        if (!existsById(id))
            throw new NotFoundException(String.format("Row with id %s was not found", id));
        if (!existsByStoryId(id))
            throw new NotFoundException(String.format("Row with id %s was not found", id));
        StoryImage storyImage = fileToStoryImage(file, id);
        storyImageRepository.save(storyImage);
    }

    @Transactional(transactionManager = "userTransactionManager")
    public void delete(long id) {
        if (!existsById(id)) throw new NotFoundException(String.format("Entity with id %s not found", id));
        storyImageRepository.deleteById(id);
    }

    private StoryImage fileToStoryImage(MultipartFile file, long id) throws IOException {
        StoryImage storyImage;
        if (file.getSize() == 0) {
            throw new RuntimeException("file size = 0");
        }
        storyImage = getFileAttributes(file);
        storyImage.setId_stories(id);
        return storyImage;
    }
    private StoryImage getFileAttributes(MultipartFile file) throws IOException {
        StoryImage storyImage = new StoryImage();
        storyImage.setOriginal_file_name(file.getOriginalFilename());
        storyImage.setContent_type(file.getContentType());
        storyImage.setSize(file.getSize());
        storyImage.setBytes(file.getBytes());
        return storyImage;
    }

    private boolean existsById(long id) {
        return storyImageRepository.existsById(id);
    }

    private boolean existsByStoryId(long id) {
        return storyRepository.existsById(id);
    }
}
