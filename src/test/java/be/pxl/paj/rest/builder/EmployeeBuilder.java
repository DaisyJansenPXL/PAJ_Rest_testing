package be.pxl.paj.rest.builder;

import be.pxl.paj.rest.domain.Employee;

public final class EmployeeBuilder {

	public static final Long EMPLOYEE_ID = 103L;
	public static final String EMPLOYEE_NAME = "Daffy Duck";
	public static final String EMPLOYEE_ROLE = "Developer";
	private Long id = EMPLOYEE_ID;
	private String name = EMPLOYEE_NAME;
	private String role = EMPLOYEE_ROLE;

	private EmployeeBuilder() {}

	public static EmployeeBuilder anEmployee() { return new EmployeeBuilder(); }

	public EmployeeBuilder withId(Long id) {
		this.id = id;
		return this;
	}

	public EmployeeBuilder withName(String name) {
		this.name = name;
		return this;
	}

	public EmployeeBuilder withRole(String role) {
		this.role = role;
		return this;
	}

	public Employee build() {
		Employee employee = new Employee();
		employee.setId(id);
		employee.setName(name);
		employee.setRole(role);
		return employee;
	}
}
