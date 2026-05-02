package re.employeedepartment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private final DepartmentRepository departmentRepository;

    @GetMapping("/employees")
    public String getEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword,
            Model model
    ) {

        if (page < 0) page = 0;

        Sort sort = sortDir.equals("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(page, 5, sort);

        Page<Employee> employeePage;

        if (keyword != null && !keyword.isEmpty()) {
            employeePage = employeeRepository
                    .findByNameContainingIgnoreCase(keyword, pageable);
        } else {
            employeePage = employeeRepository.findAll(pageable);
        }

        if (page >= employeePage.getTotalPages() && employeePage.getTotalPages() > 0) {
            page = employeePage.getTotalPages() - 1;
            pageable = PageRequest.of(page, 5, sort);

            if (keyword != null && !keyword.isEmpty()) {
                employeePage = employeeRepository
                        .findByNameContainingIgnoreCase(keyword, pageable);
            } else {
                employeePage = employeeRepository.findAll(pageable);
            }
        }

        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir",
                sortDir.equals("asc") ? "desc" : "asc");

        model.addAttribute("keyword", keyword);

        // 🔥 QUAN TRỌNG cho form
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());

        return "employee-list";
    }


    @PostMapping("/employees/save")
    public String saveEmployee(
            @ModelAttribute Employee employee,
            @RequestParam("file") MultipartFile file
    ) throws IOException {

        if (!file.isEmpty()) {
            String originalName = file.getOriginalFilename();
            String extension = originalName.substring(originalName.lastIndexOf("."));
            String newFileName = System.currentTimeMillis() + extension;

            String uploadDir = "uploads/";
            File uploadPath = new File(uploadDir);

            if (!uploadPath.exists()) uploadPath.mkdirs();

            file.transferTo(new File(uploadDir + newFileName));

            employee.setAvatar(newFileName);
        }

        if (employee.getDepartment() != null &&
                employee.getDepartment().getId() != null) {

            Department dept = departmentRepository
                    .findById(employee.getDepartment().getId())
                    .orElse(null);

            employee.setDepartment(dept);
        }

        employeeRepository.save(employee);

        return "redirect:/employees";
    }
}