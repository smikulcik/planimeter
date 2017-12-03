/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package planimeter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 *
 * @author simon.mikulcik
 */
public class PlanimeterGUI implements ActionListener, MouseListener, MouseMotionListener, KeyListener {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PlanimeterGUI p = new PlanimeterGUI();
        p.go();
    }
    JFrame frame;
    JLabel label;
    JButton button;
    paint p;
    
    Polygon polygon = new Polygon();
    PointD CM = new PointD();
    
    int buttonPressed=0;
    int selectT = -1;
    
    int mousePrevX = -1;
    int mousePrevY = -1;

    public void go() {

        frame = new JFrame("Planimeter");
        label = new JLabel("");
        button = new JButton("New Shape");
        button.addActionListener(this);

        p = new paint();
        p.addKeyListener(this);
        p.addMouseMotionListener(this);
        p.addMouseListener(this);
        //button.addKeyListener(this);


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().add(BorderLayout.NORTH, label);
        frame.getContentPane().add (BorderLayout.SOUTH, button);
        frame.getContentPane().add(BorderLayout.CENTER, p);
        frame.setSize(600, 600);
        frame.setVisible(true);
        label.setText("Draw an Irregular, Closed Shape");
        while (true) {
            frame.repaint();
            Wait(10);
        }
    }

    private void Wait(int ms) {
        try {
            Thread.sleep(ms);
            //JXInputManager.updateFeatures();
        } catch (Exception ex) {
        }
    }


    @Override
    public void actionPerformed(ActionEvent event) {
        polygon = new Polygon();
        frame.repaint();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        //label.setText("you pressed something");
        //if(event.getKeyCode() == KeyEvent.VK_DOWN)label.setText("you pressed down");
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void keyTyped(KeyEvent event) {
        //label.setText("you pressed something");
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        //vertsX.add(e.getX());
        //vertsY.add(e.getY());
        if(buttonPressed == MouseEvent.BUTTON1){
            polygon.addPoint(e.getX(), e.getY());
        }if(buttonPressed == MouseEvent.BUTTON3){
            if(selectT>=0){
                polygon.xpoints[selectT] += e.getX()-mousePrevX;
                polygon.ypoints[selectT] += e.getY()-mousePrevY;
                mousePrevX=e.getX();
                mousePrevY=e.getY();
            }
        }
        
        if(polygon.npoints>=3){
            PlanimeterAnalysis pa = Planimeter.calculateAreaTrap(polygon);
            double area = (int)(100*pa.area)/100.;
            double perimeter = (int)(100*pa.perimeter)/100.;
            double ratio = (int)(100*(pa.area/(pa.perimeter/2d)))/100.;
            CM = pa.center_of_mass;
            label.setText(
                buttonPressed + "Area = " + area +
                " Center of Mass = (" +
                (int)(pa.center_of_mass.getX())/100. + ","+
                (int)(pa.center_of_mass.getY())/100.+")" +
                " Perimeter = " + perimeter + 
                " Ratio = " + ratio +
                " PI = " + area/ratio);
        }
        frame.repaint();
    }
    
    double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        frame.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        buttonPressed=e.getButton();
        if(buttonPressed == MouseEvent.BUTTON3){
            mousePrevX = e.getX();
            mousePrevY = e.getY();
            if(polygon.npoints >0){
                int minT = 0;
                double minD = distance(e.getX(),e.getY(),polygon.xpoints[0],polygon.ypoints[0]);
                double dist;
                for(int t=0; t<polygon.npoints; t++){
                    dist = distance(e.getX(),e.getY(),polygon.xpoints[t],polygon.ypoints[t]); 
                    if(dist < minD){minT=t; minD = dist;}
                }
                selectT = minT;
                System.out.println("Selected t=" +selectT);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    class paint extends JPanel {

        Graphics2D graph;

        @Override
        public void paintComponent(Graphics g) {
            paintComponent2D((Graphics2D) g);
        }
        
        public void paintComponent2D(Graphics2D g){
            int clientW=(int) g.getClipBounds().getWidth();
            int clientH=(int) g.getClipBounds().getHeight();
            
            g.setColor(Color.white);
            g.fillRect(0, 0, clientW, clientH);
            
            g.setColor(Color.getHSBColor(0, 0, 0.9f));
            for(int i = 0; i<clientW ; i+=25)g.draw(new Line2D.Double(i, 0, i, clientH));
            for(int i = 0; i<clientH ; i+=25)g.draw(new Line2D.Double(0, i, clientW, i));
            
            g.setColor(Color.getHSBColor(.55f, .75f, .95f));
            g.fillPolygon(polygon);
            
            g.setColor(Color.getHSBColor(0, 0, 0));
            for(int i = 0; i<clientW ; i+=100)g.draw(new Line2D.Double(i, 0, i, clientH));
            for(int i = 0; i<clientH ; i+=100)g.draw(new Line2D.Double(0, i, clientW, i));
            
            
            g.setColor(Color.blue);
            for(int i = 0; i<polygon.npoints; i++)(new PointD(polygon.xpoints[i], polygon.ypoints[i])).draw(g);
            
            g.setColor(Color.black);
            CM.draw(g);
        }
        
    }
}
