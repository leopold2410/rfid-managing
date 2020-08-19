package io.devarc.rfidtiming.rfidmanaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class RfidManagingApplication {


    public static void main(String[] args) {
        SpringApplication.run(RfidManagingApplication.class, args);
    }

}
