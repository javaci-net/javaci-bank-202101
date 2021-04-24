package net.javaci.bank202101;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
public class JavaciBankApiAppTest {

    @Autowired
    private ApplicationContext applicationContext;
    
    @Test
    public void contextLoads() {
        
        List<String> beanDefinitionNames = Arrays.asList( applicationContext.getBeanDefinitionNames() );
        
        checkBean("apiSecurityConfig", beanDefinitionNames);
        
    }
    
    private void checkBean(String beanName, List<String> beanDefinitionNames) {
        assertThat(beanName + " exists", beanDefinitionNames, hasItem(beanName) );
    }

}
