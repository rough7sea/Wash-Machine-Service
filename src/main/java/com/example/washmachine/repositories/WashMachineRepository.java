package com.example.washmachine.repositories;

import com.example.washmachine.entity.WashMachine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WashMachineRepository extends JpaRepository<WashMachine, Long> {
}
