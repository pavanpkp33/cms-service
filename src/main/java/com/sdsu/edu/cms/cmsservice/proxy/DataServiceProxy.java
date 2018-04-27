package com.sdsu.edu.cms.cmsservice.proxy;

import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.cms.Submission;
import com.sdsu.edu.cms.common.models.cms.Track;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "data-service")
@RibbonClient(name = "data-service")
public interface DataServiceProxy {

    @PostMapping("/api/v1/conferences/create")
    ServiceResponse createConference(@RequestBody Conference conference);

    @PostMapping("/api/v1/conferences/tracks")
    ServiceResponse addTracks(@RequestBody List<Track> tracks, @RequestParam Map<String, String> map);

    @PostMapping("/api/v1/conferences/get/name")
    ServiceResponse getConferenceByName(@RequestParam Map<String, String> map);

    @PostMapping("/api/v1/conferences/get/tracks")
    ServiceResponse getTracks(@RequestParam Map<String, String> map);

    @PostMapping("/api/v1/conferences/update")
    ServiceResponse updateConference(@RequestBody Conference conference, @RequestParam Map<String, String> map);

    @PostMapping("/api/v1/submissions/create")
    ServiceResponse createSubmission(@RequestBody Submission submission);

    @PostMapping("/api/v1/submissions/update")
    ServiceResponse updateSubmission(@RequestBody Submission submission);

    @PostMapping("/api/v1/submissions/get")
    ServiceResponse getSubmission(@RequestParam Map<String, String> params);

    @PostMapping("/api/v1/submissions/delete")
    ServiceResponse deleteConference(@RequestParam Map<String, String> mp);

    @PostMapping("/api/v1/submissions/patch")
    ServiceResponse patchConference(@RequestBody String sid, @RequestParam Map<String, Object> mp);

    @PostMapping("/api/v1/conferences/delete")
    ServiceResponse deleteConferences(@RequestParam Map<String, String> map);

    @PostMapping("/api/v1/submissions/author/delete")
    ServiceResponse deleteAuthor(@RequestParam Map<String, String> params);

    @PostMapping("/api/v1/submissions/files/delete")
    ServiceResponse deleteFiles(@RequestParam Map<String, String> params);

    @PostMapping("/api/v1/conferences/users")
    ServiceResponse getConferenceUsers(@RequestParam Map<String, String> params);

    @PostMapping("/api/v1/conferences/users/delete")
    ServiceResponse deleteUserRoles(@RequestParam Map<String, String> params);

    @PostMapping("/api/v1/submissions/get/user")
    ServiceResponse getSubmissionForUser(@RequestParam  Map<String, String> params);

    @PostMapping("/api/v1/conferences/tracks/delete")
    ServiceResponse deleteTrack(@RequestParam Map<String, Integer> params);
}
