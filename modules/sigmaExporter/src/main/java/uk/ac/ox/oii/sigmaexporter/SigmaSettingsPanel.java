/*
 Copyright Scott A. Hale, 2016
 * 
 
 Base on code from 
 Copyright 2008-2016 Gephi
 Authors : Mathieu Bastian <mathieu.bastian@gephi.org>
 Website : http://www.gephi.org

 Portions Copyrighted 2011 Gephi Consortium.
 */
package uk.ac.ox.oii.sigmaexporter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import org.openide.util.NbPreferences;
import org.openide.windows.WindowManager;
import uk.ac.ox.oii.sigmaexporter.model.ConfigFile;

public class SigmaSettingsPanel extends javax.swing.JPanel {

    //final String LAST_PATH = "SQLiteDatabaseSettingsPanel_Last_Path";
    private File path;
    private SigmaExporter exporter;

    /** Creates new form SQLiteDatabaseSettingsPanel */
    public SigmaSettingsPanel() {
        setMaximumSize(new java.awt.Dimension(924, 752));
        setMinimumSize(new java.awt.Dimension(924, 752));
        setSize(new java.awt.Dimension(924, 752));
        setPreferredSize(new java.awt.Dimension(924, 752));
        
        initComponents();

        browseButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(pathTextField.getText());
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                //DialogFileFilter dialogFileFilter = new DialogFileFilter("SQLite files");
                //dialogFileFilter.addExtensions(new String[] {".sqlite"});
                //fileChooser.addChoosableFileFilter(dialogFileFilter);
                //fileChooser.setAcceptAllFileFilterUsed(false);
                int result = fileChooser.showSaveDialog(WindowManager.getDefault().getMainWindow());
                if (result == JFileChooser.APPROVE_OPTION) {
                    path = fileChooser.getSelectedFile();
                    pathTextField.setText(path.getAbsolutePath());
                }
            }
        });
    }

    public void setup(SigmaExporter exporter) {
        this.exporter = exporter;
        //path = new File(NbPreferences.forModule(SigmaSettingsPanel.class).get(LAST_PATH, System.getProperty("user.home")+"/sigma"));
        //pathTextField.setText(path.getAbsolutePath());
        
        //Checkbox for each node attribute (should it be included)
        /*javax.swing.JCheckBox b = new javax.swing.JCheckBox("b");
        a.setText("bbb");
        a.setVisible(true);
        //attributesPanel.add(a);
        attributesPanel.setViewportView(b);*/
       
        List<String> attributes = exporter.getNodeAttributes();
        
        for (String a : attributes) {
            ddGroupSelector.addItem(a);
            ddImageAttribute.addItem(a);
            //TODO: Also create check box for attributesPanel
            //javax.swing.JCheckBox cb = new javax.swing.JCheckBox();
            //cb.setText(a);
            //attributesPanel.add(cb);
        }
        //attributesScrollPanel.setViewportView(attributesPanel);
        
        Preferences prefs = NbPreferences.forModule(SigmaSettingsPanel.class);
        pathTextField.setText(prefs.get("path", ""));
        txtNode.setText(prefs.get("legend.node", ""));
        txtEdge.setText(prefs.get("legend.edge",""));
        txtColor.setText(prefs.get("legend.color",""));
        cbSearch.setSelected(Boolean.valueOf(prefs.get("features.search","true")));
        ddGroupSelector.setSelectedItem(prefs.get("features.groupSelectAttribute", "None"));
        ddImageAttribute.setSelectedItem(prefs.get("informationPanel.imageAttribute","None"));
        cbGroupEdges.setSelected(Boolean.valueOf(prefs.get("informationPanel.groupByEdgeDirection","false")));
        txtShort.setText(prefs.get("text.intro", ""));
        txtLong.setText(prefs.get("text.more", ""));
        txtTitle.setText(prefs.get("text.title", ""));        
        txtLogo.setText(prefs.get("logo.file", ""));        
        txtLink.setText(prefs.get("logo.link", ""));        
        txtAuthor.setText(prefs.get("logo.author", ""));
        cbRenumber.setSelected(Boolean.valueOf(prefs.get("renumber","true")));
        
        // Edges
        cbShowEdges.setSelected(Boolean.valueOf(prefs.get("edges.show","true")));
        cbShowEdgeLabel.setSelected(Boolean.valueOf(prefs.get("edges.showLabel","true")));
        ddEdgeType.setSelectedItem(prefs.get("edges.type", "Line"));
        ddEdgeColorMode.setSelectedItem(prefs.get("edges.colorMode", "Use source node color"));
        
        // Nodes
        cbShowNodes.setSelected(Boolean.valueOf(prefs.get("nodes.show","true")));
        cbShowNodeLabel.setSelected(Boolean.valueOf(prefs.get("nodes.showLabel","true")));
        ddHover.setSelectedItem(prefs.get("features.hoverBehavior", "None"));
    }

    public void unsetup(boolean update) {
        //HashMap<String,String> props = new HashMap<String,String>();
        Preferences props = NbPreferences.forModule(SigmaSettingsPanel.class);
        String path="";
        boolean renumber = false;
        if (update) {
            try {
                path = pathTextField.getText();
                renumber = cbRenumber.isSelected();
                props.put("path",path);
                props.put("renumber", String.valueOf(renumber));
                props.put("legend.node",txtNode.getText());
                props.put("legend.edge",txtEdge.getText());
                props.put("legend.color",txtColor.getText());
                props.put("features.search",String.valueOf(cbSearch.isSelected()));
                props.put("features.groupSelectAttribute",String.valueOf(ddGroupSelector.getSelectedItem()));
                props.put("informationPanel.imageAttribute",String.valueOf(ddImageAttribute.getSelectedItem()));
                props.put("informationPanel.groupByEdgeDirection",String.valueOf(cbGroupEdges.isSelected()));
                props.put("text.intro",txtShort.getText());
                props.put("text.more",txtLong.getText());
                props.put("text.title",txtTitle.getText());
                props.put("logo.file",txtLogo.getText());
                props.put("logo.link",txtLink.getText());
                props.put("logo.author",txtAuthor.getText());
                
                // Edges
                props.put("edges.show",String.valueOf(cbShowEdges.isSelected()));
                props.put("edges.showLabel",String.valueOf(cbShowEdgeLabel.isSelected()));
                props.put("edges.type",String.valueOf(ddEdgeType.getSelectedItem()));
                props.put("edges.colorMode",String.valueOf(ddEdgeColorMode.getSelectedItem()));
                
                // Nodes
                props.put("nodes.show",String.valueOf(cbShowNodes.isSelected()));
                props.put("nodes.showLabel",String.valueOf(cbShowNodeLabel.isSelected()));
                props.put("features.hoverBehavior",String.valueOf(ddHover.getSelectedItem()));
                
            } catch (Exception e) {
                Logger.getLogger(SigmaExporter.class.getName()).log(Level.SEVERE, null, e);
            }
            ConfigFile cfg = new ConfigFile();
            cfg.readFromPrefs(props);
            exporter.setConfigFile(cfg,path,renumber);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        pathTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNode = new javax.swing.JTextField();
        txtEdge = new javax.swing.JTextField();
        txtColor = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        cbShowNodes = new javax.swing.JCheckBox();
        ddHover = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtShort = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtLong = new javax.swing.JTextArea();
        txtLogo = new javax.swing.JTextField();
        txtLink = new javax.swing.JTextField();
        txtAuthor = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        ddGroupSelector = new javax.swing.JComboBox();
        cbGroupEdges = new javax.swing.JCheckBox();
        ddImageAttribute = new javax.swing.JComboBox();
        jLabel16 = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        cbUseGraphBg = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        cbSearch = new javax.swing.JCheckBox();
        cbShowNodeLabel = new javax.swing.JCheckBox();
        cbShowEdges = new javax.swing.JCheckBox();
        cbShowEdgeLabel = new javax.swing.JCheckBox();
        ddEdgeType = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        cbRenumber = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        ddEdgeColorMode = new javax.swing.JComboBox();

        setMaximumSize(new java.awt.Dimension(924, 752));
        setMinimumSize(new java.awt.Dimension(924, 752));
        setSize(new java.awt.Dimension(924, 752));

        jLabel1.setFont(jLabel1.getFont().deriveFont(jLabel1.getFont().getStyle() | java.awt.Font.BOLD, jLabel1.getFont().getSize()+3));
        jLabel1.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel1.text")); // NOI18N

        pathTextField.setText("\n"); // NOI18N

        browseButton.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.browseButton.text")); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        jLabel2.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel2.text")); // NOI18N

        jLabel3.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel3.text")); // NOI18N

        jLabel4.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel4.text")); // NOI18N

        jLabel5.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel5.text")); // NOI18N

        txtNode.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.txtNode.text")); // NOI18N
        txtNode.setToolTipText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.txtNode.toolTipText")); // NOI18N

        txtEdge.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.txtEdge.text")); // NOI18N
        txtEdge.setToolTipText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.txtEdge.toolTipText")); // NOI18N
        txtEdge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEdgeActionPerformed(evt);
            }
        });

        txtColor.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.txtColor.text")); // NOI18N
        txtColor.setToolTipText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.txtColor.toolTipText")); // NOI18N

        jLabel6.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel6.text")); // NOI18N

        jLabel8.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel8.text")); // NOI18N

        cbShowNodes.setSelected(true);
        cbShowNodes.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.cbShowNodes.text")); // NOI18N
        cbShowNodes.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbShowNodes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbShowNodesActionPerformed(evt);
            }
        });

        ddHover.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None (Default)", "Dim" }));

        jLabel7.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel7.text")); // NOI18N

        jLabel10.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel10.text")); // NOI18N

        jLabel11.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel11.text")); // NOI18N

        jLabel12.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel12.text")); // NOI18N

        txtShort.setColumns(20);
        txtShort.setRows(5);
        jScrollPane2.setViewportView(txtShort);

        txtLong.setColumns(20);
        txtLong.setRows(5);
        jScrollPane3.setViewportView(txtLong);

        txtLogo.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.txtLogo.text")); // NOI18N

        txtLink.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.txtLink.text")); // NOI18N

        txtAuthor.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.txtAuthor.text")); // NOI18N

        jLabel13.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel13.text")); // NOI18N

        jLabel14.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel14.text")); // NOI18N

        jLabel15.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel15.text")); // NOI18N

        ddGroupSelector.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None", "color" }));
        ddGroupSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddGroupSelectorActionPerformed(evt);
            }
        });

        cbGroupEdges.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.cbGroupEdges.text")); // NOI18N
        cbGroupEdges.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbGroupEdges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbGroupEdgesActionPerformed(evt);
            }
        });

        ddImageAttribute.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None (Default)" }));

        jLabel16.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel16.text")); // NOI18N

        txtTitle.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.txtTitle.text")); // NOI18N

        jLabel17.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel17.text")); // NOI18N

        cbUseGraphBg.setSelected(true);
        cbUseGraphBg.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.cbUseGraphBg.text")); // NOI18N
        cbUseGraphBg.setToolTipText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.cbUseGraphBg.toolTipText")); // NOI18N
        cbUseGraphBg.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbUseGraphBg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbUseGraphBgActionPerformed(evt);
            }
        });

        jLabel19.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel19.text")); // NOI18N

        jLabel20.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel20.text")); // NOI18N

        jLabel21.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel21.text")); // NOI18N

        cbSearch.setSelected(true);
        cbSearch.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.cbSearch.text")); // NOI18N
        cbSearch.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSearchActionPerformed(evt);
            }
        });

        cbShowNodeLabel.setSelected(true);
        cbShowNodeLabel.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.cbShowNodeLabel.text")); // NOI18N
        cbShowNodeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbShowNodeLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbShowNodeLabelActionPerformed(evt);
            }
        });

        cbShowEdges.setSelected(true);
        cbShowEdges.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.cbShowEdges.text")); // NOI18N
        cbShowEdges.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbShowEdges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbShowEdgesActionPerformed(evt);
            }
        });

        cbShowEdgeLabel.setSelected(true);
        cbShowEdgeLabel.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.cbShowEdgeLabel.text")); // NOI18N
        cbShowEdgeLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbShowEdgeLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbShowEdgeLabelActionPerformed(evt);
            }
        });

        ddEdgeType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Line", "Arrow", "Curved", "Curved Arrow", "Dashed", "Dotted" }));
        ddEdgeType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddEdgeTypeActionPerformed(evt);
            }
        });

        jLabel9.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel9.text")); // NOI18N

        cbRenumber.setSelected(true);
        cbRenumber.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.cbRenumber.text")); // NOI18N
        cbRenumber.setToolTipText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.cbRenumber.toolTipText")); // NOI18N
        cbRenumber.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        jLabel18.setText(org.openide.util.NbBundle.getMessage(SigmaSettingsPanel.class, "SigmaSettingsPanel.jLabel18.text")); // NOI18N

        ddEdgeColorMode.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Use source node color", "Use graph default color", "Use edge color specified in table column" }));
        ddEdgeColorMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ddEdgeColorModeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel9)
                                                .addComponent(jLabel18))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(ddEdgeColorMode, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(ddEdgeType, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(212, 212, 212))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(cbShowEdgeLabel)
                                            .addGap(335, 335, 335)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(55, 55, 55)
                                            .addComponent(cbShowNodeLabel))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(38, 38, 38)
                                            .addComponent(jLabel8)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(ddHover, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(cbShowNodes)))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(cbUseGraphBg)
                                    .addGap(550, 550, 550))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(cbRenumber)
                            .addGap(618, 618, 618))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(20, 20, 20)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel12)
                                        .addComponent(jLabel17)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel11))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtLogo, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtAuthor, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtLink)
                                        .addComponent(txtTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(52, 52, 52)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel3)
                                                .addComponent(jLabel4))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(txtNode, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(56, 56, 56)
                                                    .addComponent(cbSearch))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(txtEdge, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(59, 59, 59)
                                                    .addComponent(jLabel15)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(ddGroupSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(14, 14, 14)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel14))
                                    .addGap(148, 148, 148)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel2)
                                            .addGap(207, 207, 207)
                                            .addComponent(jLabel6))
                                        .addComponent(jLabel13)))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(23, 23, 23)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 559, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(14, 14, 14)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(cbShowEdges))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(cbGroupEdges))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 557, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(browseButton))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel20)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator2))
                        .addComponent(jLabel1)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabel19)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ddImageAttribute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1))
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17))
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(txtAuthor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(4, 4, 4)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(txtLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbSearch)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(txtNode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtEdge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)
                                    .addComponent(ddGroupSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(22, 22, 22))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel11)))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(cbGroupEdges)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(ddImageAttribute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbShowNodes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbShowNodeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(ddHover, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cbShowEdges)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbShowEdgeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(ddEdgeType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(ddEdgeColorMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(32, 32, 32)
                .addComponent(cbUseGraphBg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbRenumber)
                .addContainerGap(21, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbShowNodesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbShowNodesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbShowNodesActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_browseButtonActionPerformed

    private void txtEdgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEdgeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEdgeActionPerformed

    private void ddGroupSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddGroupSelectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ddGroupSelectorActionPerformed

    private void cbSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSearchActionPerformed

    private void cbShowNodeLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbShowNodeLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbShowNodeLabelActionPerformed

    private void cbShowEdgesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbShowEdgesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbShowEdgesActionPerformed

    private void cbShowEdgeLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbShowEdgeLabelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbShowEdgeLabelActionPerformed

    private void ddEdgeTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddEdgeTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ddEdgeTypeActionPerformed

    private void cbUseGraphBgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbUseGraphBgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbUseGraphBgActionPerformed

    private void ddEdgeColorModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ddEdgeColorModeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ddEdgeColorModeActionPerformed

    private void cbGroupEdgesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbGroupEdgesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbGroupEdgesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JCheckBox cbGroupEdges;
    private javax.swing.JCheckBox cbRenumber;
    private javax.swing.JCheckBox cbSearch;
    private javax.swing.JCheckBox cbShowEdgeLabel;
    private javax.swing.JCheckBox cbShowEdges;
    private javax.swing.JCheckBox cbShowNodeLabel;
    private javax.swing.JCheckBox cbShowNodes;
    private javax.swing.JCheckBox cbUseGraphBg;
    private javax.swing.JComboBox ddEdgeColorMode;
    private javax.swing.JComboBox ddEdgeType;
    private javax.swing.JComboBox ddGroupSelector;
    private javax.swing.JComboBox ddHover;
    private javax.swing.JComboBox ddImageAttribute;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField pathTextField;
    private javax.swing.JTextField txtAuthor;
    private javax.swing.JTextField txtColor;
    private javax.swing.JTextField txtEdge;
    private javax.swing.JTextField txtLink;
    private javax.swing.JTextField txtLogo;
    private javax.swing.JTextArea txtLong;
    private javax.swing.JTextField txtNode;
    private javax.swing.JTextArea txtShort;
    private javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables
}
