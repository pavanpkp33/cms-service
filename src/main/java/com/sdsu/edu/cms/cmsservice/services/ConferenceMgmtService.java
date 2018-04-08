package com.sdsu.edu.cms.cmsservice.services;

import com.sdsu.edu.cms.cmsservice.exceptions.ApiErrorException;
import com.sdsu.edu.cms.cmsservice.proxy.DataServiceProxy;
import com.sdsu.edu.cms.common.models.cms.Conference;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public ServiceResponse getConferenceByName(String name){
        return null;
    }

    public ServiceResponse getAllConferences(){
        return null;
    }

    public ServiceResponse updateConference(){
        return null;
    }
}
