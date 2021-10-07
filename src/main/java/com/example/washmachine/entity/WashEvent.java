package com.example.washmachine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import com.example.washmachine.common.WashStep;
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
public class WashEvent extends Base{

    @Id
    @Column(name = "action_id", insertable = false, updatable = false)
    private Long actionId;

    @MapsId
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "wash_action", nullable = false, foreignKey = @ForeignKey(name = "FK_event_to_action"))
    private WashAction action;

    @Enumerated(EnumType.STRING)
    private WashStep step;
}
