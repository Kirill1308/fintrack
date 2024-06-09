package com.popov.fintrack.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    // Expense Categories
    FOOD("Food"),
    GROCERIES("Groceries"),
    TRANSPORT("Transport"),
    ENTERTAINMENT("Entertainment"),
    UTILITIES("Utilities"),
    SHOPPING("Shopping"),
    HEALTH("Health"),
    HOME("Home"),
    BILLS_AND_FEES("Bills and Fees"),
    CAR("Car"),
    TRAVEL("Travel"),
    EDUCATION("Education"),
    CLOTHING("Clothing"),
    SPORT("Sport"),
    GIFTS("Gifts"),
    CHARITY("Charity"),
    RENT("Rent"),
    SUBSCRIPTION("Subscription"),

    // Income Categories
    SALARY("Salary"),
    FREELANCE("Freelance"),
    SALES("Sales"),
    BONUS("Bonus"),
    DIVIDENDS("Dividends"),
    INTEREST("Interest"),
    REFUND("Refund"),
    LOAN("Loan"),
    INVESTMENT("Investment"),
    PENSION("Pension"),
    INSURANCE("Insurance"),
    CHILD_SUPPORT("Child Support"),
    ALIMONY("Alimony"),
    GOVERNMENT_BENEFITS("Government Benefits"),
    OTHER_INCOME("Other Income"),
    BANK_TRANSFER("Bank Transfer"),

    OTHER("Other");

    private final String displayName;
}

