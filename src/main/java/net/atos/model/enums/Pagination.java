package net.atos.model.enums;

public enum Pagination {
    FIVE (5),
    TEN (10),
    FIFTEEN (15),
    TWENTY (20),
    TWENTYFIVE (25),
    THIRTY (30);

    private int description;

    private Pagination(int description) {
        this.description = description;
    }

    public int getDescription() {
        return description;
    }

}
