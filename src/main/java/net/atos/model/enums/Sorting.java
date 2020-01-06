package net.atos.model.enums;

public enum Sorting {
    MARK__ASC ("by mark ascending"),
    MARK__DESC ("by mark descending"),
    MODEL__ASC ("by model ascending"),
    MODEL__DESC ("by model descending"),
    YEAR__ASC ("by year ascending"),
    YEAR__DESC ("by year descending"),
    PRICE__ASC ("by price ascending"),
    PRICE__DESC ("by price descending");

    private String description;

    public String getName(String description)
    {
        return this.name();
    }


    private Sorting(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
