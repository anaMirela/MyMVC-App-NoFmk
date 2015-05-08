package ro.teamnet.zth.app.service;

import ro.teamnet.zth.app.domain.Employee;

import java.util.List;

/**
 * Created by Mi on 5/7/2015.
 */
public interface EmployeeService {
    List<Employee> findAllEmployees();
    Employee findOneEmployee(int id);
}
