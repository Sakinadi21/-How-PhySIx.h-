package splprac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class WellEnergySimulation extends JPanel implements ActionListener {
    private Timer timer;
    private double mass, depth;
    private double gravitationalAcceleration = 9.81;
    private int objectY, groundLevel, wellDepthPixel;
    private Image wellImage, ballImage;
    private int[] heights = {0, 0, 0}; // h, h/2, h/4
    private int currentHeightIndex = 0;

    public WellEnergySimulation(double mass, double depth) {
        this.mass = mass;
        this.depth = depth;

        groundLevel = 300;  // Set ground level at the bottom
        wellDepthPixel = (int) (depth * 10);  // Scale depth for visualization
        objectY = groundLevel + wellDepthPixel;  // Start at the bottom of the well

        // Load images (ensure these paths are correct)
        wellImage = new ImageIcon("well_image.jpg").getImage(); // Replace with your well image path
        ballImage = new ImageIcon("football.png").getImage(); // Replace with your ball image path

        // Set up the timer to refresh the simulation every 50 ms
        timer = new Timer(50, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw Well Image
        g.drawImage(wellImage, 250, groundLevel - wellDepthPixel, 100, wellDepthPixel, this);

        // Draw Ball Image (Object)
        g.drawImage(ballImage, 275, objectY, 30, 30, this);

        // Heights (h, h/2, h/4) lines
        g.setColor(Color.BLACK);
        for (int h : heights) {
            g.drawLine(250, groundLevel + h, 350, groundLevel + h);  // Draw height markers
        }

        // Energy Calculation
        double heightMeters = (depth - (objectY - groundLevel) / 10.0); // Convert to meters
        double potentialEnergy = mass * gravitationalAcceleration * heightMeters;
        g.drawString("PE = " + String.format("%.2f", potentialEnergy) + " J", 50, 50);
        g.drawString("Current Height: " + String.format("%.2f", heightMeters) + " m", 50, 70);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Lift object by decreasing objectY until it reaches the next height
        if (objectY > groundLevel + heights[currentHeightIndex]) {
            objectY -= 2; // Move object upwards
        } else {
            currentHeightIndex++; // Move to the next height
            if (currentHeightIndex >= heights.length) {
                timer.stop(); // Stop the timer once all heights have been reached
            }
        }

        repaint(); // Refresh the display
    }

    public static void main(String[] args) {
        try {
            // Prompt user for mass and depth inputs
            double mass = Double.parseDouble(JOptionPane.showInputDialog("Enter object mass (kg):"));
            double depth = Double.parseDouble(JOptionPane.showInputDialog("Enter well depth (m):"));

            // Create the JFrame for the simulation
            JFrame frame = new JFrame("Well Energy Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 600);
            frame.setResizable(false);

            // Create simulation panel and add to the frame
            WellEnergySimulation simulation = new WellEnergySimulation(mass, depth);
            frame.add(simulation);
            frame.setVisible(true);

        } catch (NumberFormatException ex) {
            // Handle input error
            JOptionPane.showMessageDialog(null, "Please enter valid numerical values.");
        }
    }
}
