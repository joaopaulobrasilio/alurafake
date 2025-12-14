package br.com.alura.AluraFake.util;

import br.com.alura.AluraFake.user.Role;
import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;


    @Test
    void should_load_user_successfully_when_user_exists() {

        User user = new User("Joao", "joao@alura.com.br", Role.INSTRUCTOR);

        when(userRepository.findByEmail("joao@alura.com.br"))
                .thenReturn(Optional.of(user));

        UserDetails userDetails =
                authenticationService.loadUserByUsername("joao@alura.com.br");

        assertNotNull(userDetails);
        assertEquals("joao@alura.com.br", userDetails.getUsername());
        assertEquals("{noop}" + user.getPassword(), userDetails.getPassword());


        assertTrue(
                userDetails.getAuthorities().stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"))
        );

        verify(userRepository).findByEmail("joao@alura.com.br");
    }

    @Test
    void should_throw_exception_when_user_not_found() {

        when(userRepository.findByEmail("naoexiste@alura.com.br"))
                .thenReturn(Optional.empty());

        UsernameNotFoundException exception =
                assertThrows(UsernameNotFoundException.class, () -> {
                    authenticationService.loadUserByUsername("naoexiste@alura.com.br");
                });

        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(userRepository).findByEmail("naoexiste@alura.com.br");
    }

    @Test
    void should_return_student_role_correctly() {

        User user = new User(
                "Maria",
                "maria@alura.com.br",
                Role.STUDENT
        );
        when(userRepository.findByEmail("maria@alura.com.br"))
                .thenReturn(Optional.of(user));
        UserDetails userDetails =
                authenticationService.loadUserByUsername("maria@alura.com.br");
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_STUDENT"))
        );
    }


}