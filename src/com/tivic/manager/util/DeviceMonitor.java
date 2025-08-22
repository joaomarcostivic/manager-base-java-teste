package com.tivic.manager.util;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;

import com.tivic.manager.grl.equipamento.Equipamento;
import com.tivic.manager.grl.equipamento.EquipamentoServices;

public class DeviceMonitor {

	private static ArrayList<HashMap<String, Object>> deviceSessions = null;
	
	
	public static final int ONLINE = 0;
	public static final int WAITING = 1;
	public static final int OFFLINE = 2;
	
	public static final int ONLINE_TIMEOUT = 5; //Em minutos
	public static final int WAITING_TIMEOUT = 15; //Em minutos. Acima do valor, OFFLINE
	
	public static final String[] ackStatus = {"Online", "Waiting", "Offline"};
	
	public static void start() {
		System.out.println("\tIniciando Monitor de Dispositivos...");
		deviceSessions = new ArrayList<HashMap<String, Object>>();
	}
	
	public static boolean started() {
		return deviceSessions!=null;
			
	}
	
	public static HashMap<String, Object> saveDeviceSession(String id, String ip) {
		
		if(!started())
			return null;
		
		HashMap<String, Object> deviceSession = new HashMap<>();
		if(existsDeviceSession(id)) {
			deviceSession = getDeviceSession(id);
			
			if(!ip.equals(deviceSession.get("IP"))) {
				addMessage(id, new GregorianCalendar(), DeviceMonitorMessage.TP_INFO, "IP alterado: de "+deviceSession.get("IP") + " para " + ip);
			}
			
		}
		else {
			Equipamento equipamento = EquipamentoServices.getByIdEquipamento(id);
			
			deviceSession.put("ID", id);
			deviceSession.put("DEVICE", equipamento);
			deviceSession.put("MESSAGES", new ArrayList<DeviceMonitorMessage>());
			deviceSession.put("CREATION", new GregorianCalendar());

			deviceSessions.add(deviceSession);
		}

		deviceSession.put("IP", ip);
		deviceSession.put("LAST_ACK", new GregorianCalendar());
		
		return deviceSession;
	}
	
	public static HashMap<String, Object> getDeviceSession(String id) {
		for (HashMap<String, Object> ds : deviceSessions) {
			if(ds.get("ID").equals(id))
				return ds;
		}
		return null;
	}
	
	public static boolean existsDeviceSession(String id) {
		for (HashMap<String, Object> ds : deviceSessions) {
			if(ds.get("ID").equals(id))
				return true;
		}
		return false;
	}
	
	public static HashMap<String, Object> removeDeviceSession(String id) {
		
		HashMap<String, Object> deviceSession = null;
		for (HashMap<String, Object> ds : deviceSessions) {
			if(ds.get("ID").equals(id)) {
				deviceSessions.remove(ds);
				deviceSession = ds;
			}
		}
		
		return deviceSession;
	}
	
	public static void updateAck(String id, GregorianCalendar time) {
		HashMap<String, Object> deviceSession = getDeviceSession(id);
		
		if(deviceSession==null) 
			LogUtils.debug("DeviceMonitor.updateAck(): ID inexistente.");
		
		deviceSession.put("LAST_ACK", time);
	}
	
	public static int getDeviceAckStatus(String id) {
		HashMap<String, Object> deviceSession = getDeviceSession(id);
		
		if(deviceSession==null) 
			LogUtils.debug("DeviceMonitor.getAckStatus(): ID inexistente.");
		
		GregorianCalendar last = (GregorianCalendar)deviceSession.get("LAST_ACK");
		
		return getAckStatus(last);
		
	}
	
	public static int getAckStatus(GregorianCalendar ackTime) {
		GregorianCalendar now = new GregorianCalendar();
		
		long delta = now.getTimeInMillis() - ackTime.getTimeInMillis();
		
		if(delta < ONLINE_TIMEOUT*60*1000)
			return ONLINE;
		else if (delta < WAITING_TIMEOUT*60*1000)
			return WAITING;
		else
			return OFFLINE;
		
	}
	
	@SuppressWarnings("unchecked")
	public static void addMessage(String id, GregorianCalendar dtMessage, int tpMessage, String dsMessage) {
		HashMap<String, Object> deviceSession = getDeviceSession(id);
		
		if(deviceSession==null) 
			LogUtils.debug("DeviceMonitor.addMessage(): Erro ao adicionar mensagem. ID inexistente.");
		
		ArrayList<DeviceMonitorMessage> messages = (ArrayList<DeviceMonitorMessage>)deviceSession.get("MESSAGES");
		messages.add(new DeviceMonitorMessage(dtMessage, tpMessage, dsMessage));
		
		deviceSession.put("MESSAGES", messages);
		
		updateAck(id, new GregorianCalendar());
	}
	
	@SuppressWarnings("unchecked")
	public static String getJSON() {
		
		String json = "[";
		
		for (HashMap<String, Object> ds : deviceSessions) {
			json += "{";
			
				json += "ID: '" + ds.get("ID") + "', ";
				json += "IP: '" + ds.get("IP") + "', ";
				json += "STATUS: '" + ackStatus[getAckStatus((GregorianCalendar)ds.get("LAST_ACK"))] +"', ";
				json += "CREATION: '" + Util.formatDate((GregorianCalendar)ds.get("CREATION"), "dd/MM/yyyy HH:mm") +"', ";
				json += "LAST_ACK: '" + Util.formatDate((GregorianCalendar)ds.get("LAST_ACK"), "dd/MM/yyyy HH:mm") +"', ";
			
				ArrayList<DeviceMonitorMessage> messages = (ArrayList<DeviceMonitorMessage>)ds.get("MESSAGES");
				
				json += "MESSAGES: [";
				
				for (DeviceMonitorMessage deviceMonitorMessage : messages) {
					json += "{";
					json += "DT_MESSAGE: '" + Util.formatDate(deviceMonitorMessage.getDtMessage(), "dd/MM/yyyy HH:mm") + "', ";
					json += "TP_MESSAGE: '" + DeviceMonitorMessage.messageTypes[deviceMonitorMessage.getTpMessage()] + "', ";
					json += "DS_MESSAGE: '" + deviceMonitorMessage.getDsMessage() + "'" ;
					json += "}";
					
					if(messages.indexOf(deviceMonitorMessage) < messages.size()-1)
						json += ", ";
				}
				json += "]";
				
			json += "}";
			
			if(deviceSessions.indexOf(ds) < deviceSessions.size()-1)
				json += ", ";
		}
		
		json += "]";
		return json;
	}
	
	public static void print() {
		System.out.println(getJSON());
	}
	
}

class DeviceMonitorMessage {

	public static final int TP_INFO = 0;
	public static final int TP_ALERT = 1;
	public static final int TP_ERROR = 2;

	public static final String[] messageTypes = {"Info", "Alert", "Error"};
	
	private GregorianCalendar dtMessage;
	private int tpMessage;
	private String dsMessage;
	
	public DeviceMonitorMessage() { }

	public DeviceMonitorMessage(GregorianCalendar dtMessage,
								int tpMessage,
								String dsMessage) {
		setDtMessage(dtMessage);
		setTpMessage(tpMessage);
		setDsMessage(dsMessage);
	}

	public GregorianCalendar getDtMessage() {
		return dtMessage;
	}

	public void setDtMessage(GregorianCalendar dtMessage) {
		this.dtMessage = dtMessage;
	}

	public int getTpMessage() {
		return tpMessage;
	}

	public void setTpMessage(int tpMessage) {
		this.tpMessage = tpMessage;
	}

	public String getDsMessage() {
		return dsMessage;
	}

	public void setDsMessage(String dsMessage) {
		this.dsMessage = dsMessage;
	}
}
