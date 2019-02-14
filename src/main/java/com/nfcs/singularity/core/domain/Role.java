package com.nfcs.singularity.core.domain;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
public class Role extends BaseEntity {
    private String name;

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
        return Objects.hash(super.hashCode(), getName());
    }

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER, mappedBy = "roles")
    @MapKey(name = "username")
    private Map<String, User> users = new HashMap<>();

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
