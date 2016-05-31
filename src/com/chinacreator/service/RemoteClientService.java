package com.chinacreator.service;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class RemoteClientService implements Runnable{


	JLabel label=new JLabel();
	Socket socket = null;
    ObjectOutputStream dos = null;
    ObjectInputStream dis = null;
	@Override
	public void run() {
        try {
            try {
                while(true){
	                int length=dis.readInt();
	                byte[] data=new byte[length];
	                dis.readFully(data);
	                ByteArrayInputStream bais=new ByteArrayInputStream(data);
	                BufferedImage bi=ImageIO.read(bais);
	                ImageIcon ii=new ImageIcon(bi);
	                BufferedImage nbi=resizeImage(ii.getImage(),1152,664);
	                final ImageIcon i=new ImageIcon(nbi);
			    	label.setIcon(i);
			    	label.repaint();
                }
            } finally {
                if (dis != null)
                	dis.close();
                if (dos != null)
                    dos.close();
                if (socket != null)
                    socket.close();
            }
        }catch (Exception e) {
        	e.printStackTrace();
        }
	}
	
	public void connService(String ip,int port) {
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip,port),
			               3 * 1000);
			dos = new ObjectOutputStream(socket.getOutputStream());
            dos.writeInt(1);
            dos.flush();
			dis = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ShowUI(){
		JFrame f=new JFrame("远程控制");
    	f.setSize(1152,700);
    	label.setSize(1152,664);
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
    	Dimension framesize = f.getSize(); 
    	int x = (int)screensize.getWidth()/2 - (int)framesize.getWidth()/2; 
    	int y = (int)screensize.getHeight()/2 - (int)framesize.getHeight()/2; 
    	f.setLocation(x,y);
    	f.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}
    	});
    	f.add(label);
    	label.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e) {
            	sendEventObject(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {
            	sendEventObject(e);
            }
            @Override
            public void keyTyped(KeyEvent e) {
           	 sendEventObject(e);
            }
	   	});
    	label.addMouseWheelListener(new MouseWheelListener(){
	            public void mouseWheelMoved(MouseWheelEvent e) {
	            	sendEventObject(e);
	            }
	   	});
    	label.addMouseMotionListener(new MouseMotionListener(){
	            public void mouseDragged(MouseEvent e) {
	            	sendEventObject(e);
	            }
	            public void mouseMoved(MouseEvent e) {
	            	sendEventObject(e);
	            }
	   	});
    	label.addMouseListener(new MouseListener(){
	            public void mouseClicked(MouseEvent e) {
	            	sendEventObject(e);
	            }
	            public void mouseEntered(MouseEvent e) {
	            	sendEventObject(e);
	            }
	            public void mouseExited(MouseEvent e) {
	            	sendEventObject(e);
	            }
	            public void mousePressed(MouseEvent e) {
	            	sendEventObject(e);
	            }
	            public void mouseReleased(MouseEvent e) {
	            	sendEventObject(e);
	            }
	   	});
    	f.setVisible(true);
	}
	private BufferedImage resizeImage(Image img,int Wlen,int Hlen){
		int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage dimg = new BufferedImage(Wlen, Hlen,BufferedImage.TYPE_INT_BGR);
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                      RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, Wlen, Hlen, 0, 0, w, h, null);
        g.dispose();
        return dimg;
	}
	public void sendEventObject(InputEvent e){
		try {
			dos.writeObject(e);
            dos.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
