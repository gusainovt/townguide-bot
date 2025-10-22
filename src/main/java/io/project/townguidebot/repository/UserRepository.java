package io.project.townguidebot.repository;

import io.project.townguidebot.model.User;
import jakarta.ws.rs.QueryParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Boolean existsUserByChatId(Long chatId);

    @Query("SELECT u.firstName FROM User u WHERE u.chatId = :chatId")
    String getNameByChatId(@QueryParam("chatId") Long chatId);
}
