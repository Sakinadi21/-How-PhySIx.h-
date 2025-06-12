package splprac;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

public class proj extends JPanel implements ActionListener {
    private final double g = 9.8; // gravity
    private final int GROUND_Y = 500;
    private final int BALL_SIZE = 20;
    private final int START_X = 150;
    private final int START_Y = GROUND_Y - BALL_SIZE / 2;

    private final int GROUND_LEVEL = 500;

    private final int BOY_X = 150;
    private final int FOOT_Y = GROUND_LEVEL - 40;

    private double angle1, angle2; // in radians
    private double velocity1, velocity2;
    private double time = 0;
    private double x1, y1, x2, y2;

    private ArrayList<Point> path1 = new ArrayList<>();
    private ArrayList<Point> path2 = new ArrayList<>();
    private Image boyImage;
    private Image footballImage;

    private Timer timer;
    private boolean stopped = false;
    private boolean soundPlayed=false;
    public proj(double v1, double a1, double v2, double a2) {
        // Convert degrees to radians
        angle1 = Math.toRadians(a1);
        angle2 = Math.toRadians(a2);

        velocity1 = v1;
        velocity2 = v2;

        double sumAngles = a1 + a2;
        double diffAngles = Math.abs(a1 - a2);

        // If angles add to 90°, adjust velocity2 to match range of first projectile
        if (Math.abs(sumAngles - 90) < 1e-6) {
            double range1 = (velocity1 * velocity1 * Math.sin(2 * angle1)) / g;
            velocity2 = Math.sqrt(range1 * g / Math.sin(2 * angle2));
        }
        // For equal angles (like 45 + 45), don't change velocity2

        x1 = START_X;
        y1 = START_Y;
        x2 = START_X;
        y2 = START_Y;

        boyImage = new ImageIcon(getClass().getResource("/splprac/boy.png")).getImage();
        footballImage = new ImageIcon(getClass().getResource("/splprac/football.png")).getImage();
        timer = new Timer(30, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.GREEN);
        g.fillRect(0, GROUND_LEVEL, getWidth(), getHeight() - GROUND_LEVEL);

        // Draw paths with different colors
        g.setColor(Color.RED);
        for (Point point : path1) {
            g.fillOval(point.x, point.y, 5, 5);
        }
        g.setColor(Color.BLUE);
        for (Point point : path2) {
            g.fillOval(point.x, point.y, 5, 5);
        }

        g.drawImage(boyImage, BOY_X - 50, GROUND_LEVEL - 100, 100, 100, this);
        g.drawImage(footballImage, (int) x1 - BALL_SIZE / 2, (int) y1 - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE, this);
        g.drawImage(footballImage, (int) x2 - BALL_SIZE / 2, (int) y2 - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (stopped) {
            timer.stop();
            return;
        }
        if(!soundPlayed){
            playSound("football.wav");
            soundPlayed=true;
        }
        time += 0.03;

        // Calculate new position for ball 1
        double newX1 = START_X + velocity1 * Math.cos(angle1) * time * 10;
        double newY1 = START_Y - (velocity1 * Math.sin(angle1) * time - 0.5 * g * time * time) * 10;
        if (newY1 >= GROUND_Y - BALL_SIZE / 2) {
            newY1 = GROUND_Y - BALL_SIZE / 2;
        }

        // Calculate new position for ball 2
        double newX2 = START_X + velocity2 * Math.cos(angle2) * time * 10;
        double newY2 = START_Y - (velocity2 * Math.sin(angle2) * time - 0.5 * g * time * time) * 10;
        if (newY2 >= GROUND_Y - BALL_SIZE / 2) {
            newY2 = GROUND_Y - BALL_SIZE / 2;
        }

        x1 = newX1;
        y1 = newY1;
        x2 = newX2;
        y2 = newY2;

        // Save points to paths
        path1.add(new Point((int) x1, (int) y1));
        path2.add(new Point((int) x2, (int) y2));

        double angle1Deg = Math.toDegrees(angle1);
        double angle2Deg = Math.toDegrees(angle2);
        double sumAngles = angle1Deg + angle2Deg;
        double diffAngles = Math.abs(angle1Deg - angle2Deg);
        double epsilonX = 5;

        // Stop condition logic
        if (y1 >= GROUND_Y - BALL_SIZE / 2 && y2 >= GROUND_Y - BALL_SIZE / 2) {
            if (Math.abs(sumAngles - 90) < 1e-6) {
                // Complementary angles: stop only if x positions close
                if (Math.abs(x1 - x2) < epsilonX) {
                    stopped = true;
                }
            } else if (diffAngles < 1e-6) {
                // Equal angles: stop when both on ground (velocities may differ)
                stopped = true;
            } else {
                // Other angles: stop when both landed
                stopped = true;
            }
        }

        repaint();
    }
    public void playSound(String fileName) {
        try {
            URL soundURL=getClass().getResource("/splprac/"+fileName);
            if(soundURL==null){
                System.err.println("Sound file not found: "+fileName);
                return;
            }
            AudioInputStream audioStream= AudioSystem.getAudioInputStream(soundURL);
            Clip clip=AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }catch(Exception e) {
            System.out.println("Error playing sound: "+e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            String v1 = JOptionPane.showInputDialog("Enter velocity 1 (m/s):");
            String a1 = JOptionPane.showInputDialog("Enter angle 1 (degrees):");
            String v2 = JOptionPane.showInputDialog("Enter velocity 2 (m/s):");
            String a2 = JOptionPane.showInputDialog("Enter angle 2 (degrees):");

            double velocity1 = Double.parseDouble(v1);
            double angle1 = Double.parseDouble(a1);
            double velocity2 = Double.parseDouble(v2);
            double angle2 = Double.parseDouble(a2);

            JFrame frame = new JFrame("Projectile Motion Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            proj sim = new proj(velocity1, angle1, velocity2, angle2);
            frame.add(sim);
            frame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter numbers.");
        }
    }
}
