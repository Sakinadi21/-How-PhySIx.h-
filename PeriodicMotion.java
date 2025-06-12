package splprac;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class PeriodicMotion extends JPanel implements ActionListener {
    private final int BALL_SIZE = 60;  // Adjust to your image size
    private final int CENTER_Y = 250;
    private final int PANEL_WIDTH = 800;
    private final int PANEL_HEIGHT = 500;

    private double amplitude; // max displacement from center
    private double frequency; // oscillations per second

    private double time = 0;
    private Timer timer;
    private Image bobImage;
    private boolean soundPlayed=false;
    public PeriodicMotion(double amplitude, double frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;

        // Load the bob image - update path to your image location
        bobImage = new ImageIcon(getClass().getResource("/splprac/gl.png")).getImage();

        timer = new Timer(20, this); // 50 FPS
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

        // draw center line
        g.setColor(Color.LIGHT_GRAY);
        g.drawLine(0, CENTER_Y, PANEL_WIDTH, CENTER_Y);

        // calculate x position using SHM formula: x = A * cos(2πft)
        double x = PANEL_WIDTH / 2 + amplitude * Math.cos(2 * Math.PI * frequency * time);

        // draw the bob image centered at (x, CENTER_Y)
        g.drawImage(bobImage, (int) x - BALL_SIZE / 2, CENTER_Y - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(!soundPlayed){
            playSound("periodic.wav");
            soundPlayed=true;
        }
        time += 0.02; // increment time in seconds (matches Timer delay / 1000)
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
        String ampStr = JOptionPane.showInputDialog("Enter amplitude :");
        String freqStr = JOptionPane.showInputDialog("Enter frequency (Hz):");

        try {
            double amplitude = Double.parseDouble(ampStr);
            double frequency = Double.parseDouble(freqStr);

            JFrame frame = new JFrame("Periodic Motion Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 500);

            PeriodicMotion panel = new PeriodicMotion(amplitude, frequency);
            frame.add(panel);
            frame.setVisible(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
        }
    }
}
