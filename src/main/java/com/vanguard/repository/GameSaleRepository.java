package com.vanguard.repository;

import com.vanguard.SearchSpecification;
import com.vanguard.SearchSpecificationBuilder;
import com.vanguard.entity.GameSaleEntity;
import com.vanguard.entity.GameSaleEntity_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.Instant;
import java.util.List;

public interface GameSaleRepository extends JpaRepository<GameSaleEntity, Long>, JpaSpecificationExecutor<GameSaleEntity> {

    interface Specs {
        static Specification<GameSaleEntity> byTimeBetween(Instant startTime, Instant endTime) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get(GameSaleEntity_.dateOfSale), startTime, endTime);
        }

        static Specification<GameSaleEntity> bySearchFilters(List<String> filters) {
            return new SearchSpecificationBuilder<GameSaleEntity>(filters).build(SearchSpecification::new);
        }
    }
}
