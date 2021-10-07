package com.example.washmachine.repositories;

import java.util.List;

import com.example.washmachine.entity.WashAction;
import com.example.washmachine.entity.WashEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WashActionRepository extends JpaRepository<WashAction, Long> {

    List<WashEvent> getByMachineId(Long machineId);

}
