package com.pentyugov.wflow.core.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public abstract class BaseDto implements Serializable {

    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (other == null || getClass() != other.getClass())
            return false;

        return Objects.equals(getId(), ((BaseDto) other).getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
