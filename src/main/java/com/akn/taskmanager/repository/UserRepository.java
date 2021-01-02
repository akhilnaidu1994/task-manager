package com.akn.taskmanager.repository;

import com.akn.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    public User findByEmail(String email);

    @Query("SELECT u from User u WHERE lower(CONCAT(u.firstName, ' ', u.lastName, ' ', u.email)) LIKE lower(concat('%',?1,'%'))")
    public List<User> searchUsers(String searchText);
}
