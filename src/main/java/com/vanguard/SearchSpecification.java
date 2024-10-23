package com.vanguard;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.Instant;

public record SearchSpecification<T>(SearchCriteria criteria) implements Specification<T> {
    @Override
    public Predicate toPredicate(@NonNull Root<T> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder builder) {
        if (criteria.operator().equalsIgnoreCase(">")) {
            var attr = root.get(criteria.key());
            if (attr.getJavaType() == Instant.class) {
                return builder.greaterThan(root.get(criteria.key()), Instant.ofEpochSecond(Long.parseLong(criteria.value())));
            } else if (attr.getJavaType() == BigDecimal.class) {
                return builder.greaterThan(root.get(criteria.key()), new BigDecimal(criteria.value()));
            } else {
                throw new IllegalArgumentException();
            }
        } else if (criteria.operator().equalsIgnoreCase("<")) {
            var attr = root.get(criteria.key());
            if (attr.getJavaType() == Instant.class) {
                return builder.lessThan(root.get(criteria.key()), Instant.ofEpochSecond(Long.parseLong(criteria.value())));
            } else if (attr.getJavaType() == BigDecimal.class) {
                return builder.lessThan(root.get(criteria.key()), new BigDecimal(criteria.value()));
            } else {
                throw new IllegalArgumentException();
            }
        } else if (criteria.operator().equalsIgnoreCase("=")) {
            var attr = root.get(criteria.key());
            var type = attr.getJavaType();

            if (type == Instant.class) {
                return builder.equal(root.get(criteria.key()), Instant.ofEpochSecond(Long.parseLong(criteria.value())));
            } else {
                return builder.equal(root.get(criteria.key()), criteria.value());
            }
        } else if (criteria.operator().equalsIgnoreCase(":")) {
            var attr = root.get(criteria.key());
            var type = attr.getJavaType();

            if (type == String.class) {
                return builder.like(root.get(criteria.key()), "%" + criteria.value() + "%");
            } else {
                throw new IllegalArgumentException();
            }
        }
        return null;
    }
}
