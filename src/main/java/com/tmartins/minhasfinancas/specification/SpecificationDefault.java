package com.tmartins.minhasfinancas.specification;

import java.io.Serializable;

public interface SpecificationDefault <T extends Serializable> {

    default SpecificationBuilder<T> builder() {
        return new SpecificationBuilder<>();
    }
}
