package splprac;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class AppleFallingSimulation extends JPanel implements ActionListener {
    private static final double GRAVITY = 0.5;
    private static final double BOUNCE_FACTOR = 0.7;
    private double appleY;
    private double velocityY;
    private Timer timer;
    private Image treeImage, appleImage;
    private JLabel velocityLabel;
    private boolean soundPlayed = false; // ✅ Added to control sound during fall

    public AppleFallingSimulation(double initialVelocity, JLabel velocityLabel) {
        appleY = 100;
        velocityY = initialVelocity;
        this.velocityLabel = velocityLabel;

        treeImage = new ImageIcon(getClass().getResource("tree.png")).getImage();
        appleImage = new ImageIcon(getClass().getResource("apple.png")).getImage();

        timer = new Timer(30, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(135, 206, 250));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(treeImage, 200, 100, 200, 300, this);
        g.drawImage(appleImage, 280, (int) appleY, 30, 30, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        velocityY += GRAVITY;
        appleY += velocityY;

        // ✅ Play sound while apple is falling down
        if (appleY < 400 && !soundPlayed) {
            playSound("apple.wav");
            soundPlayed = true;
        }

        // ✅ When it hits the ground, reverse velocity and reset sound flag
        if (appleY >= 400) {
            appleY = 400;
            velocityY = -velocityY * BOUNCE_FACTOR;
            soundPlayed = false;
        }

        velocityLabel.setText("Velocity: " + String.format("%.2f", velocityY));
        repaint();
    }

    public void playSound(String fileName) {
        try {
            URL soundURL = getClass().getResource("/splprac/" + fileName);
            if (soundURL == null) {
                System.err.println("Sound file not found: " + fileName);
                return;
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing sound: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Enter initial velocity of apple : ");
        try {
            double initialVelocity = Double.parseDouble(input);
            JFrame frame = new JFrame("Apple Falling Due to Gravity");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 500);

            JLabel velocityLabel = new JLabel("Velocity: 0 px/frame", SwingConstants.CENTER);
            velocityLabel.setFont(new Font("Kalpurush", Font.BOLD, 16));

            AppleFallingSimulation simulation = new AppleFallingSimulation(initialVelocity, velocityLabel);
            frame.setLayout(new BorderLayout());
            frame.add(velocityLabel, BorderLayout.NORTH);
            frame.add(simulation, BorderLayout.CENTER);

            frame.setVisible(true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric value.");
        }
    }
}
