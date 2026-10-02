package CivicLens.Backend.issue;

import CivicLens.Backend.user.AppUser;
import CivicLens.Backend.user.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class IssueService {

    public List<PublicIssueResponse> getPublicIssues() {

    return issueRepository.findAll()
            .stream()
            .map(issue -> new PublicIssueResponse(
                    issue.getId(),
                    issue.getType(),
                    issue.getDescription(),
                    issue.getLatitude(),
                    issue.getLongitude(),
                    issue.getSeverity(),
                    issue.getStatus(),
                    issue.getCreatedAt()
            ))
            .toList();
}

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;

    public IssueService(
            IssueRepository issueRepository,
            UserRepository userRepository) {

        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    public Issue createIssue(Issue issue, String email) {

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        issue.setReportedBy(user);

        return issueRepository.save(issue);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public List<Issue> getMyIssues(String email) {
        return issueRepository
                .findByReportedByEmailOrderByCreatedAtDesc(email);
    }

    public Issue getIssueById(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Issue not found"
                ));
    }

    public Issue updateStatus(Long id, IssueStatus status) {

        Issue issue = getIssueById(id);

        issue.setStatus(status);

        return issueRepository.save(issue);
    }

    public void deleteIssue(Long id) {

        Issue issue = getIssueById(id);

        issueRepository.delete(issue);
    }
}