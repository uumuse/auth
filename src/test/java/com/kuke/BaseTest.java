package com.kuke;

import org.junit.runner.RunWith;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@Component
@ContextConfiguration(locations = { "classpath:spring.xml", "classpath:spring-mybatis.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class BaseTest {

}
