package de.hso.badenair.domain.base;

import java.io.Serializable;

public interface Identifiable<T extends Serializable> {
    T getId();
}
