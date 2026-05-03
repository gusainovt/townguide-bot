package io.project.townguidebot.repository;

import io.project.townguidebot.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Boolean existsUserByChatId(Long chatId);

    Optional<User> findByChatId(Long chatId);

    Optional<User> findByLogin(String login);

    boolean existsByLogin(String login);

    @Query("SELECT u.firstName FROM User u WHERE u.chatId = :chatId")
    String getNameByChatId(@Param("chatId") Long chatId);
}
