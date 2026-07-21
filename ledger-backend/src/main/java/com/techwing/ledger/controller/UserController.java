package com.techwing.ledger.controller;

import com.techwing.ledger.dto.ApiResponse;
import com.techwing.ledger.model.User;
import com.techwing.ledger.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User Profile", description = "User profile management")
public class UserController {

    private final UserProfileService userProfileService;

    @GetMapping("/profile")
    @Operation(summary = "Get current user's profile")
    public ResponseEntity<ApiResponse<User>> getProfile(Authentication auth) {
        User user = userProfileService.getByEmail(auth.getName());
        user.setPassword(null); // Never return password
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/profile")
    @Operation(summary = "Update current user's profile")
    public ResponseEntity<ApiResponse<User>> updateProfile(
            Authentication auth,
            @RequestBody Map<String, String> body) {
        User currentUser = userProfileService.getByEmail(auth.getName());
        User updated = userProfileService.updateProfile(
                currentUser.getId(),
                body.get("firstName"),
                body.get("lastName"),
                body.get("phone"),
                body.get("profileImage"));
        updated.setPassword(null);
        return ResponseEntity.ok(ApiResponse.success("Profile updated", updated));
    }

    @PutMapping("/password")
    @Operation(summary = "Change current user's password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            Authentication auth,
            @RequestBody Map<String, String> body) {
        User currentUser = userProfileService.getByEmail(auth.getName());
        userProfileService.changePassword(
                currentUser.getId(),
                body.get("currentPassword"),
                body.get("newPassword"));
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true).message("Password changed successfully").build());
    }
}
