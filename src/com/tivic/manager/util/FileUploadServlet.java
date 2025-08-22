package com.tivic.manager.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.missiondata.fileupload.MonitoredDiskFileItemFactory;
import com.tivic.manager.util.FileUploadListener.FileUploadStats;


public class FileUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
    		FileUploadListener listener = new FileUploadListener(request.getContentLength());
            request.getSession().setAttribute("FILE_UPLOAD_STATS", listener.getFileUploadStats());

            DiskFileItemFactory factory = new MonitoredDiskFileItemFactory(listener);
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<?> items = upload.parseRequest(request);
            for (Iterator<?> i = items.iterator(); i.hasNext();) {
                FileItem fileItem = (FileItem) i.next();
                if (!fileItem.isFormField()) {

                	byte[] bytes = fileItem.get();

                	String className = (String)request.getSession().getAttribute("FILE_UPLOAD_CLASSNAME");
            		String methodName = (String)request.getSession().getAttribute("FILE_UPLOAD_METHOD");
            		String additionalArgs = (String)request.getSession().getAttribute("FILE_UPLOAD_ARGS");

            		ArrayList<Class<?>> addArgsClasses = new ArrayList<Class<?>>();
        			ArrayList<Object> addArgsValues = new ArrayList<Object>();
            		if(additionalArgs!=null && !additionalArgs.equals("")){
            			StringTokenizer token = new StringTokenizer(additionalArgs, "|");
            			while(token.hasMoreTokens()){
            				String arg = token.nextToken();
            				Class<?> c = int.class;//(Class)Class.forName(arg.substring(0, arg.indexOf(":")));
            				Object v = Integer.valueOf(arg.substring(arg.indexOf(":")+1)).intValue();
            				addArgsClasses.add(c);
            				addArgsValues.add(v);
            			}
            		}
            		Class<?>[] params = new Class[addArgsClasses.size()+1];
            		Object[] args = new Object[addArgsClasses.size()+1];

            		for(int j=0; j<addArgsClasses.size()+1; j++){
            			if(j==0){
            				params[j] = byte[].class;
            				args[j] = bytes;
            			}
            			else{
            				params[j] = (Class<?>)addArgsClasses.get(j-1);
            				args[j] = (Object)addArgsValues.get(j-1);
            			}
            		}

                	Object[] paramArgs = {params, args};

                	Object objectClass = Class.forName(className).newInstance();
                	Method method = ((Class<?>)Class.forName(className)).getDeclaredMethod(methodName, (Class[])paramArgs[0]);
                	Object objectReturn = method.invoke(objectClass, (Object[])paramArgs[1]);
                	PrintWriter out = response.getWriter();
                	out.print(sol.util.Jso.getStream(objectReturn));
                }
            }
        } catch (Exception e) {
            FileUploadStats stats = new FileUploadListener.FileUploadStats();
            stats.setCurrentStatus(FileUploadStats.ERROR);
            request.getSession().setAttribute("FILE_UPLOAD_STATS", stats);
            e.printStackTrace(System.out);
        }
    }
}
