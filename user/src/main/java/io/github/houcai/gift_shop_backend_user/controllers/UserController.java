package io.github.houcai.gift_shop_backend_user.controllers;

import io.github.houcai.gift_shop_backend_user.dtos.UserRequest;
import io.github.houcai.gift_shop_backend_user.dtos.UserResponse;
import io.github.houcai.gift_shop_backend_user.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.createUser(userRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id){
        return userService.getUser(id).map(
                ResponseEntity::ok
        )
                //.orElse(ResponseEntity.notFound().build()); // eager apporach
                .orElseGet(() -> ResponseEntity.notFound().build()); // lazy approach
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest userRequest){
        boolean updated = userService.updateUser(id, userRequest);
        if (updated)
            return ResponseEntity.ok(String.format("User %d updated", id));
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
