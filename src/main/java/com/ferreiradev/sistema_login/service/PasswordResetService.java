package com.ferreiradev.sistema_login.service;

import com.ferreiradev.sistema_login.exception.InvalidTokenException;
import com.ferreiradev.sistema_login.model.PasswordResetToken;
import com.ferreiradev.sistema_login.model.User;
import com.ferreiradev.sistema_login.repository.PasswordResetTokenRepository;
import com.ferreiradev.sistema_login.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Transactional
    public void requestForgotPassword(String email) {
        log.info("Solicitação de reset de senha recebida");

        // Busca silenciosa, nao lança exception. Evita user enumeration
        userRepository.findByEmail(email).ifPresentOrElse(
                this::createAnSendResetToken,
                () -> log.warn("Solicitação de redefinição de senha para e-mail inexistente")
        );

    }

    private void createAnSendResetToken(User user) {
        // Limpa token anterior se existir
        PasswordResetToken existingToken = passwordResetTokenRepository.findByUser(user).orElse(null);
        if (existingToken != null) {
            passwordResetTokenRepository.delete(existingToken);
        }

        PasswordResetToken token = new PasswordResetToken(user); // objeto ja é criado com codigo gerado
        passwordResetTokenRepository.saveAndFlush(token); //

        emailService.sendPasswordResetEmail(user.getEmail(), token.getToken());
    }

    @Transactional
    public void resetPassword(String code, String newPassword) {
        log.info("Tentativa de reset de senha");

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(code)
                .orElseThrow(() -> new InvalidTokenException("Código de redefinição de senha inválido ou expirado"));

        if (!resetToken.isValid()) {
            log.warn("Token de reset rejeitado: userId={}, used={} expiresAt={}",
                    resetToken.getUser().getId(), resetToken.isUsed(), resetToken.getExpiresAt());
            throw new InvalidTokenException("Código de redefinição de senha inválido ou expirado");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.markAsUsed(); // marca token como usado
        passwordResetTokenRepository.save(resetToken);

        // derruba sessoes ativas do user
        int revoke = refreshTokenService.revokeAllForUser(user);
        log.info("Senha redefinida com sucesso: userId={}, refreshTokenRevogados={}", user.getId(), revoke);
    }

}
