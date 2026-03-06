package io.project.townguidebot.specification;

import io.project.townguidebot.dto.request.UserFilterRequest;
import io.project.townguidebot.model.User;
import jakarta.persistence.criteria.Predicate;
import java.sql.Timestamp;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

@UtilityClass
public class UserSpecification {

  public static Specification<User> filterBy(UserFilterRequest filter) {
    return (root, query, cb) -> {

      Predicate andPredicate = cb.conjunction();
      Predicate orPredicate = cb.disjunction();
      boolean hasOrFilters = false;

      if (filter.getFirstName() != null && !filter.getFirstName().isEmpty()) {
        orPredicate = cb.or(orPredicate,
            cb.like(cb.lower(root.get("firstName")),
                "%" + filter.getFirstName().toLowerCase() + "%"));
        hasOrFilters = true;
      }

      if (filter.getLastName() != null && !filter.getLastName().isEmpty()) {
        orPredicate = cb.or(orPredicate,
            cb.like(cb.lower(root.get("lastName")),
                "%" + filter.getLastName().toLowerCase() + "%"));
        hasOrFilters = true;
      }

      if (filter.getUserName() != null && !filter.getUserName().isEmpty()) {
        orPredicate = cb.or(orPredicate,
            cb.like(cb.lower(root.get("userName")),
                "%" + filter.getUserName().toLowerCase() + "%"));
        hasOrFilters = true;
      }

      if (filter.getPhoneNumber() != null && !filter.getPhoneNumber().isEmpty()) {
        orPredicate = cb.or(orPredicate,
            cb.equal(root.get("phoneNumber"), filter.getPhoneNumber()));
        hasOrFilters = true;
      }

      if (filter.getRegisteredFrom() != null && filter.getRegisteredTo() != null) {
        Timestamp from = Timestamp.valueOf(filter.getRegisteredFrom());
        Timestamp to = Timestamp.valueOf(filter.getRegisteredTo());
        andPredicate = cb.and(andPredicate,
            cb.between(root.get("registeredAt"),
                from,
                to));
      }

      return hasOrFilters ? cb.and(andPredicate, orPredicate) : andPredicate;
    };
  }
}
