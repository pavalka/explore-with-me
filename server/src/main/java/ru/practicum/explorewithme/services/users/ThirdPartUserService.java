package ru.practicum.explorewithme.services.users;

import ru.practicum.explorewithme.entities.users.User;

import java.util.Collection;
import java.util.List;

public interface ThirdPartUserService {
    User getUser(long userId);

    List<User> getAllUsersByIds(Collection<Long> userIds);
}
