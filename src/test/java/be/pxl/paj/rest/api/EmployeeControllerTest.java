package be.pxl.paj.rest.api;

import be.pxl.paj.rest.api.EmployeeController;
import be.pxl.paj.rest.domain.Employee;
import be.pxl.paj.rest.builder.EmployeeBuilder;
import be.pxl.paj.rest.exception.EmployeeNotFoundException;
import be.pxl.paj.rest.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.pxl.paj.rest.builder.EmployeeBuilder.EMPLOYEE_ID;
import static be.pxl.paj.rest.builder.EmployeeBuilder.EMPLOYEE_NAME;
import static be.pxl.paj.rest.builder.EmployeeBuilder.EMPLOYEE_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Om mockito mogelijk te maken in de test is deze extensie
@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

	private static final Long OTHER_EMPLOYEE_ID = 18L;
	@InjectMocks
	private EmployeeController employeeController;

	@Mock
	private EmployeeRepository employeeRepository;


	@Test
	public void testCreateEmployee() {
		when(employeeRepository.save(any(Employee.class))).thenAnswer(returnsFirstArg());

		Employee employee = EmployeeBuilder.anEmployee().build();

		ResponseEntity<Employee> response = employeeController.createEmployee(employee);

		assertEquals(response.getStatusCode(), HttpStatus.CREATED);
		assertTrue(response.getHeaders().getLocation().getPath().endsWith("/" + EMPLOYEE_ID));
		Employee responseBody = response.getBody();
		assertEquals(EMPLOYEE_ID, responseBody.getId());
		assertEquals(EMPLOYEE_NAME, responseBody.getName());
		assertEquals(EMPLOYEE_ROLE, responseBody.getRole());
	}

	@Test
	public void testFindById() {

		Employee employee = EmployeeBuilder.anEmployee().withId(EMPLOYEE_ID).withName(EMPLOYEE_NAME).withRole(EMPLOYEE_ROLE).build();
		when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));

		Employee response = employeeController.find(EMPLOYEE_ID);

		// Met verity ga je controleren of de verify is aangeroepen en of de findById is uitgevoerd
		Mockito.verify(employeeRepository).findById(EMPLOYEE_ID);
		assertEquals(EMPLOYEE_ID, response.getId());
		assertEquals(EMPLOYEE_NAME, response.getName());
		assertEquals(EMPLOYEE_ROLE, response.getRole());
	}

	@Test
	public void testFindByIdWithEmployeeNotFoundException() {

		when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.empty());

		assertThrows(EmployeeNotFoundException.class, () -> employeeController.find(EMPLOYEE_ID));

	}

	@Test
	public void testFindAll() {
		Employee employee1 = EmployeeBuilder.anEmployee().withId(EMPLOYEE_ID).build();
		Employee employee2 = EmployeeBuilder.anEmployee().withId(OTHER_EMPLOYEE_ID).build();

		when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee1, employee2));

		List<Employee> response = employeeController.all();

		assertEquals(2, response.size());
		assertEquals(response.stream().map(Employee::getId).collect(Collectors.toList()), Arrays.asList(EMPLOYEE_ID, OTHER_EMPLOYEE_ID));
	}
}
