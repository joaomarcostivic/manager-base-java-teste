package com.tivic.manager.mob;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.tivic.manager.util.OpencvUtils;

public class OpenCvRTSPClient {
	
	static {
		OpencvUtils.loadNativeLibrary();
	}
	
	public static void main(String[] args) {
		try {
			Mat mat = new Mat();
			VideoCapture vc = new VideoCapture();
						
			vc.open("rtsp://192.168.1.41:554/user=admin&password=tvk!2009&channel=1&stream=0.sdp?");

		    JLabel vidpanel = new JLabel();
			JFrame jframe = new JFrame("RTSP Client");
			
		    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    jframe.setContentPane(vidpanel);
		    jframe.setSize(1280, 720);
		    jframe.setLocationRelativeTo(null);
		    jframe.setVisible(true);
		    	
		    while (true) {
		        if (vc.read(mat)) {
		            ImageIcon image = new ImageIcon(toBufferedImage(mat));
		            vidpanel.setIcon(image);
		            vidpanel.repaint();
		            vidpanel.setBackground(Color.BLACK);	
		        }
		    }
		    
//		    vc.release();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public static BufferedImage toBufferedImage(Mat image) {
		try {
			MatOfByte bytemat = new MatOfByte();
	        Imgcodecs.imencode(".jpg", image, bytemat);
	        byte[] bytes = bytemat.toArray();
	        InputStream in = new ByteArrayInputStream(bytes);
	        BufferedImage img = null;
	        img = ImageIO.read(in);
	        return img;
		} catch (Exception e) {
			return null;
		}
    }
	
	public static boolean pingURL(String url, int timeout) {
	    url = url.replaceFirst("^https", "http"); // Otherwise an exception may be thrown on invalid SSL certificates.

	    try {
	        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
	        connection.setConnectTimeout(timeout);
	        connection.setReadTimeout(timeout);
	        connection.setRequestMethod("HEAD");
	        int responseCode = connection.getResponseCode();
	        return (200 <= responseCode && responseCode <= 399);
	    } catch (IOException exception) {
	        return false;
	    }
	}
}
