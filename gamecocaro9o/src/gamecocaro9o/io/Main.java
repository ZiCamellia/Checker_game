package gamecocaro9o.io;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main {
	private static int sec = 0;
	private static Timer timer = new Timer();
	private static Board board;
	private static JLabel lblStart;
	private static JButton btnStart;
	public static void main(String[] args) {

		board = new Board();
		board.setEndGameListener(new EndGameListener() {
			
			@Override
			public void end(String player, int st) {
				// TODO Auto-generated method stub
				if(st == Board.ST_WIN) {
					JOptionPane.showMessageDialog(null, "Người chơi "+player + " thắng");
					stopGame();
				}
				else if(st == Board.ST_DRAW) {
					JOptionPane.showMessageDialog(null, "Hòa rồi");
					stopGame();
				}
				
			}
		});
		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
		//Board
		board.setPreferredSize(new Dimension(300, 300));
		//buttom panel
		JPanel buttomPanel = new JPanel();
		buttomPanel.setLayout(new FlowLayout());
		
		jPanel.add(board);
		jPanel.add(buttomPanel);
		
		btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(btnStart.getText().equals("START")) {
					startGame();
				}else {
					stopGame();
				}
				
				
			}
		});
		lblStart = new JLabel("0:0");
		buttomPanel.add(lblStart);
		buttomPanel.add(btnStart);
		
		JFrame jframe = new JFrame("Game cờ ca rô 9 ô");
		jframe.setSize(600, 600);
		//add board
		jframe.add(jPanel);
		
		jframe.setResizable(false);
		jframe.setLocationRelativeTo(null);
		//thiết kế vị trí của ứng dụng
		//jframe.setLocation(x, y);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.pack();
		jframe.setVisible(true);
	}
	private static void startGame() {
		board.soundClick();
		//hỏi ai di trước
		int choice = JOptionPane.showConfirmDialog(null, "Người chơi O đi trước đúng không?", "Ai là người đi trước", JOptionPane.YES_NO_OPTION);
		board.soundClick();
		board.resset();
		String currentlayer = (choice == 0)? Cell.O_VALUE : Cell.X_VALUE;
		board.setCurrentPlayer(currentlayer);
		//đếm ngược
		 sec = 0;
		 timer.cancel();
		 timer = new Timer();
		 lblStart.setText("0:0");
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sec++;
				String value = sec/60 + " : " + sec%60;
				lblStart.setText(value);
				
			}
		}, 1000, 1000);
		btnStart.setText("STOP");
	}
	private static void stopGame() {
		board.soundClick();
		btnStart.setText("START");
		sec = 0;
		lblStart.setText("0:0");
		timer.cancel();
		timer = new Timer();
		
		
		board.resset();
	}

}
