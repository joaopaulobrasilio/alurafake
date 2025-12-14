package br.com.alura.AluraFake;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AluraFakeApplicationTest {

    @Test
    void should_start_application() {
        AluraFakeApplication.main(new String[]{});
    }


}