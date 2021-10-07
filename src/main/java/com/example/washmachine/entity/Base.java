package com.example.washmachine.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import lombok.Data;

@Data
@MappedSuperclass
abstract class Base {

//    @UpdateTimestamp
    @Column(insertable = false)
    private Timestamp updateDate;

//    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createDate;

    @PrePersist
    protected void onCreate() {
        createDate = Timestamp.valueOf(LocalDateTime.now());
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = Timestamp.valueOf(LocalDateTime.now());
    }
}
