package com.sdsu.edu.cms.cmsservice.services;

import com.google.gson.Gson;
import com.sdsu.edu.cms.cmsservice.exceptions.ApiErrorException;
import com.sdsu.edu.cms.cmsservice.exceptions.ConferenceNotFoundException;
import com.sdsu.edu.cms.cmsservice.proxy.DataServiceProxy;
import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.cms.Track;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ConferenceMgmtService {
    @Autowired
    ServiceResponse response;
    @Autowired
    DataServiceProxy dataServiceProxy;
    public ServiceResponse createConference(Conference model) throws Exception {
        model.setConference_id(UUID.randomUUID().toString());
        response = dataServiceProxy.createConference(model);
        if((boolean)response.getData().get(0) == false){
            if(response.getMessage().equals("-2")){
                throw new ApiErrorException("Conference with this short name already exists");
            }else{
                throw new Exception("internal server error");
            }
        }
        return response;
    }

    public ServiceResponse addTracksToConf(List<Track> tracks, String cid) {
        Map<String, String > mp = new HashMap<>();
        mp.put("cid", cid);
        response = dataServiceProxy.addTracks(tracks, mp);
        return response;
    }

    public ServiceResponse getConferenceByName(String name){
        Map<String, String > mp = new HashMap<>();
        mp.put("cname", name);
        response = dataServiceProxy.getConferenceByName(mp);
        if(response.getData().get(0).equals("false")){
            throw new ConferenceNotFoundException("Conference with entered short name not found");
        }
        String confData = response.getData().get(0).toString().trim();
        Conference c =  new Gson().fromJson(confData, Conference.class);

        return new ServiceResponse(Arrays.asList(c), "Query successful");
    }

    public ServiceResponse getAllConferences(){
        return null;
    }

    public ServiceResponse updateConference(){
        return null;
    }
}
