package io.devarc.rfidtiming.rfidmanaging.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RfidTagAndTimesCollection {
    private List<RfidTagAndTimes> rfidAndReadTimes;

    public Map<RfidTag, List<LocalTime>> getTimesByRfid() {
        Map<RfidTag, List<LocalTime>> result = new HashMap<>();
        rfidAndReadTimes.forEach(rfidTagAndTimes -> {
            result.put(rfidTagAndTimes.getRfidTag(), rfidTagAndTimes.getTimes());
        });
        return result;
    }
}
