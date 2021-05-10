package be.pxl.paj.rest.repository;

import be.pxl.paj.rest.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByNameAndRole(String name, String role);
    List<Employee> findEmployeesByRole(String role);
}
