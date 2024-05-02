package cta;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import cta.designe.listener.Algoritmos;
import cta.designe.listener.EventMask;
import cta.designe.listener.ModelFilter;
import cta.designe.listener.Splited;
//import cta.designe.listener.TableListenerModel;
import cta.designe.listener.TableListenerModel;

import javax.swing.ComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.JSplitPane;
import javax.swing.border.BevelBorder;
import javax.swing.JTable;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JFileChooser;

@Component
public class Visualizador extends JFrame {

	/**
	 * 
	 */
	
	

	private int modoTrabajo = 1; // 1:Consulta; 2:MultiConsulta 3: InputStreamFile
	private ButtonGroup buttonGroup;
	private JButton conectar;
	private JButton desconectar; 
	private JButton incluir;
	private JButton borrar;
	
	private JRadioButton rdbtnModo1;
	private JRadioButton rdbtnModo2;

	public String[] aSistemas = {"IL", "SCO", "ATHS"};
	
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	private ConcurrentHashMap<String,DatagramSocket> socketSistemaRegistry; // sistema-maquina->socket
	
	private ConcurrentHashMap<String,DatagramSocket> socketDispatcherRegistry; //sistema-maquina-> socketDispatcher
	
	private ConcurrentHashMap<String,DatagramSocket> socketReceiverRegistry; //Receiver-> socketDispatcher
	
	private ConcurrentHashMap<String, Dispatcher> dispatcherSistemaRegistry; //sistema-maquina -> dispatcher(Runnable) 
	
	private ConcurrentHashMap<String,Integer> flagDisconnectRegistry;// sistema-maquina->flagDisconnect
	
	private ConcurrentHashMap<String,String[]> catalogFiltersRegistry;// sistema-maquina->catalogFilter
	
	private ConcurrentHashMap<String,InfoConexionSistema> infoConexionRegistry; //sistema-maquina-> InfoConexionSistema
	
	private ConcurrentHashMap<String, JCheckBox> ledSocketRegistry; //sistema-maquina-> Jcheckbox
	
	private ConcurrentHashMap<String, JLabel> numThreadsLabel; //sistema-maquina-> JLabel(num hilos)
	
	private ConcurrentHashMap<String, Vector<Receiver>> threadReceiverRegistry; //sistema-maquina-> Vector<Receiver(Runnable)>
	

	
	//private ConcurrentHashMap<String>
	
	private int maxThreadBySistema = 3;
	
	//private String[] catalogFilter = { "" };
	private String[] catalogListener = { "" };

	private JTextField comando;
	private JTextField textfield_Mask;
	private JTextField filter;

	public JSpinner spinner;
	private JCheckBox chckbxTOP1_IL;
	private JCheckBox chckbxTOP2_IL;
	private JCheckBox chckbxTOP1_SCO;
	private JCheckBox chckbxTOP2_SCO;
	private JCheckBox chckbxTOP1_ATHS;
	private JCheckBox chckbxTOP2_ATHS;
	
	private JLabel lblCountThreads_IL_2;
	private JLabel lblCountThreads_SCO_2;
	private JLabel lblCountThreads_ATHS_2;
	private JLabel lblCountThreads_IL_1;
	private JLabel lblCountThreads_SCO_1;
	private JLabel lblCountThreads_ATHS_1;

	

	private JTextArea textArea;
	private JComboBox<Consulta> combo_Consultas;

	private JComboBox<String> comboModelFilters;
	private JComboBox<Modulo> combo_Modulos;
	private JComboBox<String> comboFiltrosActivos;
	private JComboBox<ModelFilter> comboListenersActivos;
	private JComboBox<String> combo_Tareas;
	private JComboBox<String> comboSistemas;

	private JList listaTareas;
	private JLabel labelFiltrosCount;
	private JLabel labelListenersCount;
	private JLabel labelModulosCount;

	public JTextArea getTextAreaHandlers() {
		return textAreaHandlers;
	}

	private JCheckBox checkListener1;
	private JTextField textFieldListener1;
	private JCheckBox checkMostrarLineasTextArea;
	private JTextField linkedListCounter;
	private JTextArea textAreaHandlers;

	public JCheckBox getCheckMostrarLineasTextArea() {
		return checkMostrarLineasTextArea;
	}

	//private SimpleAttributeSet attributeSet;
	// loger
	private static final Logger logger = LoggerFactory.getLogger(Visualizador.class);
	private JTextField jTextDescripModelFilter;
	
	
	// Mapa contenedor de consultas registradas (memoria)
	private ConcurrentHashMap<String, Consulta> catalogoConsultas; 

	// Mapa contenedor de ModelFilters registrados
	private ConcurrentHashMap<String, ModelFilter> catalogoModelFilters;

	// Tareas registradas constituido por un mapa de nombre Tarea -> vector<Consultas> 
	private ConcurrentHashMap<String, Tarea> catalogoTareas; 

	// vector contenedor de modulos registrables (memoria)
	private Vector<Modulo> modulosRegistrables;

	private JTextField FilterByIdValue;
	private JTextField selectByIdValue;
	private JTextPane textPane;
	private JCheckBox filterExclusive;
	private JCheckBox chckbxBufferearConsulta;

	// Cola de cadenas recibidas (filtradas) desde los brokers de maquina
	int Max_Size_Queue = 200000;
	private ArrayBlockingQueue<String> cadenasFiltradas = new ArrayBlockingQueue<String>(this.Max_Size_Queue);

	// Definicion e la tabla de Events
	private JTable tableModelFilter;

	
	public ConcurrentHashMap<String, Consulta> getCatalogoConsultas() {
		return catalogoConsultas;
	}

	public void setCatalogoConsultas(ConcurrentHashMap<String, Consulta> catalogoConsultas) {
		this.catalogoConsultas = catalogoConsultas;
	}
	
	public ConcurrentHashMap<String, ModelFilter> getCatalogoModelFilters() {
		return catalogoModelFilters;
	}

	public void setCatalogoModelFilters(ConcurrentHashMap<String, ModelFilter> catalogoModelFilters) {
		this.catalogoModelFilters = catalogoModelFilters;
	}

	public JButton getIncluir() {
		return incluir;
	}

	public void setIncluir(JButton incluir) {
		this.incluir = incluir;
	}

	public JComboBox<Consulta> getCombo_Consultas() {
		return combo_Consultas;
	}

	public void setCombo_Consultas(JComboBox<Consulta> combo_Consultas) {
		this.combo_Consultas = combo_Consultas;
	}

	
	public JButton getDesconectar() {
		return desconectar;
	}

	public void setDesconectar(JButton desconectar) {
		this.desconectar = desconectar;
	}
	
	public JButton getConectar() {
		return conectar;
	}

	public void setConectar(JButton conectar) {
		this.conectar = conectar;
	}
	public ConcurrentHashMap<String, JLabel> getNumThreadsLabel() {
		return numThreadsLabel;
	}

	public JComboBox<String> getComboSistemas() {
		return comboSistemas;
	}

	public void setComboSistemas(JComboBox<String> comboSistemas) {
		this.comboSistemas = comboSistemas;
	}

	
	public String[] getCatalogListener() {
		return catalogListener;
	}

	public void setCatalogListener(String[] catalogListener) {
		this.catalogListener = catalogListener;
	}

	public JCheckBox getChckbxBufferearConsulta() {
		return chckbxBufferearConsulta;
	}

	public String[] getCatalogFilter(String sistemaConsulta) {
		return catalogFiltersRegistry.get(sistemaConsulta);
	}
	
	public String[] getCatalogFiltersRegistry(String sistemaConsulta) {
		return catalogFiltersRegistry.get(sistemaConsulta);
	}


	public void setCatalogFilter(String[] catalogFilter, String sistemaConsulta) {
		this.catalogFiltersRegistry.put(sistemaConsulta, catalogFilter);
	}

	public ArrayBlockingQueue<String> getCadenasFiltradas() {
		return cadenasFiltradas;
	}

	public JCheckBox getCheckListener1() {
		return checkListener1;
	}

	public JTextField getTextFieldListener1() {
		return textFieldListener1;
	}

	

	public JTextField getLinkedListCounter() {
		return linkedListCounter;
	}

	public void setLinkedListCounter(JTextField linkedListCounter) {
		this.linkedListCounter = linkedListCounter;
	}

	public JCheckBox getFilterExclusive() {
		return filterExclusive;
	}

	public void setFilterExclusive(JCheckBox filterExclusive) {
		this.filterExclusive = filterExclusive;
	}

	public JTextField getFilter() {
		return filter;
	}

	public void setFilter(JTextField filter) {
		this.filter = filter;
	}

	public int getFlagDisconnect(String sistemaSocket) {
		return this.getFlagDisconnectRegistry().get(sistemaSocket);	
	}
	
	public void setFlagDisconnect(int flagDisconnect, String sistemaSocket) {
		this.getFlagDisconnectRegistry().put(sistemaSocket,flagDisconnect);
	}

	public JTextField getComando() {
		return comando;
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Visualizador frame = new Visualizador();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public String getCatalogos() {
		return catalogos;
	}

	public void setCatalogos(String catalogos) {
		this.catalogos = catalogos;
	}

	
	@Value("${uri.file.catalogos}")
	public String catalogos; //un mapeo a la propertie en application properties
	
	/**
	 * Create the frame.
	 */
	public Visualizador() {

		// Inicializar el IHM
		this.initFrameVisualizador();
		// Inicializar repositorios de modulos 
		this.modulosRegistrables = this.initVectorModules(this.comboSistemas.getSelectedItem() + ".csv");
		// Inicializar estructuras de datos y contexto
		this.initStructures();
		//Actualizat info conexion sistemas
		this.initInfoConexiones("Madrid");
		System.out.println("Ejecutado constructor");
		
		/*Vector vCentros = new Vector();
		vCentros.add("Madrid");
		vCentros.add("Valladolid");
		Object selConsulta = JOptionPane.showInputDialog(contentPane, "Seleccione Centro",
				"Selector de Centro", JOptionPane.QUESTION_MESSAGE, null, vCentros.toArray(),
				"Seleccione Centro");
		this.initInfoConexiones((String)selConsulta);
		
		reloadCatalogos();
		//rdbtnModo1.setSelected(true);
		this.setVisible(true);*/

	}
	private void initStructures() {
		// sistema:socket
		socketSistemaRegistry = new ConcurrentHashMap<String,DatagramSocket>();

		// sistema:flagDisconnect
		flagDisconnectRegistry = new ConcurrentHashMap<String,Integer>();
		
		// sistema:catalogFilter
		catalogFiltersRegistry = new ConcurrentHashMap<String,String[]>();
		
		// mapa de conexiones socket sistemas	
		infoConexionRegistry = new ConcurrentHashMap<String,InfoConexionSistema>();
		
		// mapa de vinculos led-socket
		ledSocketRegistry =  new ConcurrentHashMap<String, JCheckBox>(); 
		
		//Mapa de threads corriendo		
		threadReceiverRegistry = new ConcurrentHashMap<String,Vector<Receiver>>();
		
		//Mapa de label count thread Sockets
		numThreadsLabel = new ConcurrentHashMap<String,JLabel>();
		
		//Mapa de Tareas
		catalogoTareas = new ConcurrentHashMap<String,Tarea>();
		
	}
	
	public ConcurrentHashMap<String, Vector<Receiver>> getThreadReceiverRegistry() {
		return threadReceiverRegistry;
	}


	public void refreshLedsSocketsStatus() {
		
		//apagar todos los leds y poner a 0 todas las label de los hilos de cada receiver
		for(Enumeration<String> ledsSockets = this.ledSocketRegistry.keys();ledsSockets.hasMoreElements();) {
			String key = ledsSockets.nextElement();
			(ledSocketRegistry.get(key)).setBackground(Color.red);
			this.numThreadsLabel.get(key).setText("0");
			//
		}

		//Poner en verde los activos 
		for(Enumeration<String> keySockets = this.socketSistemaRegistry.keys(); keySockets.hasMoreElements();) {
			this.ledSocketRegistry.get(keySockets.nextElement()).setBackground(Color.green);
		}
		
		
		//Vamos con el contaje de hilos
		for(Enumeration<String> keyThreads = this.threadReceiverRegistry.keys();keyThreads.hasMoreElements();) {
			String key = keyThreads.nextElement();
			Vector<Receiver> vReceiver = threadReceiverRegistry.get(key);
			this.numThreadsLabel.get(key).setText(""+(vReceiver.size()));
			if(threadReceiverRegistry.get(key).size() > 1) {
				logger.warn("Procesamiento paralelo de "+ key);						
			}

		}
	
	}

	public ConcurrentHashMap<String, JCheckBox> getLedSocketRegistry() {
		return ledSocketRegistry;
	}

	public void setLedSocketRegistry(ConcurrentHashMap<String, JCheckBox> ledSocketRegistry) {
		this.ledSocketRegistry = ledSocketRegistry;
	}

	private void initFrameVisualizador() {
		buttonGroup = new ButtonGroup();
		
	
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setBounds(100, 100, 1508, 864);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Real Time", null, tabbedPane_1, null);

		JPanel panel_3 = new JPanel();
		tabbedPane_1.addTab("Main Panel", null, panel_3, null);

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));

		JPanel panel_2_1 = new JPanel();
		panel_2_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JLabel lblTop = new JLabel("TOP :");
		lblTop.setFont(new Font("Dialog", Font.BOLD, 12));

		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(1, 1, 2, 1));

		conectar= new JButton("Conectar");
		conectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connect();
			}

		});
		conectar.setFont(new Font("SansSerif", Font.PLAIN, 12));

		desconectar = new JButton("Desconectar");
		desconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				disconnectManual("All");
			}
		});
		desconectar.setFont(new Font("Dialog", Font.PLAIN, 12));

		JLabel label_1 = new JLabel("SISTEMA :");
		label_1.setFont(new Font("Dialog", Font.PLAIN, 12));

		String[] fenetreStrings = { "Linea_Entrada", "Carrusel", "ATHS" };

		JButton btnEnviarComando = new JButton("Enviar Comando");
		btnEnviarComando.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				enviarComando(comando.getText(), (String)comboSistemas.getSelectedItem()+":"+ spinner.getValue());

			}
		});
		btnEnviarComando.setFont(new Font("Dialog", Font.PLAIN, 12));

		comando = new JTextField();
		comando.setFont(new Font("Dialog", Font.PLAIN, 14));

		comando.setColumns(10);
		
		JLabel lblNewLabel_4 = new JLabel("TAREAS");
		lblNewLabel_4.setFont(new Font("Dialog", Font.BOLD, 12));
		
		JButton btnNewButton_3 = new JButton("Incluir");
		btnNewButton_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				incluirConsultaAListaTareas((String)combo_Tareas.getSelectedItem());
			}
		});
		
		JButton btnNewButton_4 = new JButton("Borrar");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(listaTareas.getSelectedIndex()!=-1) {
					String sConsultaTarea = (String)listaTareas.getModel().getElementAt(listaTareas.getSelectedIndex());					
					borrarConsultaTarea((String)combo_Tareas.getSelectedItem(),sConsultaTarea);
				}
			}
		});
		btnNewButton_4.setFont(new Font("Dialog", Font.PLAIN, 12));
		

		listaTareas = new JList();
		listaTareas.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		JScrollPane scrollPaneListaTareas = new JScrollPane();
		scrollPaneListaTareas.setBorder(null);
		scrollPaneListaTareas.setViewportBorder(UIManager.getBorder("CheckBox.border"));
		scrollPaneListaTareas.setViewportView(listaTareas);
				comboSistemas = new JComboBox(fenetreStrings);
				comboSistemas.setFont(new Font("Dialog", Font.PLAIN, 12));
				comboSistemas.setModel(new DefaultComboBoxModel(this.aSistemas));
				comboSistemas.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent arg0) {
						if (!(arg0.getItem() == null && arg0.getStateChange() == 1)) {
							
								selectSistema((String) comboSistemas.getSelectedItem());
						}
					}
				});
		
		chckbxTOP1_IL = new JCheckBox("IL");
		chckbxTOP1_IL.setBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")));
		chckbxTOP1_IL.setBackground(Color.WHITE);
		
		JLabel lblNewLabel_5 = new JLabel("TOP1");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		chckbxTOP1_SCO = new JCheckBox("SCO");
		chckbxTOP1_SCO.setBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")));
		chckbxTOP1_SCO.setBackground(Color.WHITE);
		
		chckbxTOP1_ATHS = new JCheckBox("Aths");
		chckbxTOP1_ATHS.setBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")));
		chckbxTOP1_ATHS.setBackground(Color.WHITE);
			
		chckbxTOP2_IL = new JCheckBox("IL");
		chckbxTOP2_IL.setBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")));
		chckbxTOP2_IL.setBackground(Color.WHITE);
		
		chckbxTOP2_SCO = new JCheckBox("SCO");
		chckbxTOP2_SCO.setBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")));
		chckbxTOP2_SCO.setBackground(Color.WHITE);
		
		chckbxTOP2_ATHS = new JCheckBox("Aths");
		chckbxTOP2_ATHS.setBorder(new CompoundBorder(null, UIManager.getBorder("CheckBoxMenuItem.border")));
		chckbxTOP2_ATHS.setBackground(Color.WHITE);
		
		JLabel lblNewLabel_5_1 = new JLabel("TOP2");
		lblNewLabel_5_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		
		lblCountThreads_IL_2 = new JLabel("0");
		lblCountThreads_SCO_2 = new JLabel("0");
		lblCountThreads_ATHS_2 = new JLabel("0");
		lblCountThreads_IL_1 = new JLabel("0");
		lblCountThreads_SCO_1 = new JLabel("0");
		lblCountThreads_ATHS_1 = new JLabel("0");
		
		combo_Tareas = new JComboBox();
		
		JButton btnNewTarea = new JButton("Nueva");
		btnNewTarea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Nueva Tarea
				newTarea();
				
			}

		});
		btnNewTarea.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		JButton btnGuardarTarea = new JButton("Guardar");
		btnGuardarTarea.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		rdbtnModo2 = new JRadioButton("MODO FILEINPUT");
		rdbtnModo2.setFont(new Font("Dialog", Font.BOLD, 12));
		buttonGroup.add(rdbtnModo2);

		
		GroupLayout gl_panel_2_1 = new GroupLayout(panel_2_1);
		gl_panel_2_1.setHorizontalGroup(
			gl_panel_2_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2_1.createSequentialGroup()
							.addComponent(lblTop)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
							.addGap(18))
						.addComponent(btnNewTarea, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
						.addComponent(label_1)
						.addComponent(rdbtnModo2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnNewButton_3, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
						.addComponent(btnNewButton_4, GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2_1.createParallelGroup(Alignment.LEADING)
						.addComponent(comboSistemas, 0, 218, Short.MAX_VALUE)
						.addGroup(gl_panel_2_1.createSequentialGroup()
							.addComponent(conectar, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(desconectar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addComponent(scrollPaneListaTareas, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
						.addComponent(combo_Tareas, 0, 218, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2_1.createSequentialGroup()
							.addGroup(gl_panel_2_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2_1.createSequentialGroup()
									.addGroup(gl_panel_2_1.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_panel_2_1.createSequentialGroup()
											.addComponent(chckbxTOP1_ATHS, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(lblCountThreads_ATHS_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGroup(gl_panel_2_1.createSequentialGroup()
											.addComponent(chckbxTOP1_SCO, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(lblCountThreads_SCO_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGroup(gl_panel_2_1.createSequentialGroup()
											.addComponent(chckbxTOP1_IL, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(lblCountThreads_IL_1, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)))
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGroup(gl_panel_2_1.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_panel_2_1.createSequentialGroup()
											.addComponent(chckbxTOP2_IL, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(lblCountThreads_IL_2, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_panel_2_1.createSequentialGroup()
											.addGroup(gl_panel_2_1.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel_2_1.createSequentialGroup()
													.addGap(1)
													.addComponent(chckbxTOP2_ATHS, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
												.addComponent(chckbxTOP2_SCO, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(ComponentPlacement.RELATED)
											.addGroup(gl_panel_2_1.createParallelGroup(Alignment.LEADING)
												.addComponent(lblCountThreads_SCO_2, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE)
												.addComponent(lblCountThreads_ATHS_2, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)))))
								.addGroup(gl_panel_2_1.createSequentialGroup()
									.addGap(18)
									.addComponent(lblNewLabel_5)
									.addGap(51)
									.addComponent(lblNewLabel_5_1, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)))
							.addGap(179))
						.addGroup(gl_panel_2_1.createSequentialGroup()
							.addGroup(gl_panel_2_1.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(btnGuardarTarea, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(btnEnviarComando, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
								.addComponent(comando, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE))
							.addContainerGap())))
				.addGroup(gl_panel_2_1.createSequentialGroup()
					.addGap(189)
					.addComponent(lblNewLabel_4, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(452, Short.MAX_VALUE))
		);
		gl_panel_2_1.setVerticalGroup(
			gl_panel_2_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2_1.createSequentialGroup()
					.addGap(11)
					.addGroup(gl_panel_2_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboSistemas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(label_1)
						.addComponent(btnEnviarComando))
					.addGap(1)
					.addGroup(gl_panel_2_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTop, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(spinner, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(conectar, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(desconectar, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
						.addComponent(comando, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_4)
					.addGap(1)
					.addGroup(gl_panel_2_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewTarea)
						.addComponent(combo_Tareas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnGuardarTarea))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPaneListaTareas, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_2_1.createSequentialGroup()
							.addGroup(gl_panel_2_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_5)
								.addComponent(lblNewLabel_5_1))
							.addGap(6)
							.addGroup(gl_panel_2_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2_1.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel_2_1.createParallelGroup(Alignment.BASELINE)
										.addComponent(chckbxTOP1_IL, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
										.addComponent(chckbxTOP2_IL, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblCountThreads_IL_1))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel_2_1.createParallelGroup(Alignment.BASELINE)
										.addComponent(chckbxTOP1_SCO, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblCountThreads_SCO_1)
										.addComponent(chckbxTOP2_SCO, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblCountThreads_SCO_2)))
								.addGroup(gl_panel_2_1.createSequentialGroup()
									.addGap(2)
									.addComponent(lblCountThreads_IL_2)))
							.addGap(5)
							.addGroup(gl_panel_2_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(chckbxTOP1_ATHS, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCountThreads_ATHS_1)
								.addComponent(chckbxTOP2_ATHS, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblCountThreads_ATHS_2)))
						.addGroup(gl_panel_2_1.createSequentialGroup()
							.addComponent(rdbtnModo2)
							.addGap(7)
							.addComponent(btnNewButton_3)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton_4)))
					.addGap(24))
		);
		panel_2_1.setLayout(gl_panel_2_1);

		JPanel panel_3_1 = new JPanel();
		panel_3_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JButton btnNewButton = new JButton("NUEVO");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dialogAddModulo();
			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.PLAIN, 12));

		JButton btnBorrar = new JButton("BORRAR");
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Consulta consulta = (Consulta) combo_Consultas.getSelectedItem();
				borrarModuloActual(consulta.getNombreConsultaFull(), (Modulo) combo_Modulos.getSelectedItem());
			}
		});
		btnBorrar.setFont(new Font("Dialog", Font.PLAIN, 12));

		combo_Modulos = new JComboBox();
		combo_Modulos.setMaximumSize(new Dimension(40, 32767));
		combo_Modulos.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				Modulo mod = (Modulo) arg0.getItem();
				textfield_Mask.setText(mod.getMask());
			}
		});

		filter = new JTextField();
		filter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		filter.setColumns(10);

		JButton boton_HelpMask = new JButton("HELP");
		boton_HelpMask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Modulo modHelping = (Modulo) combo_Modulos.getSelectedItem();
				if (modHelping != null) {
					mostrarAyudaModulo(modHelping);
				}

			}
		});

		JButton btnGuardarConsulta = new JButton("GUARDAR CONSULTA");
		btnGuardarConsulta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (combo_Consultas.getItemCount() == 0)
					return;
				String sistema = (String) comboSistemas.getSelectedItem();
				Consulta consulta = (Consulta) combo_Consultas.getSelectedItem();
				guardarConsultaActual(sistema, consulta);
			}
		});
		btnGuardarConsulta.setFont(new Font("Dialog", Font.PLAIN, 12));

		textfield_Mask = new JTextField();
		textfield_Mask.setFont(new Font("Dialog", Font.PLAIN, 12));
		textfield_Mask.setColumns(10);

		JButton boton_Set_Mask = new JButton("SET");
		boton_Set_Mask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMaskModulo(textfield_Mask.getText());
			}

		});

		JLabel lblNewLabel = new JLabel("MODULOS ACTIVOS");
		lblNewLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 12));

		JLabel lblNewLabel_1 = new JLabel("MASCARAS");
		lblNewLabel_1.setFont(new Font("DejaVu Sans", Font.BOLD, 12));

		filterExclusive = new JCheckBox("Filtrado incluido");
		filterExclusive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox jCheckBox = (JCheckBox) e.getSource();
				logger.info("jCheckExclusive checked=" + jCheckBox.isSelected());
				// Actualizar las mascaras exclusive en el catalogo
				
				Consulta consulta = (Consulta) combo_Consultas.getSelectedItem();
			
				guardarConsultaActual("no hace falta",consulta);
			}
		});

		comboFiltrosActivos = new JComboBox();

		JCheckBox chckbxEnablePublish = new JCheckBox("Publish");

		JLabel lblListeners = new JLabel("FILTROS ACTIVOS");
		lblListeners.setFont(new Font("DejaVu Sans", Font.BOLD, 12));

		chckbxBufferearConsulta = new JCheckBox("Salida a Queue");
		chckbxBufferearConsulta.setSelected(true);

		JButton btnNew = new JButton("INCLUIR");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				incluirModelFilterAConsulta();
				guardarConsultaActual("da igual", (Consulta)combo_Consultas.getSelectedItem());

			}
		});

		JButton btnBorrar_2 = new JButton("BORRAR");
		btnBorrar_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String keyModelFilter = (String) comboFiltrosActivos.getSelectedItem();
				if(keyModelFilter==null)return;
				borrarModelFilterDeConsulta((ModelFilter) catalogoModelFilters.get(keyModelFilter));
				guardarConsultaActual(null, (Consulta)combo_Consultas.getSelectedItem());
			}
		});

		labelFiltrosCount = new JLabel("(0)");
		labelFiltrosCount.setFont(new Font("DejaVu Sans", Font.BOLD, 13));
		
		labelModulosCount = new JLabel("(0)");
		labelModulosCount.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		
				JButton button_4 = new JButton("BORRAR");
				button_4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (combo_Consultas.getItemCount() == 0)
							return;

						int confirmado = JOptionPane.showConfirmDialog(contentPane, "¿Borrar la consulta?");

						if (JOptionPane.OK_OPTION == confirmado) {
							if (combo_Consultas.getSelectedItem()== null)
								return;
							Consulta consulta = (Consulta) combo_Consultas.getSelectedItem();
						
							borrarConsultaActual(consulta.getNombreConsultaFull());
						}
					}
				});
				button_4.setFont(new Font("Dialog", Font.PLAIN, 12));
		
				JButton btnNueva_1 = new JButton("NUEVA");
				btnNueva_1.setPreferredSize(new Dimension(108, 27));
				btnNueva_1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						newConsulta();
					}
				});
				btnNueva_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		
				combo_Consultas = new JComboBox();
				combo_Consultas.addItemListener(new ItemListener() {
					// Selecionar la nueva consulta
					public void itemStateChanged(ItemEvent arg0) {
						logger.info("El estado es :" + arg0.getStateChange() + " y el valor es:" + arg0.getItem().toString());
						if (arg0.getStateChange() == ItemEvent.SELECTED) {
						
							
							Consulta consulta =(Consulta) arg0.getItem();
							logger.info("obteniendo item seleccionado " + consulta.getNombreConsultaFull());
							refreshObjectConsulta(consulta);
							
							//La cuestion ahora es que el catalogFilters es construido en base al Modo de trabajo establecido
							//1 : Consulta ; 2: MultiConsulta
							if(modoTrabajo==1) {
								String keyConsulta = consulta.getNombreConsultaFull();
								catalogFiltersRegistry.put(keyConsulta, makeCatalogFilter(consulta));
								logger.info("Actualizado catalogo de filtros para la consulta:"+keyConsulta);
							}

							logger.info("Refrescado objeto consulta");
						}
					}
				});
		
		rdbtnModo1 = new JRadioButton("MODO CONSULTA");
		rdbtnModo1.setSelected(true);
		rdbtnModo1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				JRadioButton jrb = (JRadioButton) e.getSource();
				if(jrb.isSelected()) {
					modoTrabajo=1;
					Consulta virtualConsulta = new Consulta();
					virtualConsulta.setSistemaConsulta((String) comboSistemas.getSelectedItem());
					refreshComboConsultas(virtualConsulta);// No nos situamos sobre ningun item en particular al init

				}else {
					modoTrabajo=0;
					
				}

			}
		});
		
		rdbtnModo1.setFont(new Font("Dialog", Font.BOLD, 12));
		buttonGroup.add(rdbtnModo1);


		GroupLayout gl_panel_3_1 = new GroupLayout(panel_3_1);
		gl_panel_3_1.setHorizontalGroup(
			gl_panel_3_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3_1.createSequentialGroup()
					.addGroup(gl_panel_3_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3_1.createSequentialGroup()
							.addGap(22)
							.addGroup(gl_panel_3_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_3_1.createSequentialGroup()
									.addComponent(chckbxBufferearConsulta)
									.addGap(18)
									.addComponent(chckbxEnablePublish, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel_3_1.createParallelGroup(Alignment.TRAILING, false)
									.addGroup(gl_panel_3_1.createSequentialGroup()
										.addComponent(btnNueva_1, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
									.addComponent(btnGuardarConsulta, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(filter, Alignment.LEADING)
									.addComponent(combo_Consultas, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE))))
						.addGroup(gl_panel_3_1.createSequentialGroup()
							.addGap(16)
							.addComponent(rdbtnModo1, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)))
					.addGap(11)
					.addGroup(gl_panel_3_1.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel_3_1.createSequentialGroup()
							.addGroup(gl_panel_3_1.createParallelGroup(Alignment.LEADING, false)
								.addGroup(gl_panel_3_1.createSequentialGroup()
									.addComponent(btnNewButton)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnBorrar)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(filterExclusive))
								.addComponent(combo_Modulos, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
							.addGap(40)
							.addGroup(gl_panel_3_1.createParallelGroup(Alignment.LEADING, false)
								.addComponent(boton_Set_Mask, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(textfield_Mask, GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
								.addComponent(boton_HelpMask, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(gl_panel_3_1.createSequentialGroup()
							.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(labelModulosCount)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(lblNewLabel_1)))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(gl_panel_3_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3_1.createSequentialGroup()
							.addGap(10)
							.addComponent(comboFiltrosActivos, 0, 245, Short.MAX_VALUE))
						.addGroup(gl_panel_3_1.createSequentialGroup()
							.addGap(10)
							.addComponent(btnNew)
							.addPreferredGap(ComponentPlacement.RELATED, 93, Short.MAX_VALUE)
							.addComponent(btnBorrar_2))
						.addGroup(gl_panel_3_1.createSequentialGroup()
							.addGap(21)
							.addComponent(lblListeners, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(labelFiltrosCount)))
					.addContainerGap())
		);
		gl_panel_3_1.setVerticalGroup(
			gl_panel_3_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3_1.createSequentialGroup()
					.addGap(24)
					.addGroup(gl_panel_3_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_3_1.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblNewLabel)
							.addComponent(labelModulosCount)
							.addComponent(rdbtnModo1))
						.addGroup(gl_panel_3_1.createParallelGroup(Alignment.BASELINE)
							.addComponent(labelFiltrosCount)
							.addComponent(lblListeners)
							.addComponent(lblNewLabel_1)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
						.addComponent(btnBorrar, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
						.addComponent(filterExclusive)
						.addComponent(boton_Set_Mask)
						.addComponent(btnNew)
						.addComponent(btnNueva_1, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
						.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnBorrar_2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(combo_Modulos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(textfield_Mask, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(combo_Consultas, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(comboFiltrosActivos, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_3_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3_1.createSequentialGroup()
							.addGroup(gl_panel_3_1.createParallelGroup(Alignment.BASELINE)
								.addComponent(chckbxBufferearConsulta)
								.addComponent(chckbxEnablePublish))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(filter, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnGuardarConsulta))
						.addComponent(boton_HelpMask, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
					.addGap(38))
		);
		panel_3_1.setLayout(gl_panel_3_1);
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_2_1, GroupLayout.PREFERRED_SIZE, 548, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_3_1, GroupLayout.DEFAULT_SIZE, 939, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGap(12)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_2_1, Alignment.LEADING, 0, 0, Short.MAX_VALUE)
						.addComponent(panel_3_1, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		panel_4.setLayout(gl_panel_4);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));

		textArea = new JTextArea();
		textArea.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {
					logger.info("Texto Seleccionado:" + textArea.getSelectedText());
					FilterByIdValue.setText(textArea.getSelectedText());
				}
			}
		});

		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setViewportBorder(UIManager.getBorder("CheckBox.border"));
		scrollPane.setViewportView(textArea);

		GroupLayout gl_panel_1_1 = new GroupLayout(panel_1_1);
		gl_panel_1_1.setHorizontalGroup(
			gl_panel_1_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1432, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panel_1_1.setVerticalGroup(
			gl_panel_1_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel_1_1.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
					.addContainerGap())
		);

		panel_1_1.setLayout(gl_panel_1_1);

		JButton btnClear = new JButton("CLEAR");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				cadenasFiltradas = new ArrayBlockingQueue<String>(Max_Size_Queue);
				textArea.setText("");
				linkedListCounter.setText(cadenasFiltradas.size() + " lineas");
			}
		});
		btnClear.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnClear.setHorizontalAlignment(SwingConstants.LEFT);
		btnClear.setFont(new Font("Dialog", Font.PLAIN, 12));

		checkMostrarLineasTextArea = new JCheckBox("Rellenar TextArea");
		checkMostrarLineasTextArea.setSelected(true);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_3.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_3.createSequentialGroup()
							.addGap(21)
							.addComponent(btnClear))
						.addComponent(panel_1_1, GroupLayout.DEFAULT_SIZE, 1463, Short.MAX_VALUE)
						.addComponent(checkMostrarLineasTextArea)
						.addComponent(panel_4, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_4, GroupLayout.PREFERRED_SIZE, 266, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_1_1, GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(checkMostrarLineasTextArea)
					.addGap(1)
					.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		panel_3.setLayout(gl_panel_3);

		JTabbedPane tabbedPane_3 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("PostProcesado", null, tabbedPane_3, null);

		JPanel panel = new JPanel();
		tabbedPane_3.addTab("Filtering", null, panel, null);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JButton btnClear_1 = new JButton("CLEAR");
		btnClear_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textAreaHandlers.setText("");
			}
		});

		JLabel lblBufferActualLineas = new JLabel("Lineas Engine Buffer :");

		linkedListCounter = new JTextField();
		linkedListCounter.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING,
								gl_panel.createSequentialGroup().addContainerGap()
										.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_panel.createSequentialGroup()
														.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 1299,
																Short.MAX_VALUE)
														.addContainerGap())
												.addGroup(
														Alignment.TRAILING,
														gl_panel.createSequentialGroup()
																.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 1299,
																		Short.MAX_VALUE)
																.addGap(12))
												.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
														.addComponent(lblBufferActualLineas)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(linkedListCounter, GroupLayout.PREFERRED_SIZE,
																155, GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(ComponentPlacement.RELATED, 538,
																Short.MAX_VALUE)
														.addComponent(btnClear_1).addGap(441)))));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(gl_panel
				.createSequentialGroup()
				.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
				.addGap(8).addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 546, Short.MAX_VALUE).addGap(15)
				.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(btnClear_1)
						.addComponent(lblBufferActualLineas).addComponent(linkedListCounter, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addContainerGap()));
		JButton filterById = new JButton("Filtrar por ID");
		filterById.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String[] sFilters = new String[2];
				sFilters[0] = FilterByIdValue.getText();
				sFilters[1] = FilterByIdValue.getText().toLowerCase();

				// filtrar
				//System.out.println("antes:" + cadenasFiltradas.peek());
				synchronized (cadenasFiltradas) {
					
					textPane.setText(filterLines(cadenasFiltradas, sFilters));
				}
				
				//System.out.println("despues:" + cadenasFiltradas.peek());
				
				// colorear
				colorearSelected(textPane, sFilters, Color.red);
			}
		});

		FilterByIdValue = new JTextField();
		FilterByIdValue.setColumns(10);

		JButton btnNewdesconectar = new JButton("Select por ID");
		btnNewdesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				// System.out.println("antesSelect:"+cadenasFiltradas.get(cadenasFiltradas.size()-1));
				String[] sSelects = new String[1];
				sSelects[0] = selectByIdValue.getText();
			
				// Al loro con el synchronized
				synchronized (cadenasFiltradas) {
					textPane.setText(filterLines(cadenasFiltradas, ""));
				}
				// Coloca el cursor en la primera aparicion encontrada de selectByIdValue
				try {
					textPane.setCaretPosition(colorearSelected(textPane, sSelects, Color.red));
				} catch (java.lang.IllegalArgumentException iae) {
					textPane.setText("Nada encontrado");
				}
			}
		});

		selectByIdValue = new JTextField();
		selectByIdValue.setColumns(10);

		textFieldListener1 = new JTextField();
		textFieldListener1.setColumns(10);

		checkListener1 = new JCheckBox("");
		checkListener1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				catalogListener = makeCatalogListeners();

			}
		});

		JLabel lblListenersActivos = new JLabel("LISTENERS ACTIVOS");
		lblListenersActivos.setFont(new Font("DejaVu Sans", Font.BOLD, 12));

		incluir = new JButton("INCLUIR");
		incluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				incluirModelFilterAListeners();
				catalogListener= makeCatalogListeners();
				comboListenersActivos.setSelectedIndex(comboListenersActivos.getItemCount() - 1);
				labelListenersCount.setText("(" + comboListenersActivos.getItemCount() + ")");
			}
		});

		borrar = new JButton("BORRAR");
		borrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				borrarModelFilterDeListener((ModelFilter) comboListenersActivos.getSelectedItem());

			}
		});

		comboListenersActivos = new JComboBox<ModelFilter>();

		labelListenersCount = new JLabel("(0)");
		labelListenersCount.setFont(new Font("DejaVu Sans", Font.BOLD, 13));

		JLabel lblNewLabel_2 = new JLabel("FILTRADO SOBRE BUFFER");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 13));

		JLabel lblNewLabel_3 = new JLabel("Listener rapido");

		JButton btnNewButton_2 = new JButton("Incluir");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				catalogListener = makeCatalogListeners();
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addComponent(lblNewLabel_2)
						.addGroup(gl_panel_1.createSequentialGroup()
								.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(btnNewdesconectar, Alignment.LEADING, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(filterById, Alignment.LEADING))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
										.addComponent(FilterByIdValue, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(selectByIdValue, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
				.addGap(121)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(comboListenersActivos, GroupLayout.PREFERRED_SIZE, 198,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
										.addComponent(checkListener1).addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(lblListenersActivos, GroupLayout.PREFERRED_SIZE, 146,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(labelListenersCount,
												GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
								.addGroup(Alignment.LEADING, gl_panel_1.createSequentialGroup()
										.addComponent(incluir, GroupLayout.PREFERRED_SIZE, 91,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18).addComponent(borrar, GroupLayout.PREFERRED_SIZE, 87,
												GroupLayout.PREFERRED_SIZE))))
				.addPreferredGap(ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
				.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_1.createSequentialGroup()
						.addComponent(textFieldListener1, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnNewButton_2))
						.addComponent(lblNewLabel_3, GroupLayout.PREFERRED_SIZE, 123, GroupLayout.PREFERRED_SIZE))
				.addGap(387)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup().addGap(12)
						.addGroup(gl_panel_1
								.createParallelGroup(Alignment.LEADING)
								.addGroup(
										gl_panel_1
												.createParallelGroup(Alignment.BASELINE).addComponent(lblNewLabel_2)
												.addComponent(lblListenersActivos, GroupLayout.PREFERRED_SIZE, 15,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(labelListenersCount, GroupLayout.PREFERRED_SIZE, 17,
														GroupLayout.PREFERRED_SIZE))
								.addComponent(checkListener1))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel_1.createParallelGroup(Alignment.LEADING)
										.addGroup(gl_panel_1.createSequentialGroup()
												.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING, false)
														.addComponent(FilterByIdValue, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(filterById))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(
														gl_panel_1.createParallelGroup(Alignment.BASELINE)
																.addComponent(btnNewdesconectar)
																.addComponent(selectByIdValue,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)))
										.addGroup(gl_panel_1.createSequentialGroup()
												.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
														.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																.addComponent(incluir).addComponent(borrar))
														.addGroup(gl_panel_1.createSequentialGroup().addGap(11)
																.addComponent(lblNewLabel_3)))
												.addPreferredGap(ComponentPlacement.RELATED)
												.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
														.addComponent(comboListenersActivos, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
																.addComponent(textFieldListener1,
																		GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)
																.addComponent(btnNewButton_2)))))
						.addContainerGap(16, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setSize(new Dimension(200, 110));
		splitPane.setPreferredSize(new Dimension(297, 300));
		splitPane.setMinimumSize(new Dimension(297, 500));

		JScrollPane scrollPane_1 = new JScrollPane(splitPane);
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 1213, Short.MAX_VALUE)
						.addContainerGap()));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE).addContainerGap()));

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setMinimumSize(new Dimension(600, 222));
		splitPane.setLeftComponent(scrollPane_2);

		textPane = new JTextPane();
		scrollPane_2.setViewportView(textPane);

		JScrollPane scrollPane_3 = new JScrollPane();
		splitPane.setRightComponent(scrollPane_3);

		textAreaHandlers = new JTextArea();
		textAreaHandlers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getClickCount() == 2) {
					logger.info("Texto Seleccionado:" + textAreaHandlers.getSelectedText());
					FilterByIdValue.setText(textAreaHandlers.getSelectedText());
				}
			}
		});

		DefaultCaret caret2 = (DefaultCaret) textAreaHandlers.getCaret();
		caret2.setUpdatePolicy(DefaultCaret.OUT_BOTTOM);

		scrollPane_3.setViewportView(textAreaHandlers);
		panel_2.setLayout(gl_panel_2);
		panel.setLayout(gl_panel);

		JToolBar toolBar = new JToolBar();
		toolBar.setAlignmentX(0.0f);
		GroupLayout groupLayout = new GroupLayout(this.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(toolBar, GroupLayout.DEFAULT_SIZE, 1251, Short.MAX_VALUE)
								.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1207, Short.MAX_VALUE))
						.addGap(1)));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 774, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)));

		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Filters", null, tabbedPane_2, null);

		JPanel panel_5 = new JPanel();
		tabbedPane_2.addTab("GestionListeners", null, panel_5, null);

		JPanel panel_6 = new JPanel();

		panel_6.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GroupLayout gl_panel_5 = new GroupLayout(panel_5);
		gl_panel_5.setHorizontalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup().addContainerGap()
						.addComponent(panel_6, GroupLayout.DEFAULT_SIZE, 1195, Short.MAX_VALUE).addContainerGap()));
		gl_panel_5.setVerticalGroup(gl_panel_5.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_5.createSequentialGroup().addContainerGap()
						.addComponent(panel_6, GroupLayout.PREFERRED_SIZE, 693, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(15, Short.MAX_VALUE)));

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		GroupLayout gl_panel_6 = new GroupLayout(panel_6);
		gl_panel_6.setHorizontalGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING).addGroup(gl_panel_6
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_6.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_8, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1277, Short.MAX_VALUE)
						.addComponent(panel_7, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 1277, Short.MAX_VALUE))
				.addContainerGap()));
		gl_panel_6.setVerticalGroup(gl_panel_6.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_6.createSequentialGroup().addContainerGap()
						.addComponent(panel_7, GroupLayout.PREFERRED_SIZE, 347, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_8, GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE).addContainerGap()));
		GroupLayout gl_panel_8 = new GroupLayout(panel_8);
		gl_panel_8
				.setHorizontalGroup(gl_panel_8.createParallelGroup(Alignment.LEADING).addGap(0, 1175, Short.MAX_VALUE));
		gl_panel_8.setVerticalGroup(gl_panel_8.createParallelGroup(Alignment.LEADING).addGap(0, 491, Short.MAX_VALUE));
		panel_8.setLayout(gl_panel_8);

		JButton btnNueva = new JButton("Nueva");
		btnNueva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newModelFilter();
			}

		});

		JButton btnBorrar_1 = new JButton("Borrar");
		btnBorrar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboModelFilters.getSelectedItem() == null)
					return;
				borrarModelFilterDeConsulta(catalogoModelFilters.get(comboModelFilters.getSelectedItem().toString()));
				borrarModelFilterDeListener(catalogoModelFilters.get(comboModelFilters.getSelectedItem().toString()));

				borrarModelFilter(comboModelFilters.getSelectedItem().toString());

			}
		});

		JLabel lblGestionListeners = new JLabel("Gestion Model Filters");
		lblGestionListeners.setFont(new Font("DejaVu Sans", Font.BOLD, 12));

		comboModelFilters = new JComboBox();
		comboModelFilters.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				logger.info("item status: " + arg0.getStateChange());
				if (arg0.getStateChange() == ItemEvent.SELECTED) {
					logger.info("item combo Model filter seleccionado " + arg0.getItem());
					refreshObjectTableFilter(arg0.getItem().toString());
				}
			}
		});

		// create el modelo table
		// EventMask unEvent = new EventMask("defaultMaskEvent", "Un evento por
		// defecto", "MASK_L1", "seguimiento", true);
		Vector<Object> vectorMasks = new Vector<Object>();
		// vectorMasks.add(unEvent.toVector());

		Vector<String> modelColumns = new Vector<String>();
		/*
		 * modelColumns.add("KeyEvent"); modelColumns.add("Descripcion");
		 * modelColumns.add("FormatEVent"); modelColumns.add("Topic");
		 * modelColumns.add("Enabled");
		 */

		// El
		TableListenerModel model = new TableListenerModel(vectorMasks, modelColumns);

		// DefaultTableModel model = new DefaultTableModel(vectorMasks,modelColumns);

		JPopupMenu popupMenu = new JPopupMenu();

		JMenuItem menuItemAdd = new JMenuItem("Add New Row");
		menuItemAdd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DefaultTableModel dtm = (DefaultTableModel) tableModelFilter.getModel();
				dtm.addRow(new EventMask().toVector());

			}

		});

		JMenuItem menuItemRemove = new JMenuItem("Remove Current Row");
		menuItemRemove.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				DefaultTableModel dtm = (DefaultTableModel) tableModelFilter.getModel();
				int selectedRow = tableModelFilter.getSelectedRow();
				((DefaultTableModel) tableModelFilter.getModel()).removeRow(selectedRow);

			}

		});

		JMenuItem menuItemRemoveAll = new JMenuItem("Remove All Rows");
		menuItemRemoveAll.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)	 {

				DefaultTableModel dtm = (DefaultTableModel) tableModelFilter.getModel();
				int rowCount = model.getRowCount();
				for (int i = 0; i < rowCount; i++) {
					dtm.removeRow(0);
				}
			}
		});

		popupMenu.add(menuItemAdd);
		popupMenu.add(menuItemRemove);
		popupMenu.add(menuItemRemoveAll);

		JLabel lblRegistrosDeEventos = new JLabel("Tabla Edicion Eventos para el Model Filter");
		lblRegistrosDeEventos.setFont(new Font("DejaVu Sans", Font.BOLD, 12));

		JScrollPane scrollPane_4 = new JScrollPane();

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (comboModelFilters.getSelectedItem() == null)
					return;
				guardarModelFilter(comboModelFilters.getSelectedItem().toString());
				guardarConsultaActual("nose usa",(Consulta) combo_Consultas.getSelectedItem());

				catalogListener = makeCatalogListeners();
				
			}
		});

		jTextDescripModelFilter = new JTextField();
		jTextDescripModelFilter.setColumns(10);

		JLabel lblDescripcionFilter = new JLabel("Descripcion Filter");
		lblDescripcionFilter.setFont(new Font("DejaVu Sans", Font.BOLD, 12));
		GroupLayout gl_panel_7 = new GroupLayout(panel_7);
		gl_panel_7.setHorizontalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING).addGroup(gl_panel_7
				.createSequentialGroup().addContainerGap()
				.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_4, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 1253, Short.MAX_VALUE)
						.addGroup(Alignment.TRAILING,
								gl_panel_7.createSequentialGroup()
										.addComponent(lblGestionListeners, GroupLayout.PREFERRED_SIZE, 207,
												GroupLayout.PREFERRED_SIZE)
										.addGap(947))
						.addComponent(lblRegistrosDeEventos, GroupLayout.PREFERRED_SIZE, 297,
								GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_panel_7.createSequentialGroup()
								.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING, false)
										.addComponent(comboModelFilters, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(gl_panel_7.createSequentialGroup()
												.addComponent(btnNueva, GroupLayout.PREFERRED_SIZE, 95,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED)
												.addComponent(btnBorrar_1, GroupLayout.PREFERRED_SIZE, 97,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnGuardar,
														GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)))
								.addGap(18)
								.addGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
										.addComponent(	lblDescripcionFilter, GroupLayout.PREFERRED_SIZE, 160,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(jTextDescripModelFilter, GroupLayout.PREFERRED_SIZE, 487,
												GroupLayout.PREFERRED_SIZE))))
				.addContainerGap()));
		gl_panel_7.setVerticalGroup(gl_panel_7.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_7.createSequentialGroup().addGap(14)
						.addComponent(lblGestionListeners, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE).addComponent(btnNueva)
								.addComponent(btnBorrar_1).addComponent(btnGuardar).addComponent(lblDescripcionFilter))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panel_7.createParallelGroup(Alignment.BASELINE)
								.addComponent(comboModelFilters, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(jTextDescripModelFilter, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(13).addComponent(lblRegistrosDeEventos).addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane_4, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE).addContainerGap()));

		// create the table
		tableModelFilter = new JTable(model);
		
		scrollPane_4.setViewportView(tableModelFilter);

		tableModelFilter.setBorder(new LineBorder(new Color(0, 0, 0)));

		// tableModelFilter.getColumnModel().getColumn(1).setPreferredWidth(643);
		tableModelFilter.setShowVerticalLines(true);
		tableModelFilter.setShowHorizontalLines(true);
		tableModelFilter.setCellSelectionEnabled(true);

		tableModelFilter.setComponentPopupMenu(popupMenu);

		panel_7.setLayout(gl_panel_7);
		panel_6.setLayout(gl_panel_6);
		panel_5.setLayout(gl_panel_5);

		this.getContentPane().setLayout(groupLayout);

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Opciones");
		menuBar.add(mnNewMenu);

		JMenuItem mntmOpcion_1 = new JMenuItem("Opcion2");
		mnNewMenu.add(mntmOpcion_1);

		JMenuItem mntmOpcion = new JMenuItem("Opcion1");
		mnNewMenu.add(mntmOpcion);

		JMenu mnNewMnu = new JMenu("New mnu");
		mnNewMenu.add(mnNewMnu);

		JMenuItem mntmNew_1 = new JMenuItem("New2");
		mnNewMnu.add(mntmNew_1);

		JMenuItem mntmNew = new JMenuItem("New1");
		mnNewMnu.add(mntmNew);

		JMenu mnConfiguracion = new JMenu("Configuracion");
		menuBar.add(mnConfiguracion);
		;

	}
	public void incluirModelFilterAListenersRemote(String newNameModel) {
		refreshComboListeners(newNameModel);
		catalogListener= makeCatalogListeners();
		comboListenersActivos.setSelectedIndex(comboListenersActivos.getItemCount() - 1);
		labelListenersCount.setText("(" + comboListenersActivos.getItemCount() + ")");
	}

	public void selectSistema(String nameSistemaSelected) {
		combo_Modulos.removeAllItems();
		comboFiltrosActivos.removeAllItems();
		filter.setText("");
		textfield_Mask.setText("");
	
		
	
		Consulta virtual = new Consulta();
		virtual.setSistemaConsulta(nameSistemaSelected);
		refreshComboConsultas(virtual);
		
		modulosRegistrables = initVectorModules(nameSistemaSelected + ".csv");

		//connect();
		logger.info("Nueva seleccion combo sistemas");
		
	}

	protected String[] makeCatalogListeners() {
		String listenerRapido = textFieldListener1.getText();
		ComboBoxModel<ModelFilter> cbm = comboListenersActivos.getModel();

		Vector<String> vMasks = new Vector<String>();

		// A�adimos el ListenerRapido (Solo si no esta vacio)
		if (!listenerRapido.equals(""))
			vMasks.add(listenerRapido);

		// Ahora el contenido de eventmasks en el Model Filter
		for (int i = 0; i < cbm.getSize(); i++) {
			ModelFilter mf = cbm.getElementAt(i);

			for (EventMask eventMask : mf.getVectorMasks()) {
				vMasks.add(eventMask.getNameMaskEvent());
			}
		}
		// -------------------------
		// Pasamos el vector a array
		// -------------------------
		String[] masks = new String[vMasks.size()];
		vMasks.toArray(masks);

		logger.info("Listeners =" + vMasks);
		return masks;
	}

	protected void incluirModelFilterAConsulta() {
		String sModelFilter = (String) dialogAddModelFilter();
		if (sModelFilter == null | combo_Consultas.getSelectedItem()==null)return;
		
		logger.info("El usuario ha elegido el Model Filter :" + sModelFilter);
		if(addItemComboModelFilterActivos(sModelFilter) != -1) {

			comboFiltrosActivos.setSelectedIndex(comboFiltrosActivos.getItemCount() - 1);
		}

	}

	protected String incluirModelFilterAListeners() {
		String keyModelFilter =  (String) dialogAddModelFilter();
		refreshComboListeners(keyModelFilter);
		return keyModelFilter;
	}

	private void refreshComboListeners(String keyModelFilter) {
		if(keyModelFilter==null)return;
		ModelFilter modelFilter = catalogoModelFilters.get(keyModelFilter);
		if (modelFilter == null)return;
		logger.info("El usuario ha elegido el Model Filter :" + modelFilter);
		addItemComboListenersActivos(modelFilter);

		
	}

	protected void borrarModelFilterDeConsulta(ModelFilter modelFilter) {

		if (modelFilter == null)
			return;
		comboFiltrosActivos.removeItem(modelFilter.getNameListener());
		logger.info("Removido Item Filtro Activo :" + modelFilter);

		String sistema = (String) comboSistemas.getSelectedItem();
		Consulta consulta =  (Consulta) combo_Consultas.getSelectedItem();
		consulta.setSistemaConsulta(sistema);
		guardarConsultaActual(null, consulta);
	}

	public void borrarModelFilterDeListener(ModelFilter modelFilter) {

		if (modelFilter == null)
			return;
		comboListenersActivos.removeItem(modelFilter);
		logger.info("Removido Item Listener Activo :" + modelFilter);
		catalogListener = makeCatalogListeners();
		comboListenersActivos.setSelectedIndex(comboListenersActivos.getItemCount() - 1);
		labelListenersCount.setText("(" + comboListenersActivos.getItemCount() + ")");

	}

	protected int addItemComboModelFilterActivos(String sModelFilter) {
		// Si no esta ya incluido , se añade

		if (comboFiltrosActivos.getItemCount() > 0) {
			for (int i = 0; i < comboFiltrosActivos.getItemCount(); i++) {
				if (comboFiltrosActivos.getItemAt(i).equals(sModelFilter)) {
					return -1;
				}
			}
		}
		comboFiltrosActivos.addItem(sModelFilter);
		labelFiltrosCount.setText("(" + comboFiltrosActivos.getItemCount() + ")");
		return 0;

	}
	protected void addItemComboListenersActivos(ModelFilter modelFilter) {
		// Si no esta ya incluido , se añade

		if (comboListenersActivos.getItemCount() > 0) {
			for (int i = 0; i < comboListenersActivos.getItemCount(); i++) {
				if (comboListenersActivos.getItemAt(i).getNameListener().equals(modelFilter.getNameListener())) {
					return;
				}
			}
		}
		comboListenersActivos.addItem(modelFilter);
	}
	
	protected int addItemAListaConsultasModoConsulta(String sTarea,ConsultaTarea newConsulta) {
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		Tarea tarea = catalogoTareas.get(sTarea);
		Vector<ConsultaTarea> vConsultas = tarea.getConsultas();
		//Si esta consulta ya esta return -1
		for (ConsultaTarea consultaTarea: vConsultas) {
			if(newConsulta.nombreConsultaTareaFull().equals(consultaTarea.nombreConsultaTareaFull()))return -1;
		}
		return 0;

	}

	/**
	 * 
	 * @return Object de ModelFilter
	 */
	protected Object dialogAddModelFilter() {
	
		// Con JCombobox)
		//if (comboModelFilters.getItemCount() == 0)return null;
		
		
		Vector<String> vKeys = new Vector<String>();
		
	
		for ( Enumeration<String> enumKeys = catalogoModelFilters.keys();enumKeys.hasMoreElements();) {
			   vKeys.add(enumKeys.nextElement());
		}
		
		Object selModelFilter = JOptionPane.showInputDialog(contentPane, "Seleccione Model Filter",
				"Selector de modulos de filtro activos", JOptionPane.QUESTION_MESSAGE, null, vKeys.toArray(),
				"Seleccione Modulo");
	
		
	
		return selModelFilter;
	
	}


	
	protected Object dialogAddConsulta() {

		
		Vector<String> vConsultas = new Vector<String>();
		

		for ( Enumeration<String> enumKeys = catalogoConsultas.keys();enumKeys.hasMoreElements();) {
			   vConsultas.add(enumKeys.nextElement());
		}
		
		Object selConsulta = JOptionPane.showInputDialog(contentPane, "Seleccione Consulta",
				"Selector de Consultas modo Tarea", JOptionPane.QUESTION_MESSAGE, null, vConsultas.toArray(),
				"Seleccione Modulo");

		

		return selConsulta;

	}

	protected void borrarModelFilter(String nameModelFilter) {

		if (nameModelFilter == null | comboModelFilters.getItemCount() == 0)
			return;

		catalogoModelFilters.remove(nameModelFilter);

		comboModelFilters.removeItem(nameModelFilter);
		comboModelFilters.setSelectedIndex(-1);

		jTextDescripModelFilter.setText("");

		tableModelFilter.setModel(new DefaultTableModel());
		logger.info("Borrado Model filter:" + nameModelFilter);

		salvarCatalogos();
	}

	protected void refreshObjectTableFilter(String nameModelFilter) {

		ModelFilter modelFilter = catalogoModelFilters.get(nameModelFilter);

		jTextDescripModelFilter.setText(modelFilter.getDescripListener());
		DefaultTableModel dtm = (DefaultTableModel) tableModelFilter.getModel();
		dtm.setDataVector(modelFilter.getDataVector(), modelFilter.getModelColumns());

		tableModelFilter.setModel(dtm);
		logger.info("Refrescado objeto TableFilter. Actualizado catalogo de filtros");
	}

	protected void guardarModelFilter(String nameFilter) {

		DefaultTableModel defaultTableModel = (DefaultTableModel) tableModelFilter.getModel();
		Vector<Vector> data = defaultTableModel.getDataVector();

		ModelFilter modelFilter = new ModelFilter(nameFilter);
		modelFilter.setDescripListener(jTextDescripModelFilter.getText());
		modelFilter.updateModelFilter(data);

		if (catalogoModelFilters.containsKey(nameFilter)) {
			//ModelFilter modelFilterOriginal = catalogoModelFilters.get(nameFilter);
			 catalogoModelFilters.get(nameFilter).updateModelFilter(data);
			logger.debug("Reemplazando los vectores de datos del ModelFilter " + nameFilter + " al Catalogo de ModelFilters");
		} else {
			catalogoModelFilters.put(nameFilter, modelFilter);
			logger.debug("Insertado el ModelFilter  " + nameFilter + " al Catalogo de ModelFilters");
		}

	}

	/*
	 * 
	 * 
	 */
	synchronized protected String filterLines(ArrayBlockingQueue<String> lines, String sFilter) {
		// Creamos un Stream a partir del array de String lines y filtramos por IDs
		Algoritmos algoritmo = new Algoritmos();
		
		if (sFilter.equals("")) {
			return lines.stream().map(line -> line = line.concat(System.getProperty("line.separator")))
					.collect(Collectors.joining());// Obtenemos un String final
		} else {
			return lines.stream()
					.filter(line -> line.contains(sFilter) | line.contains(sFilter.toLowerCase()))
					.map(line -> line = line.concat(System.getProperty("line.separator")))
					.collect(Collectors.joining());// Obtenemos un String final
		}
	}
	//Version de filter Lines que permite parametrizar varios Sfilter contenidos en un array
	synchronized protected String filterLines(ArrayBlockingQueue<String> lines, String[] sFilter) {
		// Creamos un Stream a partir del array de String lines y filtramos por IDs
		Algoritmos algoritmo = new Algoritmos();
		
		if (sFilter.equals("")) {
			return lines.stream().map(line -> line = line.concat(System.getProperty("line.separator")))
					.collect(Collectors.joining());// Obtenemos un String final
		} else {
			//return lines.stream().filter(line -> line.contains(sFilter) | line.contains(sFilter.toLowerCase()))
			return lines.stream()
					.filter(line -> algoritmo.filterMatch(line,sFilter,true))
					.map(line -> line = line.concat(System.getProperty("line.separator")))
					.collect(Collectors.joining());// Obtenemos un String final
		}
	}

	
	public void mostrarAyudaModulo(Modulo mod) {
		// Se obtiene el sistema desde el combo sistemas
		enviarComando("sc " + mod.getNombre() + " he",(String) comboSistemas.getSelectedItem());

	}

	/**
	 *  Para enviar un comando, dada la hibridez de sistemas posibles en la consulta
	 *  Es necesario especificar sistema y socket (aunque en un sistema rigido de instanciacion de
	 *  sockets , seria posible solo con el sistema, ya que en base a este se asignaria su socket fijo
	 * @param comando
	 * @param sistema
	 * @param socket
	 */
	
	public void enviarComando(String comando, String sistema) {
		byte[] mensaje_bytes = new byte[256];

		
		InfoConexionSistema infoSistema = this.infoConexionRegistry.get(sistema);
		// Paquete
		DatagramPacket paquete;
		InetAddress address;

		try {
			address = InetAddress.getByName(infoSistema.getIp());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		mensaje_bytes = comando.getBytes();
		paquete = new DatagramPacket(mensaje_bytes, comando.length(), address, 5008);
		try {
			DatagramSocket socket = this.socketSistemaRegistry.get(infoSistema.getNameSocketSistema());
			if (socket != null) {
				
				socket.send(paquete);
				logger.info("Enviando sobre "+ sistema+ " comando :" + comando);
			}else {
					logger.warn("Ojo, el socket es null enviando "+ comando);
			}
	
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Conectar Socket UDP a CDBGM
	// Param :IP servidor PPC
	// Constants :port listen seer cdbgm -> 5008
	public DatagramSocket conectarSocket(ConsultaTarea cTarea) {
		DatagramSocket socketSistema=null;
		// Definimos el sockets, número de bytes del buffer, y mensaje.
		InfoConexionSistema infoConnectSystem = this.infoConexionRegistry.get(cTarea.getNameSocketSistema());
		String ipPPC = infoConnectSystem.getIp();
		InetAddress address;
		byte[] mensaje_bytes = new byte[256];
		String mensaje = "";
		mensaje_bytes = mensaje.getBytes();
		// Paquete
		DatagramPacket paquete;
		try {
			// 1 Socket por sistema / 1  Receivers por dispatcher
			
			socketSistema = getSocketPoll(cTarea.getNameSocketSistema());
			address = InetAddress.getByName(ipPPC);
			mensaje = "h";
			mensaje_bytes = mensaje.getBytes();
			paquete = new DatagramPacket(mensaje_bytes, mensaje.length(), address, 5008);
			socketSistema.send(paquete);

		} catch (Exception e) {
			System.err.println("Error : " + e.toString());
		}
		return socketSistema;
	}
	
	
	
	private void instanciarReceiver(DatagramSocket socket,ConsultaTarea cTarea) {
	
			if(threadReceiverRegistry.get(cTarea.getNameSocketSistema())==null) {
				Vector<Receiver> vThread = new Vector<Receiver>();
				//No hay que controlar aqui porque es el primero en a�adir
				Receiver runReceiver = new Receiver(socket, this, cTarea);
				Thread threadReceiver = new Thread(runReceiver);
				threadReceiver.start();
				//vThread.add(threadReceiver);
				threadReceiverRegistry.put(cTarea.getNameSocketSistema(), vThread);
				logger.debug("Inicializando Procesamiento thread de "+ cTarea.getNameSocketSistema());
				
			}else {
				//Controlo si el vector de Threads tiene mas que el maximo de hilos permitidos por sistema
				Vector<Receiver> vHilos = threadReceiverRegistry.get(cTarea.getNameSocketSistema());
				if(vHilos.size() >= maxThreadBySistema) {
					logger.debug("Ya se ha alcanzado el numero maximo de hilos por receiver...");
					return;
				}else {
					Receiver runReceiver = new Receiver(socket, this, cTarea );
					Thread threadReceiver = new Thread(runReceiver);
					threadReceiver.start();
				}
			}
		refreshLedsSocketsStatus();
		
	}

	private DatagramSocket getSocketPoll(String nameSocketSistema) {
		// solo utilizamos un Socket por sistema
		DatagramSocket datagramSocket=null;
		if(socketSistemaRegistry.get(nameSocketSistema)==null){
			try {
				datagramSocket = new DatagramSocket();
				datagramSocket.setSoTimeout(4000);//Si en 4 segundos no hay respuesta raise Exception
				socketSistemaRegistry.put(nameSocketSistema, datagramSocket);
				this.getFlagDisconnectRegistry().put(nameSocketSistema, 0);

			
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}else {
			datagramSocket = socketSistemaRegistry.get(nameSocketSistema); 
		}
		
		return datagramSocket;
	}
	
	private DatagramSocket getSocketDispatcherPoll(String nameSocketSistema) {
		// solo utilizamos un SocketDispatcher por sistema
		DatagramSocket datagramSocket=null;
		if(socketDispatcherRegistry.get(nameSocketSistema)==null){
			try {
				datagramSocket = new DatagramSocket();
				datagramSocket.setSoTimeout(4000);//Si en 4 segundos no hay respuesta raise Exception
				socketDispatcherRegistry.put(nameSocketSistema, datagramSocket);
				//this.getFlagDisconnectRegistry().put(nameSocketSistema, 0);

			
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}else {
			datagramSocket = socketDispatcherRegistry.get(nameSocketSistema); 
		}
		
		return datagramSocket;
	}
	private DatagramSocket getSocketReceiverPoll(Receiver receiver, String nameSistema) {
		// solo utilizamos un SocketDispatcher por sistema
		DatagramSocket datagramSocket=null;
		if(socketDispatcherRegistry.get(nameSistema)==null){
			try {
				datagramSocket = new DatagramSocket();
				datagramSocket.setSoTimeout(4000);//Si en 4 segundos no hay respuesta raise Exception
				socketDispatcherRegistry.put(nameSistema, datagramSocket);
				//this.getFlagDisconnectRegistry().put(nameSocketSistema, 0);

			
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}else {
			datagramSocket = socketDispatcherRegistry.get(nameSistema); 
		}
		
		return datagramSocket;
	}

	
	private Dispatcher getDispatcherPoll(DatagramSocket datagramSocket ,DatagramSocket datagramDispatcher,Visualizador vis, String nameSocketSistema) {
		
		Dispatcher dispatcher=null;
		if(dispatcherSistemaRegistry.get(nameSocketSistema)==null){
				
				dispatcher = new Dispatcher(datagramSocket, datagramDispatcher, vis, nameSocketSistema);
				this.getFlagDisconnectRegistry().put(nameSocketSistema, 0);
				dispatcherSistemaRegistry.put(nameSocketSistema, dispatcher);
				
		}else {
			dispatcher = dispatcherSistemaRegistry.get(nameSocketSistema); 
		}
		
		return dispatcher;
	}

	public void connect() {
		String top =  ""+spinner.getValue();
		
		Consulta consulta = (Consulta) combo_Consultas.getSelectedItem();
		
		if(consulta==null)return;	
		
		ConsultaTarea consultaTarea = new ConsultaTarea(consulta,top) ;
		connect(consultaTarea);
	}
	
	public void connect(ConsultaTarea cTarea) {
			
		if(this.modoTrabajo==3) {
			
		}
		instanciarReceiver(conectarSocket(cTarea),cTarea);
			
	}

	
	private void initInfoConexiones(String centro) {
		InfoConexionSistema infoSistema;
		if(centro=="Madrid") {
			infoSistema = new InfoConexionSistema();
			//infoSistema.setId("Linea_Entrada1");
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.4.12.139");
			infoSistema.setNameSocketSistema("IL:1");
			infoSistema.setTopNumero(1);
			this.infoConexionRegistry.put("IL:1", infoSistema);
			this.ledSocketRegistry.put("IL:1", chckbxTOP1_IL);	
			this.numThreadsLabel.put("IL:1", lblCountThreads_IL_1);
			

			infoSistema = new InfoConexionSistema();
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.4.12.149");
			infoSistema.setNameSocketSistema("IL:2");
			infoSistema.setTopNumero(2);
			this.infoConexionRegistry.put("IL:2", infoSistema);
			this.ledSocketRegistry.put("IL:2", chckbxTOP2_IL);
			this.numThreadsLabel.put("IL:2", lblCountThreads_IL_2);			
			
			infoSistema = new InfoConexionSistema();
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.4.13.139");
			infoSistema.setNameSocketSistema("SCO:1");
			infoSistema.setTopNumero(1);
			this.infoConexionRegistry.put("SCO:1", infoSistema);
			this.ledSocketRegistry.put("SCO:1", chckbxTOP1_SCO);
			this.numThreadsLabel.put("SCO:1", lblCountThreads_SCO_1);
			
			infoSistema = new InfoConexionSistema();
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.4.13.149");
			infoSistema.setNameSocketSistema("SCO:2");
			infoSistema.setTopNumero(2);
			this.infoConexionRegistry.put("SCO:2", infoSistema);
			this.ledSocketRegistry.put("SCO:2", chckbxTOP2_SCO);
			this.numThreadsLabel.put("SCO:2", lblCountThreads_SCO_2);
			
			infoSistema = new InfoConexionSistema();
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.4.14.139");
			infoSistema.setNameSocketSistema("ATHS:1");
			infoSistema.setTopNumero(1);
			this.infoConexionRegistry.put("ATHS:1", infoSistema);
			this.ledSocketRegistry.put("ATHS:1", chckbxTOP1_ATHS);
			this.numThreadsLabel.put("ATHS:1", lblCountThreads_ATHS_1);			
			
			infoSistema = new InfoConexionSistema();
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.4.14.149");
			infoSistema.setNameSocketSistema("ATHS:2");
			infoSistema.setTopNumero(2);
			this.infoConexionRegistry.put("ATHS:2", infoSistema);
			this.ledSocketRegistry.put("ATHS:2", chckbxTOP2_ATHS);	
			this.numThreadsLabel.put("ATHS:2", lblCountThreads_ATHS_2);	

			refreshLedsSocketsStatus();
		}
	
		if(centro=="Valladolid") {
			infoSistema = new InfoConexionSistema();
			//infoSistema.setId("Linea_Entrada1");
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.22.12.139");
			infoSistema.setNameSocketSistema("IL:1");
			infoSistema.setTopNumero(1);
			this.infoConexionRegistry.put("IL:1", infoSistema);
			this.ledSocketRegistry.put("IL:1", chckbxTOP1_IL);	
			this.numThreadsLabel.put("IL:1", lblCountThreads_IL_1);
			

			infoSistema = new InfoConexionSistema();
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.22.12.149");
			infoSistema.setNameSocketSistema("IL:2");
			infoSistema.setTopNumero(2);
			this.infoConexionRegistry.put("IL:2", infoSistema);
			this.ledSocketRegistry.put("IL:2", chckbxTOP2_IL);
			this.numThreadsLabel.put("IL:2", lblCountThreads_IL_2);			
			
			infoSistema = new InfoConexionSistema();
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.22.13.139");
			infoSistema.setNameSocketSistema("SCO:1");
			infoSistema.setTopNumero(1);
			this.infoConexionRegistry.put("SCO:1", infoSistema);
			this.ledSocketRegistry.put("SCO:1", chckbxTOP1_SCO);
			this.numThreadsLabel.put("SCO:1", lblCountThreads_SCO_1);
			
			infoSistema = new InfoConexionSistema();
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.22.13.149");
			infoSistema.setNameSocketSistema("SCO:2");
			infoSistema.setTopNumero(2);
			this.infoConexionRegistry.put("SCO:2", infoSistema);
			this.ledSocketRegistry.put("SCO:2", chckbxTOP2_SCO);
			this.numThreadsLabel.put("SCO:2", lblCountThreads_SCO_2);
			
			infoSistema = new InfoConexionSistema();
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.22.14.139");
			infoSistema.setNameSocketSistema("ATHS:1");
			infoSistema.setTopNumero(1);
			this.infoConexionRegistry.put("ATHS:1", infoSistema);
			this.ledSocketRegistry.put("ATHS:1", chckbxTOP1_ATHS);
			this.numThreadsLabel.put("ATHS:1", lblCountThreads_ATHS_1);			
			
			infoSistema = new InfoConexionSistema();
			infoSistema.setCentro(centro);
			infoSistema.setIp("21.22.14.149");
			infoSistema.setNameSocketSistema("ATHS:2");
			infoSistema.setTopNumero(2);
			this.infoConexionRegistry.put("ATHS:2", infoSistema);
			this.ledSocketRegistry.put("ATHS:2", chckbxTOP2_ATHS);	
			this.numThreadsLabel.put("ATHS:2", lblCountThreads_ATHS_2);	

			refreshLedsSocketsStatus();
		}
		
	}
	

	public void disconnectManual(String sistema) {
		if(sistema.equals("All")) {
			//Por si da problemas de concurencia conflictos al borrar
			//Iterator<String> keysSocket =  this.socketSistemaRegistry.keys().asIterator();
			for(Enumeration<String> keysSockets = socketSistemaRegistry.keys();keysSockets.hasMoreElements();) {
				//Lllamamos recursivamente a este metodo
				disconnectManual(keysSockets.nextElement());
			}
		}else {
			//Ajustar el flag de control final de hilo para este sistema 
			this.getFlagDisconnectRegistry().put(sistema,1);
			enviarComando("ST ALL 0", sistema);
			logger.info("enviado ST ALL 0");
			socketSistemaRegistry.remove(sistema);
			//this.refreshLedsSocketsStatus();//Hacemos la llamada desde los hilos
		}
		
	
		
	
	}

	// Crea un array con un catalogo de cadenas como criterio para la inclusion de
	// lineas
	// Paramas: un String con una mascara de tipo "criterio1|criterio2|...criterion"
	public String[] makeCatalogFilter(Consulta consulta) {

		Vector<String> vMasker = new Vector<String>();
		// Si no hay consultas definidas salimos con default mask
		if (combo_Consultas.getItemCount() == 0 | consulta == null) {
			String[] masks = new String[1];
			masks[0] = "";
			logger.info("Masks = \"\"" );
			return masks;
		}

		// -----------------------------------------------------------
		// verificar si incluir mascaras exclusivas de modulos activos
		// -----------------------------------------------------------

		Vector<Modulo> vModulos = consulta.getModulosActivos();
		if (this.filterExclusive.isSelected()) {
			// Incluir las mascaras de raiz modulo como una cadena con su nombre.
			// De esta manera todas las lineas info de este modulo iran incluidas en reader
			// events
			vModulos.stream().forEach(modulo -> {
				vMasker.add(modulo.getNombre());
				logger.info("Añadido mask exclusive :" + modulo.getNombre());
			});
		}

		// Añadiendo las mascaras de filtro rapido
		StringTokenizer st = new StringTokenizer(consulta.getFiltro(), "|");
		if (!st.hasMoreElements()) {
			if (this.filterExclusive.isSelected()) {
				if (!consulta.getFiltro().equals(""))
					vMasker.add(consulta.getFiltro());
			} else {
				vMasker.add(consulta.getFiltro());
			}
		} else {
			while (st.hasMoreElements()) {

				vMasker.add(st.nextToken());
			}
		}

		// Añadiendo las mascaras ModelFilter registradas en la consulta. En cada Model filter hay un
		// EventMask con cada mascara registrada
		Vector<ModelFilter> filtrosEventMask = consulta.getFiltrosActivos();
		filtrosEventMask.forEach(modelFilter -> {
			Vector<EventMask> vEventMask = modelFilter.getVectorMasks();
			vEventMask.forEach(eventMask -> {
				vMasker.add(eventMask.getNameMaskEvent());//la mascara propiamente dicha
			});
		});

		// --------------------------------------------------------------------
		// Pasamos el vector a array
		String[] masks = new String[vMasker.size()];

		// Si no hay nada que filtrar se establece filtro default ""
		if (vMasker.size() == 0) {
			masks = new String[1];
			masks[0] = "";
		} else {
			vMasker.toArray(masks);
		}
		logger.info("Masks para = "+consulta +" "+ vMasker);
		return masks;
	}

	// Inicializa el vector de modulos tratables
	private Vector<Modulo> initVectorModules(String sistema) {
		Vector<Modulo> vModules = new Vector<Modulo>();

		URL url = getClass().getClassLoader().getResource("cta/resources/");
		String ruta="";
		try {
			ruta = URLDecoder.decode(new File(url.getPath()).getAbsolutePath(), StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String fichero = ruta + FileSystems.getDefault().getSeparator() +sistema;//
		logger.info("Ruta para cargar recurso Modules:" + fichero);
		
		
		InputStream inputStream=null;
		try {
			inputStream = new FileInputStream(fichero);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStreamReader isr = new InputStreamReader(inputStream);
		BufferedReader fr = new BufferedReader(isr);
		String[] datosModulo = new String[4];
		//0:sistema; 1: nombre modulo; 2: descripcion modulo; 3 : mask 
		try {
			String strCurrentLine = fr.readLine();
			while (strCurrentLine != null) {
				StringTokenizer st = new StringTokenizer(strCurrentLine, ",");
				int n = 0;
				while (st.hasMoreElements()) {
					datosModulo[n] = st.nextToken();
					n++;
				}
				Modulo mod = new Modulo();
				mod.setSistema(datosModulo[0]);
				mod.setNombre(datosModulo[1]);
				mod.setDescripcion(datosModulo[2]);
				//logger.info("Descripcion :" + mod.getDescripcion());
				mod.setMask(datosModulo[3]);
				vModules.add(mod);

				strCurrentLine = fr.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vModules;
	}

	// Muestra cuadro eleccion modulo activable
	private void dialogAddModulo() {
		// Con JCombobox
		if (combo_Consultas.getItemCount() == 0)
			return;
		Modulo miModuloClonado, clon = null;
		Object selModulo = JOptionPane.showInputDialog(contentPane, "Seleccione modulo",
				"Selector de modulos consulta activa", JOptionPane.QUESTION_MESSAGE, null,
				modulosRegistrables.toArray(), "Seleccione Modulo");

		logger.info("El usuario ha elegido " + selModulo);
		if (selModulo != null) {
			clon = (Modulo) selModulo;
			try {
				miModuloClonado = (Modulo) clon.clone();

				miModuloClonado.setMask("ffffff");
				textfield_Mask.setText("FFFFFF");
				logger.info("Declarado modulo " + miModuloClonado);
				this.combo_Modulos.addItem(miModuloClonado);

			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String sistema = (String) comboSistemas.getSelectedItem();
			Consulta consulta =  (Consulta) combo_Consultas.getSelectedItem();
			
			guardarConsultaActual(sistema, consulta);
			this.combo_Modulos.setSelectedIndex(this.combo_Modulos.getItemCount() - 1);

		}
		return;
		// return (Modulo)selModulo;
	}

	private void newModelFilter() {
		// Con caja de texto
		String nameModelFilter = JOptionPane.showInputDialog(contentPane, "De un nombre al nuevo ModelFilter", null);
		if (nameModelFilter == null)
			return;

		String descripModelFilter = JOptionPane.showInputDialog(contentPane,
				"Haga una breve descripcion de este Model Filter", null);

		ModelFilter modelFilter = new ModelFilter(nameModelFilter);

		modelFilter.setDescripListener(descripModelFilter);

		jTextDescripModelFilter.setText(descripModelFilter);
		Vector<Vector<Object>> dataModelTable = modelFilter.getDataVector();

		Vector<String> modelColumns = modelFilter.getModelColumns();

		catalogoModelFilters.put(nameModelFilter, modelFilter);

		TableListenerModel model = new TableListenerModel(dataModelTable, modelColumns);
		tableModelFilter.setModel(model);

		refreshComboModelFilters(nameModelFilter);

	}

	private void refreshComboModelFilters(String nameModelFilter) {
		int index = 0;
		int indexComp = 0;
		comboModelFilters.removeAllItems();
		String item;
		for (Enumeration<String> enumModelFilters = catalogoModelFilters.keys(); enumModelFilters.hasMoreElements();) {

			comboModelFilters.addItem(item = enumModelFilters.nextElement());
			if (item.equals(nameModelFilter))
				indexComp = index;
			index++;
		}
		if (comboModelFilters.getItemCount() > 0)
			comboModelFilters.setSelectedIndex(indexComp);
	}

	private void setMaskModulo(String text) {
		Modulo modSelected = (Modulo) this.combo_Modulos.getSelectedItem();
		if (modSelected == null)
			return;
		modSelected.setMask(text);
		
		Consulta consulta = (Consulta) this.combo_Consultas.getSelectedItem();
		// Añadimos el reciente modulo elegido a la cansulta, como activo si no estaba
		// ya
		consulta.insertarModuloActivo(modSelected);
		int indexSelectedMod = this.combo_Modulos.getSelectedIndex();

		refreshComboModulos(consulta.getNombreConsultaFull());
		this.combo_Modulos.setSelectedIndex(indexSelectedMod);

	}

	// Inicializa el Vector de consultas
	private void initVectorConsultas() {
		// pendiente implementacion Leer registro consultas
		catalogoConsultas = new ConcurrentHashMap<String, Consulta>();
	}

	private void initVectorModelFilters() {
		catalogoModelFilters = new ConcurrentHashMap<String, ModelFilter>();
	}

	// Crea una nueva consulta. Pide un nombre y la registra
	private void newConsulta() {
		// Con caja de texto
		String nameConsulta = JOptionPane.showInputDialog(contentPane, "De un nombre a la nueva consulta", null);
		if (nameConsulta == null)
			return;
		// el icono sera un iterrogante
		// borra el combo de modulos activos
		// crea una nueva entrada de consulta en el registro de consultas

		Consulta consulta = new Consulta();
		//String top = (String) spinner.getValue();
		
		consulta.setSistemaConsulta((String) this.comboSistemas.getSelectedItem());
		consulta.setNameConsulta(nameConsulta);
		consulta.setFiltro("");
		Vector<Modulo> vModulos = new Vector<Modulo>();
		consulta.setModulosActivos(vModulos);
		Vector<ModelFilter> vModelFilters = new Vector<ModelFilter>();
		consulta.setFiltrosActivos(vModelFilters);
		String nameConsultaFull = consulta.getNombreConsultaFull();

		catalogoConsultas.put(nameConsultaFull, consulta);
		//enviarComando("ST ALL 0",);
		filter.setText("");

		refreshComboConsultas(consulta);
		// combo_Consultas.setSelectedIndex(combo_Consultas.getItemCount()-1);

		logger.info("El usuario ha añadido " + nameConsulta);
	}

	private void refreshComboConsultas(Consulta cConsulta) {
		int index = 0;
		int indexComp = 0;
		combo_Consultas.removeAllItems();
		String item;
		for (Enumeration<String> enumKeysConsultas = catalogoConsultas.keys(); enumKeysConsultas.hasMoreElements();) {
		
			Consulta consulta = catalogoConsultas.get(enumKeysConsultas.nextElement());
			
			// Solo mostramos consultas filtradas para el sistema dado
		
				if (consulta.getSistemaConsulta().equals(cConsulta.getSistemaConsulta())) {
					combo_Consultas.addItem(consulta);
					if (consulta==cConsulta)
						indexComp = index;
					index++;
				}				
			

		}
		if (combo_Consultas.getItemCount() > 0)
			combo_Consultas.setSelectedIndex(indexComp);
	}

	// Actualiza estructuras de informacion consulta
	private void refreshObjectConsulta(Consulta consulta) {

		if (consulta == null)
			return;
	
		refreshComboModulos(consulta.getNombreConsultaFull());
		refreshComboFiltrosActivos(consulta.getNombreConsultaFull());

		filter.setText(consulta.getFiltro());
	
	}

	private void refreshComboModulos(String nameConsulta) {
	
		Consulta consulta = catalogoConsultas.get(nameConsulta);
	
		String sistema = consulta.getSistemaConsulta()+":"+spinner.getValue();
		combo_Modulos.removeAllItems();
		textfield_Mask.setText("");
		if(modoTrabajo==1) {		
			enviarComando("ST ALL 0",sistema);
			updateNivelesTrazaConsulta(consulta,sistema);
		}
		updateComboModulos(consulta, sistema);
		labelModulosCount.setText("(" + combo_Modulos.getItemCount() + ")");
	}
	
	private void updateNivelesTrazaConsulta(Consulta consulta, String sistema) {
		Vector<Modulo> vModulos = consulta.getModulosActivos();
		
		Iterator<Modulo> it = vModulos.iterator();
		while (it.hasNext()) {
			Modulo mod = (Modulo) it.next();
			enviarComando("ST " + mod.nombre + " " + mod.getMask(), sistema);
			logger.info("Activado modulo " + mod );
		}

	}
	
	private void updateComboModulos(Consulta consulta, String sistema){
		Vector<Modulo> vModulos = consulta.getModulosActivos();
		
		Iterator<Modulo> it = vModulos.iterator();
		while (it.hasNext()) {
			Modulo mod = (Modulo) it.next();
			combo_Modulos.addItem(mod);
		}
	}

	private void refreshComboFiltrosActivos(String nameConsultaFull) {
		
		comboFiltrosActivos.removeAllItems();

		Vector<ModelFilter> vFiltros = catalogoConsultas.get(nameConsultaFull).getFiltrosActivos();
		Iterator<ModelFilter> it = vFiltros.iterator();

		while (it.hasNext()) {
			ModelFilter filter = (ModelFilter) it.next();

			comboFiltrosActivos.addItem(filter.getNameListener());

			logger.info("A�adido Item  a comboFiltros " + filter);
		}
		labelFiltrosCount.setText("(" + comboFiltrosActivos.getItemCount() + ")");
	}

	// Guarda la consulta actual al registro de consultas
	// recoge los valores de texto en la caja filtro texto y
	// añade el vector de modulos presente en el combo modulosActivos, asi como el
	// de filtros activos

	private void guardarConsultaActual(String sistema, Consulta consulta) {
	
		String filtroTextoActual = filter.getText();
		// Preparar vector modulos activos
		Vector<Modulo> vMod = new Vector<Modulo>();
		for (int n = 0; n < combo_Modulos.getItemCount(); n++) {
			Modulo modElegido = combo_Modulos.getItemAt(n);
			vMod.add(modElegido);
		}
		// Preparar vector de filtros Activos
		Vector<ModelFilter> vFilter = new Vector<ModelFilter>();
		for (int n = 0; n < comboFiltrosActivos.getItemCount(); n++) {
			//ModelFilter filterElegido = catalogoModelFilters.get(comboFiltrosActivos.getItemAt(n));
			vFilter.add(catalogoModelFilters.get(comboFiltrosActivos.getItemAt(n)));
		}

		Consulta newConsulta = new Consulta();

		newConsulta.setFiltro(filtroTextoActual);
		newConsulta.setNameConsulta(consulta.getNameConsulta());
		newConsulta.setSistemaConsulta(consulta.getSistemaConsulta());
		newConsulta.setModulosActivos(vMod);
		newConsulta.setFiltrosActivos(vFilter);
		String nameConsultaFull = newConsulta.getNombreConsultaFull();

		if (catalogoConsultas.containsKey(nameConsultaFull)) {
			catalogoConsultas.replace(nameConsultaFull, newConsulta);
			logger.debug("Reemplazando objeto "+ nameConsultaFull +" en CatalogoConsultas");
		} else {
			catalogoConsultas.put(newConsulta.getNombreConsultaFull(), newConsulta);
			logger.debug("Poniendo un nuevo objeto "+ nameConsultaFull +" en CatalogoConsultas");
			
		}

		refreshObjectConsulta(newConsulta);
		
		//La cuestion ahora es que el catalogFilters es construido en base al Modo de trabajo establecido
		//1 : Consulta ; 2: MultiConsulta
			String keyConsulta = newConsulta.getNombreConsultaFull();
			catalogFiltersRegistry.put(keyConsulta, makeCatalogFilter(newConsulta));
			logger.info("Actualizado catalogo de filtros para la consulta:"+keyConsulta);

		// Serializamos el catalogo a fichero
		salvarCatalogos();
	}

	// Borra la consulta actual al registro de consultas y refresca
	// estructuras y filtros
	private void borrarConsultaActual(String nameConsulta) {

		// Borrar los modulos pertenecientes a esta consulta y su desactivacion de
		// trazas
		Consulta consulta = catalogoConsultas.get(nameConsulta) ;
		Vector<Modulo> iMods = consulta.modulosActivos;
		Vector <Modulo> iModsClone = (Vector<Modulo>) iMods.clone();
		for (Modulo iModsClons : iModsClone) {
			borrarModuloActual(nameConsulta, iModsClons);
		}
		
		Vector<ModelFilter> iFilters =catalogoConsultas.get(nameConsulta).getFiltrosActivos();
		for(int i=0 ; i < iFilters.size(); i++) {
			borrarModelFilterDeConsulta(iFilters.elementAt(i));
		}
		filter.setText("");
		
		catalogFiltersRegistry.remove(consulta.getNombreConsultaFull());
	

		combo_Consultas.removeItem(combo_Consultas.getSelectedItem());
		combo_Consultas.setSelectedIndex(-1);
		

		refreshComboFiltrosActivos(nameConsulta);
		
		refreshComboModulos(nameConsulta);
		
		catalogoConsultas.remove(nameConsulta);

		salvarCatalogos();

	
		logger.info("El usuario ha eliminado la consulta : " + nameConsulta);
	}

	// Borra el modulo actual seleccionado de la consulta seleccionada actual y lo
	// desactiva
	private void borrarModuloActual(String nameConsulta, Modulo modSelected) {

		Consulta consultaSelected = catalogoConsultas.get(nameConsulta);
		if (consultaSelected == null | modSelected == null)
			return;
		int top = (int) spinner.getValue();
	
		consultaSelected.getModulosActivos().remove(modSelected);
		
		combo_Modulos.removeItem(modSelected);
		//textfield_Mask.setText("");
		// enviar anulacion modulo activo a socket system
		enviarComando("ST " + modSelected.nombre + " 0", consultaSelected.getSistemaConsulta()+":"+top);
		logger.info("desactivado modulo:" + modSelected);
		guardarConsultaActual("",catalogoConsultas.get(nameConsulta));
	}

	// Serializa el objeto catalogoConsultas para conservar definiciones consultas
	private void salvarCatalogos() {
		String ruta, fichero;
		URL url;
	
		
		/*url = Visualizador.class.getClassLoader().getResource("cta/resources/");
		ruta = new File(url.getPath()).getAbsolutePath();
		try {
			ruta = URLDecoder.decode(ruta, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		ruta = catalogos;
		fichero = ruta + FileSystems.getDefault().getSeparator() + "catalogoConsultas.def";
		logger.info("Ruta para salvar Catalogos:" + fichero);
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(fichero)));
	
			oos.writeObject(this.catalogoConsultas);
			oos.close();
			logger.info("Salvado catalogoConsultas ");
		} catch (Exception e) {
	
			e.printStackTrace();
		}
	
		fichero = ruta +FileSystems.getDefault().getSeparator() + "catalogoModelFilters.def";//
	
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(fichero)));
	
			oos.writeObject(this.catalogoModelFilters);
			oos.close();
			logger.info("Salvado catalogoModelFilters ");
		} catch (Exception e) {
	
			e.printStackTrace();
		}
	
	}

	// Serializa el objeto catalogoConsultas para conservar definiciones consultas
	

	public void reloadCatalogos() {
		/*URL url = Visualizador.class.getClassLoader().getResource("cta/resources/");
		String ruta = new File(url.getPath()).getAbsolutePath();
		try {
			ruta = URLDecoder.decode(ruta, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String ruta = catalogos;
		String fichero = ruta + FileSystems.getDefault().getSeparator() + "catalogoConsultas.def";
		logger.info("Ruta para leer Catalogo:" + fichero);
		////////////////////////////////////////////////////////////////////
		// En busca de los ficheros de serializacion del catalogo consultas/
		///////////////////////////////////////////////////////////////////
		try {
			// Se crea un ObjectInputStream
			FileInputStream fis = null;
			fis = new FileInputStream(new File(fichero));
			ObjectInputStream ois = new ObjectInputStream(fis);

			// Se lee el primer objeto
			ConcurrentHashMap<String, Consulta> readObjectConsultas = (ConcurrentHashMap<String, Consulta>) ois.readObject();
			this.catalogoConsultas = readObjectConsultas;

			ois.close();

			logger.info("cargados catalogos ...");
		} catch (Exception e1) {
			logger.error("Error cargando catalogo Consultas:(Se inicializa)" + e1.getMessage());// e1.printStackTrace();
			this.initVectorConsultas();

		}
		Consulta virtualConsulta = new Consulta();
		virtualConsulta.setSistemaConsulta((String) comboSistemas.getSelectedItem());
		
		this.refreshComboConsultas(virtualConsulta);// No nos situamos sobre ningun item en particular al init

		///////////////////////////////////////////////////////////////////////
		// En busca de los ficheros de serializacion del catalogo Model Filters/
		//////////////////////////////////////////////////////////////////////
		fichero = ruta + FileSystems.getDefault().getSeparator() + "catalogoModelFilters.def";
		logger.info("Ruta para leer CatalogoModelfilter:" + ruta);

		try {
			// Se crea un ObjectInputStream
			FileInputStream fis = null;
			fis = new FileInputStream(new File(fichero));
			ObjectInputStream ois = new ObjectInputStream(fis);

			// Se lee el primer objeto
			Object readObjectModelFilters = ois.readObject();
			this.catalogoModelFilters = (ConcurrentHashMap<String, ModelFilter>) readObjectModelFilters;

			ois.close();

			logger.info("cargados catalogos ModelFilters");
		} catch (Exception e1) {
			this.initVectorModelFilters();
			logger.error("Error cargando catalogo Modelfilters(Se inicializa):" + e1.getMessage());// e1.printStackTrace();

		}
		this.refreshComboModelFilters("");// No nos situamos sobre ningun item en particular al init

	}

	public int colorearSelected(JTextPane jTextPane, String sSelected, Color color) {
		String texto = jTextPane.getText();
	
		StyledDocument sd = textPane.getStyledDocument();

		int pos = texto.indexOf(sSelected, 0);
		int posIni = pos;
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();

		if (sSelected.length() == 0 | posIni < 0)
			return -1;
		StyleConstants.setForeground(attributeSet, color);
		while (!(pos == -1)) {// hay algo
			sd.setCharacterAttributes(pos, sSelected.length(), attributeSet, false);
			pos = texto.indexOf(sSelected, pos + sSelected.length());
		}

		return posIni;
	}
	/**
	 * Version sobrecargada de colorear Selected que permite parametrizar un array de sfilters
	 * para colorear mas de una cadena en cada linea
	 * @param jTextPane
	 * @param sSelected
	 * @param color
	 * @return
	 */
	public int colorearSelected(JTextPane jTextPane, String[] sSelected, Color color) {
		
		
		
		//Fase 1 :
		//Por cada sFilter registra todas sus posiciones de aparicion en un vector
	
		Map<String, Vector<Splited>> mapOfKeySFilters = new HashMap<String, Vector<Splited>>();
		SimpleAttributeSet attributeSet = new SimpleAttributeSet();
		
		StyledDocument sd = textPane.getStyledDocument();
		Algoritmos algoritmo = new Algoritmos();
		
		String lineas[] = jTextPane.getText().split( System.getProperty("line.separator") + "|\r");
		String texto="";
		//Obtenemos el texto del propio modelo del documento, ya que asi la busqueda interpreta correctamente
		//los saltos de linea que este utilizando y 
		//un array de lineas 
		//Para situarnos hacemos la busqueda sobre el comienzo de cada cadena que representa una linea en el modelo
		//y un analisis de linea en linea para la obtencion del offset de posicionamiento de sFilter buscado
		//con lo que cada linea supone el indice de arranque para la busqueda y el resultado de busqueda se suma como offset
		try {
			texto = sd.getText(0,sd.getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int posIni = -1; // Posicion de partida para el caret
		int posNewLine;
		
		for (int i= 0 ; i < sSelected.length ; i++) {
			//vector para contener los indices splited encontrados
			if(sSelected[i].equals(""))continue;
			Vector<Splited> vSplitesActuales = mapOfKeySFilters.get(sSelected[i]);
			//Si ya existe ese registro no hacemos nada	
			if(vSplitesActuales==null) {
				vSplitesActuales = new Vector<Splited>();
				String sFilter = sSelected[i];
				posNewLine = 0 ;
				for(String linea : lineas) {
				
					posNewLine = texto.indexOf(linea, posNewLine);
					Vector<Splited> vSplitedDeAlgoritmo = algoritmo.splitedMatch(linea, posNewLine, sFilter);
					
					if(vSplitedDeAlgoritmo!=null) {//Hay ocurrencias
						vSplitesActuales.addAll(vSplitedDeAlgoritmo);
					}
					
					mapOfKeySFilters.put(sSelected[i], vSplitesActuales);
					posNewLine = posNewLine + linea.length();
				}	

			}
		System.out.println("Indices de " + sSelected[i]+ ":" + mapOfKeySFilters.get(sSelected[i]));
		}
		
		//Fase 2:
		// Por cada SSelected recorremos todos sus indices en el modelo del panel ,pintandolos				
		StyleConstants.setForeground(attributeSet, color);
		for(int i=0 ; i < sSelected.length; i++) {
			if(sSelected[i].equals(""))continue;
			Iterator<String> itKeyIndexOfs = mapOfKeySFilters.keySet().iterator();//Por cada sFilter recorrer todo el model document
			if(itKeyIndexOfs.hasNext()) {
				//Si es el primer sFilter en proceso nos quedamos con el primer indexSplited para el caret
				String keySFilter =  itKeyIndexOfs.next();
				Vector<Splited> Spliteds = mapOfKeySFilters.get( keySFilter);
				if(i==0 & Spliteds.size()>0)posIni = Spliteds.elementAt(0).getIndexSplitedString();
			
				
				for(Splited splite : Spliteds) {
					sd.setCharacterAttributes(splite.getIndexSplitedString(), splite.getSplitedString().length(), attributeSet, false);
				}

				//Y seguimos con el resto de keys de busqueda
				itKeyIndexOfs.forEachRemaining(key-> {
					final Vector<Splited> SplitedsRestantes = mapOfKeySFilters.get(key);
					
					for(Splited splite : SplitedsRestantes) {
						sd.setCharacterAttributes(splite.getIndexSplitedString(), splite.getSplitedString().length(), attributeSet, false);
					}

				});
			}
		}
		return posIni;
	}

	private void newTarea() {
		String nameTarea = JOptionPane.showInputDialog(contentPane, "De un nombre a la nueva Tarea", null);

		if (nameTarea == null)
			return;

		Tarea tarea = new Tarea();
		int top = (int) spinner.getValue();
		
		tarea.setNameTarea(nameTarea);
		tarea.setNumMaquina(top);
		tarea.setConsultas(new Vector<ConsultaTarea>());

		catalogoTareas.put(nameTarea, tarea);
		
		refreshComboTareas(nameTarea);
		refreshListaTareas(nameTarea);
	
	//	tarea.

		//catalogo.put(nameConsultaFull, consulta);
		//enviarComando("ST ALL 0",);


		//refreshComboConsultas(consulta);
		// combo_Consultas.setSelectedIndex(combo_Consultas.getItemCount()-1);

		logger.info("El usuario ha añadido  la tarea :" + nameTarea);
		
	}

	private void refreshComboTareas(String nameTarea) {
		int index = 0;
		int indexComp = 0;
		combo_Tareas.removeAllItems();
		String item;
		for (Enumeration<String> enumKeysTareas = catalogoTareas.keys(); enumKeysTareas.hasMoreElements();) {
		
			String itemTarea = enumKeysTareas.nextElement();
			
			// Solo mostramos consultas filtradas para el sistema dado
		
			
					combo_Tareas.addItem(itemTarea);
					if (nameTarea==itemTarea)
						indexComp = index;
					index++;
		}
		if (combo_Tareas.getItemCount() > 0)
			combo_Tareas.setSelectedIndex(indexComp);
		
	}

	private void refreshListaTareas(String sTarea) {
		DefaultListModel<String> dlm = new DefaultListModel<String>();
		Tarea tarea = catalogoTareas.get(sTarea);
		int indexComp=0,index=0;
		listaTareas.removeAll();
		String item;
		for (ConsultaTarea consultaTarea: tarea.getConsultas()) {
		
			dlm.addElement(consultaTarea.nombreConsultaTareaFull());
		}
		listaTareas.setModel(dlm);
		if (dlm.size() > 0)	listaTareas.setSelectedIndex(indexComp);
		
	}
	
	private void incluirConsultaAListaTareas(String sTarea) {
		String sConsulta = (String) dialogAddConsulta();

		if (sConsulta == null | combo_Tareas.getSelectedItem()==null)return;
		
		JFileChooser file=new JFileChooser();
		   file.showOpenDialog(this);
		   /**abrimos el archivo seleccionado*/
		   File abre=file.getSelectedFile();
		   //catalogoTareas.get(sTarea).getConsultas().add(consultaTarea)

		/*
		Consulta consulta = catalogoConsultas.get(sConsulta);
		ConsultaTarea consultaTarea = new ConsultaTarea(consulta,numeroMaquina);
		consultaTarea.setNumeroMaquina(numeroMaquina);
		
	
		
		//Comprobamos si ya existe una consultaTarea con el mismo nombre
		int indexSelected=addItemAListaConsultasModoConsulta(sTarea, consultaTarea);
		if(indexSelected != -1) {
			catalogoTareas.get(sTarea).getConsultas().add(consultaTarea);
			logger.info("El usuario ha añadido la Consulta para el modo Tareas :" + consultaTarea.nombreConsultaTareaFull());
		}else {
			logger.info("Consulta ya existente, no se añade"); 
		}*/
		refreshListaTareas(sTarea);
		
	}
	
	private void incluirConsultaAListaTareas2(String sTarea) {
		String sConsulta = (String) dialogAddConsulta();

		if (sConsulta == null | combo_Tareas.getSelectedItem()==null)return;
		
		String numeroMaquina = JOptionPane.showInputDialog(contentPane, "Indique el numero de Maquina para la consulta", null);
		
		Consulta consulta = catalogoConsultas.get(sConsulta);
		ConsultaTarea consultaTarea = new ConsultaTarea(consulta,numeroMaquina);
		consultaTarea.setNumeroMaquina(numeroMaquina);
		
	
		
		//Comprobamos si ya existe una consultaTarea con el mismo nombre
		int indexSelected = addItemAListaConsultasModoConsulta(sTarea, consultaTarea);
		if(indexSelected != -1) {
			catalogoTareas.get(sTarea).getConsultas().add(consultaTarea);
			logger.info("El usuario ha añadido la Consulta para el modo Tareas :" + consultaTarea.nombreConsultaTareaFull());
		}else {
			logger.info("Consulta ya existente, no se añade"); 
		}
		refreshListaTareas(sTarea);
		
	}
	private void borrarConsultaTarea(String sTarea,String sConsultaTarea) {
		Tarea tarea = catalogoTareas.get(sTarea);
		ConsultaTarea cTarea = tarea.contieneEstaConsulta(sConsultaTarea);
		if(cTarea == null) {
			return;
		}else {//removemos la consulta del vector y actualizamos el catalogo
			tarea.removeConsultaTarea(sConsultaTarea);
			catalogoTareas.put(sTarea, tarea);
			logger.debug("Actualizado catalogo despues de remover consultaTarea:"+sConsultaTarea);
		}
		refreshListaTareas(sTarea);
	}

	public ConcurrentHashMap<String,Integer> getFlagDisconnectRegistry() {
		return this.flagDisconnectRegistry;
	}
}
