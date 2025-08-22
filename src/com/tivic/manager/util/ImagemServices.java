package com.tivic.manager.util;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.tivic.manager.conf.ManagerConf;

public class ImagemServices {

	public static byte[] getBytesImageScaled(byte[] imagem, int width, int height, String urlImagemDefault){
		try{
			
			BufferedImage imageSrc;
			if(imagem==null)
				imageSrc = ImageIO.read(new URL(urlImagemDefault));
			else
				imageSrc = ImageIO.read(new ByteArrayInputStream(imagem));
			
			System.out.println(imageSrc);
			boolean visualizadorPaisagem = false;
			if(width>height)
				visualizadorPaisagem = true;

			boolean imagemPaisagem = false;
			if(imageSrc.getWidth()>imageSrc.getHeight())//paisagem
				imagemPaisagem = true;

			if(width==-1)
				width = height*imageSrc.getWidth()/imageSrc.getHeight();

			if(height==-1)
				height = width*imageSrc.getHeight()/imageSrc.getWidth();

			if((visualizadorPaisagem && imagemPaisagem) ||
			   (!visualizadorPaisagem && imagemPaisagem))
				width = height*imageSrc.getWidth()/imageSrc.getHeight();
			else if((visualizadorPaisagem && !imagemPaisagem) ||
					(!visualizadorPaisagem && !imagemPaisagem))
				height = width*imageSrc.getHeight()/imageSrc.getWidth();

			if(imageSrc.getWidth()<width && imageSrc.getHeight()<height){
				width = imageSrc.getWidth();
				height = imageSrc.getHeight();
			}


			//Image img = imageSrc.getScaledInstance(width, height ,Image.SCALE_FAST);

			BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

			for (int ti = 0; ti < width; ti++) {
	            for (int tj = 0; tj < height; tj++) {
	                int si = ti * imageSrc.getWidth()  / width;
	                int sj = tj * imageSrc.getHeight() / height;
	                bi.setRGB(ti, tj, imageSrc.getRGB(si, sj));
	            }
	        }


			//Graphics2D biContext = bi.createGraphics();
			//biContext.drawImage(imageSrc, 0, 0, width, height, null);

			ByteArrayOutputStream baout = new ByteArrayOutputStream();
			ImageIO.write(bi, "JPEG", baout);

			return baout.toByteArray();
			/*
			BufferedImage sourceImage;
			if(imagem==null)
				sourceImage = ImageIO.read(new URL(urlImagemDefault));
			else
				sourceImage = ImageIO.read(new ByteArrayInputStream(imagem));

			//orientacao do visualizador
			boolean visualizadorPaisagem = false;
			if(width>height)
				visualizadorPaisagem = true;

			boolean imagemPaisagem = false;
			if(sourceImage.getWidth()>sourceImage.getHeight())//paisagem
				imagemPaisagem = true;

			if((visualizadorPaisagem && imagemPaisagem) ||
			   (!visualizadorPaisagem && imagemPaisagem))
				width=-1;
			else if((visualizadorPaisagem && !imagemPaisagem) ||
					(!visualizadorPaisagem && !imagemPaisagem))
				height=-1;


			   BufferedImage bdest =
			      new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			   Graphics2D g = bdest.createGraphics();
			   AffineTransform at = AffineTransform.getScaleInstance((double)width, (double)height);
			   g.drawRenderedImage(sourceImage,at);

			   ByteArrayOutputStream baout = new ByteArrayOutputStream();
			   ImageIO.write(bdest, "jpg", baout);
		       return baout.toByteArray();

			/*ImageIcon image = null;
			if(imagem==null)
				image = new ImageIcon(new URL(urlImagemDefault));
			else
				image = new ImageIcon(imagem);

			//orientacao do visualizador
			boolean visualizadorPaisagem = false;
			if(width>height)
				visualizadorPaisagem = true;

			boolean imagemPaisagem = false;
			if(image.getIconWidth()>image.getIconHeight())//paisagem
				imagemPaisagem = true;

			if((visualizadorPaisagem && imagemPaisagem) ||
			   (!visualizadorPaisagem && imagemPaisagem))
				width=-1;
			else if((visualizadorPaisagem && !imagemPaisagem) ||
					(!visualizadorPaisagem && !imagemPaisagem))
				height=-1;

			ImageIcon thumb = new ImageIcon(image.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
			BufferedImage bi = new BufferedImage(
	                thumb.getIconWidth(),
	                thumb.getIconHeight(),
	                BufferedImage.TYPE_INT_RGB
	            );
			Graphics g = bi.getGraphics();
	        g.drawImage(thumb.getImage(), 0, 0, null);
	        ByteArrayOutputStream baout = new ByteArrayOutputStream();
	        ImageIO.write(bi, "jpg", baout);
	        return baout.toByteArray();*/
		}
		catch(java.lang.IllegalArgumentException e) {
			//e.printStackTrace(System.out);
			System.err.println("Erro! ImagemServices.getBytesImageScaled: " + e);
			return null;
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
			System.err.println("Erro! ImagemServices.getBytesImageScaled: " + e);
			return null;
		}
    }
	
	/**
	 * @comment works with any filetype as well (@author mauriciocordeiro)
	 * @param uploadedInputStream
	 * @return
	 */
	public static ByteArrayOutputStream writeToByteArray(InputStream uploadedInputStream) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = uploadedInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();

			return out;
		} catch (IOException e) {

			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] getArrayBytesFromArrayList(ArrayList<Byte> byteList) {
		try {
			byte[] byteArray = new byte[byteList.size()];
			for (int i = 0; i < byteList.size(); i++) {
			   byteArray[i] = (byte)Integer.parseInt(String.valueOf(byteList.get(i)));
			}
			
			return byteArray;
		} catch (Exception e) {
			System.err.println(e.getStackTrace());
			return null;
		}
	}
	
	public static File compress(File input) {
        try {
        	BufferedImage image = ImageIO.read(input);

    		File output = new File(input.getAbsolutePath()+".compressed.jpg");
            OutputStream out = new FileOutputStream(output);

            ImageWriter writer =  ImageIO.getImageWritersByFormatName("jpg").next();
            ImageOutputStream ios = ImageIO.createImageOutputStream(out);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()){
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(0.5f);
            }

            writer.write(null, new IIOImage(image, null, null), param);

            out.close();
            ios.close();
            writer.dispose();

            return output;
        } catch (IOException e) {
			e.printStackTrace(System.out);
			return input;
		}
    }
	
	public static byte[] compress(byte[] inputArray) {
		File inputFile = null;
		File outputFile = null;
		try {
			String workdir = ManagerConf.getInstance().get("TOMCAT_WORK_DIR");
			Path path = Paths.get(workdir+Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmssSSS")+".jpg");
			
			inputFile = Files.write(path, inputArray, StandardOpenOption.CREATE_NEW).toFile();
			
			outputFile = compress(inputFile);
			
			return Files.readAllBytes(outputFile.toPath());
		} catch (IOException e) {
			e.printStackTrace(System.out);
			return inputArray;
		} finally {
			if(inputFile!=null && inputFile.exists()) 
				inputFile.delete();

			if(outputFile!=null && outputFile.exists()) 
				outputFile.delete();			
		}
	}
}
