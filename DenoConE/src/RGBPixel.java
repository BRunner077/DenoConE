/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.eclipse.swt.graphics.ImageData;

/**
 *
 * @author vp
 */
public class RGBPixel {

    public int r;
    public int g;
    public int b;
    
    public RGBPixel(int r, int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public RGBPixel(int x, int y, ImageData imageData) {
        if(imageData != null) {
        int redShift = imageData.palette.redMask;
        int blueShift = imageData.palette.blueMask;
        int greenShift = imageData.palette.greenMask;

        // Extract the red, green and blue component
        int pixelValue = imageData.getPixel(x, y);
        
        r = pixelValue & redShift;
        g = (pixelValue & greenShift) >> 8;
        b = (pixelValue & blueShift) >> 16;
        }
    }
    
    public void convertToGrey(){
        r = g = b = (int) (r * 0.299 + g * 0.587 + b * 0.114);
    }
    
    public void writeToImageData(int x, int y, ImageData imageData) {
        if(imageData != null) {
            imageData.setPixel(x, y,(r + (g << 8) + (b << 16)));
        }
    }
    
    @Override
    public RGBPixel clone() {
        return new RGBPixel(r,g,b);
    }
}
