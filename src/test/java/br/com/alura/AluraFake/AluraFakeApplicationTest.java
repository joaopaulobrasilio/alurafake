package br.com.alura.AluraFake;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


@WebMvcTest(AluraFakeApplication.class)
class AluraFakeApplicationTest {

    @Test
    void should_start_application() {
        AluraFakeApplication.main(new String[]{});
    }


}