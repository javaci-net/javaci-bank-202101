package net.javaci.bank202101;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class JavaciBankApiApp {

    public static void main(String[] args) {
        try {
            SpringApplication.run(JavaciBankApiApp.class, args);
        } catch(Throwable e) {
            if(e.getClass().getName().contains("SilentExitException")) {
                // skipping for spring known bug https://github.com/spring-projects/spring-boot/issues/3100
                log.debug("Spring is restarting the main thread - See spring-boot-devtools");
            } else {
                throw e;
            }
        }
    }

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

}
