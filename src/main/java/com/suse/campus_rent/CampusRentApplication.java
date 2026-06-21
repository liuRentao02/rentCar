package com.suse.campus_rent;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.suse.campus_rent.mapper")
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableScheduling
public class CampusRentApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusRentApplication.class, args);
    }

}
