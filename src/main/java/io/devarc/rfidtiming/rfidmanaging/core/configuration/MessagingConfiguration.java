package io.devarc.rfidtiming.rfidmanaging.core.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

@Configuration
public class MessagingConfiguration {

    public static final String RFID_TIMES_QUEUE = "rfid-times";
    public static final String RFID_AND_TIMES_REQUEST_QUEUE = "rfid-and-times-requests";

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public MessageConverter messageConverter() {
        objectMapper.registerModule(new JavaTimeModule());
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
