package com.tivic.manager.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tivic.manager.util.FileUploadListener.FileUploadStats;


public class AjaxUploadStatusServlet extends HttpServlet {

  	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FileUploadStats stats = (FileUploadStats) request.getSession().getAttribute("FILE_UPLOAD_STATS");
        if(stats != null) {
            response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.addHeader("Pragma", "no-cache");
            
            double percentComplete = stats != null ? percentComplete = stats.getPercentComplete() : 0.0;
            long bytesProcessed = stats.getBytesRead();
            long sizeTotal = stats.getTotalSize();
            DecimalFormat decFormatter = new DecimalFormat(".00");
            long elapsedTimeInMilliseconds = stats.getElapsedTimeInMilliseconds();
            double bytesPerMillisecond = bytesProcessed / (elapsedTimeInMilliseconds + 0.00001);
            long estimatedMillisecondsLeft = (long)((sizeTotal - bytesProcessed) / (bytesPerMillisecond + 0.00001));
            String timeLeft = null;
            if ( ( estimatedMillisecondsLeft/3600 ) > 24 ) {
                timeLeft = (long)(estimatedMillisecondsLeft/3600) + "h";
            } else {
                Calendar c = new GregorianCalendar();
                long ad =  estimatedMillisecondsLeft - (c.get(Calendar.ZONE_OFFSET)+c.get(Calendar.DST_OFFSET));
                timeLeft = new SimpleDateFormat("HH:mm:ss").format(new Date(ad));
            }

            PrintWriter out = response.getWriter();
            
            String jsonStatus = "{percentComplete:"+decFormatter.format((percentComplete * 100)).replaceAll(",", ".")+"," +
            					 "elapsedTimeInMilliseconds:"+elapsedTimeInMilliseconds+"," +
            					 "estimatedMillisecondsLeft:"+estimatedMillisecondsLeft+"," +
            					 "timeLeft:'"+timeLeft+"'," +
            					 "transferVelocity:'"+decFormatter.format(bytesPerMillisecond)+"kb/s'," +
            					 "bytesProcessed:"+decFormatter.format((double)bytesProcessed/1024).replaceAll(",", ".")+"," +
            					 "sizeTotal:"+decFormatter.format((double)sizeTotal/1024).replaceAll(",", ".")+"," +
            					 "status:"+stats.getCurrentStatus()+"}";
            out.print(jsonStatus);
            out.flush();
        } 
        else {
        	System.out.println("{null}");
        }
    }
}