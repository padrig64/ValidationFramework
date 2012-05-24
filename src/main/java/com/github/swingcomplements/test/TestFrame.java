package com.github.swingcomplements.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;

import com.github.swingcomplements.container.SlideContainer;
import com.github.swingcomplements.container.transition.NewSlideInTransition;
import com.github.swingcomplements.container.transition.NoTransition;
import com.github.swingcomplements.container.transition.OldSlideOutTransition;

public class TestFrame extends JFrame {

	private class LeftAction extends AbstractAction {

		public LeftAction() {
			super("Left");
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			slideContainer.setSlide(((Slide) slideContainer.getSlide()).getLeft(), new OldSlideOutTransition(
					SwingConstants.EAST));
		}
	}

	private class RightAction extends AbstractAction {

		public RightAction() {
			super("Right");
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			slideContainer.setSlide(new Slide((Slide) slideContainer.getSlide(), null), new NewSlideInTransition(
					SwingConstants.EAST));
		}
	}

	private class UpAction extends AbstractAction {

		public UpAction() {
			super("Up");
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			slideContainer.setSlide(((Slide) slideContainer.getSlide()).getUp(), new OldSlideOutTransition(
					SwingConstants.SOUTH));
		}
	}

	private class DownAction extends AbstractAction {

		public DownAction() {
			super("Down");
		}

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			slideContainer.setSlide(new Slide(null, (Slide) slideContainer.getSlide()), new NewSlideInTransition(
					SwingConstants.SOUTH));
		}
	}

	private class Slide extends JPanel {

		private Slide left = null;
		private Slide up = null;

		public Slide(Slide left, Slide up) {
			this.left = left;
			this.up = up;

			init();
		}

		private void init() {
			setLayout(new BorderLayout());
			setBorder(new LineBorder(Color.GREEN));

			JButton leftButton = new JButton(new LeftAction());
			add(leftButton, BorderLayout.WEST);
			leftButton.setEnabled(left != null);

			JButton rightButton = new JButton(new RightAction());
			add(rightButton, BorderLayout.EAST);

			JButton upButton = new JButton(new UpAction());
			add(upButton, BorderLayout.NORTH);
			upButton.setEnabled(up != null);

			JButton downButton = new JButton(new DownAction());
			add(downButton, BorderLayout.SOUTH);

			JLabel textLabel = new JLabel("Slide (" + getIndexX() + ", " + getIndexY() + ")");
			textLabel.setHorizontalAlignment(SwingConstants.CENTER);
			add(textLabel, BorderLayout.CENTER);
		}

		public int getIndexX() {
			int index = 0;

			if (left != null) {
				index = left.getIndexX() + 1;
			}

			return index;
		}

		public int getIndexY() {
			int index = 0;

			if (up != null) {
				index = up.getIndexY() + 1;
			}

			return index;
		}

		public Slide getLeft() {
			return left;
		}

		public Slide getUp() {
			return up;
		}
	}

	private SlideContainer slideContainer = new SlideContainer();

	public TestFrame() {
		super();
		init();
	}

	private void init() {
		setTitle("Swing Complements Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		slideContainer.setBorder(new LineBorder(Color.RED));
		setContentPane(slideContainer);

		slideContainer.setSlide(new Slide(null, null), new NoTransition());

		// Set size
		pack();
		setSize(new Dimension(600, 600));

		// Set location
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 3);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				// Set look-and-feel
				try {
					for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (UnsupportedLookAndFeelException e) {
					// handle exception
				} catch (ClassNotFoundException e) {
					// handle exception
				} catch (InstantiationException e) {
					// handle exception
				} catch (IllegalAccessException e) {
					// handle exception
				}

				// Show window
				new TestFrame().setVisible(true);
			}
		});
	}
}
