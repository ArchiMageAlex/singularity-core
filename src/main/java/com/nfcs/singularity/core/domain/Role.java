package com.nfcs.singularity.core.domain;

import org.metawidget.inspector.annotation.UiLabel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "rle", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Role extends BaseEntity {
    @UiLabel("Role name")
    private String name;
    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER, mappedBy = "roles")
    @MapKey(name = "username")
    private List<User> users = new ArrayList<>();

    public Collection<User> getUsers() {
        return new ArrayList<User>(users);
    }

    public void addUser(User user) {
        if (users.contains(user)) return;
        this.users.add(user);
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
