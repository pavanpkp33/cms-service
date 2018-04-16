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

    @PostMapping(value = "/conferences/{cid}/submissions/{sid}")
    public ServiceResponse updateSubmissions(@ModelAttribute  Submission payLoad, @PathVariable String cid, @PathVariable String sid) throws Exception {

        payLoad.setSid(sid);
        payLoad.setCid(cid);

        return submisionMfmtService.updateSubmission(payLoad);
    }

    @PostMapping(value = "/conferences/{cid}/submissions")
    public ServiceResponse createSubmission(@RequestBody Submission request, @PathVariable String cid){

        return  submisionMfmtService.addNewSubmission(request, cid);

    }


    @GetMapping(value = "/conferences/{cid}/submissions/{sid}")
    public ServiceResponse getSubmissionById(@PathVariable String cid, @PathVariable String sid){
        return submisionMfmtService.getSubmissions(cid, sid);
    }

    @GetMapping(value = "/conferences/{cid}/submissions")
    public ServiceResponse getSubmissions(@PathVariable String cid){
        return submisionMfmtService.getSubmissions(cid, null);
    }
    /*
    REST API for Chair and reviewers to update paper status.
    To upload or edit files, use PUT.
    Single operation
     */
    @PatchMapping(value = "/conferences/{cid}/submissions/{sid}", consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    public ServiceResponse patchSubmissions(@RequestBody Map<String, Object> map, @PathVariable String cid, @PathVariable String sid){
        return submisionMfmtService.updateSubmission(sid, map);
    }

    @DeleteMapping(value = "/conferences/{cid}/submissions/{sid}/files")
    public ServiceResponse deleteFiles(@PathVariable String sid, @RequestBody Map<String, String> params){
        return submisionMfmtService.deleteFiles(sid, params.get("type_id"));
    }

    @DeleteMapping(value = "/conferences/{cid}/submissions/{sid}/{authorId}")
    public ServiceResponse deleteAuthor(@PathVariable String sid, @PathVariable  String authorId){

        return submisionMfmtService.deleteAuthor(sid, authorId);
    }


    @DeleteMapping(value = "/conferences/{cid}/submissions/{sid}")
    public ServiceResponse deleteSubmissions(@PathVariable String cid, @PathVariable String sid){
        return submisionMfmtService.deleteSubmission(sid);

    }



}
