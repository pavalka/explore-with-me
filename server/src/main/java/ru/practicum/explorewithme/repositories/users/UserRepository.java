package ru.practicum.explorewithme.repositories.users;

import ru.practicum.explorewithme.entities.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
