package splprac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;

public class MultiProjectileSimulation extends JPanel implements ActionListener {

    private static final double g = 9.8; // gravity m/s^2
    private static final int GROUND_Y = 500;
    private static final int BALL_SIZE = 20;
    private static final int START_X = 150;
    private static final int START_Y = GROUND_Y - BALL_SIZE / 2;
    private static final int GROUND_LEVEL = 500;
    private static final int BOY_X = 150;

    private Timer timer;
    private double time = 0;
    private boolean stopped = false;
    private boolean soundPlayed = false;

    private Image boyImage;
    private Image footballImage;

    // A class to store each projectile's state
    private static class Projectile {
        double velocity;
        double angleRad;
        double x, y;
        List<Point> path = new ArrayList<>();

        public Projectile(double velocity, double angleDeg) {
            this.velocity = velocity;
            this.angleRad = Math.toRadians(angleDeg);
            this.x = START_X;
            this.y = START_Y;
        }
    }

    private List<Projectile> projectiles = new ArrayList<>();

    public MultiProjectileSimulation(List<Double> velocities, List<Double> angles) {
        if (velocities.size() != angles.size()) {
            throw new IllegalArgumentException("Velocities and angles lists must have the same size");
        }

        // Initialize each projectile
        for (int i = 0; i < velocities.size(); i++) {
            projectiles.add(new Projectile(velocities.get(i), angles.get(i)));
        }

        boyImage = new ImageIcon(getClass().getResource("/splprac/boy.png")).getImage();
        footballImage = new ImageIcon(getClass().getResource("/splprac/football.png")).getImage();

        timer = new Timer(30, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Background and ground
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.GREEN);
        g.fillRect(0, GROUND_LEVEL, getWidth(), getHeight() - GROUND_LEVEL);

        // Draw boy image
        g.drawImage(boyImage, BOY_X - 50, GROUND_LEVEL - 100, 100, 100, this);

        // Colors for projectiles (extend if needed)
        Color[] colors = {Color.RED, Color.BLUE, Color.MAGENTA, Color.ORANGE, Color.CYAN, Color.PINK, Color.BLACK};

        // Draw each projectile's path and ball
        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            Color c = colors[i % colors.length];
            g.setColor(c);

            // Draw path points
            for (Point pt : p.path) {
                g.fillOval(pt.x, pt.y, 5, 5);
            }

            // Draw projectile ball
            g.drawImage(footballImage, (int) p.x - BALL_SIZE / 2, (int) p.y - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE, this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (stopped) {
            timer.stop();
            return;
        }
        if (!soundPlayed) {
            playSound("football.wav");
            soundPlayed = true;
        }
        time += 0.03;

        boolean allLanded = true;

        for (Projectile p : projectiles) {
            // Update projectile position
            double newX = START_X + p.velocity * Math.cos(p.angleRad) * time * 10;
            double newY = START_Y - (p.velocity * Math.sin(p.angleRad) * time - 0.5 * g * time * time) * 10;

            // Check ground collision
            if (newY >= GROUND_Y - BALL_SIZE / 2) {
                newY = GROUND_Y - BALL_SIZE / 2;
            } else {
                allLanded = false;
            }

            p.x = newX;
            p.y = newY;
            p.path.add(new Point((int) p.x, (int) p.y));
        }

        // Stop animation when all projectiles have landed
        if (allLanded) {
            stopped = true;
        }

        repaint();
    }

    private void playSound(String fileName) {
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
        try {
            int count = Integer.parseInt(JOptionPane.showInputDialog("How many projectiles?"));

            List<Double> velocities = new ArrayList<>();
            List<Double> angles = new ArrayList<>();

            for (int i = 1; i <= count; i++) {
                String vStr = JOptionPane.showInputDialog("Enter velocity " + i + " (m/s):");
                String aStr = JOptionPane.showInputDialog("Enter angle " + i + " (degrees):");
                velocities.add(Double.parseDouble(vStr));
                angles.add(Double.parseDouble(aStr));
            }

            JFrame frame = new JFrame("Multi Projectile Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 650);

            MultiProjectileSimulation sim = new MultiProjectileSimulation(velocities, angles);
            frame.add(sim);
            frame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter numbers.");
        }
    }
}
