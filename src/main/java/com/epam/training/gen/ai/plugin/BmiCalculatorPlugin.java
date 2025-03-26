package com.epam.training.gen.ai.plugin;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BmiCalculatorPlugin {
    @DefineKernelFunction(name = "bmiCalculator", description = "Calculates BMI based on weight and height")
    public String getBmiResult(
            @KernelFunctionParameter(description = "The weight of the person in kg", name = "weight") double weight,
            @KernelFunctionParameter(description = "The height of the person in meter. if in cm, convert to meter.", name = "height") double height) {

        log.info("BmiCalculator plugin called with parameters: weight={} kg, height={} m", weight, height);

        // Input validation
        if (weight <= 0 || height <= 0) {
            log.warn("Invalid input: weight and height must be greater than zero.");
            return "Error: Weight and height must be positive values.";
        }

        // Calculate BMI
        double bmi = calculateBMI(weight, height);
        String category = categorizeBMI(bmi);
        String result = String.format("Your BMI is: %.2f (%s)", bmi, category);

        log.info("BMI Calculation result: {}", result);
        return result;
    }

    private double calculateBMI(double weight, double height) {
        return weight / (height * height);
    }

    private String categorizeBMI(double bmi) {
        return (bmi < 18.5) ? "Underweight"
                : (bmi < 24.9) ? "Normal weight"
                : (bmi < 29.9) ? "Overweight"
                : "Obese";
    }
}
