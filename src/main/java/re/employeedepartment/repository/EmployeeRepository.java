package re.employeedepartment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import re.employeedepartment.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}