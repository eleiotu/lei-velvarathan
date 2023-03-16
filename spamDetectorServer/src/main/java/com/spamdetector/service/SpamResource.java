package com.spamdetector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.net.URL;

import jakarta.ws.rs.core.Response;

@Path("/spam")
public class SpamResource {

//    your SpamDetector Class responsible for all the SpamDetecting logic

    SpamDetector detector = new SpamDetector();
    ObjectMapper objectMapper = new ObjectMapper();



    SpamResource() throws FileNotFoundException {
//       TODO: load resources, train and test to improve performance on the endpoint calls
        System.out.print("Training and testing the model, please wait");

//      TODO: call  this.trainAndTest();
        this.trainAndTest();

    }

    //endpoint http://localhost:8080/spamDetector-1.0/api/spam

    @GET
    @Produces("application/json")
    public Response getSpamResults() throws FileNotFoundException {
//       TODO: return the test results list of TestFile, return in a Response object
        String val = "[";
        List<TestFile> spamList = this.trainAndTest();



        try{
            for(int i = 0; i < spamList.size(); i++){
                val += objectMapper.writeValueAsString(spamList.get(i)) + ",";
            }
        }catch(JsonProcessingException e){
            throw new RuntimeException(e);
        }
        val = val.substring(0, val.length()-1);
        val+= "]";


        Response myResp = Response.status(200).header("Access-Control-Allow-Origin", "http://localhost:8448")
                .header("Content-Type", "application/json")
                .entity(val)
                .build();

        return myResp;
    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() throws FileNotFoundException {
//      TODO: return the accuracy of the detector, return in a Response object
        List<TestFile> spamList = this.trainAndTest();



        return null;
    }


    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
       //      TODO: return the precision of the detector, return in a Response object


        return null;
    }

    private List<TestFile> trainAndTest() throws FileNotFoundException {
        if (this.detector==null){
            this.detector = new SpamDetector();
        }

//        TODO: load the main directory "data" here from the Resources folder

        URL url = this.getClass().getClassLoader().getResource("/data");
        File data = null;
        try{
            data = new File(url.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return this.detector.trainAndTest(data);
    }
}
