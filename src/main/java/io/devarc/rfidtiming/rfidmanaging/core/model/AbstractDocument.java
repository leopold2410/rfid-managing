package io.devarc.rfidtiming.rfidmanaging.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractDocument {
    @Id
    private BigInteger id;
}
