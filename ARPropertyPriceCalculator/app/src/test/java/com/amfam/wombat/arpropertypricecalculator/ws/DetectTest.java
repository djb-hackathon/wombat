package com.amfam.wombat.arpropertypricecalculator.ws;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DetectTest {

    String fileName = "C:\\Users\\eam045\\Documents\\wombat\\ARPropertyPriceCalculator\\app\\src\\test\\img\\blender.jpg"; //"../../../../../../img/blender.jpg";
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void detectLabels() {
    }

    @Test
    public void buildImage() {
        try {
            Image img = Detect.buildImage(fileName);

            Assert.assertNotNull(img);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }

    }

    @Test
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

    }
}