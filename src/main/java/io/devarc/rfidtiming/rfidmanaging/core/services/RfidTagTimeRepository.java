package io.devarc.rfidtiming.rfidmanaging.core.services;

import io.devarc.rfidtiming.rfidmanaging.core.model.RfidTagTime;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface RfidTagTimeRepository extends PagingAndSortingRepository<RfidTagTime, BigInteger> {
    public List<RfidTagTime> findRfidTagTimesByRfidTagIn(List<String> rfIds);
}
