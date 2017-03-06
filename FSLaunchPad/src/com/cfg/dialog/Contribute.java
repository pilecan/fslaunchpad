package com.cfg.dialog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.cfg.common.Info;
import com.cfg.factory.LauncherFactory;
import com.cfg.file.ManageConfigFile;
import com.cfg.model.Status;
import com.cfg.net.ManageStatus;
import com.cfg.util.Util;

public class Contribute implements Info{
	  private Properties prop;
	  private int numColor ;
	  private Status status;
	  private ManageConfigFile manageConfigFile;
	  private int cpt;
	  public Contribute(Status status, int numColor, ManageConfigFile configFile) {
		  this.prop = configFile.getProp();
		  this.numColor = numColor;
		  this.status = status;
		  this.manageConfigFile = configFile;
		  this.cpt = 0;
      }
	  
	  public void callDialog(){
		 Random randomGenerator = new Random();
		 int randomInt = randomGenerator.nextInt(2);
		 if (status.getCode() == 66 && cpt < 4 && randomInt == 1){
			  Message66();
			  cpt++;
		 }	  
	  }
	  
	   private void Message66() {
	      
	      GridBagConstraints constLabel = new GridBagConstraints();
          constLabel.fill = GridBagConstraints.VERTICAL;  
          constLabel.anchor = GridBagConstraints.WEST;		   
		 
          JPanel panel = new JPanel(new GridBagLayout());
	
		   JLabel message66 = getLabel(status.getMessage());
		   
		   constLabel.gridx = 0;
           constLabel.gridy = 0;
           panel.add(message66,constLabel);

		   JLabel message = getLabel(prop.getProperty("contrib.message"));
           constLabel.gridy = 1;
           panel.add(message,constLabel);

           JPanel panelfield = new JPanel();
           panelfield.add(getLabel(prop.getProperty("contrib.label")+" "));
		   JTextField keyField = new JTextField();
	       Dimension searchSize = keyField.getPreferredSize();
	       searchSize.width = 200;
	       keyField.setPreferredSize(searchSize);
	       panelfield.add(keyField);
           constLabel.gridy = 2;
	       panel.add(panelfield,constLabel);

	       JButton buttonPaypal= new JButton(prop.getProperty("contrib.paypal"));
	        buttonPaypal.addActionListener(new CallFslaunchpadListener());
	       
		   Object[] buttons = {prop.getProperty("contrib.send"),buttonPaypal,prop.getProperty("contrib.continue")};
		   int returnchoice = JOptionPane.showOptionDialog(null, panel,prop.getProperty("contrib.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
		   
		   if (returnchoice == 0  ){
				LauncherFactory factory = LauncherFactory.getInstance();
				factory.createLauncher(keyField.getText().trim(),manageConfigFile.getFsRoot(),manageConfigFile.getVersion(),manageConfigFile.getPrefs().getProperty("langue"),manageConfigFile.getPrefs().getProperty("sim"));
				
				status = ManageStatus.getWebStatus("valid/", factory.getJson());
				JOptionPane.showMessageDialog(null, getLabel(prop.getProperty(status.getMessage())));
				
				if (status.getCode() == 69 && !"".equals(keyField.getText().trim())){
					System.exit(0);
				}
		   }
		  
	    }
	   
	    private JLabel getLabel(String text){
	    	JLabel label = new JLabel();
	    	label.setText(text);
	    	label.setForeground(colorForground[numColor]);
	    	label.setFont(fontText);
	    	return label;
	    }
	    
		public class CallFslaunchpadListener implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, getLabel(prop.getProperty("contrib.pay.message")+"<br>Code : ("+status.getKey()+")</html>"));
			    Util.openWebpage("?lang="+manageConfigFile.getPrefs().getProperty("langue"), 4);
			}    
		}



}
