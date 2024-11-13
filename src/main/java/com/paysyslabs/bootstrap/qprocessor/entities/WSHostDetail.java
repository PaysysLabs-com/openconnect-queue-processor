package com.paysyslabs.bootstrap.qprocessor.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "host_details")
public class WSHostDetail {

    @Id
    @Column(name = "host_id")
    private Integer id;

    @Column(name = "host")
    private String host;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "virtualhost")
    private String virtualHost;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }
}
