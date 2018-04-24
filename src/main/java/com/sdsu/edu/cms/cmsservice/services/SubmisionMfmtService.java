package com.sdsu.edu.cms.cmsservice.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sdsu.edu.cms.cmsservice.proxy.DataServiceProxy;
import com.sdsu.edu.cms.common.models.cms.Submission;
import com.sdsu.edu.cms.common.models.response.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
            payLoad.setCamera_ready_paper(null);
            return dataServiceProxy.updateSubmission(payLoad);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;

        }


    }

    public ServiceResponse getSubmissions(String cid, String sid){
        Map<String, String> map = new HashMap<>();
        if(sid != null){
            map.put("sid", sid);
        }
        map.put("cid", cid);
        ServiceResponse response = dataServiceProxy.getSubmission(map);
        if(response.getData() == null) return new ServiceResponse(Arrays.asList(), response.getMessage());
        String res = response.getData().get(0).toString();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        Type type = new TypeToken<List<Submission>>() {}.getType();
        List<Submission> result = gson.fromJson(res, type);
        List<Object> finalObj = new ArrayList<>();
        for(Submission s : result){
            finalObj.add(s);
        }
        return new ServiceResponse(finalObj, response.getMessage());

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

    public ServiceResponse deleteSubmission(String sid){
        Map<String, String> mp = new HashMap<>();
        mp.put("sid", sid);
        return dataServiceProxy.deleteConference(mp);
    }

    /*
        this method is used to change paid, group, decision status and also valid
     */
    public ServiceResponse updateSubmission(String sid, Map<String, Object> mp){
        return dataServiceProxy.patchConference(sid, mp);
    }

    public ServiceResponse deleteAuthor(String sid, String authorId) {
        Map<String, String> params = new HashMap<>();
        params.put("sid", sid);
        params.put("aid", authorId);
        return dataServiceProxy.deleteAuthor(params);

    }

    public ServiceResponse deleteFiles(String sid, String type_id) {
        Map<String, String> params = new HashMap<>();
         params.put("sid", sid);
         params.put("type_id", type_id);
         return dataServiceProxy.deleteFiles(params);

    }

    public ServiceResponse getSubmissionsForUser(String cid, String uid) {
        Map<String, String> params  =new HashMap<>();
        params.put("cid", cid);
        params.put("uid", uid);
        ServiceResponse response = dataServiceProxy.getSubmissionForUser(params);
        if(response.getData() == null) return new ServiceResponse(Arrays.asList(), response.getMessage());
        String res = response.getData().get(0).toString();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        Type type = new TypeToken<List<Submission>>() {}.getType();
        List<Submission> result = gson.fromJson(res, type);
        List<Object> finalObj = new ArrayList<>();
        for(Submission s : result){
            finalObj.add(s);
        }
        return new ServiceResponse(finalObj, response.getMessage());
    }
}
