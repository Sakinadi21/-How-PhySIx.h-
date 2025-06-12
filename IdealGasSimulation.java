package splprac;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Random;

public class IdealGasSimulation extends JPanel implements ActionListener {
    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 500;
    private final int PARTICLE_SIZE = 30;

    private Timer timer;
    private Particle[] particles;
    private Image particleImage;

    private int wallCollisions = 0;  // for pressure
    private double totalSpeedSquared = 0; // for temperature
    private boolean soundPlayed=false;

    public IdealGasSimulation(int numParticles, int speed) {
        particleImage = new ImageIcon(getClass().getResource("/splprac/a.jpg")).getImage();

        particles = new Particle[numParticles];
        Random rand = new Random();

        for (int i = 0; i < numParticles; i++) {
            int x = rand.nextInt(PANEL_WIDTH - PARTICLE_SIZE);
            int y = rand.nextInt(PANEL_HEIGHT - PARTICLE_SIZE);
            int dx = rand.nextBoolean() ? speed : -speed;
            int dy = rand.nextBoolean() ? speed : -speed;
            particles[i] = new Particle(x, y, dx, dy);
        }

        timer = new Timer(20, this); // 50 FPS
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        // Border (container walls)
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, PANEL_WIDTH - 1, PANEL_HEIGHT - 1);

        // Draw particles
        for (Particle p : particles) {
            g.drawImage(particleImage, p.x, p.y, PARTICLE_SIZE, PARTICLE_SIZE, this);
        }

        // Display temperature and pressure
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Temperature: " + String.format("%.2f", calculateTemperature()), 20, 30);
        g.drawString("Pressure: " + wallCollisions, 20, 55);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        wallCollisions = 0;
        totalSpeedSquared = 0;
        if(!soundPlayed){
            playSound("ideal.wav");
            soundPlayed=true;
        }

        for (Particle p : particles) {
            p.x += p.dx;
            p.y += p.dy;

            // Add to total kinetic energy
            totalSpeedSquared += p.dx * p.dx + p.dy * p.dy;

            // Bounce off walls & count collisions
            if (p.x <= 0 || p.x >= PANEL_WIDTH - PARTICLE_SIZE) {
                p.dx *= -1;
                wallCollisions++;
            }
            if (p.y <= 0 || p.y >= PANEL_HEIGHT - PARTICLE_SIZE) {
                p.dy *= -1;
                wallCollisions++;
            }
        }
        repaint();
    }

    private double calculateTemperature() {
        return totalSpeedSquared / particles.length;
    }

    private static class Particle {
        int x, y, dx, dy;
        public Particle(int x, int y, int dx, int dy) {
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }
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
            String numStr = JOptionPane.showInputDialog("Enter number of particles:");
            int numParticles = Integer.parseInt(numStr);

            String speedStr = JOptionPane.showInputDialog("Enter speed of particles (e.g., 2):");
            int speed = Integer.parseInt(speedStr);

            JFrame frame = new JFrame("Ideal Gas Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);

            IdealGasSimulation panel = new IdealGasSimulation(numParticles, speed);
            frame.add(panel);
            frame.setVisible(true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
        }
    }
}
