package io.devarc.rfidtiming.rfidmanaging.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Document
@AllArgsConstructor
public class RfidTagTime extends AbstractDocument {
    @Field(name = "rfidTag")
    @Indexed(name = "idx_rfid_tag_time_rfid_tag")
    private String rfidTag;

    @Field(name = "time")
    private LocalTime time;
}
