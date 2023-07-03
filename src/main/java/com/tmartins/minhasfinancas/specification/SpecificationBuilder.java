package com.tmartins.minhasfinancas.specification;

import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

public class SpecificationBuilder <T extends Serializable> {
    private Specification<T> specification = (root, query, cb) -> (null);

    public SpecificationBuilder<T> and(Specification<T> specification) {
        this.specification = this.specification.and(specification);

        return this;
    }

    public SpecificationBuilder<T> or(Specification<T> specification) {
        this.specification = this.specification.or(specification);

        return this;
    }

    public Specification<T> build() {
        return specification;
    }
}
