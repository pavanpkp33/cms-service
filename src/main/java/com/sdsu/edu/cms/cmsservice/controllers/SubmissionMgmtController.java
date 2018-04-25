package com.sdsu.edu.cms.cmsservice.controllers;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdsu.edu.cms.cmsservice.services.SubmisionMfmtService;
import com.sdsu.edu.cms.common.models.cms.Submission;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1", produces = {APPLICATION_JSON_VALUE})
public class SubmissionMgmtController {
    @Autowired
    SubmisionMfmtService submisionMfmtService;

    @PostMapping(value = "/conferences/{cid}/submissions/{sid}")
    public ServiceResponse updateSubmissions(@RequestParam(name = "draft_paper", required = false) MultipartFile draftPaper,
                                             @RequestParam(name = "final_paper",required = false) MultipartFile finalPaper,
                                             @RequestParam(name = "camera_ready_paper", required = false) MultipartFile cameraReady,
                                             @RequestParam(name = "title", required = false) String title,
                                             @RequestParam(name = "abstract_text", required = false) String abstractText,
                                             @RequestParam(name = "uploaded_by_user") String uploadByUser,
                                             @RequestParam("track_id") int trackId,
                                             @RequestParam("keywords") String keywords,
                                             @PathVariable String cid, @PathVariable String sid) throws Exception {
        Submission payLoad = new Submission();
        payLoad.setSid(sid);
        payLoad.setCid(cid);
        Type type = new TypeToken<String[]>() {}.getType();
        String[] arr = new Gson().fromJson(keywords, type);
        payLoad.setKeyword(arr);
        payLoad.setTitle(title);
        payLoad.setTrack_id(trackId);
        payLoad.setAbstract_text(abstractText);
        payLoad.setDraft_paper(draftPaper);
        payLoad.setSubmit_author_id(uploadByUser);
        payLoad.setFinal_paper(finalPaper);
        payLoad.setCamera_ready_paper(cameraReady);

        return submisionMfmtService.updateSubmission(payLoad);
    }

    @PostMapping(value = "/conferences/{cid}/submissions")
    public ServiceResponse createSubmission(@RequestBody Submission request, @PathVariable String cid){
        return  submisionMfmtService.addNewSubmission(request, cid);

    }

    @GetMapping(value = "/conferences/{cid}/submissions/users/{uid}")
    public ServiceResponse getSubmissionsForUser(@PathVariable String cid, @PathVariable String uid){

        return submisionMfmtService.getSubmissionsForUser(cid, uid);

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

    @DeleteMapping(value = "/conferences/{cid}/submissions/{sid}/files/{type_id}")
    public ServiceResponse deleteFiles(@PathVariable String sid, @PathVariable int type_id){
        return submisionMfmtService.deleteFiles(sid, type_id);
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
