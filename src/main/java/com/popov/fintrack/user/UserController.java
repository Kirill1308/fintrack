package com.popov.fintrack.user;

import com.popov.fintrack.budget.BudgetService;
import com.popov.fintrack.budget.dto.BudgetDTO;
import com.popov.fintrack.budget.model.Budget;
import com.popov.fintrack.exception.ImageUploadException;
import com.popov.fintrack.user.dto.user.ProfileImageDTO;
import com.popov.fintrack.user.dto.user.UserDTO;
import com.popov.fintrack.user.model.ProfileImage;
import com.popov.fintrack.user.model.User;
import com.popov.fintrack.validation.OnUpdate;
import com.popov.fintrack.wallet.WalletService;
import com.popov.fintrack.wallet.dto.WalletDTO;
import com.popov.fintrack.wallet.model.Wallet;
import com.popov.fintrack.web.mapper.BudgetMapper;
import com.popov.fintrack.web.mapper.ProfileImageMapper;
import com.popov.fintrack.web.mapper.UserMapper;
import com.popov.fintrack.web.mapper.WalletMapper;
import com.popov.fintrack.web.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final ProfileImageMapper profileImageMapper;

    private final WalletService walletService;
    private final BudgetService budgetService;
    private final WalletMapper walletMapper;
    private final BudgetMapper budgetMapper;

    @PutMapping
    @PreAuthorize("@customSecurityExpression.hasAccessUser(#userDTO.id)")
    public UserDTO update(final @Validated(OnUpdate.class) @RequestBody UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.hasAccessUser(#id)")
    public UserDTO getById(final @PathVariable Long id) {
        User user = userService.getUserById(id);
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.hasAccessUser(#id)")
    public void deleteById(final @PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PostMapping("/image")
    public void uploadImage(@Validated @ModelAttribute ProfileImageDTO profileImageDTO) {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        if (userService.getUserById(userId).getFile() != null) {
            throw new ImageUploadException("User can have only one profile image.");
        }
        ProfileImage profileImage = profileImageMapper.toEntity(profileImageDTO);
        userService.uploadProfileImage(userId, profileImage);
    }

    @PutMapping("/image")
    public void updateImage(@Validated @ModelAttribute ProfileImageDTO profileImageDTO) {
        Long userId = SecurityUtils.getAuthenticatedUserId();

        ProfileImage profileImage = profileImageMapper.toEntity(profileImageDTO);
        userService.uploadProfileImage(userId, profileImage);
    }

    @DeleteMapping("/image")
    public void deleteImage() {
        Long userId = SecurityUtils.getAuthenticatedUserId();
        userService.deleteProfileImage(userId);
    }

    @GetMapping("/{userId}/wallets")
    @PreAuthorize("@customSecurityExpression.hasAccessUser(#userId)")
    public List<WalletDTO> getWallets(@PathVariable Long userId) {
        List<Wallet> wallets = walletService.getWallets(userId);
        return walletMapper.toDto(wallets);
    }

    @GetMapping("/{userId}/budgets")
    @PreAuthorize("@customSecurityExpression.hasAccessUser(#userId)")
    public List<BudgetDTO> getBudgetsByUserId(@PathVariable Long userId) {
        List<Budget> ownedBudgets = budgetService.getBudgetsByUserId(userId);
        List<Budget> invitedBudgets = budgetService.getInvitedBudgets(userId);

        List<Budget> allBudgets = new ArrayList<>();
        allBudgets.addAll(ownedBudgets);
        allBudgets.addAll(invitedBudgets);

        return budgetMapper.toDto(allBudgets);
    }
}
