/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planimeter;

import java.awt.Polygon;

/**
 *
 * @author Simon
 */
public class Planimeter {
    
    public static double calculateAreaSimp(Polygon p){
        double answer = f(0, p);
        if(p.npoints>3 && (p.npoints-3)%2==0){
            for(int t = 1; t<p.npoints-2; t+=2){
                answer+=4*f(t,p);
                answer+=2*f(t,p);
            }
            answer+=4*f(p.npoints-2,p) + f(p.npoints-1,p);
            answer/=6;
        }
        return 0;
    }
    public static PlanimeterAnalysis calculateAreaTrap(Polygon p){
        PlanimeterAnalysis analysis = new PlanimeterAnalysis();
        PointD CM = new PointD();
        double area = f(0,p);
        if(p.npoints > 2){
            for(int t=1; t<p.npoints-1; t++){
                area+=1*f(t,p);
            }
            area+=f(p.npoints-1, p);
            area/=2;
            
            double mx=0, my=0, x1, y1, x2, y2;
            
            
            //center of mass
            for(int t=0; t<p.npoints; t++){
                if(t==0){
                    x1 = p.xpoints[p.npoints-1];
                    x2 = p.xpoints[0];
                    y1 = p.ypoints[p.npoints-1];
                    y2 = p.ypoints[0];
                }else{
                    x1 = p.xpoints[t-1];
                    x2 = p.xpoints[t];
                    y1 = p.ypoints[t-1];
                    y2 = p.ypoints[t];
                }
                mx+= -1*(1./6.)*(x2-x1)*(y1*y1+ y1*y2 + y2*y2);
                my+=    (1./6.)*(y2-y1)*(x1*x1+ x1*x2 + x2*x2);
                //System.out.println(t+1 + "/"+p.npoints  + ": "+ -1*(1./6.)*(x2-x1)*(y1*y1+ y1*y2 + y2*y2));
            }
            analysis.center_of_mass.setX(my/area);
            analysis.center_of_mass.setY(mx/area);
            //System.out.println("cm+" + CM+" mx=" + mx+" my=" + my);
            
            area/=10000;
            area = -area;
            analysis.area = area;
        }
        return analysis;
    }
    
    private static double f(int t, Polygon p){
        double x,y,dx=0,dy=0;
        if(t<0 || t>p.npoints-1)throw new IndexOutOfBoundsException("t is not within [" + 0 + ", "+ p.npoints + "]");
        if(t>0)dx=p.xpoints[t]-p.xpoints[t-1];
        if(t==0)dx=p.xpoints[0]-p.xpoints[p.npoints-1];//take last point to complete curve
        x=p.xpoints[t];

        if(t>0)dy=p.ypoints[t]-p.ypoints[t-1];
        if(t==0)dy=p.ypoints[0]-p.ypoints[p.npoints-1];
        y=p.ypoints[t];
        //System.out.println(dy*x-dx*y + " = <"+dx+","+dy+"> * <"+ -1*p.ypoints[t]+","+p.xpoints[t]+"> : npoints="+p.npoints);
        
        return dy*x - dx*y;
    }
    
}
