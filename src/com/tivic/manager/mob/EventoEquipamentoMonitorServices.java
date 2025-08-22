package com.tivic.manager.mob;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.tivic.manager.conf.ManagerConf;
import com.tivic.manager.msg.bot.Bender;
import com.tivic.manager.msg.bot.WebhookBotFactory;
import com.tivic.manager.util.Util;

public class EventoEquipamentoMonitorServices {
	
	public static void checkInfracao(String path) {
		try {
			
			File file = getLatestFilefromDir(path+"/PROCESSADO/INFRACAO");
			
			Long lastMofified = file.lastModified();
			if(lastMofified == 0L)
				return;
			
			Date dtFile = new Date(lastMofified);
			
			int qtHoras = ManagerConf.getInstance().getAsInteger("QT_HORAS_FALHA_RADAR", 12);
			
			GregorianCalendar now = new GregorianCalendar();
			now.add(Calendar.HOUR_OF_DAY, -qtHoras);
			Date timeAgo = new Date(now.getTimeInMillis());
			
			if(dtFile.before(timeAgo)) {
				String parts[] = path.split("/");
				String id = parts[parts.length - 1];
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				String date = simpleDateFormat.format(dtFile);
				
				StringBuilder msg = new StringBuilder();
				msg.append(id);
				msg.append(" não tem evento processado desde ");
				msg.append(date);
				msg.append(".");
				
				WebhookBotFactory.create(Bender.class).enviar(msg.toString());
			} else {
				System.out.println("Radar OK");
			}
		}
		catch(Exception e) {
			e.printStackTrace(System.out);
		}
	}
	
	private static File getLatestFilefromDir(String dirPath){
	    File dir = new File(dirPath);
	    File[] files = dir.listFiles();
	    if (files == null || files.length == 0) {
	        return null;
	    }

	    File lastModifiedFile = files[0];
	    for (int i = 1; i < files.length; i++) {
	       if (lastModifiedFile.lastModified() < files[i].lastModified()) {
	           lastModifiedFile = files[i];
	       }
	    }
	    return lastModifiedFile;
	}
}
