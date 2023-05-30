package com.gangyunshihua.repository;

import com.gangyunshihua.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("from Employee e where e.status = 1 and e.user_id = ?1 and e.driver_status in ?2 and e.supercargo_status in ?3")
    List<Employee> findEmployee(Integer userId, Integer[] driverStatusArray, Integer[] supercargoStatusArray);
}
