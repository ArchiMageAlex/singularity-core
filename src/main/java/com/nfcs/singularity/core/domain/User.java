package com.nfcs.singularity.core.domain;

import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiMasked;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity()
@Table(name = "usr", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
public class User extends BaseEntity {
    @NotNull
    @UiLabel("User name")
    private String username;

    @UiLabel("Active")
    private boolean activated = false;

    @UiLabel("Activation code")
    private String activationCode = UUID.randomUUID().toString();

    @UiMasked
    @UiLabel("Password")
    private String password;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @MapKey(name = "id")
    @UiLabel("Roles")
    private List<Role> roles = new ArrayList<>();

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String code) {
        this.activationCode = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        if (this.roles.contains(role)) return;
        this.roles.add(role);
        role.addUser(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role.getId());
        role.removeUser(this);
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return "{" +
                "id: \'" + id +
                "\', username: \'" + username +
                "\', activated: \'" + activated +
                "\', activationCode: \'" + activationCode +
                "\', password: \'" + password +
                "\'}";
    }
}