package ro.teamnet.zth.app.service;

import ro.teamnet.zth.app.dao.DepartmentDao;
import ro.teamnet.zth.app.domain.Department;

import java.util.List;

/**
 * Created by Mi on 5/8/2015.
 */
public class DepatmentServiceImpl implements DepartmentService {
    @Override
    public List<Department> findAllDepartments() {
        DepartmentDao departmentDao = new DepartmentDao();
        return departmentDao.getAllDepartments();
    }

    @Override
    public Department findOneDepartment(int id) {
        DepartmentDao departmentDao = new DepartmentDao();
        return departmentDao.getDepartmentById(id);
    }
}
