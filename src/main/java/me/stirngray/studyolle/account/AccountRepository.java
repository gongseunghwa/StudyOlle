package me.stirngray.studyolle.account;

import me.stirngray.studyolle.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account,Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);
}
