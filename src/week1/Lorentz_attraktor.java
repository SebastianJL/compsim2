package week1;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import javax.swing.JFrame;
import javax.swing.JPanel;

import week7.CustomRainbow;

public class Lorentz_attraktor extends JPanel {

	// Constants and functions that define the Lorenz attractor
	
	static boolean bool_rotate = true;
	static double sigma = 10.;  
	static double B = 8./3.;
	static double R = 28.;
	static int kuttasteps = 100;  //how many steps until drawing one point
	static int steps = 455000; //how many points saved and drawn
		
	
	// The derivative dx/dt at a given value of x1,x2,x3
	public static double derivqx(Quaternion q)
	{
	  return 0-sigma*q.x1+sigma*q.x2;
	}
	
	// The derivative dy/dt at a given value of x1,x2,x3
	public static double derivqy(Quaternion q)
	{
	return R*q.x1 - q.x2-q.x1*q.x3;
	}
	
	//The derivative dz/dt at a given value of x1,x2,x3
	public static double derivqz(Quaternion q)
	{
	return -B*q.x3 + q.x1*q.x2;
	}
	
	//Initialize coordinates
	static double x0_0 = 1;
	static double x0_1 = 1;
	static double x0_2 = 1;
	static double x0_3 = 1;
	
	//Initialize progressional list of coordinates xyz
	Quaternion[] q = new Quaternion[steps];

	
	
	private static final long serialVersionUID = 1L;  //used for JPanel
	
 public static void main(String[] argv)
 {
	 Lorentz_attraktor lorentz_a = new Lorentz_attraktor();
	 JFrame top = new JFrame("Strange Lorentz attractor");
	 top.setBounds(00, 00, 900, 900);
	 top.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 top.getContentPane().add(lorentz_a);
	 top.setVisible(true);
		
	 System.out.println("Start the engine");
	 lorentz_a.rk4();
 }
 

 public void rk4() {
	    // Computation by 4th order Runge-Kutta
	 	System.out.println("Here we go");
	 	//initialize everything
	 	double h = 1./steps;
	 	double k1x, k2x, k3x, k4x;
	 	double k1y, k2y, k3y, k4y;
	 	double k1z, k2z, k3z, k4z;

	 	
	 
	    // Initialize q[0]=
	    q[0] = new Quaternion(x0_0,x0_1,x0_2,x0_3);
	
	    // Initialize temporary Quaternion
	    Quaternion qtemp =  new Quaternion(x0_0,x0_1,x0_2,x0_3);
	    for (int i=0; i<steps-1; i++)
	    {
	    	qtemp = q[i];
	    	for (int j=0; j<kuttasteps; j++){

	  	      // Computing all of the trial values
	  		      k1x = h * derivqx(qtemp);
	  		      k1y = h * derivqy(qtemp);
	  		      k1z = h * derivqz(qtemp);
	  		      
	  		      k2x = h * derivqx(new Quaternion(qtemp.x0, qtemp.x1 + k1x/2, qtemp.x2, qtemp.x3));
	  		      k2y = h * derivqy(new Quaternion(qtemp.x0, qtemp.x1, qtemp.x2 + k1y/2, qtemp.x3));
	  		      k2z = h * derivqz(new Quaternion(qtemp.x0, qtemp.x1, qtemp.x2, qtemp.x3 + k1z/2));

	  		      k3x = h * derivqx(new Quaternion(qtemp.x0, qtemp.x1 + k2x/2, qtemp.x2, qtemp.x3));
	  		      k3y = h * derivqy(new Quaternion(qtemp.x0, qtemp.x1, qtemp.x2 + k2y/2, qtemp.x3));
	  		      k3z = h * derivqz(new Quaternion(qtemp.x0, qtemp.x1, qtemp.x2, qtemp.x3 + k2z/2));
	  		      
	  		      k4x = h * derivqx(new Quaternion(qtemp.x0, qtemp.x1 + k3x, qtemp.x2, qtemp.x3));
	  		      k4y = h * derivqy(new Quaternion(qtemp.x0, qtemp.x1, qtemp.x2 + k3y, qtemp.x3));
	  		      k4z = h * derivqz(new Quaternion(qtemp.x0, qtemp.x1, qtemp.x2, qtemp.x3 + k3z));
	  		      
	  		      qtemp = new Quaternion(qtemp.x0, qtemp.x1 + k1x/6 + k2x/3 + k3x/3 + k4x/6, qtemp.x2 + k1y/6 + k2y/3 + k3y/3 + k4y/6, qtemp.x3 + k1z/6 + k2z/3 + k3z/3 + k4z/6);
	  	    }
	    	
	    	// Incrementing q
	    	q[i+1] = qtemp;	
		    repaint();
		    }
	    
	    if (bool_rotate == true){
			rotate();
	    }
//		repaint();
 }

 
 	// Transform the z coordinate to create 3D effect
 	public int transform3D(int ds, int d0, double z, double coordinate){
		
		double trafo = 1.*coordinate*ds/(1.*(d0+z));
		
		return((int)trafo);
	}
	
 	//Transform the x coordinate to fit the screen
	public double transformx(double B, double xsmin, double xmin, double xmax, double x)
	{	
		double xtransformed = (B-xsmin)/(xmax-xmin)*(x-xmin)+xsmin;
		return xtransformed;
	}
	
 	//Transform the y coordinate to fit the screen
	public double transformy(double H, double ysmin, double ymin, double ymax, double y)
	{	
		double ytransformed = H-(H-ysmin)/(ymax-ymin)*(y-ymin);
		return ytransformed;
	}
 
 
	
	double cos = 0;
	double sin = 1;
	
	public void rotate(){
		//rotation
		double tfraction = 0.00003;
		double tfull = 18*Math.PI;
		
		for (int t = 0; t<10000; t++){

			cos = Math.cos(t*tfraction*tfull);
			sin = Math.sin(t*tfraction*tfull);
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			repaint();
		}		
	}
 
 
 
	 public void paint(Graphics g) {	
		 	//initialize stuff
			CustomRainbow customrainbow = new CustomRainbow();

			int[] x3D = new int[steps];
			int[] y3D = new int[steps];
			
			double x = 0;
			double y = 0;
			double z = 0;
			
			
		 	//set point of view
			int view_ds = 80;
			int view_d0 = 120;
			
			
			//set window
			Rectangle b = g.getClipBounds();
//			g.setColor(Color.BLACK);
//			g.fillRect(b.x, b.y,  b.width, b.height);
			

			//transformation to coordinate system
			double[] qx_trafo = new double[steps];
			double[] qy_trafo = new double[steps];
//			double[] qz_trafo = new double[steps];
			
					
			//clear window and draw background
			super.paintComponent(g);
			g.setColor(Color.BLACK);
			g.fillRect(b.x, b.y,  b.width, b.height);

		

			for (int i = 0; i < steps-1; i++) {
							
//				//*** Rotation around z ***
//				x= cos*q[i].x1-sin*q[i].x2;
//				y= sin*q[i].x1+cos*q[i].x2;
//				z= q[i].x3;
//				
//				//*** Rotation around x ***
//				x= q[i].x1;
//				y= cos*q[i].x2-sin*q[i].x3;
//				z= sin*q[i].x2+cos*q[i].x3;

				//*** Rotation around y ***
				x= cos*q[i].x1+sin*q[i].x3;
				y= q[i].x2;
				z= 0.-sin*q[i].x1+cos*q[i].x3;
				
				
				//transformation
				qx_trafo[i] = transformx((double)b.width,(double)b.x,-45, 25, x);
				qy_trafo[i] = transformy((double)b.height,(double)b.y,-15, 45, y);
				x3D[i] = transform3D(view_ds, view_d0, z, qx_trafo[i]); 
				y3D[i] = transform3D(view_ds, view_d0, z, qy_trafo[i]);
				
				
				//change color according to causality
				g.setColor(customrainbow.rainbow((double)i/steps));

			
				//draw point
//				g.drawLine((int) x3D[i] ,(int) y3D[i],  x3D[i+1] ,(int) y3D[i+1]);
				g.fillRect((int) x3D[i] ,(int) y3D[i], 1, 1);

				

			}	    	
	 	}
	 
}
		
 
 
 
 
 
 
 
 
	
	
