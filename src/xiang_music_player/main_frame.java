package xiang_music_player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Transparency;
//import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


class Main_Frame extends JFrame implements ActionListener,MouseListener,MouseMotionListener,ChangeListener{
	
    static Point compCoords;
	int FrameWidth=1200;//暂时不考虑用户屏幕大小 写死了
	int FrameHeight=750;
	//	Toolkit kit = Toolkit.getDefaultToolkit();
	//	Dimension screenSize = kit.getScreenSize();//获得用户当前的屏幕信息
	JLabel background;//背景控件
	JLabel title;//title
	JLabel album;//album
	JLabel volume;//volume
	JLabel love;//love
	JLabel listbackground;//list 背景
	JButton button_singer;
	JButton button_album;
	JButton button_song;
	JButton buttonPlay;//播放按钮
	JButton buttonNext;//下一首
	JButton buttonLast;//上一首
	JButton buttonlist;//列表
	JButton buttonadd;
	JButton minimize;//最小化
	JButton exit;//退出程序
	JList listPlayFile;//播放列表控件
	JList textLyrics;//歌词控件
	JSlider play_slider;
	JSlider loud_slider;
	final JPanel topPanel;
	final JPanel bottomPanel;
	Container contentPane = getContentPane();
	Timer PlayTimer;//进度条定时器对象
	Timer LyricsTimer;//歌词定时器对象
	int MusicTime;//记录当前播放的歌曲的时间（单位：毫秒）
	ArrayList<song> songlist= new ArrayList<song>();
	ArrayList<Lyrics> lyricslist=new ArrayList<Lyrics>();
	audioplay audioPlay=new audioplay();
	Icon playicon=new ImageIcon(".//static//play.png");//创建播放图标对象
	Icon pauseicon=new ImageIcon(".//static//pause.png");
	public static int Playingmusic_index=-1;		//现在正在播放的音乐的编号
	public static int Playinglyrics_index=-1;		//现在正在播放的歌词编号
	public static int pagenum = 10;                  //每页显示歌词数目
	Map<String,String> map_singer;
	Map<String,String> map_album;
	
	public Main_Frame(Map m1,Map m2) throws FileNotFoundException, IOException{
		map_singer = m1;
		map_album = m2;
		setSize(new Dimension(FrameWidth,FrameHeight));
		setUndecorated(true); 
		setLocationRelativeTo(null);   
		setLayout(null);//空布局			
		topPanel = new JPanel();
		topPanel.setLayout(null);
		topPanel.setBounds(0, 0, FrameWidth, 48);
		topPanel.setOpaque(false);
		bottomPanel = new JPanel();
		bottomPanel.setLayout(null);
		bottomPanel.setBounds(0, FrameHeight-63 , FrameWidth, 63);
		bottomPanel.setOpaque(false);
		init();   //添加控件的操作封装成一个函数      
		topPanel.addMouseListener(this);//响应用户的拖拽
		topPanel.addMouseMotionListener(this);
		add(topPanel,BorderLayout.NORTH);
		add(bottomPanel,BorderLayout.SOUTH);
		setResizable(false);
		setVisible(true);//放在添加组件后面执行
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	 }

	 void Slide()
	 {
			play_slider=new JSlider(0,100,0);
			play_slider.setUI(new MySliderUI(play_slider));
			play_slider.setBounds(0,FrameHeight-63-36,1200,42);
			play_slider.setPaintTicks(false);//不显示标尺
			play_slider.setBackground(Color.BLACK);
			play_slider.setOpaque(false);//设透明
			play_slider.addChangeListener(this);
			add(play_slider);
			loud_slider=new JSlider(0,20,12);
			loud_slider.setUI(new MySliderUI(loud_slider));
			loud_slider.setBounds(800,9,100,42);
			loud_slider.setPaintTicks(false);//不显示标尺
			loud_slider.setBackground(Color.BLACK);
			loud_slider.setOpaque(false);//设透明
			loud_slider.addChangeListener(this);
			bottomPanel.add(loud_slider);
	 }
	 
	 void init() throws FileNotFoundException, IOException{//添加的控件
		 // album -------------------------------------------------- 
		 BufferedImage image1 = ImageIO.read(new FileInputStream(".//static//default.png"));
		 
		 //imagetool.Rotate(image1,math.PI)
		 Image image2 = imagetool.setClip(image1, 300);
		 image2 = image2.getScaledInstance(300,300,Image.SCALE_SMOOTH);//图片缩放 默认专辑图片是方形的
		 ImageIcon albumicon = new ImageIcon(image2);
		 album= new JLabel(albumicon);
		 album.setBounds(160,105,300,300);//后面纵向需要截取188个像素 然后放大作为背景
		 add(album);
		 		 
		 // background  -------------------------------------------------- 
		 image1 = imagetool.img_alpha(image1, 39);//39是透明度
		 image2 = image1.getSubimage(0,0,image1.getWidth(),image1.getHeight()*750/1200);
		 image2 = image2.getScaledInstance(1200,750,Image.SCALE_SMOOTH);
		 Icon bg_icon=new ImageIcon(image2);//创建图标对象
		 background = new JLabel(bg_icon);//设置背景图片
		 background.setBounds(0,0,FrameWidth,FrameHeight);//设置背景控件大小
		 getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));//背景图片控件置于最底层
		 
		 // list background -------------------------------------------------- 
		 image1 = ImageIO.read(new FileInputStream(".//static//listbackground.png"));
		 image1 = imagetool.img_alpha(image1, 70);//70是透明度
		 listbackground=new JLabel(new ImageIcon(image1));
		 listbackground.setBounds(850,160,350,470);
		 listbackground.setVisible(false);
		 getLayeredPane().add(listbackground, new Integer(Integer.MIN_VALUE+8));//背景图片控件置于最底层

		 ((JPanel)contentPane).setOpaque(false); //控件透明  相当于一层“玻璃” 可以透过它看到最底层的背景图片 
		 
		 
		 // title ---------------------------------------------------
		 Icon titleicon=new ImageIcon(".//static//title.png");//set title
		 title = new JLabel(titleicon);
		 title.setBounds(8,0,398,43);
		 topPanel.add(title);
		 
		 // buttons -------------------------------------------------- 
		 button_song = new JButton("Welcome!");
		 button_song.setBackground(Color.red);//由于后面要设置透明，这里只起占位作用 
		 button_song.setBorderPainted(false);//去掉边框
		 button_song.setMargin(new Insets(0,0,0,0));//随图案变化
		 button_song.setOpaque(false);// 设置透明
		 button_song.setBounds(110,450,400,40);
		 button_song.setFocusPainted(false);
		 button_song.setFont(new Font("楷体", Font.BOLD, 34));
		 add(button_song);
		 
		 button_singer = new JButton("歌手："+"ahu");
		 button_singer.setBackground(Color.red);//由于后面要设置透明，这里只起占位作用 
		 button_singer.setBorderPainted(false);//去掉边框
		 button_singer.setMargin(new Insets(0,0,0,0));//随图案变化
		 button_singer.setOpaque(false);// 设置透明
		 button_singer.setBounds(90,510,400,30);
		 button_singer.setFocusPainted(false);
		 button_singer.setFont(new Font("楷体", Font.BOLD, 22));
		 add(button_singer);
		 
		 button_album = new JButton("专辑："+"ahu");
		 button_album.setBackground(Color.red);//由于后面要设置透明，这里只起占位作用 
		 button_album.setBorderPainted(false);//去掉边框
		 button_album.setMargin(new Insets(0,0,0,0));//随图案变化
		 button_album.setOpaque(false);// 设置透明
		 button_album.setBounds(90,550,400,30);
		 button_album.setFocusPainted(false);
		 button_album.setFont(new Font("楷体", Font.BOLD, 22));
		 add(button_album);
				 
		 Icon minimizeicon = new ImageIcon(".//static//mini.png");
		 minimize = new JButton(minimizeicon);
		 minimize.setBackground(Color.red);//由于后面要设置透明，这里只起占位作用 
		 minimize.setBorderPainted(false);//去掉边框
		 minimize.setMargin(new Insets(0,0,0,0));//随图案变化
		 minimize.setOpaque(false);// 设置透明
		 minimize.setBounds(FrameWidth-105,0,48,48);
		 minimize.setFocusPainted(false);
		 minimize.addActionListener(this);
		 topPanel.add(minimize);
		 
		 Icon exiticon=new ImageIcon(".//static//exit.png");
		 exit = new JButton(exiticon);
		 exit.setBackground(Color.red);//由于后面要设置透明，这里只起占位作用 
		 exit.setBorderPainted(false);//去掉边框
		 exit.setMargin(new Insets(0,0,0,0));//随图案变化
		 exit.setOpaque(false);// 设置透明
		 exit.setBounds(FrameWidth-60,0,48,48);
		 exit.addActionListener(this);
		 topPanel.add(exit);
		 
		 Icon lasticon=new ImageIcon(".//static//last2.png");//创建播放图标对象
		 buttonLast=new JButton(lasticon);//添加播放按钮
		 buttonLast.setBackground(Color.red);//由于后面要设置透明，这里只起占位作用 
		 buttonLast.setBorderPainted(false);//去掉边框
	     buttonLast.setMargin(new Insets(0,0,0,0));//随图案变化
	     buttonLast.setOpaque(false);// 设置透明
	     buttonLast.setBounds(50,12,32,32); //设置播放按钮大小
	     buttonLast.addActionListener(this);
		 bottomPanel.add(buttonLast);//添加播放按钮至窗口中
		 
		 buttonPlay=new JButton(playicon);//添加播放按钮
		 buttonPlay.setBackground(Color.red);//由于后面要设置透明，这里只起占位作用 
		 buttonPlay.setBorderPainted(false);//去掉边框
	     buttonPlay.setMargin(new Insets(0,0,0,0));//随图案变化
	     buttonPlay.setOpaque(false);// 设置透明
	     buttonPlay.setBounds(110,4,48,48); //设置播放按钮大小
	     buttonPlay.addActionListener(this);
		 bottomPanel.add(buttonPlay);//添加播放按钮至窗口中
		  
		 Icon nexticon=new ImageIcon(".//static//next2.png");//创建播放图标对象
		 buttonNext=new JButton(nexticon);//添加播放按钮
		 buttonNext.setBackground(Color.red);//由于后面要设置透明，这里只起占位作用 
		 buttonNext.setBorderPainted(false);//去掉边框
	     buttonNext.setMargin(new Insets(0,0,0,0));//随图案变化
	     buttonNext.setOpaque(false);// 设置透明
	     buttonNext.setBounds(186,12,32,32); //设置播放按钮大小
	     buttonNext.addActionListener(this);
		 bottomPanel.add(buttonNext);//添加播放按钮至窗口中
		 
		 Icon listicon=new ImageIcon(".//static//list.png");//创建播放图标对象
		 buttonlist=new JButton(listicon);//添加播放按钮
		 buttonlist.setBackground(Color.red);//由于后面要设置透明，这里只起占位作用 
		 buttonlist.setBorderPainted(false);//去掉边框
	     buttonlist.setMargin(new Insets(0,0,0,0));//随图案变化
	     buttonlist.setOpaque(false);// 设置透明
	     buttonlist.setBounds(FrameWidth-78,12,32,32); //设置播放按钮大小
	     buttonlist.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	     buttonlist.addActionListener(this);
	     bottomPanel.add(buttonlist);//添加播放按钮至窗口中
		 
	     Icon buttonaddicon = new ImageIcon(".//static//add.png");
	     buttonadd = new JButton(buttonaddicon);
	     buttonadd.setBackground(Color.RED);
	     buttonadd.setBounds(1150,570,48,48);
	     buttonadd.setBorderPainted(false);//去掉边框
	     buttonadd.setOpaque(false);
	     buttonadd.setVisible(false);
	     buttonadd.addActionListener(this);
	     add(buttonadd);	
	     
		 // volume ---------------------------------------------------
		 Icon volumeicon=new ImageIcon(".//static//volume.png");
		 volume = new JLabel(volumeicon);
		 volume.setBounds(760,12,32,32);
		 bottomPanel.add(volume);
		 
		 // love
		 Icon loveicon=new ImageIcon(".//static//love.png");
		 love= new JLabel(loveicon);
		 love.setBounds(1050,12,32,32);
		 bottomPanel.add(love);
		 
		 // slide -------------------------------------------------- 
		 Slide();//创建进度条
		 		 
		 listPlayFile=new JList();	  //创建播放列表 
		 listPlayFile.setBounds(850,160,350,500); //设置播放列表大小 
		 listPlayFile.setOpaque(false);//设置播放列表透明
         listPlayFile.setVisible(false);
	     listPlayFile.setCellRenderer(new MusicListRender());
	     listPlayFile.addMouseListener(this);
		 add(listPlayFile);       //添加播放列表至窗口中
		  
		 textLyrics=new JList();   //创建歌词控件    
		 textLyrics.setBounds(650,100,400,600);//设置歌词控件大小
		 textLyrics.setOpaque(false);//歌词控件透明
		 textLyrics.setCellRenderer(new LyricsListRender());
		 add(textLyrics);    //添加歌词控件至窗口中
		 
		 
		 lyricslist.add(new Lyrics("欢迎来到xiang music player！",1));
		 lyricslist.add(new Lyrics("         ",2));
		 lyricslist.add(new Lyrics("右下角的乐单添加歌曲(#^.^#)",3));
		 lyricslist.add(new Lyrics("         ",4));
		 lyricslist.add(new Lyrics("当你切换歌曲时，背景会变动哦",5));

		 textLyrics.setListData((lyricslist.subList(0,Math.min(pagenum,lyricslist.size()))).toArray());
	 }
	 
		
	 @Override
	 public void actionPerformed(ActionEvent e) {
		if(e.getSource()==buttonPlay)
		{
			if(buttonPlay.getIcon()==playicon)
			{
				if(songlist.isEmpty()==false)
				{
					int pos=listPlayFile.getSelectedIndex();
					System.out.println(pos);
					if(pos==-1) pos=0;	//如果没有选中，默认播放第一首
					else ;
					try {
						playmusic(pos);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					buttonPlay.setIcon(pauseicon);
				}
				else {
					System.out.println("请先添加歌曲到播放列表!");
				}
			}
			else
			{
				audioPlay.stop();
				buttonPlay.setIcon(playicon);
			}
		}
		else if(e.getSource()==buttonNext)
		{
			if(Playingmusic_index!=-1)
			{
				try {
					playmusic((Playingmusic_index+1)%songlist.size());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else {
				System.out.println("请先添加歌曲到播放列表!");
			}
		}
		else if(e.getSource()==buttonLast)
		{
			if(Playingmusic_index!=-1)
			{
				try {
					playmusic((Playingmusic_index-1+songlist.size())%songlist.size());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else {
				System.out.println("请先添加歌曲到播放列表!");
			}
		}
		else if(e.getSource()==buttonlist) {
			if(listPlayFile.isVisible()==true)
			{
				System.out.println("列表给我回去！！");
				listbackground.setVisible(false);
				buttonadd.setVisible(false);
				buttonadd.setEnabled(true);
				listPlayFile.setVisible(false);
				listPlayFile.setEnabled(true);
			}
			else {
				System.out.println("列表给我出来！！");
				listbackground.setVisible(true);
				buttonadd.setVisible(true);
				buttonadd.setEnabled(true);
				listPlayFile.setVisible(true);
				listPlayFile.setEnabled(true);
			}
		}
		else if(e.getSource()==buttonadd) {
		   try{
				 FileDialog openFile=new FileDialog(this,"打开文件");//创建打开文件对话框	
				 openFile.setVisible(true);//对话框可见
				 String playFileName=openFile.getFile();//获取打开的文件名
				 if(playFileName.endsWith(".wav")==true)
				 {
					 String playFileDirectory=openFile.getDirectory();//获取打开的文件路径
					 String playFilepath=playFileDirectory+playFileName;//获得完整的路径
					 System.out.println(playFilepath);
					 playFileName = playFileName.substring(0,playFileName.length()-4);
					 song tmp=new song(playFileName,map_singer.get(playFileName),map_album.get(playFileName),playFilepath,getMusicTime(playFilepath));
					 songlist.add(tmp);
					 listPlayFile.setListData(songlist.toArray());
					 System.out.println("添加歌曲： ok!");
				 }
				 else {
					 System.out.println("请选择wav格式的音乐文件！");
				 }
			}
			catch(Exception e1)
			{
				System.out.println("选择文件时发生错误");
			}
		}
		else if(e.getSource()==minimize) {
			setState(JFrame.ICONIFIED);
		}
		else if(e.getSource()==exit) {
			System.exit(0);
		}
	 }
	 
	 @Override
	 public void mouseReleased(MouseEvent e) {
         if(e.getSource()==topPanel) {
        	 compCoords = null;
         }
 		if(e.getSource()==listPlayFile){
 			if(e.getClickCount()==2)
 			{
 				System.out.println("double click");
 				int pos=listPlayFile.getSelectedIndex();
 				//System.out.println(pos);
 				//System.out.println(songlist.get(pos).name);
 				try {
					playmusic(pos);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
 			}
 		}
     }
	 @Override
     public void mousePressed(MouseEvent e) {//得到鼠标坐标信息
		 if(e.getSource()==topPanel) {
			 compCoords = e.getPoint();
		 }
	 }
	 @Override
     public void mouseExited(MouseEvent e) {
		 
     }
	 @Override
     public void mouseEntered(MouseEvent e) {
		 
     }
	 @Override
     public void mouseClicked(MouseEvent e) {
		 
     }
	 @Override
     public void mouseMoved(MouseEvent e) {
		 
     }
	 @Override
     public void mouseDragged(MouseEvent e) {
		 if(e.getSource()==topPanel) {
			 Point currCoords = e.getLocationOnScreen();
			 setLocation(currCoords.x - compCoords.x, currCoords.y - compCoords.y);
		 }
	 }
	 @Override
	 public void stateChanged(ChangeEvent e) {
		if(e.getSource()==play_slider){
	       //System.out.println("当前值: " + play_slider.getValue());
		}
		else if(e.getSource()==loud_slider){
			
		}
	 }
	
		
	void LoadLyrics(String path)
	{
		lyricslist.clear();
		try(FileReader reader=new FileReader(path);BufferedReader br=new BufferedReader(reader))
	    {
			String line;
	        while ((line=br.readLine())!=null) 
	        {
	        	//System.out.println(line);
	        	int tmp=line.indexOf('#');
	        	lyricslist.add(new Lyrics(line.substring(0, tmp).trim(),Integer.valueOf(line.substring(tmp+1))));
	        }
	    } 
		catch(IOException e) 
		{
			//e.printStackTrace();
	    }
        if(lyricslist.size()==0) {
        	//System.out.println("没有歌词");
        	lyricslist.add(new Lyrics("暂无该歌曲歌词！",1));
        }
		textLyrics.setListData((lyricslist.subList(0,Math.min(pagenum,lyricslist.size()))).toArray());
	}
	 

	void playmusic(int index) throws FileNotFoundException, IOException//播放第几首
	{
		 File temp_file = new File(songlist.get(index).Directory.replaceAll(".wav", ".png"));
		 BufferedImage image1;
		 if(temp_file.exists()) {
			 image1 = ImageIO.read(new FileInputStream(songlist.get(index).Directory.replaceAll(".wav", ".png")));
		 }
		 else {
			 image1 = ImageIO.read(new FileInputStream(".//static//default.png"));
		 }
		 
		 remove(album);
		 getLayeredPane().remove(2);
		 
		 //imagetool.Rotate(image1,math.PI)
		 Image image2 = imagetool.setClip(image1, 300);
		 image2 = image2.getScaledInstance(300,300,Image.SCALE_SMOOTH);//图片缩放 默认专辑图片是方形的
		 ImageIcon albumicon = new ImageIcon(image2);
		 album= new JLabel(albumicon);
		 album.setBounds(160,105,300,300);//后面纵向需要截取188个像素 然后放大作为背景
		 add(album);
		 		 
		 // background  --------------------------------------------------
		 image1 = imagetool.img_alpha(image1, 39);//39是透明度
		 image2 = image1.getSubimage(0,0,image1.getWidth(),image1.getHeight()*750/1200);
		 image2 = image2.getScaledInstance(1200,750,Image.SCALE_SMOOTH);
		 Icon bg_icon=new ImageIcon(image2);//创建图标对象
		 background = new JLabel(bg_icon);//设置背景图片
		 background.setBounds(0,0,FrameWidth,FrameHeight);//设置背景控件大小
		 getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));//背景图片控件置于最底层
		
		
		audioPlay.SetPlayAudioPath(songlist.get(index).Directory);
		audioPlay.play();
		buttonPlay.setIcon(pauseicon);//更换为暂停
		Playingmusic_index=index;
		Playinglyrics_index=-1;
        MusicTime=getMusicTime(songlist.get(index).Directory);
        LoadLyrics(songlist.get(index).Directory.replaceAll(".wav", ".txt"));//更换路径名
		listPlayFile.setCellRenderer(new MusicListRender());
		if(listPlayFile.isVisible()==true)//让列表收回
		{
			//System.out.println("列表给我回去！！");
			listbackground.setVisible(false);
			buttonadd.setVisible(false);
			buttonadd.setEnabled(true);
			listPlayFile.setVisible(false);
			listPlayFile.setEnabled(true);
		}
		button_album.setText("专辑:  "+songlist.get(index).album);
		button_singer.setText("歌手:  "+songlist.get(index).singer);
		button_song.setText(songlist.get(index).name);
		//repaint();
		timerFun();
	}
	 
	//计时器
	public void timerFun()
	{
		if(PlayTimer!=null){//已经有定时器则关闭
			PlayTimer.cancel();
		}
		PlayTimer = new Timer();//创建定时器
		PlayTimer.schedule(new TimerTask(){  //匿名类
        	int nPlayTime=0;  
            public void run() { //定时器函数体
            	if(buttonPlay.getIcon()==pauseicon) nPlayTime+=1;
            	play_slider.setValue(nPlayTime);
            	if(nPlayTime==100) {
        			if(Playingmusic_index!=-1)
        			{
        				try {
        					playmusic((Playingmusic_index+1)%songlist.size());
        				} catch (FileNotFoundException e1) {
        					// TODO Auto-generated catch block
        					e1.printStackTrace();
        				} catch (IOException e1) {
        					// TODO Auto-generated catch block
        					e1.printStackTrace();
        				}
        			}
        			else {
        				System.out.println("请先添加歌曲到播放列表!");
        			}
            	}
            }
        },0,MusicTime/100);
		
		
		if(LyricsTimer!=null){
			LyricsTimer.cancel();
		}//已经有定时器则关闭
		LyricsTimer = new Timer();//创建定时器
		LyricsTimer.schedule(new TimerTask(){  //匿名类
        	int nPlayTime=0;  
            public void run() { //定时器函数体
            	if(buttonPlay.getIcon()==pauseicon) nPlayTime+=1;
            	//System.out.println(Playinglyrics_index);
            	if(Playinglyrics_index+1<lyricslist.size() && nPlayTime >= lyricslist.get(Playinglyrics_index+1).time)
            	{
            		Playinglyrics_index+=1;
            		//从lyricslist中取出部分 
            		if(Playinglyrics_index/pagenum ==(lyricslist.size()-1)/pagenum) {
            			List<Lyrics> templist = lyricslist.subList(Playinglyrics_index/ pagenum * pagenum,lyricslist.size());
            			textLyrics.setListData(templist.toArray());
            		}
            		else {
            			//System.out.print(Playinglyrics_index/pagenum );
            			List<Lyrics> templist =lyricslist.subList(Playinglyrics_index / pagenum * pagenum ,(Playinglyrics_index / pagenum+1)*pagenum );
            			textLyrics.setListData(templist.toArray());
            		}
            	}          	
            }
        },0,1000);//间隔1s
    }
		
		
	 //获得歌曲的播放时长
	 int getMusicTime(String dir)
	 {
		return (int)(1.0*(new File(dir)).length()/1024/173*1000);
	 }
	 
	 
}	
