package io.devarc.rfidtiming.rfidmanaging;

import io.devarc.db.mongodb.MongoDbCleanUpExtension;
import io.devarc.rfidtiming.rfidmanaging.api.RfidTag;
import io.devarc.rfidtiming.rfidmanaging.core.model.RfidTagTime;
import io.devarc.rfidtiming.rfidmanaging.core.services.RfidAndTimesService;
import io.devarc.rfidtiming.rfidmanaging.core.services.RfidTagTimeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ExtendWith(MongoDbCleanUpExtension.class)
public class RfidTagAndTimesServiceTests {
    @Autowired
    private RfidAndTimesService rfidAndTimesService;
    @Autowired
    private RfidTagTimeRepository rfidTagTimeRepository;
    @Test
    public void findTimesWithTwoTagsWithMultipleTimes() {
        List<RfidTag> rfidTags = Arrays.asList(new RfidTag("1"), new RfidTag("2"));


        List<RfidTagTime> tagTimes = Arrays.asList(
                new RfidTagTime("1", LocalTime.now()),
                new RfidTagTime("2", LocalTime.now()),
                new RfidTagTime("1", LocalTime.now()),
                new RfidTagTime("2", LocalTime.now()),
                new RfidTagTime("1", LocalTime.now()));
        rfidTagTimeRepository.saveAll(tagTimes);

        Map<RfidTag, List<LocalTime>> result = rfidAndTimesService.getTimesByRfids(rfidTags);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(3, result.get(new RfidTag("1")).size());
        Assertions.assertEquals(2, result.get(new RfidTag("2")).size());
    }
}
