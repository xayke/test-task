package com.haulmont.testtask.domain;

import java.util.UUID;

public class Credit {
    private UUID id;
    private String name;
    private String minLimit;
    private String maxLimit;
    private String percent;

    public Credit() {
    }

    public Credit(UUID id, String name, String minLimit, String maxLimit, String percent) {
        this.id = id;
        this.name = name;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.percent = percent;
    }

    public Credit(String name, String minLimit, String maxLimit, String percent) {
        id = UUID.randomUUID();
        this.name = name;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.percent = percent;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(String minLimit) {
        this.minLimit = minLimit;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public String getFull() {
        return name + " (" + minLimit + "руб. - " + maxLimit + "руб. [" + percent + "%])";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credit credit = (Credit) o;
        return id.equals(credit.id) && name.equals(credit.name) && minLimit.equals(credit.minLimit)
                && maxLimit.equals(credit.maxLimit) && percent.equals(credit.percent);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "{" +
                "\n\t'id': '" + id + '\'' +
                ",\n\t'name': '" + name + '\'' +
                ",\n\t'min_limit': '" + minLimit + '\'' +
                ",\n\t'max_limit': '" + maxLimit + '\'' +
                ",\n\t'percent': '" + percent + '\'' +
                "\n}";
    }
}
