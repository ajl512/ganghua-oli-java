package com.gangyunshihua.repository;

import com.gangyunshihua.entity.Diesel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DieselRepository extends JpaRepository<Diesel, Integer> {

    @Query("from Diesel d where d.type = ?1")
    Diesel findByType(String type);

    @Query("from Diesel d where d.status = ?1")
    List<Diesel> findByStatus(Integer status);

    @Query(value = "SELECT d.id dieselId,d.type type,d.price price," +
            "if(dl.price is null,'-',d.price - dl.price) differencePrice from gy_diesel d " +
            "LEFT JOIN gy_diesel_log dl on d.type = dl.type and dl.valid_time = ?1 " +
            "where d.`status` = 1 GROUP BY d.id ORDER BY d.id asc", nativeQuery = true)
    List<Object[]> findDiesel(Date yesterday);

    @Query(value = "update gy_diesel d set d.`status` = 2 where d.`status` = 1", nativeQuery = true)
    @Modifying
    void hideAll();
}
