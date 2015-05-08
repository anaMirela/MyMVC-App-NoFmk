package ro.teamnet.zth.app.service;

import ro.teamnet.zth.app.domain.Department;
import java.util.List;

/**
 * Created by Mi on 5/8/2015.
 */
public interface DepartmentService {
    List<Department> findAllDepartments();
    Department findOneDepartment(int id);
}
