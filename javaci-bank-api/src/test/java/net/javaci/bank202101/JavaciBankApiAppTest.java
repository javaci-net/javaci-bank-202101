package net.javaci.bank202101;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class JavaciBankApiAppTest {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Test
    void contextLoads() {
        
        List<String> beanDefinitonNames = Arrays.asList( applicationContext.getBeanDefinitionNames() );
        
        assertTrue(beanDefinitonNames.contains("apiSecurityConfig"));
        
        assertThat(beanDefinitonNames, hasItem("apiSecurityConfig") );  
    }

}
