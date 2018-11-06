package xiang_music_player;

import java.applet.Applet;  
import java.applet.AudioClip;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;  
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Painter;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;  



class song
{
	String name;
	String singer;
	String album;
	String Directory;
	int time;
	song(String name,String singer,String album,String Directory,int time)
	{
		this.name=name;
		this.singer=singer;
		this.album=album;
		this.Directory=Directory;
		this.time=time;
	}
//	@Override
	public String toString()
	{
		return name+"   "+fun(time);
	}
	
	String fun(int time) {
		 int totalSeconds = (int) (time / 1000);
		    int seconds = totalSeconds % 60;
		    int minutes = (totalSeconds / 60) % 60;

		    return  String.format("%02d : %02d ", minutes, seconds);
	}
}


class Lyrics
{
	String text;
	int time;
	Lyrics(String text,int time)
	{
		this.text=text;
		this.time=time;
	}
	@Override
	public String toString()
	{
		return text;
	}
}


class audioplay{//����������
	   AudioClip adc;// ������Ƶ��������
	   URL url;
	   boolean adcFlag=false;
	   boolean playFlag=false;
	   void SetPlayAudioPath(String path){
		   path="file:"+path;
	      try{  
	           url = new URL(path);  
	          // System.out.println(adc.toString());
	           if(adcFlag==true){adc.stop();playFlag=false;}
	           adc = Applet.newAudioClip(url);
	           adcFlag=true;
	         }
	      catch (MalformedURLException e1) {
	              e1.printStackTrace();  
	         }  
	   }
	   void play(){     //��ʼ����
		   System.out.println("Playing...");
	       adc.play();
	       playFlag=true;
	   }   
	   void stop(){     //ֹͣ����
		   System.out.println("Stoping...");
	       adc.stop();  
	       playFlag=false;
	   }
}


public class Player {
	@SuppressWarnings("unchecked")
	public static void main(String[] args)throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, FileNotFoundException, IOException {
		//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    Map<String,String> map_singer = new HashMap<String, String>(); 
        map_singer.put("�̻�����","�ܽ���");
        map_singer.put("���˳�", "��Ԭ��");
        map_singer.put("��������", "��ʫ��&����γ");
        map_singer.put("ɽ��С¥ҹ����", "XUN");
        map_singer.put("Ư�����", "������");
	    Map<String,String> map_album = new HashMap<String, String>(); 
        map_album.put("�̻�����","��ʱ��");
        map_album.put("���˳�", "���˳�");
        map_album.put("��������", "��������ʮ���һ� ���Ӿ�ԭ����");
        map_album.put("ɽ��С¥ҹ����", "ɽ��С¥ҹ����");
        map_album.put("Ư�����", "��������� ��Ʒ���ֻ�");
		@SuppressWarnings("unused")
		Main_Frame J1=new Main_Frame(map_singer,map_album);
	}
}
