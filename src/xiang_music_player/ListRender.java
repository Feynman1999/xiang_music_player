package xiang_music_player;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


class MusicListRender extends JButton implements ListCellRenderer<Object> 
{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public MusicListRender() 
		{
            this.setOpaque(false);
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            
            this.setFont(new Font("幼圆", Font.PLAIN, 20));
            //this.setBorderPainted(false);
            Color background = Color.WHITE;
            Color foreground = Color.BLACK;
            if (isSelected) {
                this.setFont(new Font("幼圆", Font.BOLD, 20));
            }
            //System.out.println(Play_Frame.Playingmusic_index);
            if(index==Main_Frame.Playingmusic_index)//正在播放的歌曲颜色变红
            {
            	foreground=Color.RED;
            }
            setBackground(background);
            setForeground(foreground);
            return this;
    }
}


class LyricsListRender extends JButton implements ListCellRenderer<Object> 
{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		public LyricsListRender() 
		{
            this.setOpaque(false);
        }
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            
            this.setFont(new Font("方正舒体", Font.PLAIN, 25));
            this.setBorderPainted(false);
            Color background = Color.WHITE;
            Color foreground = Color.BLACK;

            //System.out.println(Play_Frame.Playingmusic_index);
            if(Main_Frame.Playinglyrics_index>=0 && (Main_Frame.Playinglyrics_index-index)%Main_Frame.pagenum==0)
            {
            	foreground=Color.RED;
            }
            setBackground(background);
            setForeground(foreground);
            return this;
        }
}