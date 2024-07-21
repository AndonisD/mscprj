package uk.ac.kcl.mscPrj.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import uk.ac.kcl.mscPrj.dto.*;
import uk.ac.kcl.mscPrj.model.*;
import uk.ac.kcl.mscPrj.payload.AbstractResponse;
import uk.ac.kcl.mscPrj.payload.DataResponse;
import uk.ac.kcl.mscPrj.payload.StatusResponse;
import uk.ac.kcl.mscPrj.repository.*;
import uk.ac.kcl.mscPrj.service.PostService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final RateRepository rateRepository;
    private final ReportRepository reportRepository;

    @Override
    public AbstractResponse addNewPost(SubmitPostDTO post, Authentication authentication) {
        User poster = userRepository.findByUsername(authentication.getName());
        Post newPost = new Post(post.getBody(), poster, post.getIsAnonymous());
        postRepository.save(newPost);
        return new StatusResponse("Post submitted successfully", HttpStatus.CREATED);
    }
    @Override
    public AbstractResponse addNewReply(SubmitReplyDTO reply, Authentication authentication) {
        User poster = userRepository.findByUsername(authentication.getName());
        Post post = postRepository.getReferenceById(reply.getPostId());
        Reply newReply = new Reply(poster, post, reply.getBody());
        replyRepository.save(newReply);
        return new StatusResponse("Reply submitted successfully", HttpStatus.CREATED);
    }

    @Override
    public AbstractResponse rateReply(RateReplyDTO rateReplyDTO, Authentication authentication) {
        Reply reply = replyRepository.getReferenceById(rateReplyDTO.getReplyId());
        User voter = userRepository.findByUsername(authentication.getName());
        Rating existingingRating = rateRepository.findByReplyAndRater(reply, voter);

        if (existingingRating == null) {
            Rating newRating = new Rating(reply, voter, rateReplyDTO.getUpVote());
            rateRepository.save(newRating);
        } else if (existingingRating.getUpVote() != rateReplyDTO.getUpVote()) {
            existingingRating.setUpVote(rateReplyDTO.getUpVote());
            rateRepository.save(existingingRating);
        }

        return new StatusResponse("Rating submitted successfully", HttpStatus.CREATED);
    }

    @Override
    public AbstractResponse getPost(Long id) {
        Post post = postRepository.getReferenceById(id);
        List<Reply> replies = replyRepository.findAllByPost(post);
        List<GetReplyDTO> repliesDTO = new ArrayList<>();
        replies.forEach(reply -> {
            Integer score = rateRepository.findAllByReply(reply).stream().mapToInt(rating -> rating.getUpVote() ? 1 : -1).sum();
            repliesDTO.add(new GetReplyDTO(reply.getPoster().getUsername(), reply.getBody(), score));
        });

        String username = post.getIsAnonymous() ? "Anonymous" :  post.getPoster().getUsername();

        GetPostDTO responsePost = new GetPostDTO(username, post.getBody(), repliesDTO);

        return new DataResponse(responsePost, HttpStatus.CREATED);
    }

    @Override
    public AbstractResponse reportPost(ReportDTO reportDTO, Authentication authentication) {
        User reporter = userRepository.findByUsername(authentication.getName());
        Optional<Post> post = postRepository.findById(reportDTO.getReportableId());
        if (post.isEmpty()){
            return new StatusResponse("Post not found", HttpStatus.NOT_FOUND);
        }
        Report report = new Report(reporter, post.get(), "Post");
        reportRepository.save(report);
        return new StatusResponse("Report submitted successfully", HttpStatus.CREATED);
    }

    @Override
    public AbstractResponse reportReply(ReportDTO reportDTO, Authentication authentication) {
        User reporter = userRepository.findByUsername(authentication.getName());
        Optional<Reply> reply = replyRepository.findById(reportDTO.getReportableId());
        if (reply.isEmpty()){
            return new StatusResponse("Reply not found", HttpStatus.NOT_FOUND);
        }
        Report report = new Report(reporter, reply.get(), "Reply");
        reportRepository.save(report);
        return new StatusResponse("Report submitted successfully", HttpStatus.CREATED);
    }

    @Override
    public AbstractResponse getReports() {
        Map<String, Integer> reportsMap = new HashMap<>();


        Map<String, Integer> finalReportsMap = reportsMap;
        
        reportRepository.findAll().forEach(report -> {
            String key = report.getReportableType() + " | " + report.getReportable().getId();
            finalReportsMap.put(key, finalReportsMap.getOrDefault(key, 0) + 1);
        });

        reportsMap = finalReportsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        GetReportsDTO reportsDTO = new GetReportsDTO(reportsMap);
        return new DataResponse(reportsDTO, HttpStatus.CREATED);
    }
}
