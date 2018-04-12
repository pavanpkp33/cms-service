package com.sdsu.edu.cms.cmsservice.services;

import com.sdsu.edu.cms.cmsservice.proxy.DataServiceProxy;
import com.sdsu.edu.cms.common.models.cms.Submission;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class SubmisionMfmtService {

    @Autowired
    DataServiceProxy dataServiceProxy;
    @Value("${cms.paper.upload.path}")
    private String UPLOAD_DIR;
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
    public ServiceResponse updateSubmission(Submission payLoad) throws Exception {

        try {
            payLoad = processFiles(payLoad);
            payLoad.setDraft_paper(null);
            payLoad.setFinal_paper(null);
            payLoad.setCameraReadyPaperUri(null);
            return dataServiceProxy.updateSubmission(payLoad);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;

        }


    }

    private Submission processFiles(Submission payLoad) throws Exception {

        if(payLoad.getDraft_paper() != null && !payLoad.getDraft_paper().isEmpty()){
            String newName = payLoad.getSid()+"_"+"draft.pdf";
            payLoad.setDraftPaperUri(newName);
            if(!saveFile(payLoad.getDraft_paper(), UPLOAD_DIR+newName )){
                throw new Exception("Saving draft file to disk error");
            }
        }
        if(payLoad.getFinal_paper() != null && !payLoad.getFinal_paper().isEmpty()){
            String newName = payLoad.getSid()+"_"+"final.pdf";
            payLoad.setFinalPaperUri(newName);
            if(!saveFile(payLoad.getFinal_paper(), UPLOAD_DIR+newName )){
                throw new Exception("Saving final file to disk error");
            }
        }

        if(payLoad.getCamera_ready_paper() != null && !payLoad.getCamera_ready_paper().isEmpty()){
            String newName = payLoad.getSid()+"_"+"camera.pdf";
            payLoad.setCameraReadyPaperUri(newName);
            if(!saveFile(payLoad.getCamera_ready_paper(), UPLOAD_DIR+newName )){
                throw new Exception("Saving camera ready file to disk error");
            }
        }

        return payLoad;
    }

    private boolean saveFile(MultipartFile file, String file_uri){

        Path path = Paths.get(file_uri);
        try {
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
