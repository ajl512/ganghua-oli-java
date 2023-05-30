package com.gangyunshihua.repository;

import com.gangyunshihua.entity.DieselLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DieselLogRepository extends JpaRepository<DieselLog, Integer> {

    @Query("from DieselLog dl where dl.type = ?1 and dl.valid_time >= ?2 order by dl.id asc")
    List<DieselLog> findDieselLog(String type, Date minTime);
}
