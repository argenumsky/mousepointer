package com.aregumsky.mousepointer;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Application {
	ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	JButton start;

	JButton stop;

	boolean move = true;

	boolean schedulerStarted = false;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			new Application();
		});
	}
	
	public Application() {
		initialize();
	}

	private void initialize() {
		createStartButton();
		createStopButton();

		JPanel panel = new JPanel();
		panel.add(start);
		panel.add(stop);

		JFrame frame = new JFrame("Mouse Pointer");
		frame.setSize(170, 60);
		frame.setAlwaysOnTop(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setVisible(true);
	}

	void createStartButton() {
		start = new JButton("Start");
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start.setEnabled(false);
				if (stop != null) {
					stop.setEnabled(true);
				}
				move = true;

				if (!schedulerStarted) {
					startScheduledAction(5);
				}
			}
		});
	}

	void createStopButton() {
		stop = new JButton("Stop");
		stop.setEnabled(false);
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (stop != null) {
					start.setEnabled(true);
				}
				stop.setEnabled(false);
				move = false;
			}
		});
	}

	int getRandom(int max) {
		Random random = new Random();
		return random.nextInt(max);
	}

	void moveMouse() throws AWTException {
		Robot robot = new Robot();
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		robot.mouseMove(getRandom((int) screen.getWidth()), getRandom((int) screen.getHeight()));
	}

	private void startScheduledAction(int seconds) {
		scheduler.scheduleAtFixedRate(() -> {
			if (move) {
				try {
					moveMouse();
				} catch (AWTException e) {
					throw new UnsupportedOperationException(e.getLocalizedMessage());
				}
			}
		}, 0, seconds, TimeUnit.SECONDS);
		
		schedulerStarted = true;
	}
}
