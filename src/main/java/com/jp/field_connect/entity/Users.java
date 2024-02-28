package com.jp.field_connect.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@Data
@Setter
@Getter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GenericGenerator(name = "inc", strategy = "increment")
    @GeneratedValue(generator = "inc")
    private Long userId;
    @Column(name = "username", nullable = false)
    private String userName;
    private String password;
    private boolean enabled;
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "role")
    private Set<String> roles;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true,name = "former_id")
    private FormerDetails formerDetails;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true,name = "service_provider_id")
    private ServiceProviderDetails serviceProviderDetails;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(unique = true,name="worker_id")
    private WorkerDetails workerDetails;
}
