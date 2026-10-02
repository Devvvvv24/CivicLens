package CivicLens.Backend.issue;

import java.time.LocalDateTime;

public record PublicIssueResponse(
        Long id,
        IssueType type,
        String description,
        Double latitude,
        Double longitude,
        Severity severity,
        IssueStatus status,
        LocalDateTime createdAt
) {
}