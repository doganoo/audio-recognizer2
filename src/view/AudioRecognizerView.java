package view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class AudioRecognizerView extends JFrame {
	private static final long serialVersionUID = 1L;
	private static AudioRecognizerView view = null;
	private JLabel actionLabel = new JLabel("");
	private JLabel nameOfSong = new JLabel("");

	private AudioRecognizerView(String text) {
		setTitle(text);
		initializeJFrame();
	}

	public static AudioRecognizerView getInstance() {
		if (view == null) {
			view = new AudioRecognizerView("audio-recognizer2");
		}
		return view;
	}

	private void initializeJFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(500, 300);
		setLocationRelativeTo(null);
		actionLabel.setBackground(new Color(255, 64, 64));
		actionLabel.setBounds(10, 10, 50, 50);
		nameOfSong.setBackground(new Color(202, 255, 112));
		actionLabel.setBounds(50, 50, 100, 100);
		add(actionLabel);
		add(nameOfSong);
		setVisible(true);
	}

	public void setActionText(String text) {
		this.actionLabel.setText("");
		this.actionLabel.setText(text);
		invalidate();
		validate();
		repaint();
	}

	public void setNameOfSong(String text) {
		this.nameOfSong.setText("");
		this.nameOfSong.setText(text);
		invalidate();
		validate();
		repaint();
	}

}
