package BE.artifact.controller;

import BE.artifact.model.Roles;
import BE.artifact.repository.RolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("roles")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RoleController {

    private final RolesRepository rolesRepository;

    @PostMapping("add")
    public Roles addRole(@RequestBody Roles role){
        return rolesRepository.save(role);
    }

    @DeleteMapping("delete/{id}")
    public String deleteRole(@PathVariable Integer id) {
        rolesRepository.deleteById(id);
        return "Role " + id + " deleted";
    }
}
