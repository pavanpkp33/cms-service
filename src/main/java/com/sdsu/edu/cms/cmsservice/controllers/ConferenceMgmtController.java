

package com.sdsu.edu.cms.cmsservice.controllers;

import com.sdsu.edu.cms.cmsservice.services.ConferenceMgmtService;
import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.cms.Track;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1")
public class ConferenceMgmtController {

    @Autowired
    ConferenceMgmtService conferenceMgmtService;
    @PostMapping(value = "/conferences", consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    public ServiceResponse createConference(@RequestBody Conference conference) throws Exception {
        return conferenceMgmtService.createConference(conference);

    }

    @PutMapping(value = "/conferences/{id}", consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    public ServiceResponse updateConference(@RequestBody Conference payLoad, @PathVariable String id){

        return conferenceMgmtService.updateConference(payLoad, id);
    }

    @PostMapping(value = "/conferences/{id}/tracks", consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    public ServiceResponse addTracks(@RequestBody List<Track> tracks, @PathVariable  String id){
        return conferenceMgmtService.addTracksToConf(tracks, id);

    }

    @GetMapping(value = "/conferences/{id}/tracks", produces = {APPLICATION_JSON_VALUE})
    public ServiceResponse getTracks(@PathVariable  String id){
        return conferenceMgmtService.getTracksConf(id);

    }

    @GetMapping("/conferences/id/{id}")
    public ServiceResponse getConferenceById(@PathVariable String id){
        return  null;
    }

    @GetMapping("/conferences/{name}")
    public ServiceResponse getConferenceByName(@PathVariable String name){

        return conferenceMgmtService.getConferenceByName(name);
    }

    @GetMapping("/conferences")
    public ServiceResponse getConference(){
        return null;
    }




}
