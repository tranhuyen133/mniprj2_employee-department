package re.employeedepartment.controller;




import org.springframework.data.jpa.repository.JpaRepository;
import re.employeedepartment.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
