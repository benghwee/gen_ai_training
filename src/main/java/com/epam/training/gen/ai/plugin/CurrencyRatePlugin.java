package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CurrencyRatePlugin {

    @DefineKernelFunction(name = "currencyRateConvertor", description = "Converts a source currency to a target currency based on a given amount.")
    public String convertCurrencyRate(
            @KernelFunctionParameter(description = "The source currency", name = "sourceCurrency") String sourceCurrency,
            @KernelFunctionParameter(description = "The target currency", name = "targetCurrency") String targetCurrency,
            @KernelFunctionParameter(description = "The amount to be converted", name = "amount") double amount) {

        // Normalize currency codes to uppercase
        sourceCurrency = sourceCurrency.toUpperCase();
        targetCurrency = targetCurrency.toUpperCase();

        log.info("CurrencyRate plugin called with: sourceCurrency={}, targetCurrency={}, amount={}", sourceCurrency, targetCurrency, amount);

        // Validate amount
        if (amount <= 0) {
            log.warn("Invalid amount: {}. Amount must be greater than zero.", amount);
            return "Error: Amount must be greater than zero.";
        }

        double convertedAmount = convertCurrency(amount, sourceCurrency, targetCurrency);

        if (convertedAmount != -1) {
            String output = String.format("%.2f %s = %.2f %s", amount, sourceCurrency, convertedAmount, targetCurrency);
            log.info("Conversion successful: {}", output);
            return output;
        } else {
            log.warn("Conversion rate not available for {} to {}", sourceCurrency, targetCurrency);
            return "Error: Conversion rate not available.";
        }
    }

    // Mock exchange rates
    private static final Map<String, Double> EXCHANGE_RATES = new HashMap<>();

    static {
        // USD Exchange Rates
        EXCHANGE_RATES.put("USD_EUR", 0.92);
        EXCHANGE_RATES.put("USD_GBP", 0.78);
        EXCHANGE_RATES.put("USD_INR", 82.5);
        EXCHANGE_RATES.put("USD_CAD", 1.35);
        EXCHANGE_RATES.put("USD_AUD", 1.52);
        EXCHANGE_RATES.put("USD_JPY", 150.2);
        EXCHANGE_RATES.put("USD_CNY", 7.2);
        EXCHANGE_RATES.put("USD_CHF", 0.88);
        EXCHANGE_RATES.put("USD_BRL", 5.0);
        EXCHANGE_RATES.put("USD_RUB", 93.0);

        // Reverse Rates
        EXCHANGE_RATES.put("EUR_USD", 1.09);
        EXCHANGE_RATES.put("GBP_USD", 1.28);
        EXCHANGE_RATES.put("INR_USD", 0.012);
        EXCHANGE_RATES.put("CAD_USD", 0.74);
        EXCHANGE_RATES.put("AUD_USD", 0.66);
        EXCHANGE_RATES.put("JPY_USD", 0.0067);
        EXCHANGE_RATES.put("CNY_USD", 0.14);
        EXCHANGE_RATES.put("CHF_USD", 1.14);
        EXCHANGE_RATES.put("BRL_USD", 0.20);
        EXCHANGE_RATES.put("RUB_USD", 0.011);
    }

    public static double getMockExchangeRate(String fromCurrency, String toCurrency) {
        return EXCHANGE_RATES.getOrDefault(fromCurrency + "_" + toCurrency, -1.0);
    }

    public static double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double rate = getMockExchangeRate(fromCurrency, toCurrency);
        return (rate != -1) ? amount * rate : -1;
    }
}
