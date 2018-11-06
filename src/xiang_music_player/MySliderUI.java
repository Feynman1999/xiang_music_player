package xiang_music_player;

import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JSlider;
import javax.swing.plaf.basic.BasicSliderUI;

public class MySliderUI extends BasicSliderUI
{
	public MySliderUI(JSlider b) {
		super(b);
	}
	private static final Color BACKGROUND01=new Color(16,16,16);
	private static final Color BACKGROUND02=new Color(240,240,240);
	@Override
	public void paintThumb(Graphics g)
	{
		Graphics2D g2d=(Graphics2D) g;
		BasicStroke stroke=new BasicStroke(1,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
		g2d.setStroke(stroke);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.5f));
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		GradientPaint gp=new GradientPaint(0,0,BACKGROUND02,0,thumbRect.height,BACKGROUND01);
		g2d.setPaint(gp);
		g2d.fillRoundRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height, 10, 10);
		BasicStroke stroke1=new BasicStroke(3,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
		g2d.setStroke(stroke1);
		g2d.drawLine(8, thumbRect.height,thumbRect.x+3 , thumbRect.height);
	}
	public void paintTrack(Graphics g) {
		Graphics2D g2d=(Graphics2D) g;
		// …Ë∂®Ω•±‰
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_OFF);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.3f));
		g2d.setPaint(new GradientPaint(0, 0,BACKGROUND02 , 0,trackRect.height, BACKGROUND01, true));
		g2d.setStroke(new BasicStroke(4,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
		g2d.drawLine(8, trackRect.height, trackRect.width+8, trackRect.height);
	}
}