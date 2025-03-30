package com.epam.training.gen.ai.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class TemplatePopulator {
    public static String populateTemplate(String input, List<String> documents) throws IOException {
        // Load template from resources
        InputStream inputStream = TemplatePopulator.class.getClassLoader().getResourceAsStream("chat.template");
        if (inputStream == null) {
            throw new IOException("Template file not found in resources.");
        }

        // Read template content
        String template;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            template = reader.lines().collect(Collectors.joining("\n"));
        }

        // Replace placeholders
        String documentsContent = String.join("\n", documents);
        template = template.replace("{input}", input)
                .replace("{documents}", documentsContent);

        return template;
    }
}
