package be.pxl.paj.rest.api;

import be.pxl.paj.rest.builder.EmployeeBuilder;
import be.pxl.paj.rest.domain.Employee;
import be.pxl.paj.rest.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
// webmvc test maken en maken voor onderstaande controller
@WebMvcTest(value = EmployeeController.class)
public class EmployeeControllerTestAlt {
	private static final Long EMPLOYEE_ID = 14L;
	private static final Long OTHER_EMPLOYEE_ID = 18L;
	private static final String EMPLOYEE_NAME = "Daffy Duck";
	private static final String EMPLOYEE_ROLE = "animated cartoon character";

	@Autowired
	private MockMvc mockMvc;

	// mockbean omdat het een bean moet zijn die geinjecteerd moet worden op de controller
	@MockBean
	private EmployeeRepository employeeRepository;


	@Test
	public void testCreateEmployee() throws Exception {
		// maakt niet uit welk object van de employee klasse wordt gesaved, argument er uit halen door iom.getArgument op eerste plaats
		when(employeeRepository.save(any(Employee.class))).thenAnswer(iom -> {
			Employee createdEmployee = iom.getArgument(0);
			createdEmployee.setId(EMPLOYEE_ID);
			return createdEmployee;
		});

		// post
	    mockMvc.perform(MockMvcRequestBuilders.post("/employees")
				// json format
				.contentType(MediaType.APPLICATION_JSON)
				// content meegeven als een string
				.content("{\"name\": \"Demo\", \"role\": \"developer\"}")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.header().string("Location", "/employees/" + EMPLOYEE_ID))
				.andExpect(jsonPath("$.id").value(EMPLOYEE_ID))
				.andExpect(jsonPath("$.name").value("Demo"))
				.andExpect(jsonPath("$.role").value("developer"));
	}


	@Test
	public void testFindById() throws Exception {

		Employee employee = EmployeeBuilder.anEmployee().withId(EMPLOYEE_ID).withName(EMPLOYEE_NAME).withRole(EMPLOYEE_ROLE).build();
		when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.of(employee));

		mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", EMPLOYEE_ID)
				.accept(MediaType.APPLICATION_JSON))
				// http status testen
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(EMPLOYEE_ID))
				.andExpect(jsonPath("$.name").value(EMPLOYEE_NAME))
				.andExpect(jsonPath("$.role").value(EMPLOYEE_ROLE));
	}

	@Test
	public void testFindByIdWithEmployeeNotFoundException() throws Exception {

		// not found testen
		when(employeeRepository.findById(EMPLOYEE_ID)).thenReturn(Optional.empty());

		// mockmvc makkelijkste om rest endpoints te testen
		mockMvc.perform(MockMvcRequestBuilders.get("/employees/{id}", EMPLOYEE_ID)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

	}

}
