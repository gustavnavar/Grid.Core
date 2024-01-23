package me.agno.demo.controllers;

import lombok.RequiredArgsConstructor;
import me.agno.demo.model.Employee;
import me.agno.demo.repositories.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @GetMapping(value = {"/employee", "/Employee"})
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @PostMapping(value = {"/employee", "/Employee"})
    public ResponseEntity<Object> create(@RequestBody Employee employee) {

        try {
            Employee savedEmployee = employeeRepository.saveAndFlush(employee);
            return ResponseEntity.ok(savedEmployee.getEmployeeID());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage().replace('{', '(').replace('}', ')'));
        }
    }

    @GetMapping(value = {"/employee/{id}", "/Employee/{id}"})
    public ResponseEntity<Employee> get(@PathVariable int id) {

        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = {"/employee/{id}", "/Employee/{id}"})
    public ResponseEntity<Object> update(@RequestBody Employee employee, @PathVariable int id) {

        Optional<Employee> attachedEmployee = employeeRepository.findById(id);
        if (attachedEmployee.isEmpty())
            return ResponseEntity.notFound().build();

        try {
            employee.setPhoto(attachedEmployee.get().getPhoto());
            employeeRepository.saveAndFlush(employee);
            return ResponseEntity.noContent().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage().replace('{', '(').replace('}', ')'));
        }
    }

    @DeleteMapping(value = {"/employee/{id}", "/Employee/{id}"})
    public ResponseEntity<Object> delete(@PathVariable int id) {

        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty())
            return ResponseEntity.notFound().build();

        try {
            employeeRepository.delete(employee.get());
            employeeRepository.flush();
            return ResponseEntity.noContent().build();
        }
        catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(e.getMessage().replace('{', '(').replace('}', ')'));
        }
    }
}
