package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.Role;
import com.pentyugov.wflow.core.dto.RoleDto;
import com.pentyugov.wflow.core.service.RoleService;
import com.pentyugov.wflow.web.exception.ValidationException;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
public class RoleController extends AbstractController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllPositions() {
        List<RoleDto> roles = new ArrayList<>();
        roleService.getAll().forEach(role ->
                roles.add(roleService.convert(role)));
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> addNewRole(@RequestBody RoleDto roleDto) {
        Role role = roleService.add(roleDto);
        return new ResponseEntity<>(roleService.convert(role), HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateRole(@RequestBody RoleDto roleDto) {
        Role role = roleService.update(roleDto);
        return new ResponseEntity<>(roleService.convert(role), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE')")
    public ResponseEntity<HttpResponse> deleteRole(@PathVariable String id) throws ValidationException {
        Role role = roleService.getById(UUID.fromString(id));
        if (roleService.delete(role)) {
            String message = "Role was deleted";
            return response(HttpStatus.OK, message);
        } else {
            throw new ValidationException("There is users with this role");
        }

    }

}
