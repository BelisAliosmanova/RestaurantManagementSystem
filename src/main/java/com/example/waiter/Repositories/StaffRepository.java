package com.example.waiter.Repositories;

import com.example.waiter.Entities.Staff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StaffRepository extends CrudRepository<Staff, Long> {
    @Query("SELECT u FROM Staff u WHERE u.username = :username")
    public Staff getStaffByUsername(@Param("username") String username);
    Staff findByUsername(String username);
}
