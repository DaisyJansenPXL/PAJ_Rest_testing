package be.pxl.paj.rest.api;

import be.pxl.paj.rest.domain.Employee;
import be.pxl.paj.rest.exception.EmployeeNotFoundException;
import be.pxl.paj.rest.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
class EmployeeController {

	@Autowired
	private EmployeeRepository employeeRepository;

	//endpoint employees die alle employees toont
	@GetMapping("/employees")
	public List<Employee> all() {

		return employeeRepository.findAll();
	}

	@PostMapping("/employees")
	// nieuwe data wordt meegegeven met RequestBody
	// ResponseEntity dat niets returnt kan met <Void>
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee newEmployee) {

		//save function zorgt voor genereren id
		newEmployee = employeeRepository.save(newEmployee);
		URI location = URI.create(String.format("/employees/%s", newEmployee.getId()));
		//body wordt opgevuld
		return ResponseEntity.created(location).body(newEmployee);
	}


	@GetMapping("/employees/{id}")
	public Employee find(@PathVariable Long id) {

		return employeeRepository.findById(id)
				.orElseThrow(() -> new EmployeeNotFoundException(id));
	}

	@PutMapping("/employees/{id}")
	public Employee updateEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {

		return employeeRepository.findById(id)
				.map(employee -> {
					employee.setName(newEmployee.getName());
					employee.setRole(newEmployee.getRole());
					return employeeRepository.save(employee);
				})
				.orElseThrow(() ->
						new EmployeeNotFoundException(id)
				);
	}

	@DeleteMapping("/employees/{id}")
	void deleteEmployee(@PathVariable Long id) {
		employeeRepository.deleteById(id);
	}
}
