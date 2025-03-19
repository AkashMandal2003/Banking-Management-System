package com.jocata.bankingmgntsystem.tm.form;

import java.time.LocalDateTime;

public class TransactionTimePeriod {

    LocalDateTime startDate;

    LocalDateTime endDate;

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
