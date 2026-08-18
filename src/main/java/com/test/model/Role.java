package com.test.model;

import java.util.Set;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN(Set.of(Permissions.READ, Permissions.WRITE, Permissions.DELETE, Permissions.UPDATE, Permissions.CREATE)),
    USER(Set.of(Permissions.READ, Permissions.WRITE));

    private final Set<Permissions> permissions;

    Role(Set<Permissions> permissions) {
        this.permissions = permissions;
    }
}
