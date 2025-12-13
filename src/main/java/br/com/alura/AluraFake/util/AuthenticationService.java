package br.com.alura.AluraFake.util;

import br.com.alura.AluraFake.user.User;
import br.com.alura.AluraFake.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        System.out.println("Autenticando: " + username + " / " + user.getPassword() + " / " + user.getRole());

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password("{noop}" + user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
