package CivicLens.Backend.issue;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import CivicLens.Backend.user.AppUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "issues")
public class Issue { 

    @ManyToOne
    @JoinColumn(name = "reported_by_user_id")
    @JsonIgnore
    private AppUser reportedBy;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private IssueType type;

    private String description;

    private Double latitude;

    private Double longitude;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    @Enumerated(EnumType.STRING)
    private IssueStatus status;

    private LocalDateTime createdAt;

    public Issue() {
    }

    public Issue(
            IssueType type,
            String description,
            Double latitude,
            Double longitude,
            String imageUrl,
            Severity severity) {

        this.type = type;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageUrl = imageUrl;
        this.severity = severity;

        this.status = IssueStatus.REPORTED;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public IssueType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Severity getSeverity() {
        return severity;
    }

    public IssueStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;

    }

    public void setStatus(IssueStatus status) {
        this.status = status;

    }

    public void setReportedBy(AppUser reportedBy) {
        this.reportedBy = reportedBy;
    }

    public Long getReportedById() {
        return reportedBy != null ? reportedBy.getId() : null;
    }

    public String getReportedByName() {
        return reportedBy != null ? reportedBy.getName() : null;
    }

}