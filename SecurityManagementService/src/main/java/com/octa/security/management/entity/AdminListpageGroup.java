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
@Table(name = "admin_listpage_group")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminListpageGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "listpage_group_id")
    private Long listpageGroupId;

    @Column(name = "listpage_group_name", nullable = false, length = 100)
    private String listpageGroupName;

    @Column(name = "listpage_group_description", length = 500)
    private String listpageGroupDescription;

    @Column(name = "listpage_group_sequence")
    private Integer listpageGroupSequence;

    @Column(name = "module_id")
    private Long moduleId;

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
