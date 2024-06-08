package com.popov.fintrack.web.mapper;

import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.budget.model.Budget;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetMapper extends Mappable<Budget, BudgetDTO> {}
