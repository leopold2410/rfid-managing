package io.devarc.rfidtiming.rfidmanaging.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RfidTagCollection {
    private List<RfidTag> rfidTags;
}
