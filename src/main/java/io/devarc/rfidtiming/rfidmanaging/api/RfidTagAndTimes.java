package io.devarc.rfidtiming.rfidmanaging.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class RfidTagAndTimes {
    private RfidTag rfidTag;
    private List<LocalTime> times;
}
