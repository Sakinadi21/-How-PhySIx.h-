package splprac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.net.URL;

public class CarCollisionSimulator extends JPanel implements ActionListener {
    private Timer timer;
    private double car1X, car2X;
    private final int ROAD_Y = 500;
    private final int CAR_WIDTH = 100;
    private final int CAR_HEIGHT = 50;
    private final int START_POS1 = 100;
    private final int START_POS2 = 1300;
    private double speed1, speed2;
    private double mass1, mass2;
    private boolean collisionOccurred = false;
    private boolean soundPlayed = false;
    private Image car1Image, car2Image;

    public CarCollisionSimulator(double mass1, double mass2, double speed1, double speed2) {
        this.mass1 = mass1;
        this.mass2 = mass2;
        this.speed1 = speed1;
        this.speed2 = speed2;
        this.car1X = START_POS1;
        this.car2X = START_POS2;
        // image normally upload pera, class r getResource dewa lagbe
        car1Image = new ImageIcon(getClass().getResource("/splprac/car2..png")).getImage();
        car2Image = new ImageIcon(getClass().getResource("/splprac/car1.png")).getImage();
        timer = new Timer(30, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Sky background
        g.setColor(new Color(135, 206, 250));
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw trees in the background
        Image treeImage = new ImageIcon(getClass().getResource("/splprac/roadtree.png")).getImage();
        int treeY = ROAD_Y - 150; // position trees above the road
        for (int x = 50; x < getWidth(); x += 300) {
            g.drawImage(treeImage, x, treeY, 100, 150, this);
        }

        // Road and markings
        g.setColor(new Color(50, 50, 50));
        g.fillRect(0, ROAD_Y, getWidth(), 200);
        g.setColor(Color.YELLOW);
        g.fillRect(0, ROAD_Y + 10, getWidth(), 5);
        g.fillRect(0, ROAD_Y + 185, getWidth(), 5);
        g.setColor(Color.WHITE);
        for (int i = 0; i < getWidth(); i += 50) {
            g.fillRect(i, ROAD_Y + 95, 30, 5);
        }

        // Draw cars
        g.drawImage(car1Image, (int) car1X, ROAD_Y + 75, CAR_WIDTH, CAR_HEIGHT, this);
        g.drawImage(car2Image, (int) car2X, ROAD_Y + 75, CAR_WIDTH, CAR_HEIGHT, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!collisionOccurred) {
            car1X += speed1;
            car2X -= speed2;
            if (car1X + CAR_WIDTH >= car2X) {
                collisionOccurred = true;
                if (!soundPlayed) {
                    playSound("crash.wav");
                    soundPlayed = true;
                }
                double finalVelocity = (mass1 * speed1 + mass2 * (-speed2)) / (mass1 + mass2);
                speed1 = finalVelocity;
                speed2 = finalVelocity;
            }
        } else {
            car1X += speed1;
            car2X += speed1;
        }
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
        double mass1 = Double.parseDouble(JOptionPane.showInputDialog("Enter mass of Car 1 (kg): "));
        double mass2 = Double.parseDouble(JOptionPane.showInputDialog("Enter mass of Car 2 (kg): "));
        double speed1 = Double.parseDouble(JOptionPane.showInputDialog("Enter speed of Car 1 : "));
        double speed2 = Double.parseDouble(JOptionPane.showInputDialog("Enter speed of Car 2 : "));

        JFrame frame = new JFrame("Car Collision Simulation with Sound");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800);
        CarCollisionSimulator simulation = new CarCollisionSimulator(mass1, mass2, speed1, speed2);
        frame.add(simulation);
        frame.setVisible(true);
    }
}
