package com.sdsu.edu.cms.cmsservice.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sdsu.edu.cms.cmsservice.exceptions.ApiErrorException;
import com.sdsu.edu.cms.cmsservice.exceptions.ConferenceNotFoundException;
import com.sdsu.edu.cms.cmsservice.proxy.DataServiceProxy;
import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.cms.Track;
import com.sdsu.edu.cms.common.models.notification.NotifyDBModel;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import com.sdsu.edu.cms.common.models.review.Reviewers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

@Service
public class ConferenceMgmtService {
    @Autowired
    ServiceResponse response;
    @Autowired
    DataServiceProxy dataServiceProxy;
    public ServiceResponse createConference(Conference model) throws Exception {
        model.setCid(UUID.randomUUID().toString());
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

    public ServiceResponse updateConference(Conference payLoad, String id){
        Map<String , String> mp = new HashMap<>();
        mp.put("cid", id);
        response = dataServiceProxy.updateConference(payLoad, mp);
        if(!(boolean)response.getData().get(0)){
            throw new ConferenceNotFoundException("Update failed");
        }
        return new ServiceResponse(Arrays.asList(true), "Conference updated successfully.");
    }

    public ServiceResponse getTracksConf(String id) {
        Map<String, String> mp = new HashMap<>();
        mp.put("id", id);
        response =  dataServiceProxy.getTracks(mp);
        String jsonStr = response.getData().get(0).toString().trim();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Track>>() {}.getType();
        List<Track> tracks = gson.fromJson(jsonStr, type);
        List<Object> finalRes = new ArrayList<>();
        for(Track t : tracks){
            finalRes.add(t);
        }

        return new ServiceResponse(finalRes,"Query successful");

    }

    public ServiceResponse deleteConference(String id) {
        Map<String, String> mp = new HashMap<>();
        mp.put("id", id);
        return dataServiceProxy.deleteConferences(mp);

    }

    public ServiceResponse getConferenceUsers(String id) {
        Map<String, String> mp  =new HashMap<>();
        mp.put("cid", id);
        ServiceResponse response =  dataServiceProxy.getConferenceUsers(mp);
        String result = response.getData().get(0).toString();

        Type type = new TypeToken<List<Reviewers>>() {}.getType();
        List<Reviewers> reviewers = new Gson().fromJson(result, type);
        List<Object> castResult = new ArrayList<>();
        for(Reviewers r : reviewers){
            castResult.add(r);
        }
        response.setData(castResult);
        return response;

    }

    public ServiceResponse deleteRoles(String cid, String uid, String rid) {
        Map<String, String> params = new HashMap<>();
        params.put("cid", cid);
        params.put("uid", uid);
        params.put("rid", rid);

        return dataServiceProxy.deleteUserRoles(params);
    }
}
