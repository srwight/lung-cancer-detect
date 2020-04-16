package com.revature.services;

import org.dcm4che3.imageio.plugins.dcm.DicomImageReader;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.media.DicomDirReader;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.twelvemonkeys.imageio.metadata.Directory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

// import org.itk.simple.Image;
// import org.itk.simple.SimpleITK;

@Service
public class AIService {	
	private float[][][] cubes = new float[64][64][64];
	private float confidence;
	
	private void testDicomFileRead() {
		String fileName = getClass().getClassLoader().getResource("test.dcm").getFile();
		List<Float> dicomInts = new ArrayList<Float>();
		
		try (DicomInputStream dis = new DicomInputStream(new File(fileName.substring(1)))) {
			float num;
			while ((num = dis.read()) > -1) {
				dicomInts.add(num);
			}
			System.out.println(dicomInts.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
		float[] data = new float[dicomInts.size()];	
		for (int i = 0; i < dicomInts.size(); i++) {
			data[i] = dicomInts.get(i);
		}
		
		INDArray arr = Nd4j.create(data, 'c');
		System.out.println(arr);
	}
	
	
	public String getPredictedValue() {
		testDicomFileRead();
		String fileName;
		MultiLayerNetwork model;
		try {
			fileName = getClass().getClassLoader().getResource("first_model.h5").getFile();
			model = KerasModelImport.importKerasSequentialModelAndWeights(fileName.substring(1));
			
			INDArray arr = Nd4j.create(new float[33292288],new int[]{512,512,127},'c');
			
			//System.out.println(arr);

			// get the prediction
			double prediction = model.output(arr).getDouble(0);	
			
			System.out.println(prediction);
		} catch (Exception e) {
			e.printStackTrace();
		}
		float[][][] scan = randArray(512, 512, 127);
//		for(int x = 0; x < 512; x++) {
//			for(int y = 0; y < 512; y++) {
//				for(int z = 0; z < 127; z++) {
//					scan[z][y][x] = 0;
//				}
//			}
//		}
		
		int z = scan.length;
		int y = scan[0].length;
		int x = scan[0][0].length;
		int cz = cubes.length;
		int cy = cubes[0].length;
		int cx = cubes[0][0].length;
		
		// float[][][] scanresult = new float[z / cz][y / cy][x / cx];
		List<float[][][]> scanresult = new ArrayList<float[][][]>();
		
		for (int i = 0; i < z - cz; i += cz / 2) {
			for (int j = 0; j < y - cy; j += cy / 2) {
				for (int k = 0; k < x - cx; k += cx / 2) {
					scanresult.add(sub3Array(scan, k, j, i, cx, cy, cz));
				}	
			}
		}
		
		System.out.println("output: " + scanresult.size() + " subarrays");
		// int count = 1;
		// for (float[][][] item : scanresult) {
//			for (float[][] i : item) {
//				for (float[] j : i) {
//					for (float k : j) {
//						System.out.println("Output: Subarray" + count + ": " + k);
//					}	
//				}
//			}
			// System.out.println("Subarray" + count++ + " " + meanArray(item) + " mean value");
		// }
		return "output";
	}
	
	public float[][][] sub3Array(float[][][] array, int xStart, int yStart, int zStart, int xLength, int yLength, int zLength) {
		float[][][] ret;
		ret = new float[zLength][yLength][xLength];
		for(int x=0; x<xLength; x++) {
			for(int y = 0; y < yLength; y++) {
				for(int z = 0; z < zLength; z++) {
					try {
						ret[z][y][x] = array[z+zStart][y+yStart][x+xStart];
					} catch (Exception e) {
						System.out.println("Array: zl=" + array.length + " yl=" + array[0].length + " xl=" + array[0][0].length + " subarray: z=" + z+zStart + " y=" + y+yStart + " x=" + x+xStart);
						System.out.println(e.toString() + " " + e.getMessage());
						return null;
					}
				}
			}
		}
		return ret;
	}
	
	float[][][] randArray(int targetx, int targety, int targetz) {
	    float[][][] outArray;
	    outArray = new float[targetz][targety][targetx];
	    Random rand = new Random();
	    for(int x = 0; x < targetx; x++){
	        for(int y = 0; y < targety; y++){
	            for(int z = 0; z < targetz; z++){
	                outArray[z][y][x] = rand.nextFloat();
	            }
	        }
	    }
	    return outArray;
	}
	
	float meanArray(float inArray[][][]){
	    int zSize = inArray.length;
	    int ySize = inArray[0].length;
	    int xSize = inArray[0][0].length;
	    int numElements = zSize * ySize * xSize;
	    float sum = 0;
	    for(int x = 0; x < xSize; x++){
	        for(int y = 0; y < ySize; y++){
	            for(int z = 0; z<zSize; z++){
	                sum += inArray[z][y][x];
	            }
	        }
	    }
	    float return_mean = sum / numElements;
	    return return_mean;
	}
}
