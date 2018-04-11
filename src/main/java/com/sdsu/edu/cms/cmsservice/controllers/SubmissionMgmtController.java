package com.sdsu.edu.cms.cmsservice.controllers;

import com.sdsu.edu.cms.cmsservice.services.SubmisionMfmtService;
import com.sdsu.edu.cms.common.models.cms.Submission;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", produces = {APPLICATION_JSON_VALUE})
public class SubmissionMgmtController {
    @Autowired
    SubmisionMfmtService submisionMfmtService;

    @PostMapping(value = "/conferences/{cid}/submissions")
    public ServiceResponse createSubmission(@RequestBody Submission request, @PathVariable String cid){

        return  submisionMfmtService.addNewSubmission(request, cid);

    }

    @GetMapping(value = "/conferences/{cid}/submissions/{sid}")
    public ServiceResponse getSubmissionById(@PathVariable String cid, @PathVariable String sid){
        return null;
    }

    @GetMapping(value = "/conferences/{cid}/submissions")
    public ServiceResponse getSubmissions(@PathVariable String cid){
        return null;
    }
    /*
    REST API for Chair and reviewers to update paper status.
    To upload or edit files, use PUT.
     */
    @PatchMapping(value = "/conferences/{cid}/submissions/{sid}")
    public ServiceResponse patchSubmissions(@RequestParam Map<String, Object> map, @PathVariable String cid, @PathVariable String sid){
        return null;
    }

    @DeleteMapping(value = "/conferences/{cid}/submissions/{sid}")
    public ServiceResponse deleteSubmissions(@PathVariable String cid, @PathVariable String sid){
        return null;
    }

    @PutMapping(value = "/conferences/{cid}/submissions/{sid}")
    public ServiceResponse updateSubmissions(@RequestBody String payLoad, @PathVariable String cid, @PathVariable String sid){
        return null;
    }

}
