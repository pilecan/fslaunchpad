package com.cfg.dialog;

import static java.awt.GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSPARENT;
import static java.awt.GraphicsDevice.WindowTranslucency.TRANSLUCENT;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.cfg.common.Info;
import com.cfg.util.Util;

public class AboutFsl extends JFrame implements Info {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JFrame parent;
	private JPanel basePanel;

	public AboutFsl(final JFrame parent, final Properties prop,final int numColor, final JPanel basePanel,final String version) {
        super("About FSL");
        
        Util.setIcon(this, imageLogo);
        this.parent = parent;
        this.basePanel = basePanel;
        basePanel.setVisible(false);
	    basePanel.setVisible(false);
	    setAlwaysOnTop(true);
	    parent.setEnabled(false);
	    
	    
       
  	    addWindowListener(new WindowAdapter() {
           @Override
           public void windowClosing(WindowEvent e) {
        	   quitAbout();
           }
        });
        
        // Determine what the GraphicsDevice can support.
        GraphicsEnvironment ge = 
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        final boolean isTranslucencySupported = 
            gd.isWindowTranslucencySupported(TRANSLUCENT);

        //If shaped windows aren't supported, exit.
        if (!gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT)) {
            System.err.println("Shaped windows are not supported");
            //System.exit(0);
        }

        if (!isTranslucencySupported) {
            System.out.println(
                "Translucency is not supported, creating an opaque window");
        }

        // Create the GUI on the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                setLayout(new GridBagLayout());
                GridBagConstraints constPic = new GridBagConstraints();
                constPic.fill = GridBagConstraints.HORIZONTAL;

                 addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        setShape(new Ellipse2D.Double(0,0,getWidth(),getHeight()));
                    }
                });

                setUndecorated(true);
                setSize(300,240);
                setLocationRelativeTo(parent);
                
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JPanel panel = new JPanel(new GridBagLayout());
                GridBagConstraints constLabel = new GridBagConstraints();
                constLabel.fill = GridBagConstraints.VERTICAL;  
                constLabel.anchor = GridBagConstraints.WEST;
                
                constPic.weighty = 0.1;
                constPic.gridx = 0;
                constPic.gridy = 0;
                
                add(Util.setMoi(),constPic);
                
                constLabel.gridx = 0;
                constLabel.gridy = 0;
                panel.add(setLabel(" Pierre Legault", numColor),constLabel);

                constLabel.gridy = 1;
                panel.add(setLabel(" Montreal, Canada", numColor),constLabel);

                constLabel.gridy = 2;
                panel.add(setLabel(" fslauncher@gmail.com", numColor),constLabel);
 
                constLabel.gridy = 3;
                panel.add(setLabel(" FsLaunchPad (c)", numColor),constLabel);
              
                constLabel.gridy = 4;
                panel.add(setLabel(" Version "+version, numColor),constLabel);

                constLabel.gridy = 5;
                panel.add(new JLabel("         "),constLabel);
                
                JButton button = new JButton(prop.getProperty("about.thanks")); 
                button.addActionListener(new QuitListener());
                constLabel.gridy = 6;
                panel.add(button,constLabel);
                
                constPic.gridx = 1;
                add(panel,constPic);
                
                if (isTranslucencySupported) {
                    setOpacity(0.6f);
                }

                // Display the window.
                setVisible(true);
            }

        });
     }
	
	private JLabel setLabel(String text, int numColor){
        JLabel label = new JLabel(text);
        label.setForeground(colorForground[numColor]);
        return label;
		
	}
	private class QuitListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	quitAbout();
 	   }
	    
	    
	}
	private void quitAbout(){
        setVisible(false);
        dispose();
        parent.setEnabled(true);
        basePanel.setFocusable(true);
        basePanel.setVisible(true);
        basePanel.setEnabled(true);
        parent.setAlwaysOnTop(true);
        parent.setAlwaysOnTop(false);
		
	}
	

    public static void main(String[] args) {
       // ShapedWindowDemo sw = new ShapedWindowDemo();
    }
}