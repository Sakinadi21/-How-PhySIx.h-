package splprac;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrontPage extends JFrame {
    FrontPage() {    //constructor niyeci
        initComponents();
    }
    public void initComponents() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0, 0, 1600, 1000);
        this.setTitle("Welcome");

        ImageIcon bgIcon=new ImageIcon(getClass().getResource("cover.jpg"));
        Image bgImage=bgIcon.getImage().getScaledInstance(1600, 1000, Image.SCALE_SMOOTH);
        JLabel backgroundLabel=new JLabel(new ImageIcon(bgImage));
        backgroundLabel.setLayout(new GridBagLayout());

        JLabel titleLabel =new JLabel("<How PhySIx.h>");
        JLabel nameLabel =new JLabel("Sakiba Belal");
        JLabel idLabel =new JLabel("BFH2325008F");
        Font titleFont = new Font("Kalpurush", Font.BOLD, 50);
        Font infoFont = new Font("Kalpurush", Font.BOLD, 30);

        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);

        nameLabel.setFont(infoFont);
        nameLabel.setForeground(Color.WHITE);

        idLabel.setFont(infoFont);
        idLabel.setForeground(Color.WHITE);

        JPanel textPanel=new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(nameLabel);
        textPanel.add(idLabel);

        JButton startButton=new JButton("Start");
        startButton.setFont(new Font("Kalpurush", Font.BOLD, 25));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(Color.BLUE);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new PhysicsSimulatorSelection().setVisible(true);
            }
        });
        textPanel.add(Box.createVerticalStrut(20));
        textPanel.add(startButton);
        backgroundLabel.add(textPanel);
        this.setContentPane(backgroundLabel);
    }
    public static void main(String[] args) {
        FrontPage frame = new FrontPage();
        frame.setVisible(true);
    }
}
