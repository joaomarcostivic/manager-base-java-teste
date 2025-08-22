package com.tivic.manager.util;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;import org.apache.commons.lang3.SystemUtils;


public class ReflectionUtils {
	
	public static ArrayList<Class<?>> getClasses(String packageName) throws ClassNotFoundException { 
		return getClasses(packageName, null);
	}
	
	public static ArrayList<Class<?>> getClasses(String packageName, final String filter) throws ClassNotFoundException { 
		
		ArrayList<Class<?>> classes=new ArrayList<Class<?>>();  
		
		try {
			File directory=null; 
			
			try { 
				directory=new File(Thread.currentThread().getContextClassLoader().getResource('/'+packageName.replace('.', '/')).getFile()); 
			} 
			catch(NullPointerException x) { 
				throw new ClassNotFoundException("Pacote invalido: "+packageName); 
			} 
			
			if(directory.exists()) { 
				
				File[] files = new File[0];
				if(filter!=null && !filter.equals("")){
					 FileFilter ff = new FileFilter() {
						
						@Override
						public boolean accept(File f) {
							return f.getName().indexOf(filter)!=-1 || f.isDirectory();
						}
					};
					files = directory.listFiles(ff);
				}
				else files = directory.listFiles(); 
				for(int i=0; i<files.length; i++) { 
					if(files[i].isDirectory()){
						classes.addAll(getClasses(packageName+"."+files[i].getName(), filter));
					}
					else if(files[i].getName().endsWith(".class")) { 
						classes.add(Class.forName(packageName+'.'+files[i].getName().substring(0, files[i].getName().length()-6))); 
					}
				}
				//System.out.println(classes.size() + " classes encontradas");
				
			} 
			else { 
				throw new ClassNotFoundException("Pacote invalido: "+packageName); 
			}  
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return classes;
	} 
	
	public static ArrayList<String> getClassesNames(String packageName, final String filter) throws ClassNotFoundException { 
		
		ArrayList<String> classes=new ArrayList<String>();  
		
		try {
			File directory=null; 
			
			try {
				if(SystemUtils.IS_OS_WINDOWS)
					directory=new File(Thread.currentThread().getContextClassLoader().getResource(packageName.replace('.', '/')).getFile().substring(1).replaceAll("%20", " "));
				else
					directory=new File(Thread.currentThread().getContextClassLoader().getResource(packageName.replace('.', '/')).getFile());
			} 
			catch(NullPointerException x) { 
				throw new ClassNotFoundException("Pacote invalido: "+packageName); 
			} 
			
			if(directory.exists()) { 
				
				File[] files = new File[0];
				if(filter!=null && !filter.equals("")){
					 FileFilter ff = new FileFilter() {
						
						@Override
						public boolean accept(File f) {
							return f.isDirectory() || (f.getName().indexOf(filter)!=-1 && f.getName().indexOf("$")==-1);
						}
					};
					files = directory.listFiles(ff);
				}
				else files = directory.listFiles(); 
				for(int i=0; i<files.length; i++) { 
					if(files[i].isDirectory()){
						classes.addAll(getClassesNames(packageName+"."+files[i].getName(), filter));
					}
					else if(files[i].getName().endsWith(".class")) { 
						classes.add(packageName+'.'+files[i].getName().substring(0, files[i].getName().length()-6)); 
					}
				}
				
			} 
			else { 
				throw new ClassNotFoundException("Pacote invalido: "+packageName); 
			}  
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return classes;
	} 
}
