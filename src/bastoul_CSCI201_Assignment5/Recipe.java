package bastoul_CSCI201_Assignment5;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class Recipe {
	String name;
	String status;
	int wood;
	int metal;
	int plastic;
	LinkedList<Task> taskList = new LinkedList<Task>();
	
	public Recipe(String name) {
		this.name = name;
		status = "Not Built";
	}	
	public Recipe(Recipe another) {
		this.name = another.name;
		this.status = another.status;
		this.wood = another.wood;
		this.metal = another.metal;
		this.plastic = another.plastic;
		this.taskList = another.taskList;
	}
	
	public String getName() {
		return name;
	}	
	public String getStatus() {
		return status;
	}
	public void setStatus(String s) {
		status = s;
	}
	public void addTask(Task task) {
		taskList.add(task);
	}
	public void setWood(int woodCount) {
		wood = woodCount;
	}
	public void setMetal(int metalCount) {
		metal = metalCount;
	}
	public void setPlastic(int plasticCount) {
		plastic = plasticCount;
	}	
}

class Task {
	String workspace;
	int duration;
	int screwdriver;
	int hammer;
	int paintbrush;
	int pliers;
	int scissors;
	
	public Task() {
		
	}
	
	public Task(String workspace, int duration) {
		this.workspace = workspace;
		this.duration = duration;
		screwdriver = 0;
		hammer = 0;
		paintbrush = 0;
		pliers = 0;
		scissors = 0;
	}
	public void setWorkspace(String ws, int t) {
		workspace = ws;
		duration = t;
	}	
	public void setNumSD(int num) {
		screwdriver = num;
	}
	public void setNumHammers(int num) {
		hammer = num;
	}
	public void setNumPB(int num) {
		paintbrush = num;
	}
	public void setNumPliers(int num) {
		pliers = num;
	}
	public void setNumScissors(int num) {
		scissors = num;
	}
	public int getNumSD() {
		return screwdriver;
	}
	public int getNumHammers() {
		return hammer;
	}
	public int getNumPB() {
		return paintbrush;
	}
	public int getNumPliers() {
		return pliers;
	}
	public int getNumScissors() {
		return scissors;
	}
}







