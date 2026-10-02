package CivicLens.Backend.issue;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/issues")
public class PublicIssueController {

    private final IssueService issueService;

    public PublicIssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @GetMapping
    public List<PublicIssueResponse> getPublicIssues() {
        return issueService.getPublicIssues();
    }
}