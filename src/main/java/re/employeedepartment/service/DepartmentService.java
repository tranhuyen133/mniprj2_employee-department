package re.employeedepartment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.employeedepartment.controller.DepartmentRepository;
import re.employeedepartment.entity.Department;
import re.employeedepartment.entity.Employee;

import re.employeedepartment.repository.EmployeeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public int deleteDepartment(Long id) {

        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phòng ban"));

        List<Employee> employees = employeeRepository.findAll()
                .stream()
                .filter(e -> e.getDepartment() != null &&
                        e.getDepartment().getId().equals(id))
                .toList();

        // update FK về null
        for (Employee e : employees) {
            e.setDepartment(null);
        }

        employeeRepository.saveAll(employees);

        departmentRepository.delete(dept);

        return employees.size();
    }
}