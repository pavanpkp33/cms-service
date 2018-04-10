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
}
