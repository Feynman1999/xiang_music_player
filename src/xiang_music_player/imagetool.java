package xiang_music_player;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class imagetool {
	public static BufferedImage img_alpha(BufferedImage imgsrc,int alpha) {
	    try {
	        //创建一个包含透明度的图片,半透明效果必须要存储为png合适才行，存储为jpg，底色为黑色
	        BufferedImage back=new BufferedImage(imgsrc.getWidth(), imgsrc.getHeight(), BufferedImage.TYPE_INT_ARGB);
	        int width = imgsrc.getWidth();  
	        int height = imgsrc.getHeight();  
	        for (int j = 0; j < height; j++) { 
	            for (int i = 0; i < width; i++) { 
	                int rgb = imgsrc.getRGB(i, j);
	                Color color = new Color(rgb);
	                Color newcolor = new Color(color.getRed(), color.getGreen(),color.getBlue(), alpha);
	                back.setRGB(i,j,newcolor.getRGB());
	            }
	        }
	        return back;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	/**
     * 图片设置圆角
     * @param srcImage
     * @param radius
     * @param border
     * @param padding
     * @return
     * @throws IOException
     */
    public static BufferedImage setRadius(BufferedImage srcImage, int radius, int border, int padding) throws IOException{
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        int canvasWidth = width + padding * 2;
        int canvasHeight = height + padding * 2;
        
        BufferedImage image = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();
        gs.setComposite(AlphaComposite.Src);
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setColor(Color.WHITE);
        gs.fill(new RoundRectangle2D.Float(0, 0, canvasWidth, canvasHeight, radius, radius));
        gs.setComposite(AlphaComposite.SrcAtop);
        gs.drawImage(setClip(srcImage, radius), padding, padding, null);
        if(border !=0){
            gs.setColor(Color.GRAY);
            gs.setStroke(new BasicStroke(border));
            gs.drawRoundRect(padding, padding, canvasWidth - 2 * padding, canvasHeight - 2 * padding, radius, radius);    
        }
        gs.dispose();
        return image;
    }
    
    /**
     * 图片设置圆角
     * @param srcImage
     * @return
     * @throws IOException
     */
    public static BufferedImage setRadius(BufferedImage srcImage) throws IOException{
        int radius = (srcImage.getWidth() + srcImage.getHeight()) / 8;
        return setRadius(srcImage, radius, 2, 5);
    }
    
    
    /**
     * 图片切圆角
     * @param srcImage
     * @param radius
     * @return
     */
    public static BufferedImage setClip(BufferedImage srcImage, int radius){
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gs = image.createGraphics();
        gs.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gs.setClip(new RoundRectangle2D.Double(0, 0, width, height, radius, radius));
        gs.drawImage(srcImage, 0, 0, null);
        gs.dispose();
        return image;
    }
    
    
    public static BufferedImage Rotate(BufferedImage image, double angle) //创建旋转图片
    {
	    	GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	        GraphicsDevice gd = ge.getDefaultScreenDevice();
	        GraphicsConfiguration gc = gd.getDefaultConfiguration();
	        
    	    double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
    	    int w = image.getWidth(), h = image.getHeight();
    	    int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h
    	        * cos + w * sin);
    	    int transparency = image.getColorModel().getTransparency();
    	    BufferedImage result = gc.createCompatibleImage(neww, newh, transparency);
    	    Graphics2D g = result.createGraphics();
    	    g.translate((neww - w) / 2, (newh - h) / 2);
    	    g.rotate(angle, w / 2, h / 2);
    	    g.drawRenderedImage(image, null);
    	    return result;
    }
    
    /**  
     * 把多张jpg图片合成一张  
     * @param pic String[] 多个jpg文件名 包含路径  
     * @param newPic String 生成的gif文件名 包含路径  
     */  
    public synchronized void jpgToGif(BufferedImage src[] , String newPic) {  
        try {  
            AnimatedGifEncoder e = new AnimatedGifEncoder();          
            e.setRepeat(0);  
            e.start(newPic);   
            for (int i = 0; i < src.length; i++) {  
                e.setDelay(200); //设置播放的延迟时间  
                e.addFrame(src[i]);  //添加到帧中  
            }  
            e.finish();  
        } catch (Exception e) {  
            System.out.println( "jpgToGif Failed:");  
            e.printStackTrace();  
        }  
    }
}
