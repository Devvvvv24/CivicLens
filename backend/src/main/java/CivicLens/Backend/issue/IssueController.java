package CivicLens.Backend.issue;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {

    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    public ResponseEntity<Issue> createIssue(
            @RequestBody Issue issue,
            Authentication authentication) {

        Issue savedIssue = issueService.createIssue(
                issue,
                authentication.getName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedIssue);
    }

    @GetMapping
    public List<Issue> getAllIssues() {
        return issueService.getAllIssues();
    }

    @GetMapping("/my")
    public List<Issue> getMyIssues(Authentication authentication) {

        return issueService.getMyIssues(
                authentication.getName()
        );
    }

    @GetMapping("/{id}")
    public Issue getIssueById(@PathVariable Long id) {
        return issueService.getIssueById(id);
    }

    @PatchMapping("/{id}/status")
    public Issue updateStatus(
            @PathVariable Long id,
            @RequestParam IssueStatus status) {

        return issueService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {

        issueService.deleteIssue(id);

        return ResponseEntity.noContent().build();
    }
}