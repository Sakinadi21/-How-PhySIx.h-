package splprac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PhysicsSimulatorSelection extends JFrame {
     CardLayout cardLayout;  //CardLayout eksate ank panel rakeh, but ekti panel dekha jai।
    JPanel mainPanel, selectionPanel; //mainPanel e shb panel rakar jonno main container
//selectionPanel e shb simulation button
    PhysicsSimulatorSelection() {
        setTitle("Physics Simulator");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cardLayout=new CardLayout();
        mainPanel=new JPanel(cardLayout);
        selectionPanel=new JPanel(new GridLayout(4, 2, 10, 10));
        selectionPanel.setBackground(Color.BLACK);
        String[] simulations = {
                "Projectile Motion","Dual Angle Trajectory" ,"Multiple Projectile",
                "Car Collision", "Work", "Shooting Gun",
                "Gravity","Pendulum", "Periodic Motion", "Ideal Gas"
        };
        for(String sim:simulations) {
            JButton button=new JButton(sim);
            button.setFont(new Font("Kalpurush", Font.BOLD, 18));
            button.setBackground(Color.BLUE);
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
//proti simulation er jnno button
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e){
                    if(sim.equals("Projectile Motion")) {
                        showProjectileInputDialog();
                    } else if(sim.equals("Car Collision")) {
                        showCarCollisionInputDialog();
                    } else if(sim.equals("Shooting Gun")) {
                        showGunShootingInputDialog();
                    }
                    else if(sim.equals("Dual Angle Trajectory")) {
                        showMultipleProjectileInputDialog();
                    } else if(sim.equals("Multiple Projectile")) {
                        showMultiProjectileSimulationDialog();
                    }
                    else if(sim.equals("Pendulum")) {
                        showPendulumInputDialog();
                    } else if(sim.equals("Work")) {
                        showWorkSimulationInputDialog();
                    }
                    else if(sim.equals("Gravity")) {
                        showAppleSimulationInputDialog();
                    } else if(sim.equals("Periodic Motion")) {
                        showPeriodicMotionInputDialog();
                    }
                    else if(sim.equals("Ideal Gas")) {
                        showIdealGasSimulationDialog();
                    }

                    //user button click e actionPerformed() er vitore method call hbe ।
                }
            });
            selectionPanel.add(button);
        }
        mainPanel.add(selectionPanel, "Selection");
// proti ti simulation er janno panel create
        for(String sim:simulations) {
            JPanel simPanel=new JPanel();
            simPanel.setBackground(Color.BLUE);
            simPanel.setLayout(new BorderLayout());
            JLabel label=new JLabel(sim,SwingConstants.CENTER);
            label.setFont(new Font("Kalpurush", Font.BOLD, 30));
            label.setForeground(Color.WHITE);
            simPanel.add(label,BorderLayout.CENTER);
            JButton backButton = new JButton("Back");
            backButton.setFont(new Font("Kalpurush", Font.BOLD, 20));
            backButton.setBackground(Color.BLACK);
            backButton.setForeground(Color.WHITE);
            backButton.setFocusPainted(false);
            backButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            backButton.addActionListener(e -> cardLayout.show(mainPanel, "Selection"));

            simPanel.add(backButton, BorderLayout.SOUTH);
            mainPanel.add(simPanel, sim);
        }
        add(mainPanel);
        cardLayout.show(mainPanel, "Selection");
    }

    private void showProjectileInputDialog() {
        String velocityInput=JOptionPane.showInputDialog("Enter initial velocity (m/s): ");
        String angleInput=JOptionPane.showInputDialog("Enter angle of projection (degrees): ");
        try {
            double velocity=Double.parseDouble(velocityInput);
            double angle=Double.parseDouble(angleInput);
            JPanel projectilePanel=new JPanel();
            projectilePanel.setLayout(new BorderLayout());
            projectilePanel.add(new ProjectileSimulator(velocity, angle), BorderLayout.CENTER);
            mainPanel.add(projectilePanel, "Projectile Motion");
            cardLayout.show(mainPanel, "Projectile Motion");
        } catch(NumberFormatException ex){
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for velocity and angle.");
        }
    }

    private void showCarCollisionInputDialog() {
        double mass1=Double.parseDouble(JOptionPane.showInputDialog("Enter mass of Car 1 (kg): "));
        double mass2=Double.parseDouble(JOptionPane.showInputDialog("Enter mass of Car 2 (kg): "));
        double speed1=Double.parseDouble(JOptionPane.showInputDialog("Enter speed of Car 1 : "));
        double speed2=Double.parseDouble(JOptionPane.showInputDialog("Enter speed of Car 2 : "));

        JFrame frame=new JFrame("Car Collision Simulation");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(1600,800);
        CarCollisionSimulator simulation=new CarCollisionSimulator(mass1, mass2, speed1, speed2);
        frame.add(simulation);
        frame.setVisible(true);
    }

    private void showMultipleProjectileInputDialog() {
        try {
            double velocity1 = Double.parseDouble(JOptionPane.showInputDialog("Enter velocity 1 (m/s):"));
            double angle1 = Double.parseDouble(JOptionPane.showInputDialog("Enter angle 1 (degrees):"));
            double velocity2 = Double.parseDouble(JOptionPane.showInputDialog("Enter velocity 2 (m/s):"));
            double angle2 = Double.parseDouble(JOptionPane.showInputDialog("Enter angle 2 (degrees):"));

            JFrame frame = new JFrame("Projectile Motion Simulation");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(800, 600);

            proj simulation = new proj(velocity1, angle1, velocity2, angle2);
            frame.add(simulation);
            frame.setVisible(true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter numeric values.");
        }
    }


    private void showGunShootingInputDialog() {
        String bulletSpeedInput = JOptionPane.showInputDialog("Enter bullet speed : ");
        try {
            double bulletSpeed=Double.parseDouble(bulletSpeedInput);
            GunShootingSimulator gunShootingSimulator=new GunShootingSimulator(bulletSpeed);
            JFrame frame=new JFrame("Gun Shooting Simulation");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setSize(1600, 800);
            frame.add(gunShootingSimulator);
            frame.setVisible(true);
//ekta time por firing
            Timer fireTimer=new Timer(3, e -> gunShootingSimulator.fireBullet());
            fireTimer.setRepeats(false);
            fireTimer.start();

        }catch (NumberFormatException ex)
        { JOptionPane.showMessageDialog(this, "Please enter a valid number for bullet speed.");
        }
    }

    private void showPeriodicMotionInputDialog() {
        String ampStr = JOptionPane.showInputDialog("Enter amplitude:");
        String freqStr = JOptionPane.showInputDialog("Enter frequency (Hz):");

        try {
            double amplitude = Double.parseDouble(ampStr);
            double frequency = Double.parseDouble(freqStr);

            JPanel motionPanel = new JPanel();
            motionPanel.setLayout(new BorderLayout());
            motionPanel.add(new PeriodicMotion(amplitude, frequency), BorderLayout.CENTER);

            mainPanel.add(motionPanel, "Periodic Motion");
            cardLayout.show(mainPanel, "Periodic Motion");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers for amplitude and frequency.");
        }
    }
    private void showPendulumInputDialog() {
        String lengthStr = JOptionPane.showInputDialog("Enter pendulum length (meters):");
        String angleStr = JOptionPane.showInputDialog("Enter max angle (degrees):");

        try {
            double length = Double.parseDouble(lengthStr);
            double maxAngle = Double.parseDouble(angleStr);

            JPanel pendulumPanel = new JPanel(new BorderLayout());
            pendulumPanel.add(new PendulumWithImages(length, maxAngle), BorderLayout.CENTER);

            mainPanel.add(pendulumPanel, "Pendulum Motion");
            cardLayout.show(mainPanel, "Pendulum Motion");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers for length and angle.");
        }
    }
    private void showWorkSimulationInputDialog() {
        String massInput = JOptionPane.showInputDialog("Enter mass (kg):");
        String heightInput = JOptionPane.showInputDialog("Enter height (m):");

        try {
            double mass = Double.parseDouble(massInput);
            double height = Double.parseDouble(heightInput);
            double gravity = 9.8;

            JPanel workPanel = new JPanel(new BorderLayout());
            workPanel.add(new WorkSimulation(mass, height, gravity), BorderLayout.CENTER);

            mainPanel.add(workPanel, "Work Simulation");
            cardLayout.show(mainPanel, "Work Simulation");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers for mass and height.");
        }
    }
    private void showAppleSimulationInputDialog() {
        String input = JOptionPane.showInputDialog("Enter initial velocity of apple:");

        try {
            double initialVelocity = Double.parseDouble(input);

            // Create a label for velocity display
            JLabel velocityLabel = new JLabel("Velocity: 0 px/frame", SwingConstants.CENTER);
            velocityLabel.setFont(new Font("Kalpurush", Font.BOLD, 16));

            // Create simulation panel with label
            JPanel applePanel = new JPanel(new BorderLayout());
            applePanel.add(velocityLabel, BorderLayout.NORTH);
            applePanel.add(new AppleFallingSimulation(initialVelocity, velocityLabel), BorderLayout.CENTER);

            mainPanel.add(applePanel, "Apple Falling Simulation");
            cardLayout.show(mainPanel, "Apple Falling Simulation");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a numeric value.");
        }
    }
    private void showIdealGasSimulationDialog() {
        try {
            String numStr = JOptionPane.showInputDialog("Enter number of particles:");
            int numParticles = Integer.parseInt(numStr);

            String speedStr = JOptionPane.showInputDialog("Enter speed of particles (e.g., 2):");
            int speed = Integer.parseInt(speedStr);

            IdealGasSimulation gasPanel = new IdealGasSimulation(numParticles, speed);
            mainPanel.add(gasPanel, "Ideal Gas Simulation");
            cardLayout.show(mainPanel, "Ideal Gas Simulation");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter valid numbers.");
        }
    }
    private void showMultiProjectileSimulationDialog() {
        try {
            String countStr = JOptionPane.showInputDialog(this, "How many projectiles?");
            int count = Integer.parseInt(countStr);

            java.util.List<Double> velocities = new ArrayList<>();
            java.util.List<Double> angles = new ArrayList<>();

            for (int i = 1; i <= count; i++) {
                String vStr = JOptionPane.showInputDialog(this, "Enter velocity " + i + " (m/s):");
                String aStr = JOptionPane.showInputDialog(this, "Enter angle " + i + " (degrees):");
                velocities.add(Double.parseDouble(vStr));
                angles.add(Double.parseDouble(aStr));
            }

            MultiProjectileSimulation simPanel = new MultiProjectileSimulation(velocities, angles);
            mainPanel.add(simPanel, "MultiProjectile");
            cardLayout.show(mainPanel, "MultiProjectile");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred: " + ex.getMessage());
        }
    }


    public static void main(String[] args) {
        PhysicsSimulatorSelection frame = new PhysicsSimulatorSelection();
        frame.setVisible(true);
    }
}
