package ru.sqlinvestigation.RestAPI.repositories.userDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sqlinvestigation.RestAPI.models.userDB.UserStats;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserStatsRepository extends JpaRepository<UserStats, Long> {
    @Query("SELECT e FROM UserStats e WHERE e.user_id = :user_id")
    List<UserStats> findAllByUserId(long user_id);
    @Query("SELECT e FROM UserStats e WHERE e.story_id = :story_id")
    List<UserStats> findAllByStoryId(long story_id);
    @Query("SELECT e FROM UserStats e WHERE e.story_id = :story_id AND e.user_id = :user_id AND e.is_completed is false")
    List<UserStats> findNotEndStoryByStoryIdAndUserid(long story_id, long user_id);
    @Query("SELECT e FROM UserStats e WHERE e.story_id = :story_id AND e.user_id = :user_id")
    List<UserStats> findAllStatsByStoryId(long story_id, long user_id);
}
