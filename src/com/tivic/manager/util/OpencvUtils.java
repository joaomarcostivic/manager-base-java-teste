package com.tivic.manager.util;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import com.tivic.manager.mob.EventoArquivoServices;

import sol.util.ConfManager;

public class OpencvUtils {

	private static int keyUserdata[] = new int[2]; // [0] = id [1] = keycode
	private static boolean isWaitKey = false;

	/**
	 * Carregar as bibliotecas necessárias do OpenCV.
	 * 
	 * @author Edgard H
	 */
	public static void loadNativeLibrary() {
		try {
			
			System.out.println("Inicializando Bibliotecas OpenCV...");
			
			Field libs = ClassLoader.class.getDeclaredField("loadedLibraryNames");
			libs.setAccessible(true);

			String model = System.getProperty("sun.arch.data.model");
			ConfManager conf = Util.getConfManager();

			String libraryPath = conf.getProps().getProperty("OPENCV_LIB_PATH") + "/x86";

			if (model.equals("64")) {
				libraryPath = conf.getProps().getProperty("OPENCV_LIB_PATH") + "/x64";
			}

			System.setProperty("java.library.path", libraryPath);
			Field sysPath = ClassLoader.class.getDeclaredField("sys_paths");
			sysPath.setAccessible(true);
			sysPath.set(null, null);

			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
			System.loadLibrary("opencv_ffmpeg_64");
			
			System.out.println("Bibliotecas OpenCV inicializadas.");

		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	public static void initStreamServer() {
		try {
			System.out.println("Inicializando Streams do OpenCV...");
			HttpStreamServer httpStreamService = new HttpStreamServer();
	        new Thread(httpStreamService).start();
		} catch (Exception e) {
			System.err.println(e.getMessage());			
		}
	}

	/**
	 * Converter um objeto Matrix do OpenCV para um byte[]
	 * 
	 * @author Edgard H
	 * @param matrix
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage Mat2BufferedImage(Mat mat) throws IOException {
		MatOfByte mob = new MatOfByte();
		Imgcodecs.imencode(".jpg", mat, mob);
		byte ba[] = mob.toArray();

		BufferedImage bi = ImageIO.read(new ByteArrayInputStream(ba));
		return bi;
	}

	public static int waitKey(int delay) {
		isWaitKey = true;
		int timeout = delay;
		try {
			synchronized (keyUserdata) {
				while (isWaitKey && (delay == 0 || timeout > 0)) {
					if (delay > 0) {
						int seconds = timeout / 1000;
						timeout -= 50;
						if (timeout < 0) {
							return -1;
						}
					}
					keyUserdata.wait(50);
				}
			}
			return keyUserdata[1];
		} catch (Exception e) {
		}
		return -1;
	}

	static {
		KeyEventDispatcher dispatcher = new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				synchronized (keyUserdata) {
					if (e.getID() == KeyEvent.KEY_PRESSED) {
						keyUserdata[0] = e.getID();
						keyUserdata[1] = e.getKeyCode();
						isWaitKey = false;
						keyUserdata.notify();
					}
				}
				return false;
			}
		};

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(dispatcher);
	}

	public static byte[] getByteData(BufferedImage userSpaceImage) {
		WritableRaster raster = userSpaceImage.getRaster();
		DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
		return buffer.getData();
	}

	public static void saveToFile(BufferedImage img) throws FileNotFoundException, IOException {

		File outputfile = new File("D:\\Sample.png");
		ImageIO.write(img, "jpg", outputfile);
	}
	
	public static JSONArray faceDetector(byte[] file) {
		try {
			Mat 	  mat    	     = Imgcodecs.imdecode(new MatOfByte(file), Imgcodecs.CV_LOAD_IMAGE_COLOR);
			JSONArray positions = new JSONArray();
			ArrayList<String> patterns = new ArrayList<String>();
			patterns.add("/util/opencv/resources/haarcascades/haarcascade_profileface.xml");
			patterns.add("/util/opencv/resources/haarcascades/haarcascade_frontalface_alt.xml");
			patterns.add("/util/opencv/resources/haarcascades/haarcascade_frontalface_alt_tree.xml");
			patterns.add("/util/opencv/resources/haarcascades/haarcascade_frontalface_alt2.xml");
			patterns.add("/util/opencv/resources/haarcascades/haarcascade_frontalface_default.xml");
						
			for (String pattern : patterns){
				CascadeClassifier faceDetector = new CascadeClassifier();
				faceDetector.load(EventoArquivoServices.class.getResource("/"+Util.getPackageRootPath()+pattern).getPath().substring(1));
				
				MatOfRect faceDetections = new MatOfRect();
		        faceDetector.detectMultiScale(mat, faceDetections, 1.3, 1, 0 | Objdetect.CASCADE_SCALE_IMAGE, new Size(15, 15), new Size(50, 50));
		        
		        for (Rect rect : faceDetections.toArray()) {
		        	JSONObject json = new JSONObject();
		        	json.put("x", rect.x);
		        	json.put("y", rect.y);
		        	json.put("w", rect.width);
		        	json.put("h", rect.height);
		        	positions.put(json);
		        }
			}
			
			return positions;		        
		} catch (Exception e) {
			return null;
		}
	}
}
