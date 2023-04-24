package CargarValores.demo.comanys;

import CargarValores.demo.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService service;

    @GetMapping("/")
    public ResponseEntity<List<Company>> findAll() throws JsonProcessingException {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping("/newCompany")
    public ResponseEntity<String> save(@RequestBody Company company) throws ResourceNotFoundException, JsonProcessingException {
        service.save(company);
        return ResponseEntity.ok("Se agregó a la base de datos");
    }

    @PutMapping("/modifyCompany")
    public ResponseEntity<String> modify(@RequestBody Company company) throws JsonProcessingException {
        service.modify(company);
        return ResponseEntity.ok("Se modifico el usuario");
    }


    @DeleteMapping("/deleteCompany")
    public ResponseEntity<String>  delete(@RequestBody int id) throws ResourceNotFoundException {
        service.delete(id);
        return ResponseEntity.ok("Se eliminó de la base de datos la compañia con id: " + id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getById(@PathVariable int id) throws JsonProcessingException {
        return ResponseEntity.ok(service.getById(id));
    }
}
