package com.vanguard;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class SearchSpecificationBuilder<T> {

    public static final Pattern pattern = Pattern.compile("(\\w+?)([:<>=])([a-zA-Z_0-9.-]+?)");
    private final List<String> filters;

    public SearchSpecificationBuilder(List<String> filters) {
        this.filters = filters;
    }

    private List<SearchCriteria> parse(String filter) {
        return Arrays.stream(filter.split("\\|")).map(f -> {
            var matcher = pattern.matcher(f);
            if (matcher.matches()) {
                return new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3));
            } else {
                throw new IllegalArgumentException();
            }
        }).toList();
    }

    public Specification<T> build(Function<SearchCriteria, Specification<T>> converter) {
        if (ObjectUtils.isEmpty(filters)) {
            return null;
        }

        var specs = filters.stream().map(filter -> {
            var orCriteria = parse(filter);
            var spec = converter.apply(orCriteria.getFirst());

            for (int i = 1; i < orCriteria.size(); i++) {
                var param = orCriteria.get(i);
                spec = Specification.where(spec).or(converter.apply(param));
            }

            return spec;
        }).toList();

        return Specification.allOf(specs);
    }
}
