package com.gangyunshihua.repository;

import com.gangyunshihua.entity.DieselFuture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DieselFutureRepository extends JpaRepository<DieselFuture, Integer> {

    @Query("from DieselFuture df where df.valid_time <= ?1")
    List<DieselFuture> findValid(Date nowDate);
}
