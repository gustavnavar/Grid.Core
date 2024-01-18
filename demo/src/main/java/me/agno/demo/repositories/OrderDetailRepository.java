package me.agno.demo.repositories;

import me.agno.demo.model.OrderDetail;
import me.agno.demo.model.OrderDetailKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, OrderDetailKey> {
}
