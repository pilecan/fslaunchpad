package com.cfg.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MyClock {
	
	//http://stackoverflow.com/questions/18909203/jlabel-text-gets-overwritten-on-transparent-bg

    public static void main(String[] args) {
        new MyClock();
    }

    public MyClock() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {

                final DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                final JLabel label = new JLabel(df.format(new Date()));
        		Font labelFont = label.getFont();
        		label.setFont(new Font(labelFont.getName(), Font.ITALIC, 18));
        		label.setForeground(Color.yellow);
        		
                Timer timer = new Timer(500, new ActionListener() {
                   public void actionPerformed(ActionEvent e) {
                        label.setText(df.format(new Date()));
                    }
                });
                timer.start();

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.setUndecorated(true);
                frame.setBackground(new Color(0, 255, 0, 0));
                frame.add(label);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}