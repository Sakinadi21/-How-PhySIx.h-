package splprac;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.net.URL;

public class GunShootingSimulator extends JPanel implements ActionListener {
    private Timer timer;
    private double bulletX,gunX;
    private final int GUN_Y=500;
    private final int BULLET_WIDTH=30;
    private final int BULLET_HEIGHT=10;
    private boolean bulletFired=false;
    private double bulletSpeed;
    private double recoilSpeed;
    private Image gunImage,bulletImage;
    private boolean soundPlayed=false;
    private final double GUN_MASS=50;
    private final double BULLET_MASS=5;

    public GunShootingSimulator(double bulletSpeed) {
        this.bulletSpeed=bulletSpeed;
        this.gunX=100;
        this.bulletX=gunX;
        this.recoilSpeed=-(BULLET_MASS*bulletSpeed)/GUN_MASS;
        gunImage=new ImageIcon(getClass().getResource("/splprac/gun.png")).getImage();
        bulletImage=new ImageIcon(getClass().getResource("/splprac/bullet.png")).getImage();
        timer=new Timer(30,this);
        timer.start();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(135,206,250));
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(Color.GREEN);
        g.fillRect(0,GUN_Y+50,getWidth(),getHeight()-GUN_Y-50);
        g.drawImage(gunImage,(int)gunX,GUN_Y,100,50,this);
        if(bulletFired){
            g.drawImage(bulletImage,(int)bulletX,GUN_Y+15,BULLET_WIDTH,BULLET_HEIGHT,this);
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(bulletFired){
            bulletX+=bulletSpeed;
            gunX+=recoilSpeed;
            if(!soundPlayed){
                playSound("gun.wav");
                soundPlayed=true;
            }
            if(bulletX>getWidth()) {
                bulletFired=false;
                bulletX=gunX=100;
                soundPlayed=false;
            }
        }
        repaint();
    }
    public void fireBullet(){
        bulletFired=true;
    }
    public void playSound(String fileName) {
        try {
            URL soundURL=getClass().getResource("/splprac/"+fileName);
            if (soundURL==null){
                System.err.println("Sound file not found: "+fileName);
                return;
            }
            AudioInputStream audioStream=AudioSystem.getAudioInputStream(soundURL);
            Clip clip=AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }catch(Exception e){
            System.out.println("Error playing sound: "+e.getMessage());
        }
    }
    public static void main(String[] args) {
        double bulletSpeed=Double.parseDouble(JOptionPane.showInputDialog("Enter bullet speed : "));
        JFrame frame=new JFrame("Gun Shooting Simulation with Recoil and Sound");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600,800);
        GunShootingSimulator simulation=new GunShootingSimulator(bulletSpeed);
        frame.add(simulation);
        frame.setVisible(true);
        Timer fireTimer=new Timer(1000,e->simulation.fireBullet());
        fireTimer.setRepeats(false);
        fireTimer.start();
    }
}
