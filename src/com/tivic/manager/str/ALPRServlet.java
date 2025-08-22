package com.tivic.manager.str;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sol.util.Result;


public class ALPRServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	static final int BUFFER_SIZE = 4096;
     
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  { 
		doPost(request, response);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException  {  
	    try{
	    	      
            Enumeration<String> names = request.getHeaderNames();
            while (names.hasMoreElements()) {
                String headerName = names.nextElement();
                System.out.println(headerName + " = " + request.getHeader(headerName));        
            }
             
            InputStream inputStream = request.getInputStream();
             
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            //System.out.println("Recebendo dados...");
             
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
             
            //System.out.println("Dados recebidos.");
            
            byte[] fileContent = outputStream.toByteArray();
            
            outputStream.close();
            inputStream.close();
            
            if(fileContent == null || fileContent.length == 0)
            	System.out.println("Conteúdo de arquivo inexistente.");

            
            //remover
            //System.out.println("Gravando arquivo...");
            //FileUtils.writeByteArrayToFile(new File("file_"+System.currentTimeMillis()+".jpg"), fileContent);

            System.out.println("Reconhecendo imagem...");
            
            Result r = recognizeImage(fileContent);
            
            System.out.println("Resultado de reconhecimento de imagem obtido");
            
            String result = r.getMessage();
             
            // sends response to client
            response.getWriter().print(result);
        
	        
	    } 
        catch (Exception e) {  
            e.printStackTrace();  
        }
    }
	
	public static Result recognizeImage(byte[] data) {
        try {
        	
        	/*
        	 * Publishable Key pk_0ee4f7a47cf06b97c5023dc5
			 * Secret Key sk_c5d6d075aba974997df9db94
        	 */
            String secret_key = "sk_DEMODEMODEMODEMODEMODEMO";

			//byte[] encoded = Base64.getEncoder().encode(data);

            byte[] encoded = Base64.encodeBase64(data);
            		
            URL url = new URL("https://api.openalpr.com/v2/recognize_bytes?recognize_vehicle=1&country=br&secret_key=" + secret_key);
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection)con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setFixedLengthStreamingMode(encoded.length);
            http.setDoOutput(true);

            try(OutputStream os = http.getOutputStream()) {
                os.write(encoded);
            }

            int status_code = http.getResponseCode();
            if (status_code == 200) {

            	BufferedReader in = new BufferedReader(new InputStreamReader(
                                        http.getInputStream()));
                String json_content = "";
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    json_content += inputLine;
                in.close();

                return new Result(1, json_content);
            }
            else {
            	return new Result(-1, "Erro API ALPR: response "+status_code);
            }


        }
        catch (MalformedURLException e)  {
        	return new Result(-2, "URL mal formada.");
        }
        catch (IOException e)  {
        	return new Result(-3, "Falha de conexão.");
        }
    }
}
