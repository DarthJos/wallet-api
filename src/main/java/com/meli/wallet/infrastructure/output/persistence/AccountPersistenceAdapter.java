package com.meli.wallet.infrastructure.output.persistence;

import com.meli.wallet.domain.model.Account;
import com.meli.wallet.domain.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements AccountRepository {

    private final JpaAccountRepository jpaAccountRepository;

    @Override
    public Optional<Account> findByIdForUpdate(UUID id) {
        return jpaAccountRepository.findByIdForUpdate(id);
    }

    @Override
    public void save(Account account) {
        jpaAccountRepository.save(account);
    }
}
