package org.example.userauthenticationservice_sept2024.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Entity;

@Entity
@JsonDeserialize(as = Role.class)
public class Role extends BaseModel {
    private String roleName;
}
