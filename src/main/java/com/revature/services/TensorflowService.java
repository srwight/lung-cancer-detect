package com.revature.services;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;

@Service
public class TensorflowService {
	
	float[][] inputData = {{4, 3, 2, 1}};
	Tensor<Float> inputTensor = Tensor.create(inputData, Float.class);
	
	public String Check() throws IOException, URISyntaxException {
		Path modelPath = Paths.get(TensorflowService.class.getResource("saved_model.pb").toURI());
		byte[] graph = Files.readAllBytes(modelPath);
        String data = "";
		try (Graph g = new Graph()) {
		    g.importGraphDef(graph);
		    //open session using imported graph
		    try (Session sess = new Session(g)) {
		        float[][] inputData = {{4, 3, 2, 1}};
		        // We have to create tensor to feed it to session,
		        // unlike in Python where you just pass Numpy array
		        Tensor<Float> inputTensor = Tensor.create(inputData, Float.class);
		        float[][] output = predict(sess, inputTensor);
		        for (int i = 0; i < output[0].length; i++) {
		        	//should be 41. 51.5 62.
		            data += data + output[0][i]; 
		        }
		    }
		}
		return data;
	}
	
	private static float[][] predict(Session sess, Tensor<Float> inputTensor) {
	    Tensor<?> result = sess.runner()
	            .feed("input", inputTensor)
	            .fetch("not_activated_output").run().get(0);
	    float[][] outputBuffer = new float[1][3];
	    result.copyTo(outputBuffer);
	    return outputBuffer;
	}
}
