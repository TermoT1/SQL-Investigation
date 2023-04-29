package ru.sqlinvestigation.RestAPI.services.userDB.JWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.sqlinvestigation.RestAPI.models.userDB.RefreshToken;
import ru.sqlinvestigation.RestAPI.repositories.userDB.RefreshTokenRepository;
import ru.sqlinvestigation.RestAPI.repositories.userDB.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Autowired

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public List<RefreshToken> findAll() throws EntityNotFoundException {
        return refreshTokenRepository.findAll();
    }

    public String getRefreshTokenByUserId(Long id) throws EntityNotFoundException {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(id);
        if (refreshToken.isEmpty())
            throw new RuntimeException("Not found");
        return refreshToken.get().getRefresh_token();
    }

    public void create(RefreshToken refreshToken) {
        if (!existsByUserId(refreshToken.getUser_id()))
            throw new NotFoundException(String.format("Row with id %s not found", refreshToken.getUser_id()));
        refreshTokenRepository.save(refreshToken);
    }

    public void update(RefreshToken refreshToken) {
        // проверяем, существуют ли записи с таким идентификаторами
        if (!existsByUserId(refreshToken.getUser_id()))
            throw new NotFoundException(String.format("Row with id %s not found", refreshToken.getUser_id()));
        refreshTokenRepository.save(refreshToken);
    }

    @Transactional(transactionManager = "gameTransactionManager")
    public void delete(long id) {
        // проверяем, существуют ли записи с таким идентификаторами
        if (!existsByUserId(id))
            throw new NotFoundException(String.format("Row with id %s not found", id));
        refreshTokenRepository.deleteById(id);
    }

    public boolean existsByUserId(long id) {
        return userRepository.existsById(id);
    }
}
