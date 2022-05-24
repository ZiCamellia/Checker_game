package gamecocaro9o.io;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

public class Board extends JPanel{
	private static final int M = 3;
	private static final int  N = 3;
	
	private EndGameListener endGameListener;
	private Image imgX;
	private Image imgO;
	private static Cell matrix[][] = new Cell[N][M];
	private String currentPlayer = Cell.EMPTY_VALUE;
	//0: hòa, 1: player hiện tại thắng , 2:player hiện tại chưa thắng. 
	 static final int ST_DRAW = 0;
	static final int ST_WIN = 1;
	static final int ST_NOMAL = 2;
	
	public Board(String player) {
		this();
		this.currentPlayer = player;
	}
	public Board() {
		this.initMatrix();
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				
				super.mousePressed(e);
				int x = e.getX();
				int y = e.getY();
				//kiểm tra nếu các ô trống mà chưa nhấn start thì k cho chạy
				if(currentPlayer.equals(Cell.EMPTY_VALUE)) {
					return;
				}
				
				//phát ra âm thanh
				soundClick();
				
				for(int i=0; i < N; i++) {
					for(int j = 0; j < M; j++) {
						Cell cell = matrix[i][j];
						int cXStart = cell.getX();
						int cXEnd = cell.getX() + cell.getW();
						int cYStart = cell.getY();
						int cYEnd = cell.getH() + cell.getY();
						if(x >= cXStart && x < cXEnd && y >= cYStart && y < cYEnd) {
							if(cell.getValue().equals(Cell.EMPTY_VALUE)) {
								cell.setValue(currentPlayer);
								int result = checkWin(currentPlayer);
								if(endGameListener != null) {
									endGameListener.end(currentPlayer, result);
								}
								if(result == ST_NOMAL) {
									currentPlayer = currentPlayer.equals(Cell.O_VALUE) ? Cell.X_VALUE : Cell.O_VALUE;
								}
								//đảo giá trị, nếu current player == o thì click sau sẽ là x
								
								repaint();
							}
							
						}
						
					}
				}   
			}
		});
		try {
			imgX = ImageIO.read(getClass().getResource("X.png"));
			imgO = ImageIO.read(getClass().getResource("O.png"));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public synchronized void soundClick() {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("Mousclik.wav"));
					clip.open(audioInputStream);
					clip.start();
				}catch(Exception e) {
					e.printStackTrace();
				}
					}
		});
		thread.start();
		
		
	} 
	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	 
	
	public String getCurrentPlayer() {
		return currentPlayer;
	}
	public void setEndGameListener(EndGameListener endGameListener) {
		this.endGameListener = endGameListener;
	}
	private void initMatrix() {
		for(int i=0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				Cell cell = new Cell();
				matrix[i][j] = cell;
			}
		}
	}
	public void resset() {
		this.initMatrix();
		this.setCurrentPlayer(Cell.EMPTY_VALUE);
		repaint();
	}
	public int checkWin(String player) {
		//check đường chéo thứ nhất
		if(this.matrix[0][0].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[2][2].getValue().equals(player)) {
			return ST_WIN;
		}
		//check đường chéo thứ 2
		if(this.matrix[0][2].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[2][0].getValue().equals(player)) {
			return ST_WIN;
		}
		//check dòng 1{
		if(this.matrix[0][0].getValue().equals(player) && this.matrix[0][1].getValue().equals(player) && this.matrix[0][2].getValue().equals(player)) {
			return ST_WIN;
		}
		//check dòng 2
		if(this.matrix[1][0].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[1][2].getValue().equals(player)) {
			return ST_WIN;
		}
		//check dòng 3
		if(this.matrix[2][0].getValue().equals(player) && this.matrix[2][1].getValue().equals(player) && this.matrix[2][2].getValue().equals(player)) {
			return ST_WIN;
		}
		//check cột 1
		if(this.matrix[0][0].getValue().equals(player) && this.matrix[1][0].getValue().equals(player) && this.matrix[2][0].getValue().equals(player)) {
			return ST_WIN;
		}
		//check cột 2
		if(this.matrix[0][1].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[2][1].getValue().equals(player)) {
			return ST_WIN;
		}
		//check dòng 3
		if(this.matrix[0][2].getValue().equals(player) && this.matrix[1][2].getValue().equals(player) && this.matrix[2][2].getValue().equals(player)) {
			return ST_WIN;
		}
		if(this.isFull()) {
			return ST_DRAW;
		}
		return ST_NOMAL;
		
	}
	public  boolean isFull() {
		int number = N*M;
		int k = 0;
		for(int i = 0; i <N; i++) {
			for(int j = 0; j <M; j++) {
				Cell cell = matrix[i][j];
				if(!cell.getValue().equals(Cell.EMPTY_VALUE)) {
					k++;
				}
				
			}
		}
		if(k == number) {
			return true;
		}
		return false;
		
	}
	public void paint(Graphics g) {
		int w = getWidth() / 3;
		int h = getHeight()/3;
		Graphics2D graphics2d = (Graphics2D) g;
		graphics2d.setColor(Color.BLUE);
		graphics2d.fillRect(0, 0, w, h);
		
		//VẼ CÁC Ô, nếu ô lẻ thì in màu đỏ, chẵn thì in màu xanh
		int k = 0;
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < M; j++) {
				//THIẾT KẾ VỊ TRÍ CÁC Ô
				int x = j*w;// 0*200 = 0; 1*200 = 200; 2*200 = 400;
				int y = i*h;//0*200 = 0; 1*200 = 200;
				
				//cập nhật lại ma trận
				Cell cell = matrix[i][j];
				cell.setX(x);
				cell.setY(y);
				cell.setW(w);
				cell.setH(h);
				
				Color color = k % 2 == 0 ? Color.BLUE : Color.YELLOW;
				graphics2d.setColor(color);
				graphics2d.fillRect(x, y, w, h);
			
				if(cell.getValue().equals(Cell.O_VALUE)) {
					Image img = imgO;
					graphics2d.drawImage(img, x, y, w, h, this);
				}
				else if(cell.getValue().equals(Cell.X_VALUE)) {
					Image img = imgX;
					graphics2d.drawImage(img, x, y, w, h, this);
				}
				k++;
			}
		}
	}
}
