package com.example.washmachine.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.example.washmachine.common.WashActionStatus;
import com.example.washmachine.common.WashMode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WashAction extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long actionId;

    @Column(name = "machine_id", insertable = false, updatable = false)
    private Long machineId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "machine_id", nullable = false, foreignKey = @ForeignKey(name = "FK_action_to_machine"))
    private WashMachine machine;

    @JsonIgnore
    @PrimaryKeyJoinColumn
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "action_id")
    private List<WashEvent> events;

    @Enumerated(EnumType.STRING)
    private WashMode washMode;

    @Enumerated(EnumType.STRING)
    private WashActionStatus status;

    @PrimaryKeyJoinColumn
    @OneToOne(mappedBy = "action", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private WashParams customParams;
}
