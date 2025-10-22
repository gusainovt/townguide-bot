package io.project.townguidebot.mapper;

import io.project.townguidebot.dto.response.UserResponse;
import io.project.townguidebot.dto.response.UsersResponse;
import io.project.townguidebot.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

  UserResponse toResponse(User user);

  List<UserResponse> toResponseList(List<User> users);

  default UsersResponse toUsersResponse(Page<User> page) {
    return UsersResponse.builder()
        .users(toResponseList(page.getContent()))
        .build();
  }
}
