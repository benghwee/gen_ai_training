package com.epam.training.gen.ai;

import com.epam.training.gen.ai.plugin.BmiCalculatorPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class BmiCalculatorPluginTest {
    private BmiCalculatorPlugin bmiCalculator;

    @BeforeEach
    void setUp() {
        bmiCalculator = new BmiCalculatorPlugin();
    }

    @Test
    void testValidBmiCalculations() {
        assertThat(bmiCalculator.getBmiResult(70, 1.75)).isEqualTo("Your BMI is: 22.86 (Normal weight)");
        assertThat(bmiCalculator.getBmiResult(50, 1.75)).isEqualTo("Your BMI is: 16.33 (Underweight)");
        assertThat(bmiCalculator.getBmiResult(85, 1.75)).isEqualTo("Your BMI is: 27.76 (Overweight)");
        assertThat(bmiCalculator.getBmiResult(100, 1.75)).isEqualTo("Your BMI is: 32.65 (Obese)");
    }

    @Test
    void testEdgeCasesForBmiCategories() {
        assertThat(bmiCalculator.getBmiResult(60, 1.75)).isEqualTo("Your BMI is: 19.59 (Normal weight)");
        assertThat(bmiCalculator.getBmiResult(76, 1.75)).isEqualTo("Your BMI is: 24.82 (Normal weight)");
        assertThat(bmiCalculator.getBmiResult(77, 1.75)).isEqualTo("Your BMI is: 25.14 (Overweight)");
    }

    @Test
    void testInvalidInputs() {
        assertThat(bmiCalculator.getBmiResult(-70, 1.75)).isEqualTo("Error: Weight and height must be positive values.");
        assertThat(bmiCalculator.getBmiResult(70, -1.75)).isEqualTo("Error: Weight and height must be positive values.");
        assertThat(bmiCalculator.getBmiResult(0, 1.75)).isEqualTo("Error: Weight and height must be positive values.");
        assertThat(bmiCalculator.getBmiResult(70, 0)).isEqualTo("Error: Weight and height must be positive values.");
    }
}
