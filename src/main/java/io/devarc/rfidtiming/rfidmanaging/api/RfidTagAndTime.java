package io.devarc.rfidtiming.rfidmanaging.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RfidTagAndTime {
    private RfidTag rfId;
    private LocalTime time;
}
