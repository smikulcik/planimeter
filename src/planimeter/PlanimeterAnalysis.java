/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planimeter;

/**
 *
 * @author Simon
 */
public class PlanimeterAnalysis {
    public double area;
    public PointD center_of_mass;
    public double perimeter;
    
    public PlanimeterAnalysis(){
        area = 0;
        center_of_mass = new PointD(0,0);
        perimeter = 0;
    }
}
