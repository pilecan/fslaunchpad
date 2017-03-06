package com.cfg.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

import com.cfg.common.Info;
import com.cfg.file.ManageConfigFile;
import com.cfg.model.Area;
import com.cfg.util.Util;

public class ReorderScenery extends JDialog implements Info {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private File file ;
	
	
	private Map<String, List<Object>> mapListArea;
	
	private ManageConfigFile manageConfigFile;
	
	
	@SuppressWarnings("unused")
	private String actionButton;

	@SuppressWarnings("unused")
	private String previousArea;
	
	private JList<String> areaList;
    private DefaultListModel<String> areaModel;
    private String[] listElements;

	private JList<String> orderList;
    private DefaultListModel<String> orderModel;
    
    
    private JPanel areaPane;
    private JSplitPane splitPaneMove; 
    
    private boolean isDone;
    
    private int numColor;
    
    private Properties prop;
   
	private JPanel buildContentPane(Properties prop) {
		JPanel panel = new JPanel(new GridLayout(10,10,10,10));
		panel.setLayout(new FlowLayout());

		JButton bouton = new JButton(prop.getProperty("modif.btn.save"));
		bouton.addActionListener(new MoveListener());
		panel.add(bouton);

		JButton bouton2 = new JButton(prop.getProperty("modif.btn.quit"));
		bouton2.addActionListener(new QuitListener());
		panel.add(bouton2);
		
		setSize(440, 40);
		setResizable(false);
		//setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		return panel;
	}
	
	
  
/**
 * Contructor
 * 
 * @param jframe
 * @param title
 * @param manageConfigFile
 * @param actionButton
 */
  public ReorderScenery(JFrame jframe, Properties prop, ManageConfigFile manageConfigFile,String actionButton, int numColor) {
    super(jframe, prop.getProperty("reorg.title"));
    
    this.numColor = numColor;
    this.actionButton = actionButton;
    this.manageConfigFile = manageConfigFile;
    this.prop = prop;
    
    initMapWork(manageConfigFile);
    
    mapListArea = new TreeMap<String, List<Object>>(manageConfigFile.getMapListArea());
    
    listElements = Arrays.copyOf(manageConfigFile.getBothSide().keySet().toArray(), manageConfigFile.getBothSide().keySet().toArray().length, String[].class);
    Arrays.sort(listElements);
    
    areaModel = createStringListModel(listElements);
    areaList = new JList<String>(areaModel);

    MyMouseAdaptor myMouseAdaptor = new MyMouseAdaptor();
    areaList.addMouseListener(myMouseAdaptor);
    areaList.addMouseMotionListener(myMouseAdaptor);
    
    JPanel orderPane = new JPanel();
    for (int i = 0; i < listElements.length; i++) {
    	//listElements[i] = String.valueOf(Util.addZero(i+1));
    	listElements[i] = listElements[i].substring(0, listElements[i].indexOf("-"));
	}

    orderModel = createStringOrderModel(listElements);
    orderList = new JList<String>(orderModel);
    orderList.setEnabled(false);
	orderPane.add(orderList);
    
    areaPane = new JPanel();
    areaPane.add(areaList);
    Dimension listSize = areaPane.getPreferredSize();
    listSize.width = 440;
    areaPane.setPreferredSize(listSize);
    areaPane.setBackground(Color.WHITE);
    
    
    listSize = orderPane.getPreferredSize();
    listSize.width = 25;
    
  //  orderPane.setLayout(new BorderLayout());
    orderPane.setPreferredSize(listSize);
    orderPane.setBackground(Color.WHITE);
    orderPane.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
   
    
    JPanel panelScenery = new JPanel();
    panelScenery.setLayout(new BorderLayout());
    panelScenery.add( orderPane, BorderLayout.WEST );
    panelScenery.add( areaPane, BorderLayout.EAST );
    panelScenery.setBackground(Color.WHITE);

    splitPaneMove = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, new JScrollPane(panelScenery), buildContentPane(prop)){
    	   /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private final int location = 335;
    	    {
    	        setDividerLocation( location );
    	    }
    	    @Override
    	    public int getDividerLocation() {
    	        return location ;
    	    }
    	    @Override
    	    public int getLastDividerLocation() {
    	        return location ;
    	    }
   	
    };
    
    splitPaneMove.setDividerSize(0);
   
    getContentPane().add(splitPaneMove);
    setSize(440, 420);
    getRootPane().setOpaque(true);
    getRootPane().setBorder(BorderFactory.createEmptyBorder(
            20, //top
            30, //left
            0, //bottom
            30) //right
            );


    getRootPane().setBackground(colorBackground[numColor]);
    setLocationRelativeTo(jframe);
    setModal(true);
    setVisible(true);
    setResizable(false);
    

  }
  
  private class MoveListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	 		 if (JOptionPane.showConfirmDialog(null,  getLabel(prop.getProperty("reorg.mess")),prop.getProperty("reorg.title"), JOptionPane.WARNING_MESSAGE) == 0){
	 			manageConfigFile.getMapListArea().clear();
	 			manageConfigFile.getMapListArea().putAll(mapListArea);
	 			manageConfigFile.reorderScenery(areaModel);
	 			//System.out.println("Order changed");
	 			isDone= true;
	 		 }
	    	
          setVisible(false);
          dispose();
	   }
}

private class QuitListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
          setVisible(false);
          dispose();
	   }
}

private JLabel getLabel(String text){
	  	JLabel label = new JLabel();
	  	label.setText(text);
	  	label.setForeground(colorForground[numColor]);
	  	return label;
	  }

  private void initMapWork(ManageConfigFile manageConfigFile){
		manageConfigFile.getMapWork().clear();
		//mapWork.putAll(manageConfigFile.getMapIndexed());
		
		for ( Area area:manageConfigFile.getMapAllScenery().values() )
		{
			manageConfigFile.getMapWork().put(area.getTitle(),area);
			//System.out.println(aera.getTitle());
		}
		
		//System.out.println(mapWork.size());
		//System.out.println();
  }

  private class MyMouseAdaptor extends MouseInputAdapter {
      private boolean mouseDragging = false;
      private int dragSourceIndex;
      
/*      @Override
      public void mouseClicked(MouseEvent e) {
    	  System.out.println("click");
    	  int currentIndex = areaList.locationToIndex(e.getPoint());
    	  System.out.println(orderModel.get(currentIndex)+"-"+areaModel.get(currentIndex));
    	  System.out.println(manageConfigFile.getMapWork().get(areaModel.get(currentIndex).toString()));
	  }
*/      
      @Override
      public void mousePressed(MouseEvent e) {
          if (SwingUtilities.isLeftMouseButton(e)) {
              dragSourceIndex = areaList.getSelectedIndex();
              previousArea = areaModel.get(dragSourceIndex);
              mouseDragging = true;
         }
      }

      @Override
      public void mouseReleased(MouseEvent e) {
          mouseDragging = false;
  }
      
      
  

      @Override
      public void mouseMoved(MouseEvent e) {
        //   System.out.println("move");
     }
      
      @Override
      public void mouseEntered(MouseEvent e) {
         //  System.out.println("enter");
     }
      

      @Override
      public void mouseDragged(MouseEvent e) {
          if (mouseDragging) {
              int currentIndex = areaList.locationToIndex(e.getPoint());
              if (currentIndex != dragSourceIndex) {
				  int dragTargetIndex;
				  dragTargetIndex = areaList.getSelectedIndex();
				  String dragElement = areaModel.get(dragSourceIndex);
				  areaModel.remove(dragSourceIndex);
				  areaModel.add(dragTargetIndex, dragElement);
				 
				  dragSourceIndex = currentIndex;
              }
          }
      }
      
 
  }
  
  private DefaultListModel<String> createStringListModel(String[] listElements) {
      
      DefaultListModel<String> listModel = new DefaultListModel<String>();
      for (String element : listElements) {
          listModel.addElement(element.substring(element.indexOf("-")+1));
      }
      return listModel;
  }

  
  private DefaultListModel<String> createStringOrderModel(String[] listElements) {
      
      DefaultListModel<String> listModel = new DefaultListModel<String>();
      for (String element : listElements) {
          listModel.addElement(element);
      }
      return listModel;
  }

  
  
  public static void main(String args[]) {
   // new SelectDirectoryTree(new JFrame(),"C:/Program Files (x86)/Microsoft Games/Microsoft Flight Simulator X/Addon Scenery","test");
    //new MoveFileTree("C:/Program Files (x86)","test");
  }
  

  
  
/**
 * @return the file
 */
public File getFile() {
	return file;
}



/**
 * @return the isDone
 */
public boolean isDone() {
	return isDone;
}



/**
 * @param isDone the isDone to set
 */
public void setDone(boolean isDone) {
	this.isDone = isDone;
}
  
}

