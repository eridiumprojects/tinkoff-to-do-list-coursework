package com.example.todolistcoursework.repository;

import com.example.todolistcoursework.model.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findById(Long deviceId);
    Optional<Device> findByDeviceToken(UUID token);
}
