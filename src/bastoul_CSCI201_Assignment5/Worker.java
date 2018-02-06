package bastoul_CSCI201_Assignment5;

import java.awt.Graphics;
import java.awt.Image;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Worker extends Thread {
	//Task board edge
	private final int X_TASKBOARD = 550;
	//Material coordinates
	private final int Y_MATERIALS = 90;
	private final int X_WOOD = 150;
	private final int X_METAL = 310;
	private final int X_PLASTIC = 470;
	//Tool coordinates
	private final int X_TOOLS = 80;
	private final int Y_SCREWDRIVER = 150;
	private final int Y_HAMMER = 225;
	private final int Y_PAINTBRUSH = 300;
	private final int Y_PLIERS = 375;
	private final int Y_SCISSORS = 450;
	//Workspace coordinates
	private final int Y_WS_ROW_1 = 225;
	private final int Y_WS_ROW_2 = 375;
	private final int X_WS_1 = 150;
	private final int X_WS_2 = 230;
	private final int X_WS_3 = 310;
	private final int X_WS_4 = 390;
	private final int X_WS_5 = 470;	
	
	int sd=0;
	int ham=0;
	int pb=0;
	int pl=0;
	int sc=0;
	
	private int x1, y1;
	String destination;
	boolean arrived;
	Image workerImg = new ImageIcon("Images/worker.png").getImage();
	private Recipe rcp;
	FactoryPanel factoryPanel;
	Graphics g;
	Lock lock = new ReentrantLock();
	
	
	public Worker() {
		x1 = 25;
		y1 = 90;
		destination = "taskboard";
		arrived = false;
	}

	public void drawWorker(Graphics g) {
		g.drawImage(workerImg, x1, y1, factoryPanel);		
	}
	
	public void storeFactoryPanel(FactoryPanel fp) {
		factoryPanel = fp;
		g = fp.getGraphics();
	}
		
	public void run() {
		boolean done = false;
		boolean arrived = false;
		while (!done) {
			try {
				if (destination.equals("taskboard")) {
					if (x1<X_TASKBOARD) {
						x1+=2;
					}
					else {
						rcp = factoryPanel.getNextRecipe();
						if (rcp==null) {
							done = true;
						}
						else {
							destination = "acquire materials";
						}
					}
				}
				else if (destination.equals("acquire materials")) {
					if (rcp.plastic>0 && x1>X_PLASTIC) {
						x1-=2;
						if (x1<=X_PLASTIC) {
							for (int i=0; i<rcp.plastic; i++) {
								factoryPanel.plasticBin.takeMaterial();
							}
							rcp.plastic=0;
							sleep(250);
						}
					} else if (rcp.metal>0 && x1>X_METAL) {
						x1-=2;
						if (x1<=X_METAL) {
							for (int i=0; i<rcp.metal; i++) {
								factoryPanel.metalBin.takeMaterial();
							}
							rcp.metal=0;
							sleep(250);
						}
					}else if (rcp.wood>0 && x1>X_WOOD) {
						x1-=2;
						if (x1<=X_WOOD) {
							for (int i=0; i<rcp.wood; i++) {
								factoryPanel.woodBin.takeMaterial();
							}
							rcp.wood=0;
							sleep(250);
						}
					} else if (x1>X_TOOLS){
						x1-=2;
					} else {
						destination = "acquire tools";
					}
				}
				else if (destination.equals("acquire tools")) {
					if(rcp.taskList.size()!=0) {
						Task task = rcp.taskList.getFirst();
						if (task.screwdriver>0 && y1<Y_SCREWDRIVER) {
							y1+=2;
							if (y1>=Y_SCREWDRIVER) {
								for (int i=0; i<task.screwdriver; i++) {
									factoryPanel.screwdriverBin.takeTool();
									sd++;
								}
								task.screwdriver=0;
								sleep(250);
							}
						} else if (task.hammer>0 && y1<Y_HAMMER) {
							y1+=2;
							if (y1>=Y_HAMMER) {
								for (int i=0; i<task.hammer; i++) {
									factoryPanel.hammerBin.takeTool();
									ham++;
								}
								task.hammer=0;
								sleep(250);
							}
						} else if (task.paintbrush>0 && y1<Y_PAINTBRUSH) {
							y1+=2;
							if (y1>=Y_PAINTBRUSH) {
								for (int i=0; i<task.paintbrush; i++) {
									factoryPanel.paintbrushBin.takeTool();
									pb++;
								}
								task.paintbrush=0;
								sleep(250);
							}
						} else if (task.pliers>0 && y1<Y_PLIERS) {
							y1+=2;
							if (y1>=Y_PLIERS) {
								for (int i=0; i<task.pliers; i++) {
									factoryPanel.pliersBin.takeTool();
									pl++;
								}
								task.pliers=0;
								sleep(250);
							}
						} else if (task.scissors>0 && y1<Y_SCISSORS) {
							y1+=2;
							if (y1>=Y_SCISSORS) {
								for (int i=0; i<task.scissors; i++) {
									factoryPanel.scissorsBin.takeTool();
									sc++;
								}
								task.scissors=0;
								sleep(250);
							}
						} else if (y1<Y_SCISSORS) {
							y1+=2;
						} else {
							destination = "workspace";
						}
					}
					else {
						if (y1>Y_MATERIALS) {
							y1-=2;
						} else {
							factoryPanel.finishRecipe(rcp);
							destination = "taskboard";
						}
					}
				}
				else if (destination.equals("workspace")) {
					if (rcp.taskList.size()!=0) {
						Task task = rcp.taskList.getFirst();
						if (task.workspace.equals("Anvil")) {
							moveToAnvil(task);
						} else if (task.workspace.equals("Workbench")) {
							moveToAnvil(task);						
						} else if (task.workspace.equals("Furnace")) {
							moveToFurnace(task);
						} else if (task.workspace.equals("Table Saws") || task.workspace.equals("Table Saw")) {
							moveToSaw(task);
						} else if (task.workspace.equals("Painting Station") || task.workspace.equals("Paintingstation")) {
							moveToPStation(task);
						} else if (task.workspace.equals("Press")) {
							moveToPress(task);
						}
					}
					else {
						if (y1>Y_MATERIALS) {
							y1-=2;
						} else {
							factoryPanel.finishRecipe(rcp);
							destination = "taskboard";
						}
					}
				}
				else if (destination.equals("Tools Top")) {
					if(x1>X_TOOLS) {
						x1-=2;
					} else if (y1<Y_SCISSORS) {
						y1+=2;
					} else {
						destination = "Return Tools";
					}
 				}
				else if (destination.equals("Return Tools")) {
					Task task = rcp.taskList.getFirst();
					if (sc>0) {				
						for (int i=0; i<sc; i++) {
							factoryPanel.scissorsBin.returnTool();
						}
						sc=0;
						sleep(250);
					} else if (pl>0 && y1>Y_PLIERS) {
						y1-=2;
						if (y1<=Y_PLIERS) {
							for (int i=0; i<pl; i++) {
								factoryPanel.pliersBin.returnTool();
							}
							pl=0;
							sleep(250);
						}
					} else if (pb>0 && y1>Y_PAINTBRUSH) {
						y1-=2;
						if (y1<=Y_PAINTBRUSH) {
							for (int i=0; i<pb; i++) {
								factoryPanel.paintbrushBin.returnTool();
							}
							pb=0;
							sleep(250);
						}
					} else if (ham>0 && y1>Y_HAMMER) {
						y1-=2;
						if (y1<=Y_HAMMER) {
							for (int i=0; i<ham; i++) {
								factoryPanel.hammerBin.returnTool();
							}
							ham=0;
							sleep(250);
						}
					} else if (sd>0 && y1>Y_SCREWDRIVER) {
						y1-=2;
						if (y1<=Y_SCREWDRIVER) {
							for (int i=0; i<sd; i++) {
								factoryPanel.screwdriverBin.returnTool();
							}
							sd=0;
							sleep(250);
						}
					} else if (y1>Y_SCREWDRIVER) {
						y1-=2;
					} else {
						rcp.taskList.remove();
						destination = "acquire tools";
					}
					
					
				}
				
				
				//factoryPanel.repaint();
				sleep(10);
			} catch (InterruptedException e) {
				System.out.println("Thread interrupted");
			}
		}
	} 
	
	private void moveToAnvil(Task task) {
		if(!arrived) {
			if (y1>=Y_WS_ROW_1) {
				y1-=2;
			} else if (factoryPanel.anvils.openSpaces==2 && x1<X_WS_1) {
				x1+=2;
				if (x1>=X_WS_1) {
					arrived = true;
				}
			} else if (x1<X_WS_2) {
				x1+=2;
				if (x1>=X_WS_2) {
					arrived = true;
				}
			}						
		} else if (arrived) {
			try {
				int t = task.duration;
				factoryPanel.anvils.occupy(t);
				y1-=75;
				sleep(t*1000);
				factoryPanel.anvils.unoccupy();
				y1+=75;
				arrived = false;
				destination = "Tools Top";
			} catch (InterruptedException e) {
				System.out.println("IOException");
			}
		}
	}
	
	private void moveToBench(Task task) {
		if(!arrived) {
			if (y1>=Y_WS_ROW_1) {
				y1-=2;
			} else if (factoryPanel.benches.openSpaces==3 && x1<X_WS_3) {
				x1+=2;
				if (x1>=X_WS_3) {
					arrived = true;
				}
			} else if (factoryPanel.benches.openSpaces==2 && x1<X_WS_4) {
				x1+=2;
				if (x1>=X_WS_4) {
					arrived = true;
				}
			} else if (x1<X_WS_5) {
				x1+=2;
				if (x1>=X_WS_5) {
					arrived = true;
				}
			}						
		} else if (arrived) {
			try {
				int t = task.duration;
				factoryPanel.benches.occupy(t);
				y1-=75;
				sleep(t*1000);
				factoryPanel.benches.unoccupy();
				y1+=75;
				arrived = false;
				destination = "Tools Top";
			} catch (InterruptedException e) {
				System.out.println("IOException");
			}
		}
	}
	
	private void moveToFurnace(Task task) {
		if(!arrived) {
			if (y1>=Y_WS_ROW_2) {
				y1-=2;
			} else if (factoryPanel.furnaces.openSpaces==2 && x1<X_WS_1) {
				x1+=2;
				if (x1>=X_WS_1) {
					arrived = true;
				}
			} else if (x1<X_WS_2) {
				x1+=2;
				if (x1>=X_WS_2) {
					arrived = true;
				}
			}						
		} else if (arrived) {
			try {
				int t = task.duration;
				factoryPanel.furnaces.occupy(t);
				y1-=75;
				sleep(t*1000);
				factoryPanel.furnaces.unoccupy();
				y1+=75;
				arrived = false;
				destination = "Tools Top";
			} catch (InterruptedException e) {
				System.out.println("IOException");
			}
		}
	}
	
	private void moveToSaw(Task task) {
		if(!arrived) {
			if (y1>=Y_WS_ROW_2) {
				y1-=2;
			} else if (factoryPanel.saws.openSpaces==3 && x1<X_WS_3) {
				x1+=2;
				if (x1>=X_WS_3) {
					arrived = true;
				}
			} else if (factoryPanel.saws.openSpaces==2 && x1<X_WS_4) {
				x1+=2;
				if (x1>=X_WS_4) {
					arrived = true;
				}
			} else if (x1<X_WS_5) {
				x1+=2;
				if (x1>=X_WS_5) {
					arrived = true;
				}
			}						
		} else if (arrived) {
			try {
				int t = task.duration;
				factoryPanel.saws.occupy(t);
				y1-=75;
				sleep(t*1000);
				factoryPanel.saws.unoccupy();
				y1+=75;
				arrived = false;
				destination = "Tools Top";			
			} catch (InterruptedException e) {
				System.out.println("IOException");
			}
		}
	}
	
	private void moveToPStation(Task task) {
		if(!arrived) {
			if (y1>=Y_WS_ROW_2) {
				y1-=2;
			} else if (factoryPanel.pStations.openSpaces==4 && x1<X_WS_1) {
				x1+=2;
				if (x1>=X_WS_1) {
					arrived = true;
				}
			} else if (factoryPanel.pStations.openSpaces==3 && x1<X_WS_2) {
				x1+=2;
				if (x1>=X_WS_2) {
					arrived = true;
				}
			} else if (factoryPanel.pStations.openSpaces==2 && x1<X_WS_3) {
				x1+=2;
				if (x1>=X_WS_3) {
					arrived = true;
				}
			} else if (x1<X_WS_4) {
				x1+=2;
				if (x1>=X_WS_4) {
					arrived = true;
				}
			}						
		} else if (arrived) {
			try {
				int t = task.duration;
				factoryPanel.pStations.occupy(t);
				y1+=75;
				sleep(t*1000);
				factoryPanel.pStations.unoccupy();
				y1-=75;
				arrived = false;
				destination = "Tools Top";			
			} catch (InterruptedException e) {
				System.out.println("IOException");
			}
		}
	}
	
	private void moveToPress(Task task) {
		if(!arrived) {
			if (y1>=Y_WS_ROW_2) {
				y1-=2;
			} else if (x1<X_WS_5) {
				x1+=2;
				if (x1>=X_WS_5) {
					arrived = true;
				}
			} 						
		} else if (arrived) {
			try {
				int t = task.duration;
				factoryPanel.press.occupy(t);
				y1+=75;
				sleep(t*1000);
				factoryPanel.press.unoccupy();
				y1-=75;
				arrived = false;
				destination = "Tools Top";			
			} catch (InterruptedException e) {
				System.out.println("IOException");
			}
		}
	}
}
