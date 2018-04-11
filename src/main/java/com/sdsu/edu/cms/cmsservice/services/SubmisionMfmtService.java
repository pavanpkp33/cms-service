package com.sdsu.edu.cms.cmsservice.services;

import com.sdsu.edu.cms.cmsservice.proxy.DataServiceProxy;
import com.sdsu.edu.cms.common.models.cms.Submission;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmisionMfmtService {

    @Autowired
    DataServiceProxy dataServiceProxy;
    //while adding new submissions, reject final draft and camera ready paper. That comes next.
    /*
            Steps for new submission handling.
            1. Insert in submission table.
            2. Update keywords table.
            3. check for users in user table
                a. if exists, insert into conf_users table
                b. if user doesnt exists in DB, create a default user and insert into conf_users
     */
    public ServiceResponse addNewSubmission(Submission payLoad,String confId ){
        payLoad.setCid(confId);
        ServiceResponse response = dataServiceProxy.createSubmission(payLoad);
        //todo : check if conference exists with the ID and initiate the submission process.
        return response;

    }

    /*

            Steps for handling updates.
            1. Delete keywords, and re-insert.
            2. Check for file uploads and add name. Insert into files table.
            3. Update status of paper with model.
     */
    public ServiceResponse updateSubmission(Submission payLoad, String confId){
        return null;
    }
}
