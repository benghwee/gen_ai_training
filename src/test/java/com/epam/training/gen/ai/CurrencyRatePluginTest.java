package com.epam.training.gen.ai;

import com.epam.training.gen.ai.plugin.CurrencyRatePlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class CurrencyRatePluginTest {
    private CurrencyRatePlugin currencyRatePlugin;

    @BeforeEach
    void setUp() {
        currencyRatePlugin = new CurrencyRatePlugin();
    }

    @Test
    void testValidCurrencyConversions() {
        assertThat(currencyRatePlugin.convertCurrencyRate("USD", "EUR", 100))
                .isEqualTo("100.00 USD = 92.00 EUR");

        assertThat(currencyRatePlugin.convertCurrencyRate("EUR", "USD", 100))
                .isEqualTo("100.00 EUR = 109.00 USD");

        assertThat(currencyRatePlugin.convertCurrencyRate("USD", "INR", 10))
                .isEqualTo("10.00 USD = 825.00 INR");

        assertThat(currencyRatePlugin.convertCurrencyRate("INR", "USD", 1000))
                .isEqualTo("1000.00 INR = 12.00 USD");
    }

    @Test
    void testInvalidCurrencyConversions() {
        assertThat(currencyRatePlugin.convertCurrencyRate("XYZ", "USD", 100))
                .isEqualTo("Error: Conversion rate not available.");

        assertThat(currencyRatePlugin.convertCurrencyRate("USD", "XYZ", 100))
                .isEqualTo("Error: Conversion rate not available.");

        assertThat(currencyRatePlugin.convertCurrencyRate("GBP", "JPY", 50))
                .isEqualTo("Error: Conversion rate not available.");
    }

    @Test
    void testCaseInsensitiveCurrencyCodes() {
        assertThat(currencyRatePlugin.convertCurrencyRate("usd", "eur", 100))
                .isEqualTo("100.00 USD = 92.00 EUR");

        assertThat(currencyRatePlugin.convertCurrencyRate("Usd", "eUr", 100))
                .isEqualTo("100.00 USD = 92.00 EUR");
    }

    @Test
    void testEdgeCasesForAmounts() {
        assertThat(currencyRatePlugin.convertCurrencyRate("USD", "EUR", 0))
                .isEqualTo("Error: Amount must be greater than zero.");

        assertThat(currencyRatePlugin.convertCurrencyRate("USD", "EUR", -50))
                .isEqualTo("Error: Amount must be greater than zero.");
    }
}
