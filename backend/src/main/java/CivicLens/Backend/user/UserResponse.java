package CivicLens.Backend.user;

public record UserResponse(
        Long id,
        String name,
        String email,
        Role role
) {
}