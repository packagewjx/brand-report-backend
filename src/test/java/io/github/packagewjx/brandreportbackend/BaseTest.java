package io.github.packagewjx.brandreportbackend;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-7-16
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:test.properties")
@AutoConfigureMockMvc
public abstract class BaseTest {
}
