package splprac;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class PendulumWithImages extends JPanel implements ActionListener {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;

    private final double g = 9.8; // gravity

    private double length; // pendulum length in meters
    private double maxAngle; // max angle in radians

    private double period;
    private double time = 0;

    private Timer timer;

    private Image pivotImage;
    private Image bobImage;
    private boolean soundPlayed=false;

    public PendulumWithImages(double length, double maxAngle) {
        this.length = length;
        this.maxAngle = Math.toRadians(maxAngle);

        period = 2 * Math.PI * Math.sqrt(length / g);

        // Load images from resources
        pivotImage = new ImageIcon(getClass().getResource("/splprac/pivot.png")).getImage(); // replace with your path
        bobImage = new ImageIcon(getClass().getResource("/splprac/bob.png")).getImage();     // replace with your path

        timer = new Timer(20, this);
        timer.start();
    }

    private Image loadImage(String path) {
        URL url = getClass().getResource(path);
        if (url == null) {
            System.err.println("Image not found: " + path);
            return null;
        }
        return new ImageIcon(url).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 1. Clear background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

// 2. Pivot position
        int pivotX = WIDTH / 2;
        int pivotY = 100;

// 3. Calculate bob position
        double omega = 2 * Math.PI / period;
        double theta = maxAngle * Math.cos(omega * time);
        int lengthPixels = (int)(length * 200);
        int bobX = pivotX + (int)(lengthPixels * Math.sin(theta));
        int bobY = pivotY + (int)(lengthPixels * Math.cos(theta));

// 4. Draw rod from pivot to bob
        g.setColor(Color.BLACK);
        g.drawLine(pivotX, pivotY, bobX, bobY);

// 5. Draw pivot image after rod, so it appears "above" rod
        if (pivotImage != null) {
            int w = pivotImage.getWidth(this);
            int h = pivotImage.getHeight(this);
            g.drawImage(pivotImage, pivotX - w / 2, pivotY - h / 2, this);
        } else {
            g.setColor(Color.BLUE);
            g.fillOval(pivotX - 15, pivotY - 15, 30, 30);
        }

// 6. Then draw bob image after everything else
        if (bobImage != null) {
            int scaledWidth = 50;
            int scaledHeight = 50;
            g.drawImage(bobImage, bobX - scaledWidth / 2, bobY - scaledHeight / 2, scaledWidth, scaledHeight, this);
        } else {
            g.setColor(Color.RED);
            g.fillOval(bobX - 10, bobY - 10, 20, 20);
        }

// 7. Text Info (optional)
        g.setColor(Color.BLACK);
        g.drawString(String.format("Length: %.2f m", length), 10, 20);
        g.drawString(String.format("Max Angle: %.1f degrees", Math.toDegrees(maxAngle)), 10, 40);
        g.drawString(String.format("Period: %.2f s", period), 10, 60);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        time += 0.02;
        repaint();

        if(!soundPlayed){
            playSound("tick.wav");
            soundPlayed=true;
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
        String lengthStr = JOptionPane.showInputDialog("Enter pendulum length (meters):");
        String angleStr = JOptionPane.showInputDialog("Enter max angle (degrees):");

        try {
            double length = Double.parseDouble(lengthStr);
            double maxAngle = Double.parseDouble(angleStr);

            JFrame frame = new JFrame("Pendulum with Images");
            frame.setSize(1000, 1000);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            PendulumWithImages panel = new PendulumWithImages(length, maxAngle);
            frame.add(panel);
            frame.setVisible(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
        }
    }
}
