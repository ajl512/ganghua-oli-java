package com.gangyunshihua.repository;

import com.gangyunshihua.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select max(o.id) from gy_order o where o.user_id = ?1", nativeQuery = true)
    Integer findMyLastOrderId(Integer userId);

    @Query(value = "SELECT o.id orderId,o.number orderNumber,GROUP_CONCAT(CONCAT(oi.type,':',oi.weight) SEPARATOR ';') dieselInfo," +
            "IFNULL(driver.`name`,'') driverName,IFNULL(supercargo.`name`,'') supercargoName,DATE_FORMAT(o.create_time,'%Y-%m-%d %H:%i') createTime," +
            "o.`status` orderStatus,o.pick_mode pickMode,o.mobile mobile,o.company company,o.car_number carNumber,o.car_load carLoad," +
            "o.prove prove,IFNULL(driver.id_number,'') driverIdNumber,IFNULL(driver.mobile,'') driverMobile," +
            "IFNULL(supercargo.id_number,'') supercargoIdNumber,IFNULL(supercargo.mobile,'') supercargoMobile from gy_order o " +
            "LEFT JOIN gy_order_item oi on oi.order_id = o.id " +
            "LEFT JOIN gy_employee driver on driver.id = o.driver_id LEFT JOIN gy_employee supercargo on supercargo.id = o.supercargo_id " +
            "where o.user_id = ?1 and o.`status` in ?2 GROUP BY o.id ORDER BY o.id desc LIMIT ?3,?4", nativeQuery = true)
    List<Object[]> findMyOrder(Integer userId, Integer[] orderStatusArray, Integer start, Integer size);

    @Query(value = "SELECT count(o.id) from gy_order o where o.user_id = ?1 and o.`status` in ?2", nativeQuery = true)
    Integer findMyOrderCount(Integer userId, Integer[] orderStatusArray);

    @Query(value = "SELECT o.id orderId,o.number orderNumber,GROUP_CONCAT(CONCAT(oi.type,':',oi.weight) SEPARATOR ';') dieselInfo," +
            "IFNULL(driver.`name`,'') driverName,IFNULL(supercargo.`name`,'') supercargoName,DATE_FORMAT(o.create_time,'%Y-%m-%d %H:%i') createTime," +
            "o.`status` orderStatus,o.pick_mode pickMode,o.mobile mobile,o.company company,o.car_number carNumber,o.car_load carLoad," +
            "o.prove prove,IFNULL(driver.id_number,'') driverIdNumber,IFNULL(driver.mobile,'') driverMobile," +
            "IFNULL(supercargo.id_number,'') supercargoIdNumber,IFNULL(supercargo.mobile,'') supercargoMobile from gy_order o " +
            "LEFT JOIN gy_order_item oi on oi.order_id = o.id " +
            "LEFT JOIN gy_employee driver on driver.id = o.driver_id LEFT JOIN gy_employee supercargo on supercargo.id = o.supercargo_id " +
            "where o.`status` in ?1 GROUP BY o.id ORDER BY o.id desc LIMIT ?2,?3", nativeQuery = true)
    List<Object[]> findOrder(Integer[] orderStatusArray, Integer start, Integer size);

    @Query(value = "SELECT count(o.id) from gy_order o where o.`status` in ?1", nativeQuery = true)
    Integer findOrderCount(Integer[] orderStatusArray);
}
