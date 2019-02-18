package com.nfcs.singularity.core.domain;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity()
@Table(name = "usr")
public class User extends BaseEntity {
    public User() {
        this.activationCode = UUID.randomUUID().toString();
    }

    private String username;
    private boolean activated = false;
    private String activationCode;
    private String password;

    public String getActivationCode() {
        return activationCode;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @MapKey(name = "name")
    private Map<String, Role> roles = new HashMap<>();

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
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", activated=" + activated +
                '}';
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
