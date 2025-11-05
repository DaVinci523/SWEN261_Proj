package com.ufund.api.ufundapi.ai;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

public class GenerateTextfromTextInput {
    public static void main(String[] args) {
        try (Client client = Client.builder().apiKey("AIzaSyB3So1lECn4L2NRZlNhWhN7NAjmpEGlSPU").build()) {
            GenerateContentResponse response = client.models.generateContent("gemini-2.5-flash", "Give me 5 words to describe google", null);

            System.out.println(response.text());
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }
}
