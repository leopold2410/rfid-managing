package io.devarc.rfidtiming.rfidmanaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devarc.db.mongodb.MongoDbCleanUpExtension;
import io.devarc.rfidtiming.rfidmanaging.api.*;
import io.devarc.rfidtiming.rfidmanaging.core.configuration.MessagingConfiguration;
import io.devarc.rfidtiming.rfidmanaging.core.model.RfidTagTime;
import io.devarc.rfidtiming.rfidmanaging.core.services.RfidTagTimeRepository;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.ActiveMQServer;
import org.apache.activemq.artemis.core.server.ActiveMQServers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.JMSException;
import javax.jms.Message;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ExtendWith(MongoDbCleanUpExtension.class)
public class RfidTagTimesMessagingTests {
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RfidTagTimeRepository rfidTagTimeRepository;

    private ActiveMQServer server;
    @BeforeAll
    void startUpServer() throws Exception {
        server = ActiveMQServers.newActiveMQServer(new ConfigurationImpl()
                .setPersistenceEnabled(false)
                .setJournalDirectory("target/data/journal")
                .setSecurityEnabled(false)
                .addAcceptorConfiguration("invm", "vm://0"));
        server.start();
    }

    @AfterAll
    void stopServer() throws Exception {
        server.stop();
        server = null;
    }

    @Test
    void testMessageProcessed() throws InterruptedException {
        jmsTemplate.convertAndSend(MessagingConfiguration.RFID_TIMES_QUEUE,
                RfidTagAndTimeCollection.builder()
                        .rfidTagAndTimes(Collections.singletonList(RfidTagAndTime.builder()
                                .rfId(new RfidTag("1"))
                                .time(LocalTime.now())
                                .build()))
                        .build());

        //delaying one second, so the message can be processed
        TimeUnit.SECONDS.sleep(1);
        List<RfidTagTime> rfidTagTimes = new ArrayList<>();
        rfidTagTimeRepository.findAll().forEach(rfidTagTimes::add);
        assertEquals(1, rfidTagTimes.size());
    }

    @Test
    void testMultiTagMessageProcessed() throws InterruptedException {
        jmsTemplate.convertAndSend(MessagingConfiguration.RFID_TIMES_QUEUE,
                RfidTagAndTimeCollection.builder()
                        .rfidTagAndTimes(Arrays.asList(RfidTagAndTime.builder()
                                .rfId(new RfidTag("1"))
                                .time(LocalTime.now())
                                .build(), RfidTagAndTime.builder()
                                .rfId(new RfidTag("2"))
                                .time(LocalTime.now())
                                .build()))
                        .build());

        //delaying one second, so the message can be processed
        TimeUnit.SECONDS.sleep(1);
        List<RfidTagTime> rfidTagTimes = new ArrayList<>();
        rfidTagTimeRepository.findAll().forEach(rfidTagTimes::add);
        assertEquals(2, rfidTagTimes.size());
    }

    @Test
    void testRfidAndTimesRequestService() throws JMSException, JsonProcessingException {

        List<RfidTag> rfidTags = Arrays.asList(new RfidTag("1"), new RfidTag("2"));


        List<RfidTagTime> tagTimes = Arrays.asList(
                new RfidTagTime("1", LocalTime.now()),
                new RfidTagTime("2", LocalTime.now()),
                new RfidTagTime("1", LocalTime.now()),
                new RfidTagTime("2", LocalTime.now()),
                new RfidTagTime("1", LocalTime.now()));
        rfidTagTimeRepository.saveAll(tagTimes);

        Message response = jmsTemplate.sendAndReceive(MessagingConfiguration.RFID_AND_TIMES_REQUEST_QUEUE, session -> {
            try {
                String requestContent = objectMapper.writeValueAsString(
                        RfidTagCollection
                                .builder()
                                .rfidTags(rfidTags)
                                .build());
                Message result = session.createTextMessage(requestContent
                        );
                result.setStringProperty("_type", RfidTagCollection.class.getCanonicalName());
                return result;
            } catch (JsonProcessingException e) {
                throw new JMSException("createMessage failed");
            }
        });

        Assertions.assertNotNull(response);
        String content = response.getBody(String.class);
        RfidTagAndTimesCollection responseMessage = objectMapper.readValue(content,
                RfidTagAndTimesCollection.class);
        Assertions.assertFalse(responseMessage.getRfidAndReadTimes().isEmpty());
        Map<RfidTag, List<LocalTime>> result = responseMessage.getTimesByRfid();
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(3, result.get(new RfidTag("1")).size());
        Assertions.assertEquals(2, result.get(new RfidTag("2")).size());
    }
}
