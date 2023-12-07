package com.octa.security.management.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin_module")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "module_id")
    private Long moduleId;

    @Column(name = "module_name", nullable = false, length = 100)
    private String moduleName;

    @Column(name = "module_description", length = 2000)
    private String moduleDescription;

    @Column(name = "module_sequence")
    private Integer moduleSequence;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "created_datetime")
    private Date createdDatetime;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_datetime")
    private Date modifiedDatetime;

    @Column(name = "active")
    private Integer active;

    @Column(name = "deleted")
    private Integer deleted;

}
