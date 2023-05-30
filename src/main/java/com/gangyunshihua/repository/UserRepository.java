package com.gangyunshihua.repository;

import com.gangyunshihua.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("from User u where u.open_id = ?1")
    User findByOpenId(String openId);
}
