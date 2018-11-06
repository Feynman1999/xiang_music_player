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


class audioplay{//播放音乐类
	   AudioClip adc;// 声音音频剪辑对象
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
	   void play(){     //开始播放
		   System.out.println("Playing...");
	       adc.play();
	       playFlag=true;
	   }   
	   void stop(){     //停止播放
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
        map_singer.put("烟花易冷","周杰伦");
        map_singer.put("离人愁", "李袁杰");
        map_singer.put("三生三世", "苏诗丁&杨宗纬");
        map_singer.put("山外小楼夜听雨", "XUN");
        map_singer.put("漂洋过海", "梁静茹");
	    Map<String,String> map_album = new HashMap<String, String>(); 
        map_album.put("烟花易冷","跨时代");
        map_album.put("离人愁", "离人愁");
        map_album.put("三生三世", "三生三世十里桃花 电视剧原声带");
        map_album.put("山外小楼夜听雨", "山外小楼夜听雨");
        map_album.put("漂洋过海", "理性与感性 作品音乐会");
		@SuppressWarnings("unused")
		Main_Frame J1=new Main_Frame(map_singer,map_album);
	}
}
