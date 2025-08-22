package com.tivic.manager.mob.radar;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tivic.manager.grl.equipamento.EquipamentoServices;
import com.tivic.manager.util.DeviceMonitor;

@WebServlet("/RadarRequestServlet")
public class RadarRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


    public RadarRequestServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			PrintWriter out = response.getWriter();
			
			String id = request.getParameter("id");
			String ip = request.getRemoteAddr();
			
			String strResponse = "=====================================================================================\n";
			strResponse += "[" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "] ID: " + id + " - IP: " + ip + "\n";
			strResponse += "=====================================================================================\n";

			System.out.println(strResponse);
			
			EquipamentoServices.updateHost(id, ip, null);
			
			DeviceMonitor.saveDeviceSession(id, ip);
			//DeviceMonitor.print();
			
			out.print(strResponse);			
		} catch (Exception e) {
			
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
