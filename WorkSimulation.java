package splprac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WorkSimulation extends JPanel implements ActionListener {
    private Timer timer;
    private double mass, height, gravity;
    private double workDone;
    private int boxY, groundLevel;
    private int initialY;
    private Image boxImage;

    public WorkSimulation(double mass, double height, double gravity) {
        this.mass = mass;
        this.height = height;
        this.gravity = gravity;
        this.workDone = mass * gravity * height;

        groundLevel = 500;
        initialY = groundLevel;
        boxY = groundLevel;

        boxImage = new ImageIcon(getClass().getResource("box.png")).getImage();

        timer = new Timer(30, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Ground
        g.setColor(Color.GREEN);
        g.fillRect(0, groundLevel, getWidth(), getHeight() - groundLevel);

        // Work Done Text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Kalpurush", Font.BOLD, 24));
        g.drawString("Work Done (W = mgh) = " + String.format("%.2f", workDone) + " J", 50, 50);

        // Drawing the lifting box
        g.drawImage(boxImage, 300, boxY, 80, 80, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (boxY > groundLevel - (int) height * 10) {
            boxY -= 2;  // Lifting animation
            repaint();
        } else {
            timer.stop();
        }
    }

    public static void main(String[] args) {
        try {
            String massInput = JOptionPane.showInputDialog("Enter mass (kg): ");
            double mass = Double.parseDouble(massInput);

            String heightInput = JOptionPane.showInputDialog("Enter height (m): ");
            double height = Double.parseDouble(heightInput);

            double gravity = 9.8; // Constant gravitational acceleration

            JFrame frame = new JFrame("Work Done Simulation");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setResizable(false);

            WorkSimulation simulation = new WorkSimulation(mass, height, gravity);
            frame.add(simulation);
            frame.setVisible(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid numerical values.");
        }
    }
}
