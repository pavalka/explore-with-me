package ru.practicum.explorewithme.services.users;

import ru.practicum.explorewithme.dto.users.NewUserRequest;
import ru.practicum.explorewithme.dto.users.UserDto;
import ru.practicum.explorewithme.entities.users.User;
import ru.practicum.explorewithme.exceptions.users.UserNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.practicum.explorewithme.mappers.users.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.pageable.PageableByOffsetAndSize;
import ru.practicum.explorewithme.repositories.users.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, ThirdPartUserService{
    private final UserRepository userRepository;

    @Override
    public User getUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public List<User> getAllUsersByIds(Collection<Long> userIds) {
        return userRepository.findAllById(userIds);
    }

    @Override
    public List<UserDto> getUsers(@NonNull List<Long> usersIds) {
        return UserMapper.mapToFullDto(
                userRepository.findAllById(usersIds));
    }

    @Override
    public List<UserDto> getUsers(int from, int size) {
        return UserMapper.mapToFullDto(
                userRepository.findAll(
                        new PageableByOffsetAndSize(from, size, Sort.unsorted())
                        )
                .toList());
    }

    @Override
    @Transactional
    public UserDto createUser(@NonNull NewUserRequest user) {
        return UserMapper.mapToFullDto(
                userRepository.save(
                        UserMapper.mapFromDto(user)
                )
        );
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        userRepository.delete(getUser(userId));
    }
}
