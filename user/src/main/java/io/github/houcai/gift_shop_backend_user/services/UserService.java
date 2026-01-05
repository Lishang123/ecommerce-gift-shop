package io.github.houcai.gift_shop_backend_user.services;

import io.github.houcai.gift_shop_backend_user.dtos.UserRequest;
import io.github.houcai.gift_shop_backend_user.dtos.UserResponse;
import io.github.houcai.gift_shop_backend_user.mappers.ResponseMapper;
import io.github.houcai.gift_shop_backend_user.models.User;
import io.github.houcai.gift_shop_backend_user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest userRequest){
        User user = new User();
        user.updateFrom(userRequest);
        userRepository.save(user);
        return ResponseMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers(){
        return userRepository.findAll()
                .stream()
                .map(ResponseMapper::toResponse)
                .toList();
    }

    public Optional<UserResponse> getUser(Long id){
        return userRepository.findById(id).map(ResponseMapper::toResponse);
    }

    public boolean updateUser(Long id, UserRequest updateUserRequest){
        return userRepository.findById(id).map(
                user -> {
                    user.updateFrom(updateUserRequest);
                    userRepository.save(user);
                    return true;
                }
        ).orElse(false);
    }

    public void deleteUser(Long id){
         userRepository.deleteById(id);
    }
}
