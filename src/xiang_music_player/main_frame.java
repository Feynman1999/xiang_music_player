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
	int FrameWidth=1200;//��ʱ�������û���Ļ��С д����
	int FrameHeight=750;
	//	Toolkit kit = Toolkit.getDefaultToolkit();
	//	Dimension screenSize = kit.getScreenSize();//����û���ǰ����Ļ��Ϣ
	JLabel background;//�����ؼ�
	JLabel title;//title
	JLabel album;//album
	JLabel volume;//volume
	JLabel love;//love
	JLabel listbackground;//list ����
	JButton button_singer;
	JButton button_album;
	JButton button_song;
	JButton buttonPlay;//���Ű�ť
	JButton buttonNext;//��һ��
	JButton buttonLast;//��һ��
	JButton buttonlist;//�б�
	JButton buttonadd;
	JButton minimize;//��С��
	JButton exit;//�˳�����
	JList listPlayFile;//�����б�ؼ�
	JList textLyrics;//��ʿؼ�
	JSlider play_slider;
	JSlider loud_slider;
	final JPanel topPanel;
	final JPanel bottomPanel;
	Container contentPane = getContentPane();
	Timer PlayTimer;//��������ʱ������
	Timer LyricsTimer;//��ʶ�ʱ������
	int MusicTime;//��¼��ǰ���ŵĸ�����ʱ�䣨��λ�����룩
	ArrayList<song> songlist= new ArrayList<song>();
	ArrayList<Lyrics> lyricslist=new ArrayList<Lyrics>();
	audioplay audioPlay=new audioplay();
	Icon playicon=new ImageIcon(".//static//play.png");//��������ͼ�����
	Icon pauseicon=new ImageIcon(".//static//pause.png");
	public static int Playingmusic_index=-1;		//�������ڲ��ŵ����ֵı��
	public static int Playinglyrics_index=-1;		//�������ڲ��ŵĸ�ʱ��
	public static int pagenum = 10;                  //ÿҳ��ʾ�����Ŀ
	Map<String,String> map_singer;
	Map<String,String> map_album;
	
	public Main_Frame(Map m1,Map m2) throws FileNotFoundException, IOException{
		map_singer = m1;
		map_album = m2;
		setSize(new Dimension(FrameWidth,FrameHeight));
		setUndecorated(true); 
		setLocationRelativeTo(null);   
		setLayout(null);//�ղ���			
		topPanel = new JPanel();
		topPanel.setLayout(null);
		topPanel.setBounds(0, 0, FrameWidth, 48);
		topPanel.setOpaque(false);
		bottomPanel = new JPanel();
		bottomPanel.setLayout(null);
		bottomPanel.setBounds(0, FrameHeight-63 , FrameWidth, 63);
		bottomPanel.setOpaque(false);
		init();   //��ӿؼ��Ĳ�����װ��һ������      
		topPanel.addMouseListener(this);//��Ӧ�û�����ק
		topPanel.addMouseMotionListener(this);
		add(topPanel,BorderLayout.NORTH);
		add(bottomPanel,BorderLayout.SOUTH);
		setResizable(false);
		setVisible(true);//��������������ִ��
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	 }

	 void Slide()
	 {
			play_slider=new JSlider(0,100,0);
			play_slider.setUI(new MySliderUI(play_slider));
			play_slider.setBounds(0,FrameHeight-63-36,1200,42);
			play_slider.setPaintTicks(false);//����ʾ���
			play_slider.setBackground(Color.BLACK);
			play_slider.setOpaque(false);//��͸��
			play_slider.addChangeListener(this);
			add(play_slider);
			loud_slider=new JSlider(0,20,12);
			loud_slider.setUI(new MySliderUI(loud_slider));
			loud_slider.setBounds(800,9,100,42);
			loud_slider.setPaintTicks(false);//����ʾ���
			loud_slider.setBackground(Color.BLACK);
			loud_slider.setOpaque(false);//��͸��
			loud_slider.addChangeListener(this);
			bottomPanel.add(loud_slider);
	 }
	 
	 void init() throws FileNotFoundException, IOException{//��ӵĿؼ�
		 // album -------------------------------------------------- 
		 BufferedImage image1 = ImageIO.read(new FileInputStream(".//static//default.png"));
		 
		 //imagetool.Rotate(image1,math.PI)
		 Image image2 = imagetool.setClip(image1, 300);
		 image2 = image2.getScaledInstance(300,300,Image.SCALE_SMOOTH);//ͼƬ���� Ĭ��ר��ͼƬ�Ƿ��ε�
		 ImageIcon albumicon = new ImageIcon(image2);
		 album= new JLabel(albumicon);
		 album.setBounds(160,105,300,300);//����������Ҫ��ȡ188������ Ȼ��Ŵ���Ϊ����
		 add(album);
		 		 
		 // background  -------------------------------------------------- 
		 image1 = imagetool.img_alpha(image1, 39);//39��͸����
		 image2 = image1.getSubimage(0,0,image1.getWidth(),image1.getHeight()*750/1200);
		 image2 = image2.getScaledInstance(1200,750,Image.SCALE_SMOOTH);
		 Icon bg_icon=new ImageIcon(image2);//����ͼ�����
		 background = new JLabel(bg_icon);//���ñ���ͼƬ
		 background.setBounds(0,0,FrameWidth,FrameHeight);//���ñ����ؼ���С
		 getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));//����ͼƬ�ؼ�������ײ�
		 
		 // list background -------------------------------------------------- 
		 image1 = ImageIO.read(new FileInputStream(".//static//listbackground.png"));
		 image1 = imagetool.img_alpha(image1, 70);//70��͸����
		 listbackground=new JLabel(new ImageIcon(image1));
		 listbackground.setBounds(850,160,350,470);
		 listbackground.setVisible(false);
		 getLayeredPane().add(listbackground, new Integer(Integer.MIN_VALUE+8));//����ͼƬ�ؼ�������ײ�

		 ((JPanel)contentPane).setOpaque(false); //�ؼ�͸��  �൱��һ�㡰������ ����͸����������ײ�ı���ͼƬ 
		 
		 
		 // title ---------------------------------------------------
		 Icon titleicon=new ImageIcon(".//static//title.png");//set title
		 title = new JLabel(titleicon);
		 title.setBounds(8,0,398,43);
		 topPanel.add(title);
		 
		 // buttons -------------------------------------------------- 
		 button_song = new JButton("Welcome!");
		 button_song.setBackground(Color.red);//���ں���Ҫ����͸��������ֻ��ռλ���� 
		 button_song.setBorderPainted(false);//ȥ���߿�
		 button_song.setMargin(new Insets(0,0,0,0));//��ͼ���仯
		 button_song.setOpaque(false);// ����͸��
		 button_song.setBounds(110,450,400,40);
		 button_song.setFocusPainted(false);
		 button_song.setFont(new Font("����", Font.BOLD, 34));
		 add(button_song);
		 
		 button_singer = new JButton("���֣�"+"ahu");
		 button_singer.setBackground(Color.red);//���ں���Ҫ����͸��������ֻ��ռλ���� 
		 button_singer.setBorderPainted(false);//ȥ���߿�
		 button_singer.setMargin(new Insets(0,0,0,0));//��ͼ���仯
		 button_singer.setOpaque(false);// ����͸��
		 button_singer.setBounds(90,510,400,30);
		 button_singer.setFocusPainted(false);
		 button_singer.setFont(new Font("����", Font.BOLD, 22));
		 add(button_singer);
		 
		 button_album = new JButton("ר����"+"ahu");
		 button_album.setBackground(Color.red);//���ں���Ҫ����͸��������ֻ��ռλ���� 
		 button_album.setBorderPainted(false);//ȥ���߿�
		 button_album.setMargin(new Insets(0,0,0,0));//��ͼ���仯
		 button_album.setOpaque(false);// ����͸��
		 button_album.setBounds(90,550,400,30);
		 button_album.setFocusPainted(false);
		 button_album.setFont(new Font("����", Font.BOLD, 22));
		 add(button_album);
				 
		 Icon minimizeicon = new ImageIcon(".//static//mini.png");
		 minimize = new JButton(minimizeicon);
		 minimize.setBackground(Color.red);//���ں���Ҫ����͸��������ֻ��ռλ���� 
		 minimize.setBorderPainted(false);//ȥ���߿�
		 minimize.setMargin(new Insets(0,0,0,0));//��ͼ���仯
		 minimize.setOpaque(false);// ����͸��
		 minimize.setBounds(FrameWidth-105,0,48,48);
		 minimize.setFocusPainted(false);
		 minimize.addActionListener(this);
		 topPanel.add(minimize);
		 
		 Icon exiticon=new ImageIcon(".//static//exit.png");
		 exit = new JButton(exiticon);
		 exit.setBackground(Color.red);//���ں���Ҫ����͸��������ֻ��ռλ���� 
		 exit.setBorderPainted(false);//ȥ���߿�
		 exit.setMargin(new Insets(0,0,0,0));//��ͼ���仯
		 exit.setOpaque(false);// ����͸��
		 exit.setBounds(FrameWidth-60,0,48,48);
		 exit.addActionListener(this);
		 topPanel.add(exit);
		 
		 Icon lasticon=new ImageIcon(".//static//last2.png");//��������ͼ�����
		 buttonLast=new JButton(lasticon);//��Ӳ��Ű�ť
		 buttonLast.setBackground(Color.red);//���ں���Ҫ����͸��������ֻ��ռλ���� 
		 buttonLast.setBorderPainted(false);//ȥ���߿�
	     buttonLast.setMargin(new Insets(0,0,0,0));//��ͼ���仯
	     buttonLast.setOpaque(false);// ����͸��
	     buttonLast.setBounds(50,12,32,32); //���ò��Ű�ť��С
	     buttonLast.addActionListener(this);
		 bottomPanel.add(buttonLast);//��Ӳ��Ű�ť��������
		 
		 buttonPlay=new JButton(playicon);//��Ӳ��Ű�ť
		 buttonPlay.setBackground(Color.red);//���ں���Ҫ����͸��������ֻ��ռλ���� 
		 buttonPlay.setBorderPainted(false);//ȥ���߿�
	     buttonPlay.setMargin(new Insets(0,0,0,0));//��ͼ���仯
	     buttonPlay.setOpaque(false);// ����͸��
	     buttonPlay.setBounds(110,4,48,48); //���ò��Ű�ť��С
	     buttonPlay.addActionListener(this);
		 bottomPanel.add(buttonPlay);//��Ӳ��Ű�ť��������
		  
		 Icon nexticon=new ImageIcon(".//static//next2.png");//��������ͼ�����
		 buttonNext=new JButton(nexticon);//��Ӳ��Ű�ť
		 buttonNext.setBackground(Color.red);//���ں���Ҫ����͸��������ֻ��ռλ���� 
		 buttonNext.setBorderPainted(false);//ȥ���߿�
	     buttonNext.setMargin(new Insets(0,0,0,0));//��ͼ���仯
	     buttonNext.setOpaque(false);// ����͸��
	     buttonNext.setBounds(186,12,32,32); //���ò��Ű�ť��С
	     buttonNext.addActionListener(this);
		 bottomPanel.add(buttonNext);//��Ӳ��Ű�ť��������
		 
		 Icon listicon=new ImageIcon(".//static//list.png");//��������ͼ�����
		 buttonlist=new JButton(listicon);//��Ӳ��Ű�ť
		 buttonlist.setBackground(Color.red);//���ں���Ҫ����͸��������ֻ��ռλ���� 
		 buttonlist.setBorderPainted(false);//ȥ���߿�
	     buttonlist.setMargin(new Insets(0,0,0,0));//��ͼ���仯
	     buttonlist.setOpaque(false);// ����͸��
	     buttonlist.setBounds(FrameWidth-78,12,32,32); //���ò��Ű�ť��С
	     buttonlist.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	     buttonlist.addActionListener(this);
	     bottomPanel.add(buttonlist);//��Ӳ��Ű�ť��������
		 
	     Icon buttonaddicon = new ImageIcon(".//static//add.png");
	     buttonadd = new JButton(buttonaddicon);
	     buttonadd.setBackground(Color.RED);
	     buttonadd.setBounds(1150,570,48,48);
	     buttonadd.setBorderPainted(false);//ȥ���߿�
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
		 Slide();//����������
		 		 
		 listPlayFile=new JList();	  //���������б� 
		 listPlayFile.setBounds(850,160,350,500); //���ò����б��С 
		 listPlayFile.setOpaque(false);//���ò����б�͸��
         listPlayFile.setVisible(false);
	     listPlayFile.setCellRenderer(new MusicListRender());
	     listPlayFile.addMouseListener(this);
		 add(listPlayFile);       //��Ӳ����б���������
		  
		 textLyrics=new JList();   //������ʿؼ�    
		 textLyrics.setBounds(650,100,400,600);//���ø�ʿؼ���С
		 textLyrics.setOpaque(false);//��ʿؼ�͸��
		 textLyrics.setCellRenderer(new LyricsListRender());
		 add(textLyrics);    //��Ӹ�ʿؼ���������
		 
		 
		 lyricslist.add(new Lyrics("��ӭ����xiang music player��",1));
		 lyricslist.add(new Lyrics("         ",2));
		 lyricslist.add(new Lyrics("���½ǵ��ֵ���Ӹ���(#^.^#)",3));
		 lyricslist.add(new Lyrics("         ",4));
		 lyricslist.add(new Lyrics("�����л�����ʱ��������䶯Ŷ",5));

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
					if(pos==-1) pos=0;	//���û��ѡ�У�Ĭ�ϲ��ŵ�һ��
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
					System.out.println("������Ӹ����������б�!");
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
				System.out.println("������Ӹ����������б�!");
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
				System.out.println("������Ӹ����������б�!");
			}
		}
		else if(e.getSource()==buttonlist) {
			if(listPlayFile.isVisible()==true)
			{
				System.out.println("�б���һ�ȥ����");
				listbackground.setVisible(false);
				buttonadd.setVisible(false);
				buttonadd.setEnabled(true);
				listPlayFile.setVisible(false);
				listPlayFile.setEnabled(true);
			}
			else {
				System.out.println("�б���ҳ�������");
				listbackground.setVisible(true);
				buttonadd.setVisible(true);
				buttonadd.setEnabled(true);
				listPlayFile.setVisible(true);
				listPlayFile.setEnabled(true);
			}
		}
		else if(e.getSource()==buttonadd) {
		   try{
				 FileDialog openFile=new FileDialog(this,"���ļ�");//�������ļ��Ի���	
				 openFile.setVisible(true);//�Ի���ɼ�
				 String playFileName=openFile.getFile();//��ȡ�򿪵��ļ���
				 if(playFileName.endsWith(".wav")==true)
				 {
					 String playFileDirectory=openFile.getDirectory();//��ȡ�򿪵��ļ�·��
					 String playFilepath=playFileDirectory+playFileName;//���������·��
					 System.out.println(playFilepath);
					 playFileName = playFileName.substring(0,playFileName.length()-4);
					 song tmp=new song(playFileName,map_singer.get(playFileName),map_album.get(playFileName),playFilepath,getMusicTime(playFilepath));
					 songlist.add(tmp);
					 listPlayFile.setListData(songlist.toArray());
					 System.out.println("��Ӹ����� ok!");
				 }
				 else {
					 System.out.println("��ѡ��wav��ʽ�������ļ���");
				 }
			}
			catch(Exception e1)
			{
				System.out.println("ѡ���ļ�ʱ��������");
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
     public void mousePressed(MouseEvent e) {//�õ����������Ϣ
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
	       //System.out.println("��ǰֵ: " + play_slider.getValue());
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
        	//System.out.println("û�и��");
        	lyricslist.add(new Lyrics("���޸ø�����ʣ�",1));
        }
		textLyrics.setListData((lyricslist.subList(0,Math.min(pagenum,lyricslist.size()))).toArray());
	}
	 

	void playmusic(int index) throws FileNotFoundException, IOException//���ŵڼ���
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
		 image2 = image2.getScaledInstance(300,300,Image.SCALE_SMOOTH);//ͼƬ���� Ĭ��ר��ͼƬ�Ƿ��ε�
		 ImageIcon albumicon = new ImageIcon(image2);
		 album= new JLabel(albumicon);
		 album.setBounds(160,105,300,300);//����������Ҫ��ȡ188������ Ȼ��Ŵ���Ϊ����
		 add(album);
		 		 
		 // background  --------------------------------------------------
		 image1 = imagetool.img_alpha(image1, 39);//39��͸����
		 image2 = image1.getSubimage(0,0,image1.getWidth(),image1.getHeight()*750/1200);
		 image2 = image2.getScaledInstance(1200,750,Image.SCALE_SMOOTH);
		 Icon bg_icon=new ImageIcon(image2);//����ͼ�����
		 background = new JLabel(bg_icon);//���ñ���ͼƬ
		 background.setBounds(0,0,FrameWidth,FrameHeight);//���ñ����ؼ���С
		 getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));//����ͼƬ�ؼ�������ײ�
		
		
		audioPlay.SetPlayAudioPath(songlist.get(index).Directory);
		audioPlay.play();
		buttonPlay.setIcon(pauseicon);//����Ϊ��ͣ
		Playingmusic_index=index;
		Playinglyrics_index=-1;
        MusicTime=getMusicTime(songlist.get(index).Directory);
        LoadLyrics(songlist.get(index).Directory.replaceAll(".wav", ".txt"));//����·����
		listPlayFile.setCellRenderer(new MusicListRender());
		if(listPlayFile.isVisible()==true)//���б��ջ�
		{
			//System.out.println("�б���һ�ȥ����");
			listbackground.setVisible(false);
			buttonadd.setVisible(false);
			buttonadd.setEnabled(true);
			listPlayFile.setVisible(false);
			listPlayFile.setEnabled(true);
		}
		button_album.setText("ר��:  "+songlist.get(index).album);
		button_singer.setText("����:  "+songlist.get(index).singer);
		button_song.setText(songlist.get(index).name);
		//repaint();
		timerFun();
	}
	 
	//��ʱ��
	public void timerFun()
	{
		if(PlayTimer!=null){//�Ѿ��ж�ʱ����ر�
			PlayTimer.cancel();
		}
		PlayTimer = new Timer();//������ʱ��
		PlayTimer.schedule(new TimerTask(){  //������
        	int nPlayTime=0;  
            public void run() { //��ʱ��������
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
        				System.out.println("������Ӹ����������б�!");
        			}
            	}
            }
        },0,MusicTime/100);
		
		
		if(LyricsTimer!=null){
			LyricsTimer.cancel();
		}//�Ѿ��ж�ʱ����ر�
		LyricsTimer = new Timer();//������ʱ��
		LyricsTimer.schedule(new TimerTask(){  //������
        	int nPlayTime=0;  
            public void run() { //��ʱ��������
            	if(buttonPlay.getIcon()==pauseicon) nPlayTime+=1;
            	//System.out.println(Playinglyrics_index);
            	if(Playinglyrics_index+1<lyricslist.size() && nPlayTime >= lyricslist.get(Playinglyrics_index+1).time)
            	{
            		Playinglyrics_index+=1;
            		//��lyricslist��ȡ������ 
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
        },0,1000);//���1s
    }
		
		
	 //��ø����Ĳ���ʱ��
	 int getMusicTime(String dir)
	 {
		return (int)(1.0*(new File(dir)).length()/1024/173*1000);
	 }
	 
	 
}	
