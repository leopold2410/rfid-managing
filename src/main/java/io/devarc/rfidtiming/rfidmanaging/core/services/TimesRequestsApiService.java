package io.devarc.rfidtiming.rfidmanaging.core.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devarc.rfidtiming.rfidmanaging.core.configuration.MessagingConfiguration;
import io.devarc.rfidtiming.rfidmanaging.api.RfidTagAndTimes;
import io.devarc.rfidtiming.rfidmanaging.api.RfidTagCollection;
import io.devarc.rfidtiming.rfidmanaging.api.RfidTagAndTimesCollection;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.ArrayList;
import java.util.List;


/**
 * Service provides endpoint for requesting reading times for rfid tags
 * for proper decoupling of the microservices we as well use messaging.
 * The onTimesRequest returns a response message (RfidTagAndTimesCollection) on the reply channel
 */
@Service
public class TimesRequestsApiService {
    private final RfidAndTimesService rfidAndTimesService;
    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    public TimesRequestsApiService(RfidAndTimesService rfidAndTimesService, JmsTemplate jmsTemplate, ObjectMapper objectMapper) {
        this.rfidAndTimesService = rfidAndTimesService;
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = MessagingConfiguration.RFID_AND_TIMES_REQUEST_QUEUE)
    public void onTimesRequests(@Payload RfidTagCollection payload, Message message) throws JMSException, JsonProcessingException {
        List<RfidTagAndTimes> idAndTimes = new ArrayList<>();
        rfidAndTimesService.getTimesByRfids(payload.getRfidTags()).forEach((rfid, times) -> {idAndTimes.add(new RfidTagAndTimes(rfid, times));});

        RfidTagAndTimesCollection response = RfidTagAndTimesCollection.builder()
                .rfidAndReadTimes(idAndTimes)
                .build();
        jmsTemplate.convertAndSend(message.getJMSReplyTo(), response);
    }
}
