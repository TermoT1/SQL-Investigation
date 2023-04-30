package ru.sqlinvestigation.RestAPI.repositories.userDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sqlinvestigation.RestAPI.models.userDB.StoryImage;


@Transactional(transactionManager = "userTransactionManager")
@Repository
public interface StoryImageRepository extends JpaRepository<StoryImage, Long> {
}
