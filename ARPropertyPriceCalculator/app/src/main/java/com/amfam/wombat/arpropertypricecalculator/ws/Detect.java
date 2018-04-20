package com.amfam.wombat.arpropertypricecalculator.ws;

/*
 * Copyright 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.util.Base64;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;
import com.google.protobuf.ByteString;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Detect {



    public static Image buildImage(String filePath) throws Exception {
        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();

        return img;
    }

    public static String buildRequestJSON(Image img) {
        String requestJSON = "";
        String base64data = Base64.encodeToString(img.toByteArray(), Base64.URL_SAFE);
        String requestURL = "https://vision.googleapis.com/v1/images:annotate?key=AIzaSyDL2EgkAvpP1tVvqCdn1HaedgyPG-t621c";

        try {
            // Create an array containing
            //  the LABEL_DETECTION feature
            JSONArray features = new JSONArray();
            JSONObject feature = new JSONObject();
            feature.put("type", "LABEL_DETECTION");
            features.put(feature);

            // Create an object containing
            // the Base64-encoded image data
            JSONObject imageContent = new JSONObject();
            imageContent.put("content", base64data);

            // Put the array and object into a single request
            // and then put the request into an array of requests
            JSONArray requests = new JSONArray();
            JSONObject request = new JSONObject();
            request.put("image", imageContent);
            request.put("features", features);
            requests.put(request);
            JSONObject postData = new JSONObject();
            postData.put("requests", requests);

            // Convert the JSON into a string
            requestJSON = postData.toString();
        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
        return requestJSON;
    }

    /***
    public static String doStuff(String requestJSON) {

        Fuel.post(requestURL)
                .header(
                        new Pair<String, Object>("content-length", requestJSON.length()),
                        new Pair<String, Object>("content-type", "application/json")
                )
                .body(body.getBytes())
                .responseString(new Handler<String>() {
                    @Override
                    public void success(@NotNull Request request,
                                        @NotNull Response response,
                                        String data) {
                        // More code goes here
                    }

                    @Override
                    public void failure(@NotNull Request request,
                                        @NotNull Response response,
                                        @NotNull FuelError fuelError) {}
                });

    }
     **/

    public  static Feature buildFeature() {
        Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
        return feat;
    }

    public static AnnotateImageRequest buildRequest(Image img, Feature feat) {
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        return request;
    }

    public static AnnotateImageResponse getResponse(List<AnnotateImageRequest> requests) {
        List<AnnotateImageResponse> responses = new ArrayList<AnnotateImageResponse>();
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {

            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            responses = response.getResponsesList();

            if (responses.get(0).hasError()) {
                //System.out.printf("Error: %s\n", res.getError().getMessage());
                return null;
            }
        } catch (Exception e){

        }
        return responses.get(0);
    }


    //res.getLabelAnnotationsList() = annotations
    public static List<EntityAnnotation> getTopThree(List<EntityAnnotation> annotations) {
        List<EntityAnnotation> choices = new ArrayList<>();

        int i = 0;
        int max = 3;
        while (i < annotations.size() && i < max){
            choices.add(annotations.get(i));
            i++;
        }

        EntityAnnotation other = EntityAnnotation.newBuilder().setDescription("Other").build();
        choices.add(other);
        return choices;
    }

    public static void printResponses(List<AnnotateImageResponse> responses, PrintStream out) {
        for (AnnotateImageResponse res : responses) {
            if (res.hasError()) {
                out.printf("Error: %s\n", res.getError().getMessage());
                return;
            }

            // For full list of available annotations, see http://g.co/cloud/vision/docs
            for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                annotation.getAllFields().forEach((k, v) ->
                        System.out.printf("%s : %s\n", k, v.toString()));
            }
        }
    }

    public static List<EntityAnnotation> getTopAnnotations(String filePath) {
        List<EntityAnnotation> topAnnotations = new ArrayList<EntityAnnotation>();
        try{
            Image img = buildImage(filePath);
            Feature feat = buildFeature();
            AnnotateImageRequest request = buildRequest(img, feat);
            List<AnnotateImageRequest> requests = new ArrayList<AnnotateImageRequest>();
            requests.add(request);
            AnnotateImageResponse response = getResponse(requests);
            topAnnotations = getTopThree(response.getLabelAnnotationsList());

        } catch(Exception e) {
            System.err.print(e.getMessage());
        }
        return topAnnotations;
    }
    /**
     * Detects labels in the specified local image.
     *
     * @param filePath The path to the file to perform label detection on.
     * @param out A {@link PrintStream} to write detected labels to.
     * @throws Exception on errors while closing the client.
     * @throws IOException on Input/Output errors.
     *
    public static void detectLabels(String filePath, PrintStream out) throws Exception, IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {

                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields().forEach((k, v) ->
                            System.out.printf("%s : %s\n", k, v.toString()));
                }
            }
        }
    }*/

    /**
     * Detects labels in the specified remote image on Google Cloud Storage.
     *
     * @param gcsPath The path to the remote file on Google Cloud Storage to perform label detection
     *                on.
     * @param out A {@link PrintStream} to write detected features to.
     * @throws Exception on errors while closing the client.
     * @throws IOException on Input/Output errors.
     */
    public static void detectLabelsGcs(String gcsPath, PrintStream out) throws Exception,
            IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
        Image img = Image.newBuilder().setSource(imgSource).build();
        Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    out.printf("Error: %s\n", res.getError().getMessage());
                    return;
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields().forEach((k, v) ->
                            out.printf("%s : %s\n", k, v.toString()));
                }
            }
        }
    }

}

