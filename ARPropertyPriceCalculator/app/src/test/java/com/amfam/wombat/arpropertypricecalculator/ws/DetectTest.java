package com.amfam.wombat.arpropertypricecalculator.ws;

import android.content.Context;
import android.test.AndroidTestCase;


import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import junit.framework.Assert;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class DetectTest{

    /**/String fileName = "C:\\Users\\jal113\\Desktop\\Hackathon\\wombat\\couch.jpg"; //"../../../../../../img/blender.jpg";
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

/*    @Test
    public void getResponse() {
        try {
            byte[] byteArray = Detect.buildImage(fileName).toByteArray();
            String response = new Detect().getResponse(InstrumentationRegistry.
                    ,byteArray);
            Assert.assertNotNull(response);

        } catch(Exception e){

        }
    }*/


   @Test
    public void buildImage() {
        try {
            Image img = Detect.buildImage(fileName);

            Assert.assertNotNull(img);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

    }

    /*@Test
    public void buildFeature() {
        Feature feature = Detect.buildFeature();
        Assert.assertNotNull(feature);
    }

    @Test
    public void buildRequest() {
        try {
            Image img = Detect.buildImage(fileName);
            Feature feat = Detect.buildFeature();
            AnnotateImageRequest request = Detect.buildRequest(img, feat);
            Assert.assertNotNull(request);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void detectLabelsGcs() {
    }

    @Test
    public void printResponses() {
        List<AnnotateImageResponse> responses = new ArrayList<AnnotateImageResponse>();

        //AnnotateImageResponse resp = AnnotateImageResponse.
        //responses.add(resp);

    }

    @Test
    public void getTopResults() {
        List<EntityAnnotation> result = Detect.getTopAnnotations(fileName);
        Assert.assertNotNull(result);
        Assert.assertEquals(4,result.size());

    }*/
}