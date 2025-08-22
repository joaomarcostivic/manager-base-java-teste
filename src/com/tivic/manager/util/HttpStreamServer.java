package com.tivic.manager.util;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.repository.EquipamentoDAO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HttpStreamServer implements Runnable {

	private ServerSocket serverSocket;
	private HttpServer server;
	private Socket socket;
	private final String boundary = "stream";
	public ArrayList<Mat> imgs;
	public int port = 3000;
	
	public HttpStreamServer() {}
	
	public HttpStreamServer(ArrayList<Mat> frame) {
		this.imgs = frame;
	}

	public void startStreamingServer() throws IOException {
		server = HttpServer.create(new InetSocketAddress(port), 0);			

		server.createContext("/stream", new Stream());
		
		server.setExecutor(java.util.concurrent.Executors.newCachedThreadPool()); // creates a default executor
		server.start();
	}

	class Stream implements HttpHandler {

		private String url = null;
		private static final String NL = "\r\n";
		private static final String BOUNDARY = "--boundary";
		private static final String HEAD = NL + NL + BOUNDARY + NL + "Content-Type: image/jpeg" + NL
				+ "Content-Length: ";
		
		public Stream() {}
		
		public Stream(String url) {
			this.url = url;
		}

		@Override
		public void handle(HttpExchange t) throws IOException {
			Map<String, String> queryMap = queryToMap(t.getRequestURI().getQuery());
			OutputStream os = t.getResponseBody();
			Headers h = t.getResponseHeaders();
			
			try {
				h.add("Access-Control-Allow-Origin", "*");
//				h.add("Access-Control-Allow-Credentials", "true");
//		        h.add("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
//		        h.add("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
//		        h.add("Access-Control-Max-Age", "1728000");
				h.set("Connection", "close");
				h.set("Cache-Control", "no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0");
				h.set("Pragma", "no-cache");
				h.set("Content-Type", "multipart/x-mixed-replace; boundary=stream");
				t.sendResponseHeaders(200, 0);
				
				String cdEquipamento = queryMap.get("cdEquipamento");
				
				Equipamento equipamento = EquipamentoDAO.get(Integer.valueOf(cdEquipamento));
				Mat mat = new Mat();
				VideoCapture vc = new VideoCapture(equipamento.getNmUrlStream());
								
				while (true) {			        
					if (!vc.read(mat)) {
						break;
					}

					Size scaleSize = new Size(1280, 720);
					Imgproc.resize(mat, mat, scaleSize , 0, 0, Imgproc.INTER_AREA);
					MatOfByte bytemat = new MatOfByte();
					Imgcodecs.imencode(".jpg", mat, bytemat, new MatOfInt(Imgcodecs.CV_IMWRITE_JPEG_QUALITY, 80));
					byte[] bytes = bytemat.toArray();
					
					os.write(("Content-type: image/jpeg\r\n" + "Content-Length: " + bytes.length + "\r\n" + "\r\n").getBytes());
					os.write(bytes);
					os.write(("\r\n--" + boundary + "\r\n").getBytes());
				}
				
			} catch (Exception e) {
				t.sendResponseHeaders(401, 0);
				os.write(("Equipamento não informado").getBytes());
				os.flush();
				os.close();
			}
		}

		public Map<String, String> queryToMap(String query) {
			Map<String, String> result = new HashMap<String, String>();
			for (String param : query.split("&")) {
				String[] entry = param.split("=");
				if (entry.length > 1) {
					result.put(entry[0], entry[1]);
				} else {
					result.put(entry[0], "");
				}
			}
			return result;
		}
	}

	public void pushImage(Mat frame, OutputStream outputStream) throws IOException {
		if (frame == null)
			return;
		try {
			outputStream = socket.getOutputStream();

			MatOfByte bytemat = new MatOfByte();
			Imgcodecs.imencode(".jpg", frame, bytemat);
			byte[] bytes = bytemat.toArray();

			outputStream
					.write(("Content-type: image/jpeg\r\n" + "Content-Length: " + bytes.length + "\r\n" + "\r\n")
							.getBytes());
			outputStream.write(bytes);
			outputStream.write(("\r\n--" + boundary + "\r\n").getBytes());
			outputStream.flush();
		} catch (Exception ex) {
		}
	}

	public void run() {
		try {
			System.out.println("go to  http://localhost:" + port + " with browser");
			startStreamingServer();
		} catch (IOException e) {
			return;
		}

	}

	public void stopStreamingServer() throws IOException {
		socket.close();
		serverSocket.close();
	}

	public static BufferedImage Mat2bufferedImage(Mat image) throws IOException {
		MatOfByte bytemat = new MatOfByte();
		Imgcodecs.imencode(".jpg", image, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = null;
		img = ImageIO.read(in);
		return img;
	}
}