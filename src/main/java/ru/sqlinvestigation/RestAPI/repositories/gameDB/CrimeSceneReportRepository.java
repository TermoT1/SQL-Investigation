package ru.sqlinvestigation.RestAPI.repositories.gameDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sqlinvestigation.RestAPI.models.gameDB.CrimeSceneReport;


@Repository
public interface CrimeSceneReportRepository extends JpaRepository<CrimeSceneReport, Long> {
}
