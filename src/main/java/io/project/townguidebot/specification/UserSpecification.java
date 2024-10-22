package io.project.townguidebot.specification;

import io.project.townguidebot.dto.request.UserFilterRequest;
import io.project.townguidebot.model.User;
import jakarta.persistence.criteria.Predicate;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class UserSpecification {

  public static Specification<User> filterBy(UserFilterRequest filter) {
    return (root, query, cb) -> {

      Predicate andPredicate = cb.conjunction();
      Predicate orPredicate = cb.disjunction();

      if (filter.getFirstName() != null && !filter.getFirstName().isEmpty()) {
        orPredicate = cb.or(orPredicate,
            cb.like(cb.lower(root.get("firstName")),
                "%" + filter.getFirstName().toLowerCase() + "%"));
      }

      if (filter.getLastName() != null && !filter.getLastName().isEmpty()) {
        orPredicate = cb.or(orPredicate,
            cb.like(cb.lower(root.get("lastName")),
                "%" + filter.getLastName().toLowerCase()));
      }

      if (filter.getUserName() != null && !filter.getUserName().isEmpty()) {
        orPredicate = cb.or(orPredicate,
            cb.like(cb.lower(root.get("userName")),
                "%" + filter.getUserName().toLowerCase() + "%"));
      }

      if (filter.getPhoneNumber() != null && !filter.getPhoneNumber().isEmpty()) {
        orPredicate = cb.or(orPredicate,
            cb.equal(root.get("phoneNumber"), filter.getPhoneNumber()));
      }

      if (filter.getRegisteredFrom() != null && filter.getRegisteredTo() != null) {
        andPredicate = cb.and(andPredicate,
            cb.between(root.get("registeredAt"),
                filter.getRegisteredFrom(),
                filter.getRegisteredTo()));
      }

      return cb.and(andPredicate, orPredicate);
    };
  }
}
