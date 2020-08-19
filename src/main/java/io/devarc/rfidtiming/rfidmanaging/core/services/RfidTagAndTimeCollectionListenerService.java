package io.devarc.rfidtiming.rfidmanaging.core.services;

import io.devarc.rfidtiming.rfidmanaging.core.configuration.MessagingConfiguration;
import io.devarc.rfidtiming.rfidmanaging.api.RfidTagAndTime;
import io.devarc.rfidtiming.rfidmanaging.api.RfidTagAndTimeCollection;
import io.devarc.rfidtiming.rfidmanaging.core.model.RfidTagTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RfidTagAndTimeCollectionListenerService {
    private final RfidTagTimeRepository rfidTagTimeRepository;

    public RfidTagAndTimeCollectionListenerService(RfidTagTimeRepository rfidTagTimeRepository) {
        this.rfidTagTimeRepository = rfidTagTimeRepository;
    }

    private static RfidTagTime convert(RfidTagAndTime rfidTagAndTime) {
        RfidTagTime rfidTagTime = new RfidTagTime(rfidTagAndTime.getRfId().getId(), rfidTagAndTime.getTime());
        return rfidTagTime;
    }

    @JmsListener(destination = MessagingConfiguration.RFID_TIMES_QUEUE)
    public void onRfidTimes(@Payload RfidTagAndTimeCollection payload) {
        List<RfidTagTime> rfidTagTimesToPersist = payload.getRfidTagAndTimes()
                .stream()
                .map(RfidTagAndTimeCollectionListenerService::convert)
                .collect(Collectors.toList());

        rfidTagTimeRepository.saveAll(rfidTagTimesToPersist);
    }
}
