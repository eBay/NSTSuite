package com.ebay.nst.tutorials.sharedtutorialutilities.rest;

public enum CanadaHoliday {

    NEW_YEARS_DAY(1),
    LOUIS_RIEL_DAY(2),
    GOOD_FRIDAY(7),
    VICTORIA_DAY(11),
    CANADA_DAY(15),
    LABOUR_DAY(24),
    THANKSGIVING(25),
    CHRISTMAS_DAY(27);

    private final Integer holidayId;

    CanadaHoliday(Integer holidayId) {
        this.holidayId = holidayId;
    }

    public Integer getHolidayId() {
        return holidayId;
    }
}
