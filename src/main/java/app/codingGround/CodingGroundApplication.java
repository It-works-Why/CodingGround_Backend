package app.codingGround;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CodingGroundApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodingGroundApplication.class, args);
    }

}
