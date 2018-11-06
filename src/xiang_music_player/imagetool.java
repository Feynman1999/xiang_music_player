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
	        //����һ������͸���ȵ�ͼƬ,��͸��Ч������Ҫ�洢Ϊpng���ʲ��У��洢Ϊjpg����ɫΪ��ɫ
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
     * ͼƬ����Բ��
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
     * ͼƬ����Բ��
     * @param srcImage
     * @return
     * @throws IOException
     */
    public static BufferedImage setRadius(BufferedImage srcImage) throws IOException{
        int radius = (srcImage.getWidth() + srcImage.getHeight()) / 8;
        return setRadius(srcImage, radius, 2, 5);
    }
    
    
    /**
     * ͼƬ��Բ��
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
    
    
    public static BufferedImage Rotate(BufferedImage image, double angle) //������תͼƬ
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
     * �Ѷ���jpgͼƬ�ϳ�һ��  
     * @param pic String[] ���jpg�ļ��� ����·��  
     * @param newPic String ���ɵ�gif�ļ��� ����·��  
     */  
    public synchronized void jpgToGif(BufferedImage src[] , String newPic) {  
        try {  
            AnimatedGifEncoder e = new AnimatedGifEncoder();          
            e.setRepeat(0);  
            e.start(newPic);   
            for (int i = 0; i < src.length; i++) {  
                e.setDelay(200); //���ò��ŵ��ӳ�ʱ��  
                e.addFrame(src[i]);  //��ӵ�֡��  
            }  
            e.finish();  
        } catch (Exception e) {  
            System.out.println( "jpgToGif Failed:");  
            e.printStackTrace();  
        }  
    }
}
