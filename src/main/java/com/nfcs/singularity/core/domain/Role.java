package com.nfcs.singularity.core.domain;

import org.metawidget.inspector.annotation.UiLabel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.security.RolesAllowed;
import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Entity
@Configuration
@Table(name = "rle", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@RolesAllowed({"ADMIN"})
public class Role extends BaseEntity {
    @UiLabel("Role name")
    private String name;

    @UiLabel("Users list")
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER, mappedBy = "roles")
    @MapKey(name = "username")
    private Map<String, User> users = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return getName().equals(role.getName());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Collection<User> getUsers() {
        return new HashMap<String, User>(users).values();
    }

    public void addUser(User user) {
        if (users.containsKey(user.getUsername())) return;
        this.users.put(user.getUsername(), user);
        user.addRole(this);
    }

    public void removeUser(User user) {
        this.users.remove(user.getUsername());
        user.removeRole(this);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
