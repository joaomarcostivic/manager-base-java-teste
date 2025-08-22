package com.tivic.manager.mob.alpr;

import java.awt.Point;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;

public class CateyeALPRServices implements AlprService {
	
	private String workdir;
	private String script;
	private String server;
	
	public CateyeALPRServices() {
		this.workdir = ManagerConf.getInstance().get("TOMCAT_WORK_DIR");
		this.script = ManagerConf.getInstance().get("CATEYE_SCRIPT");
		this.server = ManagerConf.getInstance().get("CATEYE_SERVER");
	}

	@Override
	public AlprResult recognize(byte[] file) throws IOException, InterruptedException, JSONException {
		return recognize(file, Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss'.jpg'"));
	}

	@Override
	public AlprResult recognize(byte[] file, String filename) throws IOException, InterruptedException {
		return recognize(file, filename, Long.toString(new GregorianCalendar().getTimeInMillis()));
	}

	@Override
	public AlprResult recognize(byte[] file, String filename, String id) throws IOException, InterruptedException {
		Path path = null;
		try {			
			filename = (this.workdir+filename).replaceAll("\"", "");
			path = save(file, filename);
			
			CateyeResultBuilder rBuilder = new CateyeResultBuilder(runAlpr(filename));		
			return rBuilder.build();
			
		} catch (JSONException e) {
			System.out.println("CateyeALPRServices.recognize: Erro ao formatar JSON de resposta");
			return new AlprResult(0, 0, 0l, null, new ArrayList<Plate>(), new ArrayList<Point>());
		} finally {
			if(path!=null)
				delete(path);
		}
	}
	
	private Path save(byte[] file, String name) throws IOException {
				
		Path path = Paths.get(name);		
		Path _path = Files.write(path, file);
		
		Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
		perms.add(PosixFilePermission.OWNER_READ);
		perms.add(PosixFilePermission.OWNER_WRITE);
		perms.add(PosixFilePermission.OWNER_EXECUTE);
		perms.add(PosixFilePermission.OTHERS_READ);
		perms.add(PosixFilePermission.OTHERS_WRITE);
		perms.add(PosixFilePermission.OTHERS_EXECUTE);
		perms.add(PosixFilePermission.GROUP_READ);
		perms.add(PosixFilePermission.GROUP_WRITE);
		perms.add(PosixFilePermission.GROUP_EXECUTE);
		
//		Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-rw-r--");
		
		Files.setPosixFilePermissions(path, perms);		
		
		return _path;
	}
	
	private String runAlpr(String filename, String id) throws IOException, InterruptedException {
		return runAlpr(this.script, this.server, filename, id);
	}
	
	private String runAlpr(String script, String server, String filename, String id) throws IOException, InterruptedException {
		
		id = id.replaceAll(" ", "_");
		
		String python = ManagerConf.getInstance().get("CATEYE_PYTHON", "python");
		String user = ManagerConf.getInstance().get("CATEYE_USER", null);
		
		StringBuilder result = new StringBuilder();
		
		ProcessBuilder builder = new ProcessBuilder();
		
		if(user == null) {
			builder.command(python, script, "-s", server, "-c", id, "-i", filename);
		} else {
			StringBuilder cmd = new StringBuilder();
			cmd.append("su -");
			cmd.append(" ");
			cmd.append("tivic_server");
			cmd.append(" ");
			cmd.append("-c");
			cmd.append(" ");
			cmd.append("\"");
			cmd.append(python);
			cmd.append(" ");
			cmd.append(script);
			cmd.append(" ");
			cmd.append("-s");
			cmd.append(" ");
			cmd.append(server);
			cmd.append(" ");
			cmd.append("-c");
			cmd.append(" ");
			cmd.append(id);
			cmd.append(" ");
			cmd.append("-i");
			cmd.append(" ");
			cmd.append(filename);			
			cmd.append("\"");
			
			System.out.println("\n\n\t"+cmd.toString());
			
			builder.command(cmd.toString());			
		}
		
		builder.directory(new File(this.workdir));
		
		Process process = builder.start();
		
		StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), result);
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		int exitCode = process.waitFor();
		assert exitCode == 0;
		
		System.out.println("\t"+result.toString()+"\n\n");

		return result.toString();
	}
	
	private String runAlpr(String filename) {
		try {			
			String url = ManagerConf.getInstance().get("CATEYE_WEB", "http://192.168.1.20:5000/alpr");
			
//			Unirest.setTimeouts(0, 0);
			HttpResponse<String> response = Unirest.post(url)
			  .field("file", new File(filename))
			  .asString();
			
			return response.getBody();
		} catch (Exception e) {
			e.printStackTrace(System.out);
			return null;
		}
		
		
	}
	
	private boolean delete(Path path) {
		try {
			return path.toFile().delete();
		} catch (Exception e) {
			return false;
		}
	}

}
