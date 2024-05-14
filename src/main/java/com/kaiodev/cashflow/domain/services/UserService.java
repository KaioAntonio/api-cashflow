package com.kaiodev.cashflow.domain.services;

import com.kaiodev.cashflow.domain.repositories.UserRepository;
import com.kaiodev.cashflow.domain.user.AuthenticationDTO;
import com.kaiodev.cashflow.domain.user.LoginResponseDTO;
import com.kaiodev.cashflow.domain.user.RegisterDTO;
import com.kaiodev.cashflow.domain.user.User;
import com.kaiodev.cashflow.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final TokenService tokenService;


    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    public User getUser(HttpServletRequest request) throws BusinessException {
        String token = request.getHeader("Authorization").replace("Bearer ", "");

        String sub = tokenService.validateToken(token);
        if (sub == null) throw new BusinessException("Token inválido");
        User user = (User) userRepository.findByEmail(sub);
        return user;
    }

    public User createUser(RegisterDTO data) throws BusinessException {
        if (this.userRepository.findByEmail(data.email()) != null)
            throw new BusinessException("Este email já está cadastrado.");

        if (data.password().length() < 5) {
            throw new BusinessException("A senha deve possuir no mínimo 6 caracteres");
        } else if (!Pattern.compile("\\d").matcher(data.password()).find()) {
            throw new BusinessException("A senha deve possuir ao menos 1 número");
        } else if (!Pattern.compile("(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=[{}]:;',./?\\\\])").matcher(data.password())
                .find()) {
            throw new BusinessException("A senha deve conter ao menos 1 caractere especial");
        } else if (!Pattern.compile("(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=[{}]:;',./?\\\\])(?=.*[A-Z])")
                .matcher(data.password()).find()) {
            throw new BusinessException("A senha deve conter ao menos 1 letra maiúscula");
        }

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.email(), data.name(), encryptedPassword);

        userRepository.save(newUser);

        return newUser;
    }

    public LoginResponseDTO login(AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());
        return new LoginResponseDTO(token);
    }
}
