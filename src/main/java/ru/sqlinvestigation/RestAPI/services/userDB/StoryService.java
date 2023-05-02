package ru.sqlinvestigation.RestAPI.services.userDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.webjars.NotFoundException;
import ru.sqlinvestigation.RestAPI.dto.userDB.StoryDTO;
import ru.sqlinvestigation.RestAPI.models.userDB.Story;
import ru.sqlinvestigation.RestAPI.repositories.userDB.StoryRepository;
import ru.sqlinvestigation.RestAPI.util.BindingResultChecker;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class StoryService {
    private final StoryRepository storyRepository;
    private final BindingResultChecker bindingResultChecker;

    @Autowired
    public StoryService(StoryRepository storyRepository, BindingResultChecker bindingResultChecker) {
        this.storyRepository = storyRepository;
        this.bindingResultChecker = bindingResultChecker;
    }

    public boolean isAnswerCorrect(long userId, String answer, long storyId) {
        Optional<Story> optionalStory = storyRepository.findById(storyId);
        if(optionalStory.isEmpty()){
            throw new NotFoundException(String.format("Entity with id %s not found", storyId));
        }
        Story story = optionalStory.get();
        return story.getAnswer().equals(answer);
    }

    public List<Story> findAll() throws EntityNotFoundException {
        return storyRepository.findAll();
    }
    public StoryDTO findById(long id) throws EntityNotFoundException {
        Optional<Story> story = storyRepository.findById(id);
        if(story.isEmpty())
            throw new EntityNotFoundException("Not found");
        var str = story.get();
        StoryDTO storyDTO = new StoryDTO();

        storyDTO.setId(str.getId());
        storyDTO.setTitle(str.getTitle());
        storyDTO.setDifficulty(str.getDifficulty());
        storyDTO.setDescription(str.getDescription());
        storyDTO.setStory_text(str.getStory_text());
        return storyDTO;
    }

    public void create(Story story, BindingResult bindingResult) {
        bindingResultChecker.check(bindingResult);
        if (existsById(story.getId()))
            throw new NotFoundException(String.format("Row with id %s already exists", story.getId()));
//        story.setAnswer(passwordEncoder.encode(story.getAnswer()));
        storyRepository.save(story);
    }

    @Transactional(transactionManager = "userTransactionManager")
    public void update(Story story, BindingResult bindingResult) {
        bindingResultChecker.check(bindingResult);
        // проверяем, существуют ли записи с таким идентификаторами
        if (!existsById(story.getId()))
            throw new NotFoundException(String.format("Row with id %s was not found", story.getId()));
        storyRepository.save(story);
    }

    @Transactional(transactionManager = "userTransactionManager")
    public void delete(long id) {
        if (!existsById(id))
            throw new NotFoundException(String.format("Entity with id %s not found", id));
        storyRepository.deleteById(id);
    }
    public boolean existsById(long id) {
        return storyRepository.existsById(id);
    }
}
