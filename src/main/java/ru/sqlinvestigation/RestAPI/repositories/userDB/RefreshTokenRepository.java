package ru.sqlinvestigation.RestAPI.repositories.userDB;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sqlinvestigation.RestAPI.models.userDB.RefreshToken;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
