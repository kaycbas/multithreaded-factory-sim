package bastoul_CSCI201_Assignment5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class Factory extends JFrame {
	FactoryPanel factoryPanel;
	DefaultTableModel tableModel;
	JTable taskTable;
	Vector<Recipe> recipeVector;
	
	public Factory() {
		super("Factory");
		setSize(800, 600);
		setLocation(200, 50);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		instantiateGlobals();
		add(factoryPanel, BorderLayout.CENTER);
		addMenuBar();
		addTaskBoard();
		
		setVisible(true);	
	}
	
	private void instantiateGlobals() {
		factoryPanel = new FactoryPanel();
		String [] title = {"Task Board"};
		tableModel = new DefaultTableModel(title , 0);
		taskTable = new JTable(tableModel);
		recipeVector = new Vector<Recipe>();
	}
	
	private void addMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenuItem menuItem = new JMenuItem("Open File");
		menuBar.add(menuItem);
		setJMenuBar(menuBar);
		addMenuBarAction(menuItem);
	}
	
	private void addTaskBoard() {
		JPanel taskPanel = new JPanel();
		taskPanel.setLayout(new BorderLayout());
		taskTable.setEnabled(false);
		//taskTable.setBackground(Color.LIGHT_GRAY);
		JScrollPane jsp = new JScrollPane(taskTable);
		jsp.setPreferredSize(new Dimension(200, 600));
		taskPanel.add(taskTable.getTableHeader(), BorderLayout.NORTH);
		taskPanel.add(jsp, BorderLayout.CENTER);
		this.add(taskPanel, BorderLayout.EAST);
	}
	
	private void addMenuBarAction(JMenuItem menuItem) {
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    int returnVal = chooser.showOpenDialog(menuItem);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			    	String directoryPath = chooser.getSelectedFile().getAbsolutePath();
			    	File folder = new File(directoryPath);
			    	readRecipes(folder);
			    	readFactory(folder);
			    }
			}
		});
	}
	
	private void readFactory(File folder) {
		boolean found = false;
		int workerCount = 1;
		
		for (File fileEntry : folder.listFiles()) {
			if(found==false) {
				if(fileEntry.isFile()) {
					String filename = fileEntry.getName();
					String extension = filename.substring(filename.lastIndexOf("."), filename.length());
					if(extension.equals(".factory")) {
						try {
							BufferedReader br = new BufferedReader(new FileReader(fileEntry));
							String line;
							line = br.readLine();
							int ind = line.lastIndexOf(":")+1;
							String count = line.substring(ind, line.indexOf("]", ind));
							workerCount = Integer.parseInt(count);
							found = true;
							
						} catch (IOException e) {
							e.printStackTrace();
						}	
					}
				}
			}
		}
		
		Vector<Worker> workerVector = new Vector<Worker>();
		//workerVector.add(new Worker());
		//workerVector.add(new Worker());
		//GOOD CODE FOR MULT WORKER THREADS
		for (int i=0; i<workerCount; i++) {
			Worker w = new Worker();
			workerVector.add(w);
		}
		factoryPanel.storeWorkerVector(workerVector);
	}
	
	private void readRecipes(File folder) {
		for (File fileEntry : folder.listFiles()) {
			if(fileEntry.isFile()) {
				String filename = fileEntry.getName();
				String extension = filename.substring(filename.lastIndexOf("."), filename.length());
				if(extension.equals(".rcp")) {
					try {
						BufferedReader br = new BufferedReader(new FileReader(fileEntry));
						String line;
						line = br.readLine();
						String name = line.substring(1, line.lastIndexOf("]"));
						String count = line.substring(line.lastIndexOf("x")+1, line.lastIndexOf("x")+2);
						int recipeCount = Integer.parseInt(count);
						Recipe rcp = new Recipe(name);
						int wood=0;
						int metal=0;
						int plastic=0;				
						while ((line = br.readLine()) != null) {
							String id = line.substring(1, 4);
							if(!id.equals("Use")) {	//'line' is a material line
								String material = line.substring(1, line.lastIndexOf(":"));
								String materialCount = line.substring(line.lastIndexOf(":")+1, line.lastIndexOf(":")+2);
								if(material.equals("Wood")) {
									rcp.setWood(Integer.parseInt(materialCount));
								} else if(material.equals("Metal")) {
									rcp.setMetal(Integer.parseInt(materialCount));
								} else if(material.equals("Plastic")) {
									rcp.setPlastic(Integer.parseInt(materialCount));
								}
							}
							else { //'line' is a task line
								Task task = new Task();
								
								if(!Character.isLetter(line.charAt(5))) { //tool(s) specified in task								
									boolean allToolsRead = false;
									int startInd = 0;
									int ind;									
									while(!allToolsRead) {
										ind = line.indexOf("x", startInd);
										if (ind==-1) {
											allToolsRead=true;
										} else {	
											int toolCnt = Character.getNumericValue(line.charAt(ind-1));
											ind+=2;
											String tool = line.substring(ind, line.indexOf(" ", ind));
											//System.out.println("x" + toolCnt + " " + tool);
											if (tool.equals("Screwdriver") || tool.equals("Screwdrivers")) {
												task.setNumSD(toolCnt);
											} else if (tool.equals("Hammer") || tool.equals("Hammers")) {									
												task.setNumHammers(toolCnt);
											} else if (tool.equals("Paintbrush") || tool.equals("Paintbrushes")) {
												task.setNumPB(toolCnt);
											} else if (tool.equals("Plier") || tool.equals("Pliers")) {
												task.setNumPliers(toolCnt);
											} else if (tool.equals("Scissor") || tool.equals("Scissors")) {
												task.setNumScissors(toolCnt);
											} 
											
											startInd = ind;
										}
									
									}
									ind = line.indexOf("at", startInd);
									ind+=3;
									String workspace = line.substring(ind, line.indexOf(" ", ind));
									ind = line.indexOf("for", ind);
									ind+=4;
									String worktimeStr = line.substring(ind, line.indexOf("s", ind));		
									int workTime = Integer.parseInt(worktimeStr);
									//System.out.println(workspace + " for " + workTime + "s");
									task.setWorkspace(workspace, workTime);
								}
								else { //tools not specified, just location/duration
									String workspace = line.substring(5, line.indexOf(" ", 5));
									int ind = line.indexOf("for", 0);
									ind+=4;
									String worktimeStr = line.substring(ind, line.indexOf("s", ind));									
									int workTime = Integer.parseInt(worktimeStr);
									task.setWorkspace(workspace, workTime);
								}
								rcp.addTask(task);
							}
							
						}
						
						recipeVector.add(rcp);
						for (int i=1; i<recipeCount; i++) {
							Recipe copy = new Recipe(rcp);
							recipeVector.add(copy);		
						}
						factoryPanel.storeRecipeVector(recipeVector);
					} catch (IOException e) {
						e.printStackTrace();
					}		
				} 
			}		
		}
		
		/*
		for(Recipe rcp : recipeVector) {
			System.out.println(rcp.taskList.size());
			for(Task task : rcp.taskList) {
				System.out.println(task.workspace + " for " + task.duration + "s");
				System.out.println("With: " + task.screwdriver + "sds");
				System.out.println(task.hammer + "hammers");
				System.out.println(task.paintbrush + "pbs");
				System.out.println(task.pliers + "pliers");
				System.out.println(task.scissors + "scissors");
			}
		}
		*/
		
		initializeTaskBoard();
	}
	
	private void initializeTaskBoard() {
		tableModel.setNumRows(recipeVector.size());
		for (int i=0; i<recipeVector.size(); i++) {
			String object = recipeVector.get(i).getName();
			String status = recipeVector.get(i).getStatus();
			String line = object + " ... " + status;
			taskTable.setValueAt(line, i, 0);
		}
		factoryPanel.storeJTable(taskTable);
		//new Timer(10, this.taskPerformer).start();
	}
	
	/*public void updateTaskBoard() {
		for (int i=0; i<recipeVector.size(); i++) {
			String object = recipeVector.get(i).getName();
			String status = recipeVector.get(i).getStatus();
			String line = object + " ... " + status;
			taskTable.setValueAt(line, i, 0);
		}
	}*/
	
	ActionListener taskPerformer = new ActionListener() {
		public void actionPerformed(ActionEvent ae) {
			factoryPanel.repaint();
		}
	};
	
	public static void main(String[] args) {
		Factory factWin = new Factory();
		new Timer(10, factWin.taskPerformer).start();
	}
}
