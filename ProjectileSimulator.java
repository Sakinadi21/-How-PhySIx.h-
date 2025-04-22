package splprac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.net.URL;
import java.util.ArrayList;

public class ProjectileSimulator extends JPanel implements ActionListener {
    private Timer timer;
    private double angle;
    private double velocity;
    private double time;
    private double x,y;
    private final double g=9.8;
    private final int GROUND_LEVEL=500;
    private final int BALL_SIZE=20;
    private final int BOY_X=150;
    private final int FOOT_Y=GROUND_LEVEL-40;
    private ArrayList<Point>path;
    private Image boyImage;
    private Image footballImage;
    private boolean soundPlayed=false;

    public ProjectileSimulator(double velocity,double angle) { //constructor
        this.velocity=velocity;
        this.angle=Math.toRadians(angle);
        this.time=0;
        this.x=BOY_X+40;
        this.y=FOOT_Y;
        this.path=new ArrayList<>();
        boyImage=new ImageIcon(getClass().getResource("/splprac/boy.png")).getImage();
        footballImage=new ImageIcon(getClass().getResource("/splprac/football.png")).getImage();
        timer=new Timer(30,this);
        timer.start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(Color.GREEN);
        g.fillRect(0,GROUND_LEVEL,getWidth(),getHeight()-GROUND_LEVEL);
        g.setColor(Color.GRAY);
        for(Point point:path){
            g.fillOval(point.x,point.y,5,5);
        }
        g.drawImage(boyImage,BOY_X-50,GROUND_LEVEL-100,100,100,this);
        g.drawImage(footballImage,(int)x-BALL_SIZE/2,(int)y-BALL_SIZE/2,BALL_SIZE,BALL_SIZE,this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        time+=0.03;
        x=BOY_X+40+velocity*Math.cos(angle)*time*10;
        y=FOOT_Y-(velocity*Math.sin(angle)*time-0.5*g*time*time)*10;
        path.add(new Point((int)x,(int)y));
        if(!soundPlayed){
            playSound("football.wav");
            soundPlayed=true;
        }
        if(y>=GROUND_LEVEL-BALL_SIZE/2) {
            timer.stop();
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
            AudioInputStream audioStream=AudioSystem.getAudioInputStream(soundURL);
            Clip clip=AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }catch(Exception e) {
            System.out.println("Error playing sound: "+e.getMessage());
        }
    }
    public static void main(String[] args){
        String velocityInput=JOptionPane.showInputDialog("Enter initial velocity (m/s): ");
        double velocity=Double.parseDouble(velocityInput);
        String angleInput=JOptionPane.showInputDialog("Enter angle of projection (degrees): ");
        double angle=Double.parseDouble(angleInput);
        JFrame frame=new JFrame("Projectile Motion Simulation with Sound");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 800);
        ProjectileSimulator simulation=new ProjectileSimulator(velocity, angle);
        frame.add(simulation);
        frame.setVisible(true);
    }
}
