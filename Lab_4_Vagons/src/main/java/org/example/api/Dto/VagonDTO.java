package org.example.api.Dto;

public class VagonDTO {

    private int cost;
    private String name;
    private String description;

    public VagonDTO() {}

    public VagonDTO(int cost, String name, String description) {
        this.cost = cost;
        this.name = name;
        this.description = description;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "VagonDTO{" +
                "cost=" + cost +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}