package bastoul_CSCI201_Assignment5;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTable;

public class FactoryPanel extends JPanel {
	MaterialBin woodBin = new MaterialBin(999);
	MaterialBin metalBin = new MaterialBin(999);
	MaterialBin plasticBin = new MaterialBin(999);
	
	//Bins not necessarily initialized to 5
	ToolBin screwdriverBin = new ToolBin(5);
	ToolBin hammerBin = new ToolBin(5);
	ToolBin paintbrushBin = new ToolBin(5);
	ToolBin pliersBin = new ToolBin(5);
	ToolBin scissorsBin = new ToolBin(5);
	
	Workspace anvils = new Workspace(2);
	Workspace benches = new Workspace(3);
	Workspace furnaces = new Workspace(2);
	Workspace saws = new Workspace(3);
	Workspace pStations = new Workspace(4);
	Workspace press = new Workspace(1);
	
	
	Vector<Worker> workerVector;
	Vector<Recipe> recipeVector;
	boolean workersStored=false;
	JTable taskTable;
	Lock lock = new ReentrantLock();

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawWoodBin(g);
		drawMetalBin(g);
		drawPlasticBin(g);
		drawScrewdriverBin(g);
		drawHammerBin(g);
		drawPaintbrushBin(g);
		drawPliersBin(g);
		drawScissorsBin(g);
		drawAnvils(g);
		drawWorkbenches(g);
		drawFurnaces(g);
		drawTableSaws(g);
		drawPaintingStations(g);
		drawPress(g);
		if (workersStored) {
			for (Worker w : workerVector) {
				w.drawWorker(g);
			}
		}
		//worker.darwWorker(g, this);
	}
	
	public void storeWorkerVector(Vector<Worker> wV) {
		workerVector = wV;
		for (Worker w : workerVector) {
			w.storeFactoryPanel(this);
			//System.out.println("check");
			w.start();
		}
		workersStored=true;
	}
	
	public void storeRecipeVector(Vector<Recipe> rV) {
		recipeVector = rV;
	}

	public void storeJTable (JTable jt) {
		taskTable = jt;
	}
	
	public void updateTaskTable() {
		for (int i=0; i<recipeVector.size(); i++) {
			String object = recipeVector.get(i).getName();
			String status = recipeVector.get(i).getStatus();
			String line = object + " ... " + status;
			taskTable.setValueAt(line, i, 0);
		}
	}
	
	public Recipe getNextRecipe() {
		lock.lock();
		for (int i=0; i<recipeVector.size(); i++) {
			Recipe rcp = recipeVector.get(i);
			if (rcp.getStatus().equals("Not Built")) {
				rcp.setStatus("In Progress");
				this.updateTaskTable();
				lock.unlock();
				return rcp;
			}
		}
		//program reaches here -> all recipes completed
		lock.unlock();
		return null;	
	}
	
	public void finishRecipe(Recipe r) {
		int ind = recipeVector.indexOf(r);
		Recipe rcp = recipeVector.get(ind);
		rcp.setStatus("Built");
		this.updateTaskTable();
	}
	
	private void drawWoodBin(Graphics g) {
		Image wbImg = new ImageIcon("Images/wood.png").getImage();
		g.drawImage(wbImg, 150, 33, this);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Serif", Font.BOLD, 14));
		g.drawString("WOOD", 152, 28);
		g.setFont(new Font("Serif", Font.BOLD, 16));
		g.drawString(woodBin.available(), 164, 63);
	}
	
	private void drawMetalBin(Graphics g) {
		Image mImg = new ImageIcon("Images/metal.png").getImage();
		g.drawImage(mImg, 310, 33, this);
		g.setFont(new Font("Serif", Font.BOLD, 14));
		g.drawString("METAL", 312, 28);		
		g.setFont(new Font("Serif", Font.BOLD, 16));
		g.drawString(metalBin.available(), 324, 63);
	}
		
	private void drawPlasticBin(Graphics g) {
		Image pImg = new ImageIcon("Images/plastic.png").getImage();
		g.drawImage(pImg, 470, 33, this);
		g.setFont(new Font("Serif", Font.BOLD, 14));
		g.drawString("PLASTIC", 466, 28);
		g.setFont(new Font("Serif", Font.BOLD, 16));
		g.drawString(plasticBin.available(), 484, 63);				
	}
	
	
	private void drawScrewdriverBin(Graphics g) {
		Image sdImg = new ImageIcon("Images/screwdriver.png").getImage();
		g.drawImage(sdImg, 25, 150, this);
		g.setFont(new Font("Serif", Font.BOLD, 12));
		g.drawString("SCREWDRIVER", 7, 145);
		g.setFont(new Font("Serif", Font.BOLD, 16));
		g.drawString(screwdriverBin.available() + "/" + screwdriverBin.total(), 40, 180);	
	}
	
	private void drawHammerBin(Graphics g) {
		Image hImg = new ImageIcon("Images/hammer.png").getImage();
		g.drawImage(hImg, 25, 225, this);
		g.setFont(new Font("Serif", Font.BOLD, 12));
		g.drawString("HAMMER", 23, 220);
		g.setFont(new Font("Serif", Font.BOLD, 16));
		g.drawString(hammerBin.available() + "/" + hammerBin.total(), 40, 255);
	}
	
	private void drawPaintbrushBin(Graphics g) {
		Image pbImg = new ImageIcon("Images/paintbrush.png").getImage();
		g.drawImage(pbImg, 25, 300, this);
		g.setFont(new Font("Serif", Font.BOLD, 12));
		g.drawString("PAINTBRUSH", 12, 295);
		g.setFont(new Font("Serif", Font.BOLD, 16));
		g.drawString(paintbrushBin.available() + "/" + paintbrushBin.total(), 40, 330);
	}
	
	private void drawPliersBin(Graphics g) {
		Image pImg = new ImageIcon("Images/pliers.png").getImage();
		g.drawImage(pImg, 25, 375, this);
		g.setFont(new Font("Serif", Font.BOLD, 12));
		g.drawString("PLIERS", 30, 370);
		g.setFont(new Font("Serif", Font.BOLD, 16));
		g.drawString(pliersBin.available() + "/" + pliersBin.total(), 40, 405);
	}
	
	private void drawScissorsBin(Graphics g) {
		Image sImg = new ImageIcon("Images/scissors.png").getImage();
		g.drawImage(sImg, 25, 450, this);
		g.setFont(new Font("Serif", Font.BOLD, 12));
		g.drawString("SCISSORS", 19, 445);
		g.setFont(new Font("Serif", Font.BOLD, 16));
		g.drawString(scissorsBin.available() + "/" + scissorsBin.total(), 40, 480);
	}
	
	public void drawAnvils(Graphics g) {
		Image img = new ImageIcon("Images/anvil.png").getImage();
		g.drawImage(img, 150, 150, this);
		g.drawImage(img, 230, 150, this);
		g.setFont(new Font("Serif", Font.BOLD, 14));
		g.drawString("ANVILS", 190, 220);
		for (int i=0; i<2; i++) {
			g.drawString(anvils.spaceStatus[i], 155+(i*80), 145);
		}
	}
	
	public void drawWorkbenches(Graphics g) {
		Image img = new ImageIcon("Images/workbench.png").getImage();
		g.drawImage(img, 310, 150, this);
		g.drawImage(img, 390, 150, this);
		g.drawImage(img, 470, 150, this);
		g.drawString("WORKBENCHES", 362, 220);
		for (int i=0; i<3; i++) {
			g.drawString(benches.spaceStatus[i], 315+(i*80), 145);
		}
	}
	
	public void drawFurnaces(Graphics g) {
		Image img = new ImageIcon("Images/furnace.png").getImage();
		g.drawImage(img, 150, 300, this);
		g.drawImage(img, 230, 300, this);
		g.drawString("FURNACE", 185, 370);
		for (int i=0; i<2; i++) {
			g.drawString(furnaces.spaceStatus[i], 155+(i*80), 295);
		}
	}
	
	public void drawTableSaws(Graphics g) {
		Image img = new ImageIcon("Images/tablesaw.png").getImage();
		g.drawImage(img, 310, 300, this);
		g.drawImage(img, 390, 300, this);
		g.drawImage(img, 470, 300, this);
		g.drawString("TABLE SAWS", 372, 370);
		for (int i=0; i<3; i++) {
			g.drawString(saws.spaceStatus[i], 315+(i*80), 295);
		}
	}
	
	public void drawPaintingStations(Graphics g) {
		Image img = new ImageIcon("Images/paintingstation.png").getImage();
		g.drawImage(img, 150, 450, this);
		g.drawImage(img, 230, 450, this);
		g.drawImage(img, 310, 450, this);
		g.drawImage(img, 390, 450, this);
		g.drawString("PAINTING STATIONS", 227, 520);
		for (int i=0; i<4; i++) {
			g.drawString(pStations.spaceStatus[i], 155+(i*80), 445);
		}
	}
	
	public void drawPress(Graphics g) {
		Image img = new ImageIcon("Images/press.png").getImage();
		g.drawImage(img, 470, 450, this);
		g.drawString("PRESS", 475, 520);
		g.drawString(pStations.spaceStatus[0], 475, 445);
	}
	

}


