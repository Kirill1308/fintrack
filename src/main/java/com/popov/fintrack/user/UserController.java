package com.popov.fintrack.user;

import com.popov.fintrack.budget.BudgetService;
import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.exception.ImageUploadException;
import com.popov.fintrack.user.dto.user.ProfileImageDTO;
import com.popov.fintrack.user.dto.user.UserDTO;
import com.popov.fintrack.user.model.ProfileImage;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.utills.validation.OnUpdate;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.dto.WalletDTO;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.mapper.BudgetMapper;
import com.popov.fintrack.web.mapper.ProfileImageMapper;
import com.popov.fintrack.web.mapper.UserMapper;
import com.popov.fintrack.web.mapper.WalletMapper;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "API related to user management")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ProfileImageMapper profileImageMapper;

    private final WalletService walletService;
    private final BudgetService budgetService;
    private final WalletMapper walletMapper;
    private final BudgetMapper budgetMapper;

    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.hasAccessUser(#id)")
    @Operation(summary = "Get user details", description = "Retrieves the details of a user by ID")
    @ApiResponses(value = {
            @ApiResponse(description = "User details", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public UserDTO getById(@PathVariable Long id) {
        log.info("Fetching user details for ID: {}", id);
        User user = userService.getUserById(id);
        log.info("User details fetched successfully for ID: {}", user.getUsername());
        return userMapper.toDto(user);
    }

    @PutMapping
    @PreAuthorize("@customSecurityExpression.hasAccessUser(#userDTO.id)")
    @Operation(summary = "Update user details", description = "Updates the details of an existing user")
    @ApiResponses(value = {
            @ApiResponse(description = "User details updated successfully", responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public UserDTO update(@Validated(OnUpdate.class) @RequestBody UserDTO userDTO) {
        log.info("Updating user details for ID: {}", userDTO.getId());
        User user = userMapper.toEntity(userDTO);
        User updatedUser = userService.updateUser(user);
        return userMapper.toDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.hasAccessUser(#id)")
    @Operation(summary = "Delete user", description = "Deletes a user by ID")
    @ApiResponses(value = {
            @ApiResponse(description = "User deleted successfully", responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public void deleteById(@PathVariable Long id) {
        log.info("Deleting user with ID: {}", id);
        userService.deleteUserById(id);
    }

    @PostMapping("/image")
    @Operation(summary = "Upload profile image", description = "Uploads a new profile image for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(description = "Profile image uploaded successfully", responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public void uploadImage(@Validated @ModelAttribute ProfileImageDTO profileImageDTO) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        log.info("Uploading profile image for user ID: {}", userId);
        if (userService.getUserById(userId).getFile() != null) {
            throw new ImageUploadException("User can have only one profile image.");
        }
        ProfileImage profileImage = profileImageMapper.toEntity(profileImageDTO);
        userService.uploadProfileImage(userId, profileImage);
    }

    @PutMapping("/image")
    @Operation(summary = "Update profile image", description = "Updates the profile image for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(description = "Profile image updated successfully", responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public void updateImage(@Validated @ModelAttribute ProfileImageDTO profileImageDTO) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        log.info("Updating profile image for user ID: {}", userId);

        ProfileImage profileImage = profileImageMapper.toEntity(profileImageDTO);
        userService.uploadProfileImage(userId, profileImage);
    }

    @DeleteMapping("/image")
    @Operation(summary = "Delete profile image", description = "Deletes the profile image of the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(description = "Profile image deleted successfully", responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public void deleteImage() {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        log.info("Deleting profile image for user ID: {}", userId);
        userService.deleteProfileImage(userId);
    }

    @GetMapping("/{userId}/wallets")
    @PreAuthorize("@customSecurityExpression.hasAccessUser(#userId)")
    @Operation(summary = "Get user wallets", description = "Retrieves the wallets associated with a user by user ID")
    @ApiResponses(value = {
            @ApiResponse(description = "List of wallets", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = WalletDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public List<WalletDTO> getWallets(@PathVariable Long userId) {
        log.info("Fetching wallets for user ID: {}", userId);
        List<Wallet> wallets = walletService.getWallets(userId);
        return walletMapper.toDto(wallets);
    }

    @GetMapping("/{userId}/budgets")
    @PreAuthorize("@customSecurityExpression.hasAccessUser(#userId)")
    @Operation(summary = "Get user budgets", description = "Retrieves the budgets associated with a user by user ID")
    @ApiResponses(value = {
            @ApiResponse(description = "List of budgets", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = BudgetDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access Denied")
    })
    public List<BudgetDTO> getBudgetsByUserId(@PathVariable Long userId) {
        log.info("Fetching budgets for user ID: {}", userId);
        List<Budget> budgets = budgetService.getBudgets(userId);
        return budgetMapper.toDto(budgets);
    }
}
