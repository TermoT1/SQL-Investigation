package ru.sqlinvestigation.RestAPI.controllers.userDB;//package ru.sqlinvestigation.RestAPI.controllers.userDB;

import org.modelmapper.ModelMapper;
import org.springdoc.api.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import ru.sqlinvestigation.RestAPI.dto.userDB.UserDTO;
import ru.sqlinvestigation.RestAPI.models.userDB.Role;
import ru.sqlinvestigation.RestAPI.models.userDB.Story;
import ru.sqlinvestigation.RestAPI.models.userDB.User;
import ru.sqlinvestigation.RestAPI.services.userDB.UserDetailsServiceImpl;
import ru.sqlinvestigation.RestAPI.services.userDB.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/userDB/user")
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserDetailsServiceImpl userDetailsService, UserService userService, ModelMapper modelMapper) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/index")
    public List<User> get() {
        return userService.findAll();
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> performRegistration(@RequestBody @Valid UserDTO userDTO,
                                                          BindingResult bindingResult) throws Exception {
        if (userService.validate(userDTO.getUsername())){
            throw new Exception("A person with this username already exists!");
        }
        User user = convertToUser(userDTO);
        user.setRole(String.valueOf(Role.USER.getAuthority()));

        userService.create(user, bindingResult);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<HttpStatus> performLogin(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        User user = convertToUser(userDTO);
        userService.update(user, bindingResult);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteDriverLicense(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMessage(exception.getMessage()));
    }

    public User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        return this.modelMapper.map(userDTO, User.class);
    }
}
