// package com.vtxlab.project.bc_stock_finnhub.annotation;

// import java.util.Objects;
// import jakarta.validation.ConstraintValidator;
// import jakarta.validation.ConstraintValidatorContext;

// public class SymbolValidator
//     implements ConstraintValidator<SymbolCheck, StockSymbol> {

//   @Override
//   public boolean isValid(StockSymbol value, ConstraintValidatorContext context) {
//     try {
//       return Objects.nonNull(value) && value.getStockIds().contains(value);
//     } catch (Exception e) {
//       return false;
//     }
//   }
// }
