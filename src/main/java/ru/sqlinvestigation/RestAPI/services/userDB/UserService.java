package ru.sqlinvestigation.RestAPI.services.userDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.webjars.NotFoundException;
import ru.sqlinvestigation.RestAPI.models.userDB.RefreshToken;
import ru.sqlinvestigation.RestAPI.models.userDB.User;
import ru.sqlinvestigation.RestAPI.repositories.userDB.RefreshTokenRepository;
import ru.sqlinvestigation.RestAPI.repositories.userDB.UserRepository;
import ru.sqlinvestigation.RestAPI.util.BindingResultChecker;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final RefreshTokenRepository refreshTokenRepo;
    private final BindingResultChecker bindingResultChecker;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RefreshTokenRepository refreshTokenRepo,
                       BindingResultChecker bindingResultChecker, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.refreshTokenRepo = refreshTokenRepo;
        this.bindingResultChecker = bindingResultChecker;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> getById(Long id) throws NotFoundException {
        return userRepository.findById(id);
    }

    public Optional<User> getByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() throws EntityNotFoundException {
        return userRepository.findAll();
    }

    @Transactional(transactionManager = "userTransactionManager")
    public void create(User user, BindingResult bindingResult) {
        bindingResultChecker.check(bindingResult);
        if (existsById(user.getId()))
            throw new NotFoundException(String.format("Row with id %s already exists", user.getId()));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        refreshTokenRepo.save( new RefreshToken(userRepository.findByUsername(user.getUsername())
                .get().getId(),"ad"));
    }

    @Transactional(transactionManager = "userTransactionManager")
    public void update(User user, BindingResult bindingResult) {
        bindingResultChecker.check(bindingResult);
        // проверяем, существуют ли записи с таким идентификаторами
        if (!existsById(user.getId()))
            throw new NotFoundException(String.format("Row with id %s was not found", user.getId()));

        user.setRole(userRepository.findById(user.getId()).get().getRole());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional(transactionManager = "userTransactionManager")
    public void delete(long id) {
        if (!existsById(id))
            throw new NotFoundException(String.format("Entity with id %s not found", id));
        refreshTokenRepo.deleteById(id);
        userRepository.deleteById(id);
    }
    public boolean existsById(long id ) {
        return userRepository.existsById(id);
    }

    public boolean validate(String username) {
        return userRepository.existsByUsername(username);
    }
}
