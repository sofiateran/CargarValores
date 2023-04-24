package CargarValores.demo.users;

import CargarValores.demo.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/")
    public ResponseEntity<List<User>> findAll() throws JsonProcessingException {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/newUser")
    public ResponseEntity<String> save(@RequestBody User user) throws ResourceNotFoundException, JsonProcessingException {
        service.save(user);
        return ResponseEntity.ok("Se agregó a la base de datos");
    }

    @PutMapping("/modifyUser")
    public ResponseEntity<String> modify(@RequestBody User user) throws JsonProcessingException {
        service.modify(user);
        return ResponseEntity.ok("Se modifico el usuario");
    }


    @DeleteMapping("/deleteUser")
    public ResponseEntity<String>  delete(@RequestBody int id) throws ResourceNotFoundException {
        service.delete(id);
        return ResponseEntity.ok("Se eliminó de la base de datos el usuario con id: " + id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable int id) throws JsonProcessingException {
        return ResponseEntity.ok(service.getById(id));
    }
}
