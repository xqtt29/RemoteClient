package com.chinacreator.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.chinacreator.service.DataOprService;
import com.chinacreator.service.RemoteClientService;

public class ClientMain {

	public static void main(String[] args) {
		final Map<String,String> conf=DataOprService.getInstance().getProp();
		final JFrame frame=new JFrame("远程控制");
		frame.setLayout(new BorderLayout());
    	frame.setSize(300,200);
    	JButton button=new JButton("连接");
    	button.addMouseListener(new MouseAdapter() {
    		public void mouseClicked(MouseEvent e){
    			frame.setVisible(false);
    			RemoteClientService rcs=new RemoteClientService();
    			rcs.ShowUI();
    			rcs.connService(conf.get("defaultIp").toString(), Integer.parseInt(conf.get("defaultPort").toString()));
    			Thread t=new Thread(rcs);
    			t.start();
    		}
		});
    	JLabel label1=new JLabel("左");
    	JLabel label2=new JLabel("右");
    	frame.add(button,BorderLayout.NORTH);
    	frame.add(label1,BorderLayout.WEST);
    	frame.add(label2,BorderLayout.EAST);
    	Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    	Dimension framesize = frame.getSize(); 
    	int x = (int)screensize.getWidth()/2 - (int)framesize.getWidth()/2; 
    	int y = (int)screensize.getHeight()/2 - (int)framesize.getHeight()/2; 
    	frame.setLocation(x,y);
    	frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
    	});
        frame.setVisible(true);
	}
}
