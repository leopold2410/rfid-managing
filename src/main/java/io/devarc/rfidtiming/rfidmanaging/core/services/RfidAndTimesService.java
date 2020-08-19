package io.devarc.rfidtiming.rfidmanaging.core.services;

import io.devarc.rfidtiming.rfidmanaging.api.RfidTag;
import io.devarc.rfidtiming.rfidmanaging.core.model.RfidTagTime;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RfidAndTimesService {
    private final RfidTagTimeRepository rfidTagTimeRepository;

    public RfidAndTimesService(RfidTagTimeRepository rfidTagTimeRepository) {
        this.rfidTagTimeRepository = rfidTagTimeRepository;
    }

    public Map<RfidTag, List<LocalTime>> getTimesByRfids(List<RfidTag> rfidTags) {
        Map<RfidTag, List<LocalTime>> result = new HashMap<>();
        List<RfidTagTime> tagTimes = rfidTagTimeRepository.findRfidTagTimesByRfidTagIn(
                rfidTags.stream().map(RfidTag::getId).collect(Collectors.toList()));

        tagTimes.forEach(rfidTagTime -> {
            result.merge(new RfidTag(rfidTagTime.getRfidTag()), Collections.singletonList(rfidTagTime.getTime()), (prevList, newList) ->
            {
                List<LocalTime> resultingTimes = new ArrayList<>();
                resultingTimes.addAll(prevList);
                resultingTimes.addAll(newList);
                return  resultingTimes;
            });
        });

        return result;
    }
}
