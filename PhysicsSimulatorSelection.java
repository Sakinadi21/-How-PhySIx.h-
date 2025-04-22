package splprac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                "Projectile Motion", "Car Collision", "Work", "Shooting Gun",
                "Gravity", "Periodic Motion", "Ideal Gas"
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
    public static void main(String[] args) {
        PhysicsSimulatorSelection frame = new PhysicsSimulatorSelection();
        frame.setVisible(true);
    }
}
