package re.employeedepartment.spec;

import org.springframework.data.jpa.domain.Specification;
import re.employeedepartment.entity.Employee;

public class EmployeeSpecification {

    public static Specification<Employee> filter(
            String name,
            Long departmentId,
            Integer minAge,
            Integer maxAge
    ) {
        return (root, query, cb) -> {

            var predicate = cb.conjunction(); // true mặc định

            // 1. Lọc theo tên (LIKE, ignore case)
            if (name != null && !name.isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + name.toLowerCase() + "%"
                        )
                );
            }

            // 2. Lọc theo phòng ban
            if (departmentId != null) {
                predicate = cb.and(predicate,
                        cb.equal(
                                root.get("department").get("id"),
                                departmentId
                        )
                );
            }

            // 3. Tuổi >= minAge
            if (minAge != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("age"),
                                minAge
                        )
                );
            }

            // 4. Tuổi <= maxAge
            if (maxAge != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(
                                root.get("age"),
                                maxAge
                        )
                );
            }

            return predicate;
        };
    }
}