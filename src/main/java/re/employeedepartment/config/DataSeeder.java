package re.employeedepartment.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import re.employeedepartment.controller.DepartmentRepository;
import re.employeedepartment.entity.Department;
import re.employeedepartment.entity.Employee;
import re.employeedepartment.entity.Status;
import re.employeedepartment.repository.EmployeeRepository;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void run(String... args) {

        if (departmentRepository.count() == 0) {

            Department it = Department.builder()
                    .name("IT")
                    .location("Hà Nội")
                    .build();

            Department hr = Department.builder()
                    .name("HR")
                    .location("Đà Nẵng")
                    .build();

            departmentRepository.save(it);
            departmentRepository.save(hr);

            Employee e1 = Employee.builder()
                    .name("Nguyễn Văn A")
                    .age(25)
                    .avatar("a.jpg")
                    .status(Status.ACTIVE)
                    .department(it)
                    .build();

            Employee e2 = Employee.builder()
                    .name("Trần Thị B")
                    .age(28)
                    .avatar("b.jpg")
                    .status(Status.INACTIVE)
                    .department(hr)
                    .build();

            employeeRepository.save(e1);
            employeeRepository.save(e2);
        }
    }
}
