package re.employeedepartment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import re.employeedepartment.entity.Department;
import re.employeedepartment.entity.Employee;
import re.employeedepartment.repository.EmployeeRepository;

import java.io.File;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public String getAllEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "employee-list";
    }
    @GetMapping("/employees/create")
    public String showCreateForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        return "employee-form";
    }
    @PostMapping("/employees/save")
    public String saveEmployee(
            @ModelAttribute Employee employee,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        // 1. Xử lý upload file
        if (!file.isEmpty()) {

            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));

            String newFileName = System.currentTimeMillis() + extension;

            String uploadDir = "uploads/";
            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            file.transferTo(new File(uploadDir + newFileName));

            // chỉ lưu tên file
            employee.setAvatar(newFileName);
        }

        // 2. Fix binding department (tránh null)
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            Department dept = departmentRepository
                    .findById(employee.getDepartment().getId())
                    .orElse(null);
            employee.setDepartment(dept);
        }

        employeeRepository.save(employee);

        return "redirect:/employees";
    }
}
