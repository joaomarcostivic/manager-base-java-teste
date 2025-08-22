package com.tivic.manager.util;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.awt.image.BufferedImage;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.impl.codabar.CodabarBean;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.impl.int2of5.Interleaved2Of5Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

public class BarcodeGenerator extends HttpServlet{
	public static final int _Code128Bean = 0;
	public static final int _CodabarBean = 1;
	public static final int _Code39Bean  = 2;
	public static final int _Interleaved2Of5Bean = 3;
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException
    {
		response.setContentType("image/jpeg");
		OutputStream out = response.getOutputStream();
      	try{
      		int barcodeType = (request.getParameter("barcodeType")!=null) ? Integer.parseInt(request.getParameter("barcodeType")): _Code128Bean;
      		AbstractBarcodeBean bean;
      		if(barcodeType==_CodabarBean)
      			bean = new CodabarBean();
      		else if(barcodeType==_Code39Bean)
      			bean = new Code39Bean();
      		else if(barcodeType==_Interleaved2Of5Bean)
      			bean = new Interleaved2Of5Bean();
      		else
      			bean = new Code128Bean();

			String cdBarcode = request.getParameter("cdBarcode");

			/* Apaga os dígitos impressos abaixo do CB:*/
			String showDig = request.getParameter("showDig");
			if(showDig!=null && showDig.equals("false"))
				bean.setFontSize(0.0);

			/* Ajusta para a altura desejada: */
			bean.setModuleWidth(UnitConv.in2mm(1.0f / 80));

			/* Ajusta para a altura desejada: */
			int height = (request.getParameter("height")!=null) ? Integer.parseInt(request.getParameter("height")): 25;
			bean.setBarHeight(height);
			bean.doQuietZone(false);

			// Modo monocromático e JPEG:
			BitmapCanvasProvider canvas = new BitmapCanvasProvider(out,"image/jpeg",
												80, BufferedImage.TYPE_BYTE_BINARY, false, 0);

			// Gera o BARCODE.
			bean.generateBarcode(canvas, cdBarcode);
			// Encerra o design.
			canvas.finish();
		}
		catch (Exception e){
			e.printStackTrace(System.out);
		}
		finally{
			if (out != null)
				out.close();
		}
    }

    public String getServletInfo()
    {
    	return "Barcode Generator - Central de Saude - Conquista Tecnologia";
    }
  }
