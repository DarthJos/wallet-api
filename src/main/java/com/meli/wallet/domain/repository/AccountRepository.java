package com.meli.wallet.domain.repository;

import com.meli.wallet.domain.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findByIdForUpdate(UUID id); // El "ForUpdate" es la clave del bloqueo
    void save(Account account);
}
