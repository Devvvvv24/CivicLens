package CivicLens.Backend.user;

public record AuthResponse(
        String token,
        Long id,
        String name,
        String email,
        Role role
) {
}