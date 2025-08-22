package com.tivic.manager.mob.alpr;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.util.Util;

/**
 * Requer o ApenALPR instalado localmente
 * 
 * @author Maurício <mauricio@tivic.com.br>
 *
 */
public class OpenALPRServices implements AlprService {
	
	private String workdir;
	private boolean useDefault;
	private boolean onlyPatternMatches;
	private String country;
	private String pattern;
	private int topN;
	
	public OpenALPRServices() {
		this.workdir 			= ManagerConf.getInstance().get("TOMCAT_WORK_DIR");
		this.useDefault 		= ManagerConf.getInstance().getAsBoolean("OALPR_USE_DEFAULT");
		this.onlyPatternMatches = ManagerConf.getInstance().getAsBoolean("OALPR_ONLY_MATCHES");
		this.country 			= ManagerConf.getInstance().get("OALPR_COUNTRY", "br");
		this.pattern 			= ManagerConf.getInstance().get("OALPR_PATTERN", "br");
		this.topN 				= ManagerConf.getInstance().getAsInteger("OALPR_TOP_N", 3);
	}
	
	@Override
	public AlprResult recognize(byte[] file) throws IOException, InterruptedException, JSONException {
		return recognize(file, Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss'.jpg'"));
	}

	@Override
	public AlprResult recognize(byte[] file, String filename) throws IOException, InterruptedException, JSONException {
		return recognize(file, filename, Long.toString(new GregorianCalendar().getTimeInMillis()));
	}
	
	@Override
	public AlprResult recognize(byte[] file, String filename, String id) throws IOException, InterruptedException, JSONException {
		
		filename = (this.workdir+filename).replaceAll("\"", "");
		
		AlprResult result = new AlprResult();
		
		Path path = save(file, filename);
		
		if(this.useDefault) {
			result = new OpenALPRResultBuilder(runAlpr(filename)).build();
		} else {
			result = merge(this.onlyPatternMatches, 
					new OpenALPRResultBuilder(runAlpr("br", "br", 5, filename)).build(),
					new OpenALPRResultBuilder(runAlpr("br", "mercosul", 5, filename)).build(),
					
					new OpenALPRResultBuilder(runAlpr("br2", "br", 5, filename)).build(),
					new OpenALPRResultBuilder(runAlpr("br2", "mercosul", 5, filename)).build(),
					
					new OpenALPRResultBuilder(runAlpr("br3", "br", 5, filename)).build(),
					new OpenALPRResultBuilder(runAlpr("br3", "mercosul", 5, filename)).build());
		}
				
		if(ManagerConf.getInstance().getAsBoolean("OALPR_RESULT")) {
			store(file, result);
		}
		
		if(ManagerConf.getInstance().getAsBoolean("OALPR_TAG")) {
			toTagFile(file, result);
		}
		
		delete(path);
		
		return result;
	}
	
	private Path save(byte[] file, String name) throws IOException {
		Path path = Paths.get(name);
        return Files.write(path, file);
	}
	
	private String runAlpr(String filename) throws IOException, InterruptedException {
		return runAlpr(this.country, this.pattern, this.topN, filename);
	}
	
	private String runAlpr(String country, String pattern, int topN, String filename) throws IOException, InterruptedException {
		
		StringBuilder result = new StringBuilder();
		
		ProcessBuilder builder = new ProcessBuilder();
		builder.command("alpr", "-c", country, "-p", pattern, "-n", Integer.toString(topN), "-j", filename);
		builder.directory(new File(System.getProperty("user.dir")));
		
		Process process = builder.start();
		
		StreamGobbler streamGobbler = new StreamGobbler(process.getInputStream(), result);
		Executors.newSingleThreadExecutor().submit(streamGobbler);
		int exitCode = process.waitFor();
		assert exitCode == 0;

		return result.toString();
	}
	
	private boolean delete(Path path) {
		try {
			return path.toFile().delete();
		} catch (Exception e) {
			return false;
		}
	}
	
	private AlprResult merge(Boolean hasMatch, AlprResult... array) {
		AlprResult result = new AlprResult(
				array[0].getImgWidth(), 
				array[0].getImgHeight(), 
				array[0].getProcessingTimeMillis(), 
				null, null, null);

		result.setCoordinates(new ArrayList<Point>());
		result.setCandidates(new ArrayList<Plate>());
		for (AlprResult alprResult : array) {
			alprResult.getCandidates().forEach(p -> {
				if(hasMatch) {
					if(p.getMatchesPattern() && !result.contains(p.getPlate()))
						result.getCandidates().add(p);
				} else {
					if(!result.contains(p.getPlate()))
						result.getCandidates().add(p);
				}
			});
			
			if(result.getCoordinates().isEmpty())
				result.getCoordinates().addAll(alprResult.getCoordinates());
			
			if(result.getPlate() == null) {
				result.setPlate(alprResult.getPlate());
			}
		}
		
		return result;
	}
	
	private void store(byte[] file, AlprResult result) throws IOException {
		String path = ManagerConf.getInstance().get("OALPR_RESULT_DIR");		
		String stamp = Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss");
		
		StringBuilder jpg = new StringBuilder();
		jpg.append(path);
		jpg.append(result.getPlate());
		jpg.append("_");
		jpg.append(stamp);
		jpg.append(".jpg");
		
		StringBuilder openalpr = new StringBuilder();
		openalpr.append(path);
		openalpr.append(result.getPlate());
		openalpr.append("_");
		openalpr.append(stamp);
		openalpr.append(".openalpr");

		save(file, jpg.toString());
		result.toFile(openalpr.toString());
		
	}
	
	private void toTagFile(byte[] file, AlprResult result) throws IOException {
		if(result.getPlate() == null)
			return;
		
		String path = ManagerConf.getInstance().get("OALPR_TAG_DIR");		
		String stamp = Util.formatDate(new GregorianCalendar(), "yyyyMMddHHmmss");
		
		StringBuilder jpg = new StringBuilder();
		jpg.append(path);
		jpg.append(result.getPlate());
		jpg.append("_");
		jpg.append(stamp);
		jpg.append(".jpg");
		
		save(file, jpg.toString());
		
		StringBuilder yaml = new StringBuilder();
		yaml.append(path);
		yaml.append(result.getPlate());
		yaml.append("_");
		yaml.append(stamp);
		yaml.append("-0.yaml");
		
		File tagFile = new File(yaml.toString());
		
		StringBuilder content = new StringBuilder();
		
		content.append("image_file: ");
		content.append(jpg.toString().replaceFirst(path, ""));	
		content.append("\n");
		
		content.append("image_width: ");
		content.append(result.getImgWidth());		
		content.append("\n");
		
		content.append("image_height: ");
		content.append(result.getImgHeight());		
		content.append("\n");
		
		content.append("plate_corners_gt: ");
		content.append(toString(result.getCoordinates()));		
		content.append("\n");
		
		content.append("plate_number_gt: ");
		content.append(result.getPlate());		
		content.append("\n");
		
		content.append("plate_inverted_gt: false");		
		
				
		FileUtils.writeStringToFile(tagFile, content.toString());
	}
	
	private String toString(List<Point> coordinates) {
		StringBuilder str = new StringBuilder();
		
		for (Point point : coordinates) {
			str.append((int) point.getX());
			str.append(" ");
			str.append((int) point.getY());
			str.append(" ");
		}
		
		
		return str.toString().trim();
	}
	

}
