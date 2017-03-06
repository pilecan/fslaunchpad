package com.cfg.prog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.ItemSelectable;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ProgressMonitor;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.OceanTheme;

import com.cfg.common.Info;
import com.cfg.dialog.AboutFsl;
import com.cfg.dialog.Contribute;
import com.cfg.dialog.Launchpad;
import com.cfg.dialog.ReorderScenery;
import com.cfg.dialog.WaitMessage;
import com.cfg.factory.LauncherFactory;
import com.cfg.file.ManageConfigFile;
import com.cfg.file.ManageXMLFile;
import com.cfg.model.Area;
import com.cfg.model.Placemark;
import com.cfg.model.Status;
import com.cfg.net.ManageStatus;
import com.cfg.plan.CreateFSLPlan;
import com.cfg.plan.CreateFSLPlan.NoPoints;
import com.cfg.util.AlphaContainer;
import com.cfg.util.FileUtil;
import com.cfg.util.FormUtility;
import com.cfg.util.ImageFileView;
import com.cfg.util.ImageFilter;
import com.cfg.util.ImagePreview;
import com.cfg.util.InspectArea;
import com.cfg.util.SelectedListCellRenderer;
import com.cfg.util.Util;
import com.geo.util.Geoinfo;

public class FSLaunchPad extends JPanel implements Info{

  /**
	 * 
	 */
	
  private static int numColor = 0;
  private static FSLaunchPad fSLauncpad;	
  
  private static int bvd = 0;
  
  private static final long serialVersionUID = 1L;

  private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);

  private static final Insets INSETS_LABEL_SEARCH = new Insets(0, 20, 20, 0);

/*  private static final Insets INSETS_LABEL_SOURCE = new Insets(5, 30, 0, 0);
 
  private static final Insets INSETS_LABEL_DEST = new Insets(5, 0, 0, 30);
*/  
  private static final String ADD_BUTTON_LABEL = ">>";

  private static final String REMOVE_BUTTON_LABEL = "<<";

  private static final String DEFAULT_SOURCE_CHOICE_LABEL = "Addon Source";

  private static final String DEFAULT_DEST_CHOICE_LABEL = "Destination";
  
   private static JFrame jframe ;

  private static JPanel basePanel;

  private JLabel sourceLabel;

  private JList <String>sourceList;

  private SortedListModel sourceListModel;

  private JList <String>destList;

  private SortedListModel destListModel;

  private JLabel destLabel;

  private JButton addButton;

  private JButton removeButton;

  private JButton manageButton;

  private JButton flightPlanButton;

  private JButton preferenceButton;

  private JButton launchPadButton;

  private JButton saveButton;
  
  private JButton resetButton;

  private JButton searchSceneryButton;
  
  private JTextField searchText;
  
  private JCheckBox caseSensitive;
  private JCheckBox alphabOrder;
  private JCheckBox showDefaultScenery;
  
  private UIManager UI;
  
  private JPanel jpaneSearch;
  
  private JPanel jpaneLaunch;
  
  private static JMenu fileMenu;
  private static JMenuItem addItem;
  private static JMenuItem reorderItem;
  private static JMenuItem exitItem;
   private static JMenuItem saveItem;

  private static JMenu searchMenu;
  private static JMenuItem searchItem;
  
  private static JMenu langueMenu;
  private static JMenuItem langueItem;

  private static JMenu manageMenu;
  private static JMenuItem destItem;
  private static JMenuItem prefItem;

  private static JMenu helpMenu;
  private static JMenuItem helpItem;
  private static JMenuItem checkPlaneItem;
  private static JMenuItem aboutItem;
  private static JMenuItem contributeItem;
  
  private static ManageConfigFile manageConfigFile;
  
  private static ManageXMLFile manageXMLFile;
   
  private static Path currentRelativePath = Paths.get("");

  private static Status status;
  
  private Launchpad launchpad;
  
  private Properties prop;
  private Properties perfs;
  
  private Contribute contribute;

  private String currentLangue;
  
  private ProgressMonitor progressMonitor;
  private Timer timer;
  
  private CreateFSLPlan createFSLPlan;

  
  public FSLaunchPad() {
    initScreen();
  }

  public String getSourceChoicesTitle() {
    return sourceLabel.getText();
  }

  public void setSourceChoicesTitle(String newValue) {
    sourceLabel.setText(newValue);
  }

  public String getDestinationChoicesTitle() {
    return destLabel.getText();
  }

 
  public void clearSourceListModel() {
    sourceListModel.clear();
  }

  public void clearDestinationListModel() {
    destListModel.clear();
  }

  public void addSourceElements(ListModel <String>newValue) {
    fillListModel(sourceListModel, newValue);
  }

  public void setSourceElements(ListModel <String> newValue) {
    clearSourceListModel();
    addSourceElements(newValue);
  }

  public void addDestinationElements(ListModel<String> newValue) {
    fillListModel(destListModel, newValue);
  }

  private void fillListModel(SortedListModel model, ListModel <String>newValues) {
    int size = newValues.getSize();
    for (int i = 0; i < size; i++) {
      model.add(newValues.getElementAt(i));
    }
  }

  public void addSourceElements(Object newValue[]) {
    fillListModel(sourceListModel, newValue);
  }

  public void setSourceElements(Object newValue[]) {
    clearSourceListModel();
    addSourceElements(newValue);
  }

  public void addDestinationElements(Object newValue[]) {
    fillListModel(destListModel, newValue);
  }

  private void fillListModel(SortedListModel model, Object newValues[]) {
    model.addAll(newValues);
  }

  public Iterator<Object>  sourceIterator() {
    return sourceListModel.iterator();
  }

  public Iterator<Object> destinationIterator() {
    return destListModel.iterator();
  }

  public void setSourceCellRenderer(ListCellRenderer <String>newValue) {
    sourceList.setCellRenderer(newValue);
  }

  public ListCellRenderer<?> getSourceCellRenderer() {
    return sourceList.getCellRenderer();
  }

  public void setDestinationCellRenderer(ListCellRenderer <String>newValue) {
    destList.setCellRenderer(newValue);
  }

  public ListCellRenderer<?> getDestinationCellRenderer() {
    return destList.getCellRenderer();
  }

  public void setVisibleRowCount(int newValue) {
    sourceList.setVisibleRowCount(newValue);
    destList.setVisibleRowCount(newValue);
  }

  public int getVisibleRowCount() {
    return sourceList.getVisibleRowCount();
  }

  public void setSelectionBackground(Color newValue) {
    sourceList.setSelectionBackground(newValue);
    destList.setSelectionBackground(newValue);
  }

  public Color getSelectionBackground() {
    return sourceList.getSelectionBackground();
  }

  public void setSelectionForeground(Color newValue) {
    sourceList.setSelectionForeground(newValue);
    destList.setSelectionForeground(newValue);
  }

  public Color getSelectionForeground() {
    return sourceList.getSelectionForeground();
  }

  private void clearSourceSelected() {
    Object selected[] = sourceList.getSelectedValuesList().toArray();
    for (int i = selected.length - 1; i >= 0; --i) {
      sourceListModel.removeElement(selected[i]);
    }
    sourceList.getSelectionModel().clearSelection();
  }

  private void clearDestinationSelected() {
    Object selected[] = destList.getSelectedValuesList().toArray();
    for (int i = selected.length - 1; i >= 0; --i) {
      destListModel.removeElement(selected[i]);
    }
    destList.getSelectionModel().clearSelection();
  }

  private static final GridBagConstraints gbc;
  static {
      gbc = new GridBagConstraints();
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.weightx = 1.0;
      gbc.weighty = 1.0;
      gbc.fill = GridBagConstraints.BOTH;
      gbc.anchor = GridBagConstraints.NORTHWEST;
  }

  public static JPanel wrapInBackgroundImage(JComponent component,
          Icon backgroundIcon) {
      return wrapInBackgroundImage(
              component,
              backgroundIcon,
              JLabel.TOP,
              JLabel.LEADING);
  }
  
  /**
   * Wraps a Swing JComponent in a background image. The vertical and horizontal
   * alignment of background image can be specified using the alignment
   * contants from JLabel.
   *
   * @param component - to wrap in the a background image
   * @param backgroundIcon - the background image (Icon)
   * @param verticalAlignment - vertical alignment. See contants in JLabel.
   * @param horizontalAlignment - horizontal alignment. See contants in JLabel.
   * @return the wrapping JPanel
   */
  public static JPanel wrapInBackgroundImage(JComponent component,
          Icon backgroundIcon,
          int verticalAlignment,
          int horizontalAlignment) {
      
      // make the passed in swing component transparent
      component.setOpaque(false);
      
      // create wrapper JPanel
      JPanel backgroundPanel = new JPanel(new GridBagLayout());
      
      // add the passed in swing component first to ensure that it is in front
      backgroundPanel.add(component, gbc);
      
      // create a label to paint the background image
      JLabel backgroundImage = new JLabel(backgroundIcon);
      
      // set minimum and preferred sizes so that the size of the image
      // does not affect the layout size
      backgroundImage.setPreferredSize(new Dimension(1,1));
      backgroundImage.setMinimumSize(new Dimension(1,1));
      
      // align the image as specified.
      backgroundImage.setVerticalAlignment(verticalAlignment);
      backgroundImage.setHorizontalAlignment(horizontalAlignment);
      
      // add the background label
      backgroundPanel.add(backgroundImage, gbc);
      
      // return the wrapper
      return backgroundPanel;
  }

  /**
   * 
   *
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
private void initScreen() {
	UIManager.put("OptionPane.background", colorBackground[numColor]);
	UIManager.put("OptionPane.foreground", colorForground[numColor]);
	UIManager.put("Panel.background", colorBackground[numColor]);
    UIManager.put("Panel.foreground", colorForground[numColor]);
    
    prop = manageConfigFile.getProp();
    
    currentLangue = manageConfigFile.getPrefs().getProperty("langue");
    
    bvd = 99;
 
    //setBorder(BorderFactory.createEtchedBorder());
    setLayout(new GridBagLayout());
    
    basePanel = new JPanel();
    basePanel = this;
    
    setBackground(colorBackground[numColor]);
    
    setBorder(BorderFactory.createEmptyBorder(
            10, //top
            30, //left
            0, //bottom
            30) //right
            );
    
 
    sourceLabel = getLabel(DEFAULT_SOURCE_CHOICE_LABEL);
    
    sourceListModel = new SortedListModel();

    sourceList = new JList(sourceListModel);
    add(sourceLabel, new GridBagConstraints(0, 0, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    
    add(new JScrollPane(sourceList), new GridBagConstraints(0, 1, 1, 5, .025,
        1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        EMPTY_INSETS, 0, 0));

    addButton = new JButton(ADD_BUTTON_LABEL);
    add(addButton, new GridBagConstraints(1, 0, 1, 2, 0, .25,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    
    addButton.addActionListener(new AddListener());

    
    removeButton = new JButton(REMOVE_BUTTON_LABEL);
    add(removeButton, new GridBagConstraints(1, 2, 1, 2, 0, .25,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 5, 0, 5), 0, 0));
    removeButton.addActionListener(new RemoveListener());


    flightPlanButton = new JButton(prop.getProperty("flightPlan"));
    add(flightPlanButton, new GridBagConstraints(1, 4, 1, 2, 0, .25,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 5, 0, 5), 0, 0));
    flightPlanButton.setToolTipText(prop.getProperty("flightPlan.button.tip"));
    flightPlanButton.addActionListener(new FlightplanListener(jframe));
    
    preferenceButton = new JButton(prop.getProperty("preferences"));
    add(preferenceButton, new GridBagConstraints(1, 6, 1, 2, 0, .25,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 5, 0, 5), 0, 0));
    preferenceButton.addActionListener(new PreferenceListener(jframe));
    

 
    manageButton = new JButton(prop.getProperty("manage"));
    
    launchPadButton = new JButton(prop.getProperty("launch.pad"));
    launchPadButton.setToolTipText(prop.getProperty("launch.tip"));
    saveButton = new JButton(prop.getProperty("save"));
    saveButton.setToolTipText(prop.getProperty("save.tip"));
    //resetButton = new JButton(prop.getProperty("reset"));
    resetButton = new JButton("Gérer");
    resetButton.setToolTipText(prop.getProperty("reset.tip"));

    showDefaultScenery = new JCheckBox();
    showDefaultScenery.setSelected(true);
    showDefaultScenery.addItemListener(new ItemListener() {
        public void itemStateChanged(ItemEvent e) {
          resetArea();
        }
      });
    showDefaultScenery.setToolTipText(prop.getProperty("hide.tip"));
    showDefaultScenery.setBackground(new Color(255, 0, 0, 20));
    jframe.add( new AlphaContainer(showDefaultScenery));

    
    jpaneLaunch = new JPanel(); 
    jpaneLaunch.add(showDefaultScenery);
    jpaneLaunch.add(manageButton);
    jpaneLaunch.add(saveButton);
    jpaneLaunch.add(launchPadButton);
    
    jpaneLaunch.setBackground( new Color(255, 0, 0, 20) );
    jframe.add( new AlphaContainer(jpaneLaunch));
   
    //jpaneLaunch.setBackground(colorBack[numColor])
    add(jpaneLaunch, new GridBagConstraints(2, 6, 1, 2, 0, .25,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 10, 0, 10), 0, 0));

    manageButton.addActionListener(new ManageAreaListener());
    launchPadButton.addActionListener(new LauchpadListener());
    saveButton.addActionListener(new SaveNameListener());
    resetButton.addActionListener(new ManageAreaListener());

    Border border = BorderFactory.createLineBorder(Color.gray);
    
    
    alphabOrder = new JCheckBox();
    alphabOrder.setToolTipText(prop.getProperty("alpha.tip"));
    alphabOrder.setBackground(new Color(255, 0, 0, 20));
    jframe.add( new AlphaContainer(alphabOrder));
    
    alphabOrder.setSelected(false);
    alphabOrder.addItemListener(new ItemListener() {
    public void itemStateChanged(ItemEvent e) {
        	sourceListModel.clear();
        	manageConfigFile.initMapAlphabetic(alphabOrder.isSelected());
			addSourceElements(manageConfigFile.getMapSource().keySet().toArray());

    		clearSourceSelected();
			basePanel.setVisible(true);
         }
      });

    
    searchSceneryButton = new JButton(prop.getProperty("search"));
    searchSceneryButton.setToolTipText(prop.getProperty("search.tip"));
    
    searchText = new JTextField(10);
    searchText.setEnabled(true);
    searchText.setBorder(border);
    caseSensitive = new JCheckBox();
    caseSensitive.setToolTipText(prop.getProperty("case.tip"));
    caseSensitive.setBackground(new Color(255, 0, 0, 20));
    jframe.add( new AlphaContainer(caseSensitive));

    jpaneSearch = new JPanel(); 
    jpaneSearch.add(alphabOrder);
    jpaneSearch.add(searchSceneryButton);
    jpaneSearch.add(searchText); 
    jpaneSearch.add(caseSensitive);
    //jpaneSearch.setBackground(colorBack[numColor]);
    searchSceneryButton.addActionListener(new SearchSceneryListener());
    searchText.addActionListener(new SearchSceneryListener());

    jpaneSearch.setBackground( new Color(255, 0, 0, 20) );
    jframe.add( new AlphaContainer(jpaneSearch));

    add(jpaneSearch, new GridBagConstraints(0, 6, 1, 2, 0, .25,
        GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
            0, 10, 0, 10), 0, 0));
    
    JPanel panelLabel = new JPanel();
    panelLabel.setSize(40, 20);
    destLabel = getLabel(DEFAULT_DEST_CHOICE_LABEL);
    panelLabel.add(destLabel);
    destLabel.setSize(40, 30);
    
    if (!"".equals(manageConfigFile.getAreaName())){
    	destLabel.setText(DEFAULT_DEST_CHOICE_LABEL+" "+manageConfigFile.getAreaName());
    }
    destListModel = new SortedListModel();
    destList = new JList(destListModel);

    setListColor();
    
    destList.addMouseListener(mouseListener);
    sourceList.addMouseListener(mouseListener);  
    
    add(destLabel, new GridBagConstraints(2, 0, 1, 1, 0, 0,
        GridBagConstraints.CENTER, GridBagConstraints.NONE,
        EMPTY_INSETS, 0, 0));
    add(new JScrollPane(destList), new GridBagConstraints(2, 1, 1, 5, .1,
        1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        EMPTY_INSETS, 0, 0));
 
	  destList.setCellRenderer(new SelectedListCellRenderer());
	  sourceList.setCellRenderer(new SelectedListCellRenderer());
    
   }
  
	public void changeLanguage() {

		currentLangue = (currentLangue.equals("fr")) ? "en" : "fr";
		manageConfigFile.getPrefs().setProperty("langue", currentLangue);
	    manageConfigFile.savePrefProperties();

		manageConfigFile.readLangueProperties(currentLangue);
		launchPadButton.setText(manageConfigFile.getProp().getProperty("launch.pad"));
		launchPadButton.setToolTipText(prop.getProperty("launch.tip"));
		saveButton.setText(prop.getProperty("save"));
		saveButton.setToolTipText(prop.getProperty("save.tip"));
		resetButton.setText(prop.getProperty("reset"));
		resetButton.setToolTipText(prop.getProperty("reset.tip"));
		searchSceneryButton.setText(prop.getProperty("search"));
		searchSceneryButton.setToolTipText(prop.getProperty("search.tip"));
		preferenceButton.setText(prop.getProperty("preferences"));
		preferenceButton.setText(prop.getProperty("preferences"));
		caseSensitive.setToolTipText(prop.getProperty("case.tip"));
	    alphabOrder.setToolTipText(prop.getProperty("alpha.tip"));
		manageButton.setText(prop.getProperty("manage"));
		manageButton.setToolTipText(prop.getProperty("manage.tip"));
		flightPlanButton.setText(prop.getProperty("flightPlan"));
		flightPlanButton.setToolTipText(prop.getProperty("flightPlan.button.tip"));
		showDefaultScenery.setToolTipText(prop.getProperty("hide.tip"));

		fileMenu.setText(prop.getProperty("file"));
		searchMenu.setText(prop.getProperty("search"));
		manageMenu.setText(prop.getProperty("manage"));
		helpMenu.setText(prop.getProperty("help"));

		addItem.setText(prop.getProperty("add.scenery"));
		saveItem.setText(prop.getProperty("save"));
		exitItem.setText(prop.getProperty("exit"));

		searchItem.setText(prop.getProperty("search.scenery"));
		destItem.setText(prop.getProperty("destination"));
		prefItem.setText(prop.getProperty("preferences"));
		reorderItem.setText(prop.getProperty("reorder"));

		helpItem.setText(prop.getProperty("help.online"));
		checkPlaneItem.setText(prop.getProperty("checkplane"));
		contributeItem.setText(prop.getProperty("contribute"));
		aboutItem.setText(prop.getProperty("about"));

		langueItem.setText(prop.getProperty("current"));
		

	}
  
  /**
   * 
   */
  public void setListColor(){
    destList.setFont(fontList);
    sourceList.setFont(fontList);
    sourceLabel.setFont(fontTitle);
    destLabel.setFont(fontTitle);

    sourceList.setBackground(colorBackground[numColor]);
    sourceList.setForeground(colorForground[numColor]);
    sourceList.setFixedCellWidth(280);
    sourceList.setFixedCellHeight(15);
    sourceList.setVisibleRowCount(16);
 
    destList.setBackground(colorBackground[numColor]);
    destList.setForeground(colorForground[numColor]);
    destList.setFixedCellWidth(50);
    destList.setFixedCellHeight(15);
    destList.setVisibleRowCount(16);
  }
  
  /**
   * 
   * 
   */
  private static void createAndShowGUI() {
	    jframe = new JFrame();
	    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    manageXMLFile = new ManageXMLFile();
	    
	    manageConfigFile = new ManageConfigFile();
	    manageConfigFile.init();
	    jframe.setTitle("FSLaunchPad "+manageConfigFile.getVersion());


	    Properties prop = manageConfigFile.getProp();
        manageConfigFile.setFsRoot(manageConfigFile.getPrefs().getProperty("fsRoot"));
        
 		initLookAndFeel();
	    
	    JMenuBar mbar = new JMenuBar();
	    mbar.setBackground(colorBackground[numColor]);

	    jframe.setJMenuBar(mbar);

	    fileMenu = new JMenu(prop.getProperty("file"));
//	    fileMenu.setBackground(colorBackground[numColor]);
	    fileMenu.setMnemonic('F');
	    addItem = new JMenuItem(prop.getProperty("add.scenery"));
	    addItem.setAccelerator
	       (KeyStroke.getKeyStroke(KeyEvent.VK_A,
	       InputEvent.CTRL_MASK));

	    reorderItem = new JMenuItem(prop.getProperty("reorder"));
	    reorderItem.setAccelerator
	       (KeyStroke.getKeyStroke(KeyEvent.VK_R,
	       InputEvent.CTRL_MASK));
	    
	    saveItem = new JMenuItem(prop.getProperty("save"));
	    saveItem.setAccelerator
	       (KeyStroke.getKeyStroke(KeyEvent.VK_S,
	       InputEvent.CTRL_MASK));
	    exitItem  = new JMenuItem(prop.getProperty("exit"));
	 
	    mbar.add(makeMenu(fileMenu,
	            new Object[]
	            {  addItem,
	    		   saveItem,
	               null,
	               exitItem
	            },
	            jframe));

	    searchMenu = new JMenu(prop.getProperty("search"));
	    fileMenu.setMnemonic('S');
	    searchItem = new JMenuItem(prop.getProperty("search.scenery"));
	    searchItem.setAccelerator
	       (KeyStroke.getKeyStroke(KeyEvent.VK_S,
	       InputEvent.SHIFT_MASK));
	    
	    mbar.add(makeMenu(searchMenu,
	            new Object[]
	            {  searchItem
	            },
	            jframe));

	    manageMenu = new JMenu(prop.getProperty("manage"));
	    fileMenu.setMnemonic('S');
	    destItem = new JMenuItem(prop.getProperty("destination"));
	    prefItem = new JMenuItem(prop.getProperty("preferences"));
	    reorderItem = new JMenuItem(prop.getProperty("reorder"));
	    reorderItem.setAccelerator
	       (KeyStroke.getKeyStroke(KeyEvent.VK_R,
	       InputEvent.CTRL_MASK));

	    mbar.add(makeMenu(manageMenu,
	            new Object[]
	            {  destItem,
	    		   reorderItem,
	    		   prefItem
	            },
	            jframe));

	    langueMenu = new JMenu(prop.getProperty("langue"));
	    langueItem = new JMenuItem(prop.getProperty("current"));
	    
	    mbar.add(makeMenu(langueMenu,
	            new Object[]
	            {  langueItem
	            },
	            jframe));

	    
	    
	    helpMenu = new JMenu(prop.getProperty("help"));
	    helpItem = new JMenuItem(prop.getProperty("help.online"));
	    checkPlaneItem = new JMenuItem(prop.getProperty("checkplane"));
	    contributeItem = new JMenuItem(prop.getProperty("contribute"));
	    aboutItem = new JMenuItem(prop.getProperty("about"));

	    mbar.add(makeMenu(helpMenu,
	            new Object[]
	            {  helpItem,
	    		null,
	    		checkPlaneItem,
	    		null,
	    		contributeItem,
	    		null,
	    		aboutItem
	    		
	            },
	            jframe));
	    
	    
	    
	    status = new Status();
	    
	    numColor = Integer.parseInt(manageConfigFile.getPrefs().getProperty("numcolor"));
	    
	    fSLauncpad = new FSLaunchPad();
	    
	   jframe.getContentPane().add(fSLauncpad, BorderLayout.CENTER);
	   jframe.setContentPane(wrapInBackgroundImage(basePanel,
	            new ImageIcon(
	            		currentRelativePath.toAbsolutePath().toString()+"\\images\\"+manageConfigFile.getPrefs().getProperty("background"))));
	
	    jframe.setSize(800, 500);
	    jframe.setResizable(false); 
	    
	    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	    jframe.setLocation(dim.width/2 - jframe.getWidth()/2, dim.height/2 - jframe.getHeight()/2);
	    Util.setIcon(jframe,imageLogo);
	    
	    getBasePanel().setVisible(false);
	    jframe.setVisible(true);

	    fSLauncpad.initLaunchpad();
	    
	    getBasePanel().setVisible(true);

  }
  
  private void setConfig(String message){
	    JButton button = new JButton("Set");
	    button.addActionListener(fSLauncpad.new PreferenceListener(jframe));

	    Object[] butYesCancel = {button,"Verify",prop.getProperty("exit")}; 
	    
	    int result = JOptionPane.showOptionDialog(null,message,"Setup Config", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, butYesCancel, butYesCancel[0]) ;

		if (result == 2){
			System.exit(0);
		}
	  
  }
  
  
  /**
   * 
   * 
   */
	public void initLaunchpad() {

		final JDialog loading = new JDialog();
		JPanel p1 = new JPanel(new BorderLayout());
		JLabel label = new JLabel();
			
		final WaitMessage waitMessage = new WaitMessage(null);
		
		p1.add(label, BorderLayout.CENTER);
		loading.setUndecorated(true);
		loading.getContentPane().add(p1);
		loading.pack();
		loading.setLocationRelativeTo(this);
		loading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		loading.setModal(true);
		
		boolean isConfigOk = false;

	    manageConfigFile.init();

		if (!FileUtil.isAdmin()){
			 JOptionPane.showMessageDialog(null, getLabel(prop.getProperty("admin.message")));
			 System.exit(0);
		}
		
		
		do {
			isConfigOk = false;
			try {
				manageConfigFile.process();
				isConfigOk = true;
			} catch (FileNotFoundException e) {
				isConfigOk = false;
		    	setConfig("<html>Thanks for installing FSLaunchePad!<br><br>First of all FSL must be configured.<br> Please set the value for "+ e.getMessage());
			    manageConfigFile.init();
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, getLabel(prop.getProperty("server.message.null")));
				System.exit(0);
			} catch (Exception e) {
				 JOptionPane.showMessageDialog(null, getLabel(prop.getProperty("server.message.null")));
				 ManageStatus.sendError("message/", manageConfigFile.getPrefs().getProperty("fsProgram").replace(".", "_")+":"+(e.getMessage().replaceAll(" ", "%20")));
				 System.exit(0);
			}
		} while (!isConfigOk);
		
		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
			@Override
			protected String doInBackground() throws InterruptedException {
				fSLauncpad.addDestinationElements(manageConfigFile.getMapDestination(fSLauncpad.getShowDefaultScenery().isSelected()));
				fSLauncpad.addSourceElements(manageConfigFile.getMapSource().keySet().toArray());

				saveItem.addActionListener(fSLauncpad.new SaveNameListener());
				exitItem.addActionListener(fSLauncpad.new ExitListener());

				addItem.addActionListener(fSLauncpad.new AddSceneryListener());
				reorderItem.addActionListener(fSLauncpad.new ReorderSceneryListener());
                checkPlaneItem.addActionListener(fSLauncpad.new CallCheckplaneListener());
                helpItem.addActionListener(fSLauncpad.new CallFslaunchpadListener());
                contributeItem.addActionListener(fSLauncpad.new CallFslaunchpadListener());
				searchItem.addActionListener(fSLauncpad.new GoogleSearch());
				aboutItem.addActionListener(fSLauncpad.new CallMe());
				langueItem.addActionListener(fSLauncpad.new LangueListener());
				destItem.addActionListener(fSLauncpad.new ManageAreaListener());
				prefItem.addActionListener(fSLauncpad.new PreferenceListener(jframe));
				
				//System.out.println(manageConfigFile.getPrefs().toString());
				
				
			    Runnable r = new Runnable() {
			         public void run() {
			        	 updateService();
			         }
			     };

			     new Thread(r).start();	
				
				return null;
				

			}

			@Override
			protected void done() {
			    loading.dispose();
				waitMessage.setVisible(false);
			}
		};
	   worker.execute();
	   loading.setVisible(true);
	   
	   
	  try {
	      worker.get();
	  } catch (Exception e1) {
			System.err.println(e1);
	     // e1.printStackTrace();
	  } finally {

   		  contribute = new Contribute(status,numColor,manageConfigFile);
		  contribute.callDialog();
		    if (!"".equals(manageConfigFile.getAreaName())){
		    	destLabel.setText(DEFAULT_DEST_CHOICE_LABEL+" "+manageConfigFile.getAreaName());
		    }

	  }
	  
}

  private void updateService(){
		LauncherFactory factory = LauncherFactory.getInstance();
		factory.createLauncher("0000",manageConfigFile.getFsRoot(),manageConfigFile.getVersion(),manageConfigFile.getPrefs().getProperty("langue"),manageConfigFile.getPrefs().getProperty("fsProgram"));

		status = ManageStatus.getWebStatus("add/", factory.getJson());
		if ((status != null && (status.getCode() == bvd || status.getCode()==55))){
			// waitMessage.setVisible(false);
	 		 JOptionPane.showMessageDialog(null, getLabel(status.getMessage()));
	 		 if (status.getCode() == bvd){
		 		 System.exit(0);
	 		 }
		}
		

		if (launchpad != null){
			launchpad.quitLaunchpad();
		}

  }
	
  public static void main(String args[]) throws IOException{
	  javax.swing.SwingUtilities.invokeLater(new Runnable() {
          public void run() {
              createAndShowGUI(); 
          }
      });
	  
  }
  
  
  public static JMenu makeMenu(Object parent,
	      Object[] items, Object target)
	   {  JMenu m = null;
	      if (parent instanceof JMenu)
	         m = (JMenu)parent;
	      else if (parent instanceof String)
	         m = new JMenu((String)parent);
	      else
	         return null;

	      for (int i = 0; i < items.length; i++)
	      {  if (items[i] == null)
	            m.addSeparator();
	         else
	            m.add(makeMenuItem(items[i], target));
	      }
	      m.setBackground(colorBackground[numColor]);

	      return m;
	   }
 
	private static  JMenuItem makeMenuItem(Object item, Object target) {
		JMenuItem r = null;
		if (item instanceof String)
			r = new JMenuItem((String) item);
		else if (item instanceof JMenuItem)
			r = (JMenuItem) item;
		else
			return null;

		if (target instanceof ActionListener)
			r.addActionListener((ActionListener) target);
		r.setBackground(colorBackground[numColor]);

		return r;
	}


public void setIconImage(JFrame jframe, Image image)
  {
    ArrayList<Image> imageList = new ArrayList<Image>();
    if (image != null)
    {
      imageList.add(image);
    }
    jframe.setIconImages(imageList);
  }
  
  private static void initLookAndFeel() {
      String lookAndFeel = "";
      String LOOKANDFEEL = "Metal";
      String THEME = "Ocean";
      
      if (LOOKANDFEEL != null) {
          if (LOOKANDFEEL.equals("Metal")) {
        	  
              lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            //  an alternative way to set the Metal L&F is to replace the 
            // previous line with:
            lookAndFeel = "javax.swing.plaf.metal.MetalLookAndFeel";
               
          }
           
          else if (LOOKANDFEEL.equals("System")) {
              lookAndFeel = UIManager.getSystemLookAndFeelClassName();
          } 
           
          else if (LOOKANDFEEL.equals("Motif")) {
              lookAndFeel = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
          } 
           
          else if (LOOKANDFEEL.equals("GTK")) { 
              lookAndFeel = "com.sun.java.swing.plaf.gtk.GTKLookAndFeel";
          } 
           
          else {
              System.err.println("Unexpected value of LOOKANDFEEL specified: "
                                 + LOOKANDFEEL);
              lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
          }

          try {
              UIManager.setLookAndFeel(lookAndFeel);
               
              // If L&F = "Metal", set the theme
               
              if (LOOKANDFEEL.equals("Metal")) {
                if (THEME.equals("DefaultMetal"))
                   MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());
                else if (THEME.equals(""))
                   MetalLookAndFeel.setCurrentTheme(new OceanTheme());
               
                    
                UIManager.setLookAndFeel(new MetalLookAndFeel()); 
              }   
                   
          } 
          catch (ClassNotFoundException e) {
              System.err.println("Couldn't find class for specified look and feel:"
                                 + lookAndFeel);
              System.err.println("Did you include the L&F library in the class path?");
              System.err.println("Using the default look and feel.");
          } 
           
          catch (UnsupportedLookAndFeelException e) {
              System.err.println("Can't use the specified look and feel ("
                                 + lookAndFeel
                                 + ") on this platform.");
              System.err.println("Using the default look and feel.");
          } 
           
          catch (Exception e) {
              System.err.println("Couldn't get specified look and feel ("
                                 + lookAndFeel
                                 + "), for some reason.");
              System.err.println("Using the default look and feel.");
              e.printStackTrace();
          }          
      }}       

/**
 * 
 * @author Pierre
 *
 */
  private class AddListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = sourceList.getSelectedValuesList().toArray();
      
      String before = "";
      for (int i = 0; i < selected.length; i++) {
    	  before = selected[i].toString();
       	 if (alphabOrder.isSelected()){
            selected[i] = Util.formatAreaNum(manageConfigFile.getMapSource().get(selected[i].toString()).getNum()+"", manageConfigFile.getMapAllScenery().size()+1) +"-"+ selected[i];
       	 }
      	 manageConfigFile.getMapDestination().put(selected[i].toString(),manageConfigFile.getMapSource().get(before));
    	 manageConfigFile.getMapSource().remove(before);
      }
      addDestinationElements(selected);
      clearSourceSelected();
   }
  }
  
/**
 * 
 * @author Pierre
 *
 */
  private class RemoveListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Object selected[] = destList.getSelectedValuesList().toArray();
      
      String before = "";
      for (int i = 0; i < selected.length; i++) {
    	  before = selected[i].toString();
    	  if (alphabOrder.isSelected()){
    		  selected[i]=  selected[i].toString().substring(selected[i].toString().indexOf("-")+1);
    	  }
    	 manageConfigFile.getMapSource().put(selected[i].toString(),manageConfigFile.getMapDestination().get(before));
    	 manageConfigFile.getMapDestination().remove(before);
      }
      addSourceElements(selected);
      clearDestinationSelected();
    }
  }

  
  
  /**
   * 
   * 
   * @author Pierre
   *
   */
   private class ManageAreaListener implements ActionListener {

	   public void actionPerformed(ActionEvent e) {
		   prop = manageConfigFile.getProp();
		   
		   String SELECT = prop.getProperty("manage.pop.select");
		   String DELETEALL = prop.getProperty("manage.pop.delall");
		   String DELETESELECTED = prop.getProperty("manage.pop.delsel");
		   String RESET = prop.getProperty("manage.pop.reset");
		   String CANCEL = prop.getProperty("manage.pop.cancel");
		   Object[] buttons; 
		   
		   JPanel panel = new JPanel();
		   
		   String[] choices = manageConfigFile.getListOfArea();
		   choices [0] = prop.getProperty("manage.pop.selectgroup");
		   JComboBox <String>  searchChoice = new JComboBox<String> (choices);
		   
		   searchChoice.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
		          Object item = e.getItem();
		          if (!item.equals(prop.getProperty("manage.pop.selectgroup"))){
				    	//int selectIndex = item.getSelectedIndex();
				    	manageConfigFile.initMapsWithSenerySelected((String)item);
				    	manageConfigFile.setAreaName((String)item);
				    	manageConfigFile.getPrefs().setProperty("areaName", (String)item);
			  	    	manageConfigFile.savePrefProperties();
				    	destLabel.setText(DEFAULT_DEST_CHOICE_LABEL+" "+(String)item);
				    	resetArea();
		          } else {
//					searchChoice.setSelectedItem(prop.getProperty("manage.pop.selectgroup"));
					manageConfigFile.initMap();
			    	manageConfigFile.setAreaName("");
			    	destLabel.setText(DEFAULT_DEST_CHOICE_LABEL);
			    	setPropNone();
					alphabOrder.setSelected(false);
			    	resetArea();
			    	resetArea();
		        	  
		          }

			}
		});
		   
		   int returnchoice = -1;
		   
		   JButton resetButton = new JButton(RESET);
		   resetButton.addActionListener(new ResetListener(searchChoice));
		   
		 if (manageConfigFile.getSceneryAreaFile().exists() && manageConfigFile.getMapListArea().size() > 1){
			   buttons = new Object[5];
			   buttons[0] = SELECT;
			   buttons[1] = DELETEALL;
			   buttons[2] = DELETESELECTED;
			   buttons[3] = resetButton;
			   buttons[4] = CANCEL;
			   Dimension searchSize = searchChoice.getPreferredSize();
		       searchSize.width = 200;
		       searchChoice.setPreferredSize(searchSize);
		       searchChoice.setSelectedItem(manageConfigFile.getAreaName());
		       panel.add(searchChoice);
		   } else {
			   panel.add(new JLabel(prop.getProperty("manage.pop.no.selection")));
			   buttons = new Object[2];
			   buttons[0] = resetButton;
			   buttons[1] = CANCEL;
		   }

		   
		   Object[] butYesNo = {prop.getProperty("button.yes"),prop.getProperty("button.no")}; 
		   returnchoice = JOptionPane.showOptionDialog(jframe, panel,prop.getProperty("manage.pop.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
 		   
		   Object choice = (returnchoice != -1)?buttons[returnchoice]:CANCEL;
		   if (SELECT.equals(choice)){
		   } else if (DELETEALL.equals(choice)){
		 		 if (JOptionPane.showOptionDialog(null,getLabel(prop.getProperty("manage.pop.mess.delall")),prop.getProperty("manage.pop.mess.delall.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, butYesNo, butYesNo[0]) == 0){
		 			 manageConfigFile.deleteListAreaFile();
			    	 setPropNone();
			    	 
		 			 resetArea();
		 		 }
		   } else if (DELETESELECTED.equals(choice)){
			     int selectIndex = searchChoice.getSelectedIndex();//"Do you want to delete ("+choices[selectIndex]+") from list?"
		 		 if (JOptionPane.showOptionDialog(null,getLabel(prop.getProperty("manage.pop.mess.delsel")+ " ("+choices[selectIndex]+")?"),prop.getProperty("manage.pop.mess.delsel.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, butYesNo, butYesNo[0]) == 0){
				    
		 			 manageConfigFile.getMapListArea().remove(choices[selectIndex]);

				    if (manageConfigFile.getMapListArea().size() == 0){
			    		manageConfigFile.deleteListAreaFile();
			    		setPropNone();
			    	} else {
				    	if (choices[selectIndex].trim().equals(manageConfigFile.getPrefs().getProperty("areaName").trim())){
				    		setPropNone();
				    	}
						manageConfigFile.saveMapListArea(manageConfigFile.getMapListArea());
			    	}
			    	resetArea();
	 		   }
		   } else if (CANCEL.equals(choice)){
		   }
			
	    }
	   
		private void setPropNone() {
			manageConfigFile.setAreaName("");
			manageConfigFile.initMap();
			manageConfigFile.getPrefs().setProperty("areaName", "none");
			manageConfigFile.savePrefProperties();
			destLabel.setText(DEFAULT_DEST_CHOICE_LABEL);

		}

}

	public class ResetListener implements ActionListener {
		private JComboBox <String>  searchChoice;
		public ResetListener(JComboBox <String>  searchChoice) {
			this.searchChoice = searchChoice;
		}
		public void actionPerformed(ActionEvent e) {
			searchChoice.setSelectedIndex(0);
//			searchChoice.setSelectedItem(prop.getProperty("manage.pop.selectgroup"));
			manageConfigFile.initMap();
	    	manageConfigFile.setAreaName("");
	    	destLabel.setText(DEFAULT_DEST_CHOICE_LABEL);
			alphabOrder.setSelected(false);
	    	resetArea();
	    	resetArea();
		}
	}
   
	private void resetArea() {
		sourceListModel.clear();
		destListModel.clear();
		addDestinationElements(manageConfigFile.getMapDestination(getShowDefaultScenery().isSelected()));
		addSourceElements(manageConfigFile.getMapSource().keySet().toArray());
		clearDestinationSelected();
		clearSourceSelected();

	}  
  
	public class ReorderSceneryListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			contribute.callDialog();
			alphabOrder.setSelected(false);
			manageConfigFile.initMap();
		    if ( new ReorderScenery(jframe,  prop, manageConfigFile, "Move",numColor).isDone()){
				manageConfigFile.initMap();
		    	manageConfigFile.setAreaName("");
		    	destLabel.setText(DEFAULT_DEST_CHOICE_LABEL);
	 			resetArea();
		    }
		}    
	}

	public class CallCheckplaneListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
		    Util.openWebpage("", 3);
		}    
	}

	public class CallMe implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			new AboutFsl(jframe, prop, numColor, getBasePanel(),manageConfigFile.getVersion());
		}    
	}
	
	public class LangueListener implements ActionListener {
		public LangueListener() {
		}
		
		public void actionPerformed(ActionEvent e) {
			changeLanguage();
			
		}    
	}


	public class CallFslaunchpadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		    Util.openWebpage("?lang="+manageConfigFile.getPrefs().getProperty("langue"), 4);
		}    
	}

	public class LauchpadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			if ("".equals(manageConfigFile.getAreaName().trim())){
				manageConfigFile.setAreaName(prop.getProperty("unnamed"));
				destLabel.setText(DEFAULT_DEST_CHOICE_LABEL+" "+manageConfigFile.getAreaName());
			}

			contribute.callDialog();
			launchpad = new Launchpad(jframe, prop.getProperty("pad.title"),numColor, getBasePanel(), manageConfigFile);
			
			
/*			int returnValue = 0;
			while ("".equals(manageConfigFile.getAreaName().trim()) && returnValue == 0) {
				returnValue = manageSaveName("save.pop.title.before");
			}
			if (!"".equals(manageConfigFile.getAreaName().trim()) ){
				contribute.callDialog();
				launchpad = new Launchpad(jframe, prop.getProperty("pad.title"),numColor, getBasePanel(), manageConfigFile);
			}
*/		}
	}

  /**
 * 
 * @author Pierre
 *
 */
	public class SaveNameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int returnValue = 0;
			while (returnValue == 0) {
				returnValue = manageSaveName("save.pop.title");
			}
 		}
		
	}
	private int manageSaveName(String title){
        JPanel form = new JPanel();
        prop = manageConfigFile.getProp();
        
         form.setLayout(new GridBagLayout());
         FormUtility formUtility = new FormUtility();
         
         formUtility.addLabel(prop.getProperty("save.pop.label")+" ", form,colorForground[numColor],fontText);
         JTextField areaName = new JTextField();
         areaName.setText(manageConfigFile.getAreaName());
         Dimension titleSize = areaName.getPreferredSize();
         titleSize.width = 150;
         areaName.setPreferredSize(titleSize);
         JPanel titlePanel = new JPanel();
         titlePanel.setLayout(new BorderLayout());
         titlePanel.add(areaName, BorderLayout.WEST);
         formUtility.addLastField(titlePanel, form);
		
         Object[] choix = {prop.getProperty("save.pop.save"),prop.getProperty("save.pop.quit")};
         int returnValue = JOptionPane.showOptionDialog(null, form, prop.getProperty(title), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, choix, choix[0]);
       
         if (returnValue == 0 && !"".equals(areaName.getText())){
       	         manageConfigFile.setAreaName(areaName.getText());
		        Object selected[] = manageConfigFile.getMapDestination(true);
		        
				manageConfigFile.saveAreaDatList(new ArrayList<Object>(Arrays.asList(selected)));
	  	    	manageConfigFile.getPrefs().setProperty("areaName", areaName.getText().trim());
	  	    	manageConfigFile.savePrefProperties();
	  	    	
	  	    	destLabel.setText(DEFAULT_DEST_CHOICE_LABEL+" "+areaName.getText().trim());
		    	returnValue = 1;
         } else if (returnValue == 0 && "".equals(areaName.getText().trim())){
       	  	JOptionPane.showMessageDialog(null,getLabel(prop.getProperty("save.pop.warning")) );
         }
         
         return returnValue;
	}
	
	public class FlightplanListener extends JDialog implements ActionListener   {

		private static final long serialVersionUID = 1L;
		
		private JPanel form;
		private JFrame parent;
		private SpinnerModel distanceModel;
		
		private Map <String,JCheckBox> mapCheckGoogle;
		
		private String flightplan;
        
		private FlightplanListener(JFrame parent){
			this.parent = parent;
			mapCheckGoogle = new HashMap<String, JCheckBox>();
			mapCheckGoogle.put("earth", new JCheckBox());
			mapCheckGoogle.put("fsx", new JCheckBox());
			mapCheckGoogle.put("addon", new JCheckBox());
		}
		
		
		public void actionPerformed(ActionEvent e) {
			  flightplan = "";
    		
	          form = new JPanel();
	          form.setLayout(new GridBagLayout());
	          FormUtility formUtility = new FormUtility();
	          
	          formUtility.addLabel(manageConfigFile.getProp().getProperty("flight.plan.label")+": ", form,colorForground[numColor],fontText);
	          JButton fpButton = new JButton(manageConfigFile.getProp().getProperty("flight.plan.button"));
	          fpButton.setToolTipText(manageConfigFile.getProp().getProperty("flight.plan.tip"));//----
	          fpButton.addActionListener(new SelectFlightPlanListener());
	          Dimension buttonSize = fpButton.getPreferredSize();
	          buttonSize.width = 120;
	          fpButton.setPreferredSize(buttonSize);
	          JPanel fpPanel = new JPanel();
	          fpPanel.setLayout(new BorderLayout());
	          fpPanel.add(fpButton, BorderLayout.WEST);
	          formUtility.addLastField(fpPanel, form);
	
	
		         // form.setForeground(colorForground[numColor]);
	          distanceModel =  new SpinnerNumberModel(60, //----
	        		  20, //min
	        		  200, //max
	        	      20);               
	          
	          JSpinner elapseSpinner=new JSpinner(distanceModel);
	          elapseSpinner.setToolTipText(manageConfigFile.getProp().getProperty("flight.distance.tip"));
	          formUtility.addLabel(manageConfigFile.getProp().getProperty("flight.distance.label") + ": ", form,colorForground[numColor],fontText);
	          JPanel elapsePanel = new JPanel();
	          elapsePanel.setLayout(new BorderLayout());
	          elapsePanel.add(elapseSpinner, BorderLayout.WEST);
	          formUtility.addLastField(elapsePanel, form);
	
	
	
	          formUtility.addLabel(manageConfigFile.getProp().getProperty("flight.google.label")+": ", form,colorForground[numColor],fontText);
	          mapCheckGoogle.get("earth").setToolTipText(manageConfigFile.getProp().getProperty("flight.google.tip"));//----
	          mapCheckGoogle.get("earth").setSelected(true);
	          mapCheckGoogle.get("earth").addItemListener(new ItemListener() {
	              public void itemStateChanged(ItemEvent e) {
	                mapCheckGoogle.get("fsx").setEnabled(mapCheckGoogle.get("earth").isSelected());
	                mapCheckGoogle.get("fsx").setSelected(mapCheckGoogle.get("earth").isSelected());
	              }
	            });
	
	          JPanel checkPanel = new JPanel();
			  checkPanel.setLayout(new BorderLayout());
			  checkPanel.add(new AlphaContainer(mapCheckGoogle.get("earth")), BorderLayout.WEST);
	          formUtility.addLastField(checkPanel, form);
	          
	          
	          formUtility.addLabel(manageConfigFile.getProp().getProperty("flight.fsx.label")+": ", form,colorForground[numColor],fontText);
	          mapCheckGoogle.get("fsx").setToolTipText(manageConfigFile.getProp().getProperty("flight.fsx.tip"));//----
	          mapCheckGoogle.get("fsx").setSelected(true);
	          JPanel checkFSXPanel = new JPanel();
	          checkFSXPanel.setLayout(new BorderLayout());
	          checkFSXPanel.add(new AlphaContainer(mapCheckGoogle.get("fsx")), BorderLayout.WEST);
	          formUtility.addLastField(checkFSXPanel, form);
	          
	          formUtility.addLabel(manageConfigFile.getProp().getProperty("flight.addon.label")+": ", form,colorForground[numColor],fontText);
	          mapCheckGoogle.get("addon").setToolTipText(manageConfigFile.getProp().getProperty("flight.addon.tip"));//----
	          mapCheckGoogle.get("addon").setSelected(true);
	          JPanel checkAddonPanel = new JPanel();
	          checkAddonPanel.setLayout(new BorderLayout());
	          checkAddonPanel.add(new AlphaContainer(mapCheckGoogle.get("addon")), BorderLayout.WEST);
	          formUtility.addLastField(checkAddonPanel, form);
	          
/*	          JButton button = new JButton("Start");
	          
	          button.addActionListener(startActionListener);
*/	          
//	          Object[] choix = {button,prop.getProperty("save.pop.quit")};
	          Object[] choix = {manageConfigFile.getProp().getProperty("flight.button.load"),prop.getProperty("save.pop.quit")};
	 	      int returnValue = JOptionPane.showOptionDialog(null, form, manageConfigFile.getProp().getProperty("flight.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, choix, choix[0]);
	 	      
	          if (returnValue == 0 ){
	
	      	    	if (flightplan != null && "".equals(flightplan)){
	        	    	JOptionPane.showMessageDialog(null, getLabel(manageConfigFile.getProp().getProperty("flight.error1")));
	        	    	return;
	    	    	} 
	    	    	
	    	    	if (!mapCheckGoogle.get("earth").isSelected() && !mapCheckGoogle.get("fsx").isSelected() && !mapCheckGoogle.get("addon").isSelected()){
	        	    	JOptionPane.showMessageDialog(null, getLabel(manageConfigFile.getProp().getProperty("flight.error2")));
	        	    	return;
	    	    	} 
	
	    	    	try {
	    	    		createFSLPlan = new CreateFSLPlan(flightplan, mapCheckGoogle.get("earth").isSelected(), Double.parseDouble(distanceModel.getValue().toString()),manageXMLFile, manageConfigFile,mapCheckGoogle);
	    	    	    new FlightplanResultListener(createFSLPlan.getAddonList(),createFSLPlan.getTotalPlacemarks(), createFSLPlan.getTotalFsxPlacemarks(), createFSLPlan.getTotalAddonPlacemarks(),flightplan);
	    	    		
					} catch (NullPointerException | IOException | NumberFormatException | NoPoints e1) {
						JOptionPane.showMessageDialog(null, getLabel(manageConfigFile.getProp().getProperty("flight.error3")));
					}	        	    
		      }
	          
		}
		
/*	    ActionListener startActionListener = new ActionListener() {
	        public void actionPerformed(ActionEvent actionEvent) {
      	    	if (flightplan != null && "".equals(flightplan)){
        	    	JOptionPane.showMessageDialog(null, getLabel(manageConfigFile.getProp().getProperty("flight.error1")));
        	    	return;
    	    	} 
    	    	
    	    	if (!mapCheckGoogle.get("earth").isSelected() && !mapCheckGoogle.get("fsx").isSelected() && !mapCheckGoogle.get("addon").isSelected()){
        	    	JOptionPane.showMessageDialog(null, getLabel(manageConfigFile.getProp().getProperty("flight.error2")));
        	    	return;
    	    	} 
	          Component parent = (Component) actionEvent.getSource();
	  	      final ProgressMonitor monitor = new ProgressMonitor(FSLaunchPad.this, "Loading Progress", "Getting Started...", 0, manageXMLFile.getHashPlacemark().size());
	          if (monitor != null) {
	              if (timer == null) {
	                timer = new Timer(10, new ActionListener() {

	                  public void actionPerformed(ActionEvent e) {
	          	    	try {
	    		    		createFSLPlan = new CreateFSLPlan(flightplan, mapCheckGoogle.get("earth").isSelected(), Double.parseDouble(distanceModel.getValue().toString()),manageXMLFile, manageConfigFile,mapCheckGoogle);
	    		    		new FlightplanResultListener(createFSLPlan.getAddonList(),createFSLPlan.getTotalPlacemarks(), createFSLPlan.getTotalFsxPlacemarks(), createFSLPlan.getTotalAddonPlacemarks(),flightplan);
	    		    		
	    				} catch (NullPointerException | IOException | NumberFormatException | NoPoints e1) {
	    					JOptionPane.showMessageDialog(null, getLabel(manageConfigFile.getProp().getProperty("flight.error3")));
	    				}	        	    
	                    if (monitor == null)
	                      return;
	                    if (monitor.isCanceled() || createFSLPlan.getCurrent() >=  manageXMLFile.getHashPlacemark().size()) {
	                      System.out.println("Monitor canceled");
	                      timer.stop();
	                    } else {
	                      monitor.setProgress(createFSLPlan.getCurrent() );
	                      monitor.setNote("Loaded " + createFSLPlan.getCurrent() + " files");
	                    }
	                  }
	                });
	              }
	              timer.start();
	            }
	        }
	      };

*/		
		public class SelectFlightPlanListener implements ActionListener {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser = selectDirectoryProgram(prop.getProperty("flight.select.title"), "");
		        setAlwaysOnTop(false);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					System.out.println(chooser.getSelectedFile());
					flightplan = chooser.getSelectedFile().toString();
			        
				}
				
		        //setAlwaysOnTop(false);

				//frame.setEnabled(true);
			}
			
		    private JFileChooser selectDirectoryProgram(String title,String directory){
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File(directory));
				chooser.setDialogTitle(title);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				String[] EXTENSION=new String[]{"plg","pln"};
				 FileNameExtensionFilter filter=new FileNameExtensionFilter("FSX/FS9 (.pln), Plan-G (.plg)",EXTENSION);
				 chooser.setFileFilter(filter);
				 chooser.setMultiSelectionEnabled(false);
				
				return chooser;
		    	
		    }
		}
	

	
	}
	public class FlightplanResultListener extends JDialog   {

		private static final long serialVersionUID = 1L;
		
		private JPanel form;
		private JDialog parent;
		
		private List <String> addonList;
		
		private int totalPlacemarks;
		private int totalFsxPlacemarks;
		private int totalAddonPlacemarks;
		
		private DefaultListModel listModel;
		
		private JScrollPane jScrollPane;
		
		private JList jlist;
		
		private String flightplan;
				
		private FlightplanResultListener(List<String> addonList, int totalPlacemarks, int totalFsxPlacemarks, int totalAddonPlacemarks, String flightplan) {
			super();
			this.parent = parent;
			this.addonList = addonList;
			this.totalPlacemarks = totalPlacemarks;
			this.totalFsxPlacemarks = totalFsxPlacemarks;
			this.totalAddonPlacemarks = totalAddonPlacemarks;
			this.flightplan = new File(flightplan).getName();
			this.flightplan = this.flightplan.substring(0,this.flightplan.indexOf("."));
			
		    listModel = new DefaultListModel();
		    jlist = new JList(listModel);
		    
		    for(String scene:addonList){
		    	listModel.addElement(scene);
		    }
			
		    jScrollPane =  new JScrollPane(jlist);
		    
		    jScrollPane.setPreferredSize( new Dimension (180, 100) );

		    int start = 0;
	        int end = jlist.getModel().getSize() - 1;
	        if (end >= 0) {
	        	jlist.setSelectionInterval(start, end);
	        }

		    
			showResult();
		}
		
		
		public FlightplanResultListener(ActionListener actionListener,
				List<String> addonList2, int totalPlacemarks2,
				int totalFsxPlacemarks2, int totalAddonPlacemarks2,
				String flightplan2) {
			// TODO Auto-generated constructor stub
		}


		public void showResult() {
    		
	          form = new JPanel();
	          form.setLayout(new GridBagLayout());
	          FormUtility formUtility = new FormUtility();
	
	          formUtility.addLabel(manageConfigFile.getProp().getProperty("flight.result.all")+": ", form,colorForground[numColor],fontText);
	          JPanel resultPanel = new JPanel();
	          resultPanel.setLayout(new BorderLayout());
	          resultPanel.add(new JLabel(String.valueOf(totalPlacemarks)), BorderLayout.WEST);
	          formUtility.addLastField(resultPanel, form);	          
	          
	          formUtility.addLabel(manageConfigFile.getProp().getProperty("flight.result.fsx")+": ", form,colorForground[numColor],fontText);
	          resultPanel = new JPanel();
	          resultPanel.setLayout(new BorderLayout());
	          resultPanel.add(new JLabel(String.valueOf(totalFsxPlacemarks)), BorderLayout.WEST);
	          formUtility.addLastField(resultPanel, form);	   
	          
	          formUtility.addLabel(manageConfigFile.getProp().getProperty("flight.result.addon")+" ("+totalAddonPlacemarks+")--> ", form,colorForground[numColor],fontText);
	          resultPanel = new JPanel();
	          resultPanel.setLayout(new BorderLayout());
	          resultPanel.add(jScrollPane, BorderLayout.WEST);
	          formUtility.addLastField(resultPanel, form);	   
	           	
	          Object[] choix = {manageConfigFile.getProp().getProperty("flight.result.button"),prop.getProperty("save.pop.quit")};
	          
	 	      int returnValue = JOptionPane.showOptionDialog(null, form, manageConfigFile.getProp().getProperty("flight.result.title")+" "+flightplan, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, choix, choix[0]);
	 	      
			  if (returnValue == 0) {
				 if (jlist.getModel().getSize() > 0) {
					transfertResult(jlist, "");
					setVisible(false);
					dispose();
				 }
			  }
		}

	}

	
	public class PreferenceListener extends JDialog implements ActionListener   {

		private static final long serialVersionUID = 1L;
		private String titleFSX = "FS";
		private String titleAddon = ADDON_SCENERY;
		private String titleElapse = "Elapsed Time (sec)";
		private String titleConfig = "Scenery.cfg";
		private String titleImage = "Background Image";
		private SpinnerModel elapseModel;

		private String fsRoot;
		private String addonScenery;
		private String sceneryRoot;
		private String sceneryFileName;
		private String fsProgram;
		
		private JPanel form;
		private JFrame parent;
        
		String[] colorTypeEn= {"Yellow/Brown","Blue/Azur","Gray/Black","Green/LightGreen","LightGreen/Green"};
		String[] colorTypeFr= {"Jaune/Brun","Bleu/Azur","Gris/Noir","Vert/Vert pâle","Lime/Vert"};
		String[] colorType = new String[4];
		
		String[] quitTypeEn= {"No","Yes"};
		String[] quitTypeFr= {"Non","Oui"};
		String[] quitType= new String[1];
		int quitAfter = 0;
        
		private PreferenceListener(JFrame parent){
			this.parent = parent;
		}
		public void actionPerformed(ActionEvent e) {
	          
	          prop = manageConfigFile.getProp();
	          perfs = manageConfigFile.getPrefs();
	          
	          addonScenery = manageConfigFile.getPrefs().getProperty("addonScenery");
	          fsRoot =  manageConfigFile.getPrefs().getProperty("fsRoot");
	          sceneryRoot =  manageConfigFile.getPrefs().getProperty("sceneryRoot");
	          sceneryFileName =  manageConfigFile.getPrefs().getProperty("sceneryFileName");
	          fsProgram =  manageConfigFile.getPrefs().getProperty("fsProgram");
	          if (manageConfigFile.getPrefs().getProperty("quitAfter")==null){
	        	  quitAfter = 0;
	          } else {
	        	  quitAfter = Integer.parseInt(manageConfigFile.getPrefs().getProperty("quitAfter"));
	          }

	          
	          colorType = ("en".equals(currentLangue)?colorTypeEn:colorTypeFr);
	          quitType = ("en".equals(currentLangue)?quitTypeEn:quitTypeFr);

		  	  titleFSX = "FS Program (exe)";
			  titleAddon = ADDON_SCENERY+" Directory";
			  titleConfig = "Scenery.cfg";
			  titleElapse = prop.getProperty("pref.pop.elapse");
			  titleImage = prop.getProperty("pref.pop.bg");
	          
	         //form.setBackground(colorBack[numColor]);

	          form = new JPanel();
	          form.setLayout(new GridBagLayout());
	          FormUtility formUtility = new FormUtility();
	          
	          formUtility.addLabel(titleFSX+":", form,colorForground[numColor],fontText);
	          JButton fsxButton = new JButton(titleFSX);
	          fsxButton.setToolTipText(fsRoot+"\\"+fsProgram);//----
	          fsxButton.addActionListener(new OpenOptionListener(titleFSX,(fsRoot),new String[]{"exe"}));
	          Dimension buttonSize = fsxButton.getPreferredSize();
	          buttonSize.width = 120;
	          fsxButton.setPreferredSize(buttonSize);
	          JPanel fsxPanel = new JPanel();
	          fsxPanel.setLayout(new BorderLayout());
	          fsxPanel.add(fsxButton, BorderLayout.WEST);
	          formUtility.addLastField(fsxPanel, form);

	          formUtility.addLabel(titleAddon+":", form,colorForground[numColor],fontText);
	          JButton addonButton = new JButton(titleAddon);
	          addonButton.setToolTipText(addonScenery);//----
	          addonButton.addActionListener(new OpenOptionListener(titleAddon,addonScenery,null));
	          addonButton.setPreferredSize(buttonSize);
	          JPanel addonPanel = new JPanel();
	          addonPanel.setLayout(new BorderLayout());
	          addonPanel.add(addonButton, BorderLayout.WEST);
	          formUtility.addLastField(addonPanel, form);
	          
	          formUtility.addLabel(titleConfig+":", form,colorForground[numColor],fontText);
	          JButton sceneryButton = new JButton(titleConfig);
	          sceneryButton.setToolTipText(sceneryRoot+"\\"+sceneryFileName);//----
	          sceneryButton.addActionListener(new OpenOptionListener(titleConfig,sceneryRoot,new String[]{"cfg"}));
	          sceneryButton.setPreferredSize(buttonSize);
	          JPanel sceneryPanel = new JPanel();
	          sceneryPanel.setLayout(new BorderLayout());
	          sceneryPanel.add(sceneryButton, BorderLayout.WEST);
	          formUtility.addLastField(sceneryPanel, form);
	          
	         // form.setForeground(colorForground[numColor]);
	          int elapse = Integer.parseInt(manageConfigFile.getPrefs().getProperty("elapse"));
	          elapseModel =  new SpinnerNumberModel(elapse, //----
	        		  10, //min
	        		  30, //max
	        	      5);               
	          
	          //elapseModel.setValue("20");
	          JSpinner elapseSpinner=new JSpinner(elapseModel);
	          elapseSpinner.setToolTipText(prop.getProperty("pref.pop.elapse.tip"));
	          formUtility.addLabel(titleElapse+":", form,colorForground[numColor],fontText);
	          JPanel elapsePanel = new JPanel();
	          elapsePanel.setLayout(new BorderLayout());
	          elapsePanel.add(elapseSpinner, BorderLayout.WEST);
	          formUtility.addLastField(elapsePanel, form);

	          
	          JComboBox<String> colorChoice    = new JComboBox<String>(colorType);
	          colorChoice.setSelectedIndex(numColor);

	          formUtility.addLabel(prop.getProperty("pref.pop.font"), form,colorForground[numColor],fontText);
	          colorChoice.setPreferredSize(buttonSize);
	          colorChoice.addItemListener(new ColorListener(this));
	          JPanel colorPanel = new JPanel();
	          colorPanel.setLayout(new BorderLayout());
	          colorPanel.add(colorChoice, BorderLayout.WEST);
	          formUtility.addLastField(colorPanel, form);
	          
	          JComboBox<String> quitChoice    = new JComboBox<String>(quitType);
	          quitChoice.setSelectedIndex(quitAfter);

	          formUtility.addLabel(prop.getProperty("pref.pop.quit")+": ", form,colorForground[numColor],fontText);
	          quitChoice.setPreferredSize(buttonSize);
	          quitChoice.addItemListener(new QuitFslListener());
	          JPanel quitPanel = new JPanel();
	          quitPanel.setLayout(new BorderLayout());
	          quitPanel.add(quitChoice, BorderLayout.WEST);
	          formUtility.addLastField(quitPanel, form);

	          formUtility.addLabel(titleImage+":", form,colorForground[numColor],fontText);
	          JButton imageButton = new JButton(prop.getProperty("pref.pop.click"));
	          imageButton.addActionListener(new SelectImageListener());
	          imageButton.setPreferredSize(buttonSize);
	          JPanel imagePanel = new JPanel();
	          imagePanel.setLayout(new BorderLayout());
	          imagePanel.add(imageButton, BorderLayout.WEST);
	          formUtility.addLastField(imagePanel, form);
	          
	          
	          Object[] choix = {prop.getProperty("save.pop.save"),prop.getProperty("save.pop.quit")};
	 	      int returnValue = JOptionPane.showOptionDialog(null, form, prop.getProperty("pref.pop.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, choix, choix[0]);
	 	      
	          if (returnValue == 0 ){
	        	   // manageConfigFile.setElapse(new Double(elapseModel.getValue().toString())*1000);
	        	   // manageConfigFile.getPreferences().setElapsedTime(new Double(elapseModel.getValue().toString())*1000);
	      	    	numColor = colorChoice.getSelectedIndex();
	      	    	//manageConfigFile.getPreferences().setNumColor(numColor);
	        	    manageConfigFile.getPrefs().setProperty("elapse", elapseModel.getValue().toString());
	      	    	manageConfigFile.getPrefs().setProperty("numcolor",Integer.toString(numColor));
	      	    	manageConfigFile.getPrefs().setProperty("fsRoot", fsRoot);
	      	    	manageConfigFile.getPrefs().setProperty("addonScenery", addonScenery);
	      	    	manageConfigFile.getPrefs().setProperty("sceneryFileName", sceneryFileName);
	      	    	manageConfigFile.getPrefs().setProperty("fsProgram", fsProgram);
	      	    	manageConfigFile.getPrefs().setProperty("sceneryRoot", sceneryRoot);
	      	    	manageConfigFile.getPrefs().setProperty("quitAfter", String.valueOf(quitAfter));

	      	    	manageConfigFile.setAddonScenery(addonScenery);
	      	    	manageConfigFile.setSceneryRoot(sceneryRoot);
	      	    	manageConfigFile.setFsRoot(fsRoot);
	      	    	manageConfigFile.setFsProgram(fsProgram);
	      	    	manageConfigFile.setQuitAfter(String.valueOf(quitAfter));
	      	    	manageConfigFile.setSceneryFileName(sceneryFileName);
	      	    	
	      	    	manageConfigFile.savePrefProperties();
	      	    	setGuicolor(numColor);
	      	    	setListColor();
	          }
	          setSize(400,400);
		      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		      setLocationRelativeTo(parent);
		      setModal(true);
	          pack();
		}

		/**
		 * 
		 * @author Pierre
		 *
		 */
		private class QuitFslListener implements ItemListener{
			@Override
			public void itemStateChanged(ItemEvent event) {
		    	if (event.getStateChange() == ItemEvent.SELECTED) {
		    		Object item = event.getItem();
		    		 for (int i = 0; i < quitType.length; i++) {
		    			 if (quitType[i].equals(item)){
		    				 quitAfter = i;
		    				 break;
		    			 }
					}
		    	}
			}
		}
		
		/**
		 * 
		 * @author legaulpe
		 *
		 */
		private class ColorListener implements ItemListener {
			private JDialog dialog;
			public ColorListener(JDialog dialog){
				this.dialog = dialog;
			}
		    public void itemStateChanged(ItemEvent event) {
		    	 if (event.getStateChange() == ItemEvent.SELECTED) {
		    		 Object item = event.getItem();
		    		 for (int i = 0; i < colorType.length; i++) {
		    			 if (colorType[i].equals(item)){
		    				 numColor = i;
		    				 break;
		    			 }
					}
		    	 }
		    	 manageConfigFile.getPrefs().setProperty("numcolor", Integer.toString(numColor));
		    	 setGuicolor(numColor);
		    	 setListColor();
		    }
		}

		private class SelectImageListener implements ActionListener {
			JFileChooser fc;
			public SelectImageListener(){
			}
			public void actionPerformed(ActionEvent e) {
		   		String imageDirectory = currentRelativePath.toAbsolutePath().toString()+"\\images";
		   		System.out.println(imageDirectory);
		        if (fc == null) {
		            fc = new JFileChooser();
		            fc.setCurrentDirectory(new java.io.File(imageDirectory));
		            fc.addChoosableFileFilter(new ImageFilter());
		            fc.setAcceptAllFileFilterUsed(false);
		            fc.setFileView(new ImageFileView());
		            fc.setAccessory(new ImagePreview(fc));
		        }

		        //Process the results.
		        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
		            File file = fc.getSelectedFile();
		            manageConfigFile.getPrefs().setProperty("background",file.getName());
		    	    jframe.setContentPane(wrapInBackgroundImage(basePanel,
		    	            new ImageIcon(
		    	            		currentRelativePath.toAbsolutePath().toString()+"\\images\\"+manageConfigFile.getPrefs().getProperty("background"))));//----

		 		   jframe.setVisible(true);
		        } else {
		        }

		        //Reset the file chooser for the next time it's shown.
		        fc.setSelectedFile(null);
		    }
		}
		
		
		private void setGuicolor(int numColor){
			UIManager.put("OptionPane.background", colorBackground[numColor]);
			UIManager.put("OptionPane.foreground", colorForground[numColor]);
			UIManager.put("Panel.background", colorBackground[numColor]);
		    UIManager.put("Panel.foreground", colorForground[numColor]);
 	    	getBasePanel().setBackground(colorBackground[numColor]);
  	    	sourceLabel.setForeground(colorForground[numColor]);
  	    	destLabel.setForeground(colorForground[numColor]);
 
  	    	//form.setBackground(colorBackground[numColor]);
  	    	setBackground(colorBackground[numColor]);
  	    	
  	    	getJpaneSearch().setBackground(colorBackground[numColor]);
  	    	getJpaneLaunch().setBackground(colorBackground[numColor]);
  	    	getCaseSensitive().setBackground(colorBackground[numColor]);
  	    	
  	    	
 
  		  jframe.setVisible(true);
		}
		
		private class OpenOptionListener implements ActionListener {
			private String directory = "Set Up Launcher";
			private String title;
			private String[] extension;
			
			public OpenOptionListener(String title, String directory,String[] extension) {
				this.directory = directory;
				this.title = title;
				this.extension = extension;
			}

			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = null;
				if (extension == null){
					chooser = selectDirectory(prop.getProperty("pref.pop.select")+" "+title, directory);
				} else {
					chooser = selectDirectoryProgram(prop.getProperty("pref.pop.select")+" "+title, directory,extension);
				}
				
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					if (titleFSX.equals(title)){
						//System.out.println(chooser.getSelectedFile());
						fsProgram = chooser.getSelectedFile().getName();
						fsRoot = chooser.getSelectedFile().getPath();
						fsRoot = fsRoot.replace("\\"+fsProgram, "");
					} else if (titleAddon.equals(title)){
						System.out.println(chooser.getSelectedFile());
						addonScenery = chooser.getSelectedFile().toString();
					} else if (titleConfig.equals(title)){
						sceneryFileName = chooser.getSelectedFile().getName();
						sceneryRoot = chooser.getSelectedFile().getPath();
						sceneryRoot = sceneryRoot.replace("\\"+sceneryFileName, "");

					}
			    }
			
			}
		    private JFileChooser selectDirectoryProgram(String title,String directory,String[] extension){
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File(directory));
				chooser.setDialogTitle(title);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				 FileNameExtensionFilter filter=new FileNameExtensionFilter(extension[0],extension);
				 chooser.setFileFilter(filter);
				 chooser.setMultiSelectionEnabled(false);
				
				return chooser;
		    }

		}
	}
	
	/**
	 * 
	 * @author Pierre
	 *
	 */
	
	
	private class SearchSceneryListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			List<Integer> founds = new ArrayList<Integer>();
			InspectArea inspect = InspectArea.getInstance();	
			if ("".equals(searchText.getText().trim())){
				return;
			}
			
			for (int i = 0; i < sourceList.getModel().getSize(); i++) {
				Object item = sourceList.getModel().getElementAt(i);
				try {
					//System.out.println(manageConfigFile.getMapIndexed().get(item).toString());
					if (inspect.isInside(manageConfigFile.getMapSource().get(item), searchText.getText().trim(),caseSensitive.isSelected())){
						founds.add(i);
					}
				} catch (NullPointerException e1) {
				//	System.err.println(manageConfigFile.getMapSource().get(item).toString());
					 ManageStatus.sendError("message/", manageConfigFile.getPrefs().getProperty("fsProgram").replace(".", "_")+":"+(e1.getMessage().replaceAll(" ", "%20")));

				}
			}
			
			String[] listFounds = new String[founds.size()];
			int i = 0;
			for (int found :  founds){
				listFounds[i++] = sourceList.getModel().getElementAt(found).toString();
			}
			     
			sourceList.setSelectedIndices(toPrimitive(founds));
			
			sourceList.setBackground(colorBackground[numColor]); 
			
			new SearchSceneryDialog(jframe, prop.getProperty("search.title"), (founds.size()==0?prop.getProperty("search.mess.err"): founds.size()+" "+prop.getProperty("search.mess.found")),listFounds,fSLauncpad,manageConfigFile, searchText.getText());
		}

		private int[] toPrimitive(List<Integer> IntegerArray) {
			 
			int[] result = new int[IntegerArray.size()];
			for (int i = 0; i < IntegerArray.size(); i++) {
				result[i] = IntegerArray.get(i).intValue();
			}
			return result;
		}
	}
	
	
	/**
	 * 
	 * @author Pierre
	 *
	 */
	private class SearchSceneryDialog extends JDialog {
		private static final long serialVersionUID = 1L;
		JList<String> listFound;
		String areaSearch;
		private SearchSceneryDialog(JFrame parent, String title, String message,String[] data, FSLaunchPad fSLauncpad,ManageConfigFile configFile, String areaSearch ) {
	        super(parent, title);

    		contribute.callDialog();

	        setLayout(new GridBagLayout());
	        //setBackground(colorBack[numColor]);

	        listFound = new JList<String>(data);
	        listFound.setBackground(colorBackground[numColor]);
	        this.areaSearch = areaSearch;
	        
	        int start = 0;
	        int end = listFound.getModel().getSize() - 1;
	        if (end >= 0) {
	        	listFound.setSelectionInterval(start, end);
	        }

//	        getContentPane().setBackground(colorBack[numColor]);
	        getContentPane().add(getLabel(message), new GridBagConstraints(0, 0, 1, 1, 0, 0,
	            GridBagConstraints.CENTER, GridBagConstraints.NONE,
	            INSETS_LABEL_SEARCH, 0, 0));
	        getContentPane().add(new JScrollPane(listFound), new GridBagConstraints(0, 1, 1, 5, .1,
	                1, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
	                INSETS_LABEL_SEARCH, 0, 0));

	        JPanel buttonAddPane = new JPanel();
//	        buttonAddPane.setBackground(colorBack[numColor]);
	        JButton buttonAdd = new JButton(prop.getProperty("search.btn.add"));
	        buttonAddPane.add(buttonAdd);
	        getContentPane().add(buttonAddPane, new GridBagConstraints(1, 1, 1, 1, 0, .1,
	                GridBagConstraints.CENTER, GridBagConstraints.NONE,
	                INSETS_LABEL_SEARCH, 0, 0));

	        buttonAdd.addActionListener(new AddActionListener(listFound,areaSearch));

		    JPanel buttonClosePane = new JPanel();
//		    buttonClosePane.setBackground(colorBack[numColor]);
	        JButton buttonClose = new JButton(prop.getProperty("search.btn.close"));
	        buttonClosePane.add(buttonClose);
	        getContentPane().add(buttonClosePane, new GridBagConstraints(1, 2, 1, 2, 0, .1,
	                GridBagConstraints.CENTER, GridBagConstraints.NONE,
	                INSETS_LABEL_SEARCH, 0, 0));
	        buttonClose.addActionListener(new CloseActionListener());
	        
	       // getContentPane().add(buttonReset);
	        
        
	        setSize(400,250);
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);

	        setResizable(true);
	        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
//	        /pack();
	        setModal(true);
	        getRootPane().setDefaultButton(buttonAdd);
	        buttonAdd.requestFocus();
	        setVisible(true);
	    }
		
	    
	    public JRootPane createRootPane() {
	        JRootPane rootPane = new JRootPane();
	        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER|KeyEvent.VK_ESCAPE, 0);
	        Action action = new AbstractAction() {
	            private static final long serialVersionUID = 1L;
	            public void actionPerformed(ActionEvent e) {
	            	//if (stroke.equals(KeyEvent.VK_ENTER))
	                setVisible(false);
	                dispose();
	            }
	        };
	        if (stroke.equals(KeyEvent.VK_ENTER)){
            	transfertResult(listFound, areaSearch);		           
            	clearFoundSelected(listFound);
	        }
	        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	        inputMap.put(stroke, "ESCAPE");
	        rootPane.getActionMap().put("ESCAPE", action);
	        return rootPane;
	    }
	 
	    class CloseActionListener implements ActionListener {
	        //close and dispose of the window.
	        public void actionPerformed(ActionEvent e) {
	            setVisible(false);
	            dispose();
	        }
	    }
	   
	    public class AddActionListener implements ActionListener {
	    	JList <String>myList; 
	    	String areaSearch;
	    	JDialog jdialog;
	    	public AddActionListener(JList <String>myList, String areaSearch){
	    		this.myList = myList;
	    		this.areaSearch = areaSearch;
	    	}
	        public void actionPerformed(ActionEvent e) {
	        	if (myList.getModel().getSize() > 0){
		        	transfertResult(myList, areaSearch);
		            setVisible(false);
		            dispose();
	        	}
	        }
	    }
	    
	}

    private void transfertResult(JList <String>myList, String areaSearch){
        Object selected[] = myList.getSelectedValuesList().toArray();
        for (int i = 0; i < selected.length; i++) {
      	    String before = selected[i].toString();
         	 if (alphabOrder.isSelected()){
              selected[i] = Util.formatAreaNum(manageConfigFile.getMapSource().get(selected[i].toString()).getNum()+"", manageConfigFile.getMapAllScenery().size()+1) +"-"+ selected[i];
         	 }
        	 manageConfigFile.getMapDestination().put(selected[i].toString(),manageConfigFile.getMapSource().get(before));
        	 manageConfigFile.getMapSource().remove(before);
        }
        
        if (!"".equals(areaSearch)){
            if (!"".equals(manageConfigFile.getAreaName())){
            	areaSearch = manageConfigFile.getAreaName()+"-"+areaSearch;
            }
            manageConfigFile.setAreaName(areaSearch);
            destLabel.setText(DEFAULT_DEST_CHOICE_LABEL+" "+areaSearch);
        }
       
        
        addDestinationElements(selected);
        clearFoundSelected(myList);
    }
    
    public void clearFoundSelected(JList<String> listFound ) {
        Object selected[] = listFound.getSelectedValuesList().toArray();
        for (int i = selected.length - 1; i >= 0; --i) {
        	sourceListModel.removeElement(selected[i]);
        }
        listFound.getSelectionModel().clearSelection();
    	sourceList.getSelectionModel().clearSelection();
      }

	public class ExitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	public class GoogleSearch implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			googleSearch(jframe, "");
		}
	}
	
	 MouseListener mouseListener = new MouseAdapter() {
	      @SuppressWarnings({"rawtypes", "unchecked" })
		public void mouseClicked(MouseEvent mouseEvent) {
	        JList <String> theList = (JList) mouseEvent.getSource();
	        if (mouseEvent.getClickCount() == 2) {
	          int index = theList.locationToIndex(mouseEvent.getPoint());
	          if (index >= 0) {
	            Object o = theList.getModel().getElementAt(index);
	             //System.out.println("Double-clicked on: " + o.toString());
	            popupArea(jframe, o.toString(), manageConfigFile.getGoodArea(o)) ;
	          }
	        }
	      }
	    };

	    
		private void popupArea(JFrame parent, String title, Area area){
			
			
			if (area.getNum().contains("General")){
				String last ="<html>Clean_on_Exit : "+area.getCleanOnExit()+((area.getCashSize() != null)?"<br>Cache_Size : "+area.getCashSize():""); 
				 Object[] message = {  
			 	    		getLabel("Title  : "+area.getTitle()),  
			 	    		getLabel("Description : "+area.getDescription()),
			 	    		getLabel(last)
			 	    		
				 };
				JOptionPane.showMessageDialog(null, message);
				return;
			}
		   // JButton modifButton = new JButton("Modify");
		   //modifButton.addActionListener(new ModifyScenery(jframe, area, directory));

			Object[] butYesNo = {prop.getProperty("button.yes"),prop.getProperty("button.no")}; 
			
			String last ="<html>Required : "+area.getRequired()+((area.getExclude() != null)?"<br>Exclude : "+area.getExclude():"")+((area.getTexture() != null)?"<br>Texture_ID : "+area.getTexture():""); 

			Object[] buttons = { prop.getProperty("scen.btn.edit"), prop.getProperty("scen.btn.det"), prop.getProperty("scen.btn.move"), prop.getProperty("scen.btn.explore"), prop.getProperty("scen.btn.search") , prop.getProperty("scen.btn.quit")}; 
	 	    Object[] message = {  
	 	    		getLabel("Area  : "+area.getNum()),  
	 	    		getLabel("Local : "+area.getLocal()),
	 	    		getLabel("Active : "+area.getActive()),
	 	    		getLabel(last)
	 		   };  

	 	      int returnValue = JOptionPane.showOptionDialog(parent, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);

	 	     if (returnValue == 0){
	 	    	 //new ModifyScenery(jframe, area, directory);\
	 	    	UpdateScenery updateScenery = new UpdateScenery(jframe, area, title);
	 	    	updateScenery.setVisible(true);
	 			alphabOrder.setSelected(false);

		 	  } else if (returnValue == 1){
		 		 if (JOptionPane.showOptionDialog(null,getLabel( prop.getProperty("scen.mess.remove")+" ("+manageConfigFile.getGoodArea(title).getTitle()+")?"),prop.getProperty("scen.title")+" "+area.getTitle(), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, butYesNo, butYesNo[0]) == 0){
			 			 manageConfigFile.deleteScenery(title);
			 			 alphabOrder.setSelected(false);
			 			 manageConfigFile.setAreaName("");
			 			 destLabel.setText(DEFAULT_DEST_CHOICE_LABEL);

			 			 resetArea();
			 			 resetArea();
			 		 }
		 	  } else if (returnValue == 2){
					JFileChooser chooser = selectDirectory(prop.getProperty("scen.dialog.title")+" "+area.getTitle(),manageConfigFile.getPrefs().getProperty("addonScenery"));
						if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				 	  if (Util.isDirectoryIsMovable(chooser.getSelectedFile())){
					 		 if (JOptionPane.showOptionDialog(null,getLabel(prop.getProperty("scen.dialog.confirm")+" "+chooser.getSelectedFile().getName()),prop.getProperty("scen.title")+" "+manageConfigFile.getGoodArea(title).getTitle(), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, butYesNo, butYesNo[0]) == 0){
					 			 try {
									manageConfigFile.moveSceneryLocation(title, chooser.getSelectedFile().getPath());
						 			 alphabOrder.setSelected(false);
						 			 //System.out.println(manageConfigFile.getGoodArea(title)+" moved");
								} catch (Exception e) {
									JOptionPane.showMessageDialog(null, getLabel(prop.getProperty("scen.err.move1")+prop.getProperty("scen.err.move2")));
								}
					 		 }
						  
					  } else {
							JOptionPane.showMessageDialog(null, getLabel(prop.getProperty("scen.err.move1")+prop.getProperty("scen.err.move3")));
					  }
				  
					}
		 		 //new SelectDirectoryTree(parent,directory,manageConfigFile,area.getLocal());
		 	  } else if (returnValue == 3){
		 		 manageConfigFile.openExplorer(area.getLocal());
		 	  } else if (returnValue == 4){
			 	 googleSearch(jframe,area.getTitle());
		 	  } 
		}
		


		
		public void googleSearch(JFrame parent, String searchText){
		   String[] btGoogle = { "Google Web",  "Google Maps", "Google Earth", prop.getProperty("scen.btn.quit")}; 
		 //  String[] trueFalse= {"ICAO(s)","Country,City, Place"};
		   
			contribute.callDialog();
	   
		   JTextField criteria = new JTextField();
		   criteria.setText(searchText.trim());
		   
		   Object[] criteriaChoice = {getLabel("Enter Criteria"),criteria  };
 		   int returnchoice = JOptionPane.showOptionDialog(parent, criteriaChoice, prop.getProperty("scen.google.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, btGoogle, btGoogle[0]);
 		    if (returnchoice == 1 ||returnchoice == 0){
		 		  Util.openWebpage(criteria.getText().trim(),returnchoice);
 		    } else if (returnchoice == 2){
 		    	new SearchDialog(jframe,criteria.getText().trim());
 		    }
		
	}
	   //=========================================================== 
    private class SearchDialog extends JDialog 
    {
      /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JTextField fieldCriteria;
		private JFrame jframe;
		private JComboBox <String> searchChoice;
     
        SearchDialog(JFrame parent, String criteria) {
    	   super(parent, "Google Earth Seach");
    	   setSize(400,400);
           setResizable(false);  
//           setBackground(colorBack[numColor]);

           this.jframe = parent;
           
           searchDialog(parent, criteria);
        }
		 /** @return the jframe
		 */
		public JFrame getJframe() {
			return jframe;
		}
		/**
		 * @param jframe the jframe to set
		 */
		
		private void searchDialog(JFrame parent, String criteria){
			
		   prop = manageConfigFile.getProp();
		   Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		   setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);

//			   setBackground(colorBack[numColor]);
          JPanel form = new JPanel();
          parent.getContentPane().setLayout(new BorderLayout());
          parent.getContentPane().add(form, BorderLayout.WEST);

          form.setLayout(new GridBagLayout());
          FormUtility formUtility = new FormUtility();
          
          formUtility.addLabel("Criteria : ", form,colorForground[numColor],fontText);
          fieldCriteria = new JTextField();
          fieldCriteria.setText(criteria);
          Dimension titleSize = fieldCriteria.getPreferredSize();
          titleSize.width = 300;
          fieldCriteria.setPreferredSize(titleSize);
          JPanel titlePanel = new JPanel();
          titlePanel.setLayout(new BorderLayout());
          titlePanel.add(fieldCriteria, BorderLayout.WEST);
          formUtility.addLastField(titlePanel, form);
          
          String[] someChoise= {"ICAOs","IATA",prop.getProperty("scen.earth.choice3")};
          
          searchChoice    = new JComboBox<String>(someChoise);
          searchChoice.setSelectedItem("ICAOs");

          formUtility.addLabel("Search Type : ", form,colorForground[numColor],fontText);
          Dimension searchSize = searchChoice.getPreferredSize();
          searchSize.width = 200;
          searchChoice.setPreferredSize(searchSize);
          JPanel activePanel = new JPanel();
          activePanel.setLayout(new BorderLayout());
          activePanel.add(searchChoice, BorderLayout.WEST);
          formUtility.addLastField(activePanel, form);
          
          Object[] buttons = {prop.getProperty("scen.btn.search"),prop.getProperty("scen.btn.reset"),prop.getProperty("scen.btn.quit")};

 		   int returnchoice = JOptionPane.showOptionDialog(jframe, form, prop.getProperty("scen.google.search.title"), JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
 		   
 		   if (returnchoice == 0){
            	 if (searchChoice.getSelectedIndex() == 1){//ICAO
            		 List<Placemark> placemarks = manageXMLFile.seachAirport(fieldCriteria.getText(), 0); 
            		 if (placemarks.size() > 0){
            		    	if (searchChoice.getSelectedIndex() == 1 && fieldCriteria.getText().length() > 3 || Util.isHaveNumber(fieldCriteria.getText())){
            		    		JOptionPane.showMessageDialog(null, getLabel(prop.getProperty("scen.google.iata.mess")));
            		    		return;
       	 		    		}
            		 }
            	 }
            	 String searchStr = Geoinfo.stripSearchText(fieldCriteria.getText().trim());
		    	 List<Placemark> placemarks = manageXMLFile.seachAirport(searchStr, searchChoice.getSelectedIndex());
 		    	 if (placemarks.size() > 0){
 		    		int option = JOptionPane.showConfirmDialog(parent, getLabel("<html>"+placemarks.size() +" "+prop.getProperty("scen.google.found.mess1")+" "+fieldCriteria.getText()+prop.getProperty("scen.google.found.mess2")), "Warning", JOptionPane.YES_OPTION);
 		    		if (option == 0) { 
 	                     manageXMLFile.createKMLFile();
                        setVisible(false); 
 		    		} 		    		
 		    	 } else {
 		    		JOptionPane.showMessageDialog(getJframe(), getLabel(prop.getProperty("scen.google.not.found")+" "+fieldCriteria.getText()));
 		    		searchDialog(parent, fieldCriteria.getText().trim()); 
 		    		//setVisible(false); 
 		    	 }
		   
           } else if (returnchoice == 1){
        	   searchDialog(parent, "");
           } else if (returnchoice == 2 || returnchoice == -1){
              setVisible(false); 
           } 
		}
	
    }
	
    /**
     * 
     * @author Pierre
     *
     */
    public class AddSceneryListener extends AddListener{
		public void actionPerformed(ActionEvent e) {
			addScenery();
		}	
    	
    }
   
    /**
     * Add new sceneries 
     * 
     * 
     */
	private void addScenery (){
		JFileChooser chooser = selectDirectory(prop.getProperty("scen.add.title"),manageConfigFile.getPrefs().getProperty("addonScenery"));
		chooser.setPreferredSize(new Dimension(650, 400));
		chooser.setApproveButtonText(prop.getProperty("pad.btn.add"));
		chooser.setApproveButtonToolTipText(prop.getProperty("scen.add.title"));
		chooser.setMultiSelectionEnabled(true);
		chooser.setToolTipText(prop.getProperty("scen.add.title"));
		
		contribute.callDialog();

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			String message = manageConfigFile.addArea(chooser.getSelectedFiles());
			
			JOptionPane.showMessageDialog(null, getLabel(message));

	    	manageConfigFile.setAreaName("");
	    	destLabel.setText(DEFAULT_DEST_CHOICE_LABEL);
			
			resetArea();resetArea(); setVisible(true);
			alphabOrder.setSelected(false);

		}
	}
			
	
/**
 * 	UpdateScenery
 * 
 * @author Pierre
 *
 */
    class UpdateScenery extends JDialog 
    {
		private static final long serialVersionUID = 1L;      
		private JTextField fieldTitle;
		private String title;
		
		private boolean isSource;
		private Area area;
		private String[] trueFalse= {"TRUE","FALSE"};
		private JComboBox <String>activeChoice;
	    private JComboBox <String>requieredChoice;
    
		UpdateScenery(JFrame parent,Area area, String title) {
    	  super(parent, prop.getProperty("modif.top.title")+" ("+area.getTitle()+")");
          
          this.area = area;
          this.title = title;
          
          try {
			isSource = (sourceListModel.getElementAt(sourceList.getSelectedIndex()).equals(title));
		  } catch (ArrayIndexOutOfBoundsException e) {
			isSource = false;
		  }

          JPanel form = new JPanel();
          getContentPane().setLayout(new BorderLayout());
          getContentPane().add(form, BorderLayout.WEST);

          // Set the form panel's layout to GridBagLayout
          // and create a FormUtility to add things to it.
          form.setLayout(new GridBagLayout());
          FormUtility formUtility = new FormUtility();
          
          formUtility.addLabel(prop.getProperty("modif.title")+": ", form,colorForground[numColor],fontText);
          fieldTitle = new JTextField();
          fieldTitle.setText(area.getTitle());
          Dimension titleSize = fieldTitle.getPreferredSize();
          titleSize.width = 150;
          fieldTitle.setPreferredSize(titleSize);
          JPanel titlePanel = new JPanel();
          titlePanel.setLayout(new BorderLayout());
          titlePanel.add(fieldTitle, BorderLayout.WEST);
          formUtility.addLastField(titlePanel, form);
          
	      formUtility.addLabel("Local: ", form,colorForground[numColor],fontText);
	      formUtility.addLastField(getLabel(area.getLocal()), form);

          activeChoice    = new JComboBox<String>(trueFalse);
          activeChoice.setSelectedItem(area.getActive());

          formUtility.addLabel("Active: ", form,colorForground[numColor],fontText);
          Dimension activeSize = activeChoice.getPreferredSize();
          activeSize.width = 66;
          activeChoice.setPreferredSize(activeSize);
          JPanel activePanel = new JPanel();
          activePanel.setLayout(new BorderLayout());
          activePanel.add(activeChoice, BorderLayout.WEST);
          formUtility.addLastField(activePanel, form);
            
          requieredChoice = new JComboBox<String>(trueFalse);
          requieredChoice.setSelectedItem(area.getRequired());
          formUtility.addLabel(prop.getProperty("modif.required")+": ", form,colorForground[numColor],fontText);
          Dimension requiredSize = requieredChoice.getPreferredSize();
          requiredSize.width = activeSize.width;
          requieredChoice.setPreferredSize(requiredSize);
          JPanel requiredPanel = new JPanel();
          requiredPanel.setLayout(new BorderLayout());
          requiredPanel.add(requieredChoice, BorderLayout.WEST);
          formUtility.addLastField(requiredPanel, form);
          
          Object[] choix = {prop.getProperty("modif.btn.save"),prop.getProperty("modif.btn.quit")};
          
          final JOptionPane editDialog = new JOptionPane(form,
                                JOptionPane.INFORMATION_MESSAGE,
                                JOptionPane.OK_CANCEL_OPTION,
                        null, choix);


/*	      Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	      setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);
*/	        
          setSize(400,200);
          Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
          setLocation(dim.width/2 - getWidth()/2, dim.height/2 - getHeight()/2);

          setContentPane(editDialog);
	      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	      setModal(true);
          pack();

			editDialog.addPropertyChangeListener(new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent e) {
					//System.out.println("-->" + editDialog.getValue());
					if (editDialog.getValue().equals(prop.getProperty("modif.btn.save"))) {
						update();
					} else if (editDialog.getValue().equals(prop.getProperty("modif.btn.quit"))) {
					}
					setVisible(false);
				}
			});
			addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.out.println("fermeture");
				}
			});
          
        }
		
		private void update(){
 	      boolean isSameRequired = (area.getRequired().equals(trueFalse[requieredChoice.getSelectedIndex()]));
 	    	
		  area.setAreaModified(fieldTitle.getText(),trueFalse[activeChoice.getSelectedIndex()], trueFalse[requieredChoice.getSelectedIndex()]);
		  manageConfigFile.saveModifyScenery(area);
		
		  String newName;
		 if (isSource){
			 if (alphabOrder.isSelected()){
				 newName = area.getTitle();  
				 
			 } else {
				 newName = title.substring(0, title.indexOf("-"))+"-"+area.getTitle();  
				 
			 }
			 sourceListModel.removeElement(title); 
			 sourceListModel.add(newName); 
			 manageConfigFile.getMapSource().remove(title);
			 manageConfigFile.getMapSource().put(newName, area);
			 if (!isSameRequired){
				 Object selected[] = sourceList.getSelectedValuesList().toArray();
			     addDestinationElements(selected);
			     clearSourceSelected();
				 
			 }
		 } else {
			 newName = title.substring(0, title.indexOf("-"))+"-"+area.getTitle();  
			 destListModel.removeElement(title); 
			 destListModel.add(newName); 
			 manageConfigFile.getMapDestination().remove(title);
			 manageConfigFile.getMapDestination().put(newName, area);
		     if (!isSameRequired){
				 Object selected[] = destList.getSelectedValuesList().toArray();
			     addSourceElements(selected);
			     clearDestinationSelected();
		     }
		 }
		}
    }
    
    private JFileChooser selectDirectory(String title,String directory){
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(directory));
		chooser.setDialogTitle(title);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		
		return chooser;
    	
    }

    private JLabel getLabel(String text){
    	JLabel label = new JLabel();
    	label.setText(text);
    	label.setForeground(colorForground[numColor]);
    	label.setFont(fontText);
    	return label;
    }
    
	/**
	 * @return the sourceList
	 */
	public JList<String> getSourceList() {
		return sourceList;
	}

	/**
	 * @param sourceList the sourceList to set
	 */
	public void setSourceList(JList  <String>sourceList) {
		this.sourceList = sourceList;
	}

	/**
	 * @return the sourceListModel
	 */
	public SortedListModel getSourceListModel() {
		return sourceListModel;
	}

	/**
	 * @param sourceListModel the sourceListModel to set
	 */
	public void setSourceListModel(SortedListModel sourceListModel) {
		this.sourceListModel = sourceListModel;
	}

	/**
	 * @return the destList
	 */
	public JList <String>getDestList() {
		return destList;
	}

	/**
	 * @param destList the destList to set
	 */
	public void setDestList(JList <String>destList) {
		this.destList = destList;
	}

	/**
	 * @return the destListModel
	 */
	public SortedListModel getDestListModel() {
		return destListModel;
	}

	/**
	 * @param destListModel the destListModel to set
	 */
	public void setDestListModel(SortedListModel destListModel) {
		this.destListModel = destListModel;
	}

	/**
	 * @return the jpaneSearch
	 */
	public JPanel getJpaneSearch() {
		return jpaneSearch;
	}

	/**
	 * @param jpaneSearch the jpaneSearch to set
	 */
	public void setJpaneSearch(JPanel jpaneSearch) {
		this.jpaneSearch = jpaneSearch;
	}

	/**
	 * @return the jpaneLaunch
	 */
	public JPanel getJpaneLaunch() {
		return jpaneLaunch;
	}

	/**
	 * @param jpaneLaunch the jpaneLaunch to set
	 */
	public void setJpaneLaunch(JPanel jpaneLaunch) {
		this.jpaneLaunch = jpaneLaunch;
	}

	/**
	 * @return the caseSensitive
	 */
	public JCheckBox getCaseSensitive() {
		return caseSensitive;
	}

	/**
	 * @param caseSensitive the caseSensitive to set
	 */
	public void setCaseSensitive(JCheckBox caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/**
	 * @return the basePanel
	 */
	public static JPanel getBasePanel() {
		return basePanel;
	}

	/**
	 * @param basePanel the basePanel to set
	 */
	public static void setBasePanel(JPanel basePanel) {
		FSLaunchPad.basePanel = basePanel;
	}

	public static JFrame getJframe() {
		return jframe;
	}

	public JCheckBox getShowDefaultScenery() {
		return showDefaultScenery;
	}

	public void setShowDefaultScenery(JCheckBox showDefaultScenery) {
		this.showDefaultScenery = showDefaultScenery;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getCurrentLangue() {
		return currentLangue;
	}

	public void setCurrentLangue(String currentLangue) {
		this.currentLangue = currentLangue;
	}



}



class SortedListModel extends AbstractListModel<Object> {

  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
   private SortedSet<Object> model;

  public SortedListModel() {
    model = new TreeSet<Object>();
  }

  public int getSize() {
    return model.size();
  }

  public Object getElementAt(int index) {
    return model.toArray()[index];
  }

  public void add(Object element) {
    if (model.add(element)) {
      fireContentsChanged(this, 0, getSize());
    }
  }

  public void addAll(Object elements[]) {
    Collection<Object> c = Arrays.asList(elements);
    model.addAll(c);
    fireContentsChanged(this, 0, getSize());
  }

  public void clear() {
    model.clear();
    fireContentsChanged(this, 0, getSize());
  }

  public boolean contains(Object element) {
    return model.contains(element);
  }

  public Object firstElement() {
    return model.first();
  }

  public Iterator<Object> iterator() {
    return model.iterator();
  }

  public Object lastElement() {
    return model.last();
  }

  public boolean removeElement(Object element) {
    boolean removed = model.remove(element);
    if (removed) {
      fireContentsChanged(this, 0, getSize());
    }
    return removed;
  }
  
  
  
  
}
