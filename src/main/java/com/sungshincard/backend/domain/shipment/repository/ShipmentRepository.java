package com.sungshincard.backend.domain.shipment.repository;

import com.sungshincard.backend.domain.order.entity.Orders;
import com.sungshincard.backend.domain.shipment.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByOrder(Orders order);
}
