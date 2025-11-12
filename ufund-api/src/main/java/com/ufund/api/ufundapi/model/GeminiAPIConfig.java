    package com.ufund.api.ufundapi.model;
    
    import com.google.genai.Client;

    import com.google.genai.Client.Builder;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;

    /** 
     * Class to define GeminiAPI as a Spring Bean 
     * @author CTRL TEAM       
    */ 
    @Configuration
    public  class GeminiAPIConfig {

        // declaration of Gemini Client as a Bean
        @Bean
        public static Client genaiClient() {

            // creates new Builder instance to create the client
            Builder clientBuilder = com.google.genai.Client.builder();

            // sets the API to use in the Builder 
            clientBuilder = clientBuilder.apiKey("AIzaSyB3So1lECn4L2NRZlNhWhN7NAjmpEGlSPU");

            // builds the client
            return clientBuilder.build();
        }
    }