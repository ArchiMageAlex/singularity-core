package com.nfcs.singularity.core.domain;

import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiMasked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Entity()
@Table(name = "usr", uniqueConstraints = @UniqueConstraint(columnNames = {"username"}))
@RolesAllowed({"USER"})
@Component
public class User extends BaseEntity {
    @Transient
    private static Logger LOG = Logger.getLogger(User.class.getName());
    @Transient
    private static PasswordEncoder passwordEncoder;
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
    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @MapKey(name = "id")
    private Map<String, Role> roles = new HashMap<>();

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        User.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        LOG.info(User.passwordEncoder.toString());
    }

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
        //this.password = passwordEncoder.encode(password);
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

    public Collection<Role> getRoles() {
        return new HashMap<>(roles).values();
    }

    public void addRole(Role role) {
        if (this.roles.containsKey(role.getName())) return;
        this.roles.put(role.getName(), role);
        role.addUser(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role.getName());
        role.removeUser(this);
    }

    @Override
    public String toString() {
        return "{" +
                "id: \'" + id +
                "\', username: \'" + username +
                "\', activated: \'" + activated +
                "\', activationCode: \'" + activationCode +
                "\', password: \'" + password +
                "\', roles: \'[" + roles.values().stream().map(Role::getName).collect(Collectors.joining(", ")) +
                "]\'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}