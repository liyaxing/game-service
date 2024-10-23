package com.vanguard;

import java.io.Serializable;

public record SearchCriteria(String key, String operator, String value) implements Serializable {
}
