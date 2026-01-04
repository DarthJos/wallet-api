package com.meli.wallet.application.service;

import com.meli.wallet.domain.model.Account;
import com.meli.wallet.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountRepository accountRepository;

    @Transactional // Abre una transacción de base de datos
    public void transfer(UUID sourceId, UUID destinationId, BigDecimal amount) {

        // 1. Bloquear y obtener las cuentas (Pessimistic Lock)
        Account source = accountRepository.findByIdForUpdate(sourceId)
                .orElseThrow(() -> new RuntimeException("ALERTA! Cuenta de origen no encontrada..."));

        Account destination = accountRepository.findByIdForUpdate(destinationId)
                .orElseThrow(() -> new RuntimeException("ALERTA! Cuenta de destino no encontrada..."));

        // 2. Validar saldo
        if (source.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("ALERTA! Saldo insuficiente...");
        }

        // 3. Ejecutar el movimiento de dinero
        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));

        // 4. Guardar cambios
        accountRepository.save(source);
        accountRepository.save(destination);

        // Al terminar el method, Spring hace el COMMIT automáticamente.
        // Si hay una excepción, hace ROLLBACK.
    }
}
