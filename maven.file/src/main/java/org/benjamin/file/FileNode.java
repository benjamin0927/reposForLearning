package org.benjamin.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileNode {
	private FileNode parent;
	protected List<FileNode> children = new ArrayList<FileNode>();
	protected File file;
	private String name;
	protected CompareStatus compareStatus;
	private int depth;

	public FileNode(FileNode parent) {
		super();
		this.parent = parent;
		
		// Set the depth
		if(parent == null) {
			this.depth = 0;
		} else {
			this.depth = parent.depth + 1;
		}
		
	}
	
	public FileNode(File file, FileNode parent) {
		this(parent);
		this.file = file;
		this.compareStatus = CompareStatus.InNone;
	}
	

	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public FileNode getParent() {
		return parent;
	}

	public List<FileNode> getChildren() {
		return children;
	}

	public void setChildren(List<FileNode> children) {
		this.children = children;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public CompareStatus getCompareStatus() {
		return compareStatus;
	}
	
	public void setCompareStatus(CompareStatus compareStatus) {
		this.compareStatus = compareStatus;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		if(this.file != null) {
			this.name = file.getName();
		}
		return this.name;
	}

	public boolean isLeaf() {
		return !this.file.isDirectory();
	}

	public void loadFileNodeChildren(){
		children = new ArrayList<FileNode>();
		if(!this.isLeaf()) { 
			File[] files = file.listFiles();
			for(File file : files){
				FileNode childFileNode = new FileNode(file, this);
				System.out.println(childFileNode.getName() + " - " + childFileNode.getDepth() + " - " + childFileNode.getCompareStatus());
				children.add(childFileNode);
				childFileNode.loadFileNodeChildren();
			}
		}
	}
	
	public FileNode comparingFileNodes(FileNode comparedNode, CompareStatus compareStatusFrom, FileNode fileNodeFrom, FileNode fileNodeTo) {
		
		for (FileNode nodeFrom : fileNodeFrom.getChildren()) {
			boolean inBooth = false;
			String nodeFileFromName = nodeFrom.getName();
			System.out.println("Node From Name - " + nodeFileFromName);
			
			// If the node not existed in the comparedNode, then initial the Node child
			FileNode node = comparedNode.hasChildByName(nodeFileFromName);
			if(node != null) {
				// If the node is existed and it's not a leaf, compare the children
				this.comparingFileNodes(node, compareStatusFrom, nodeFrom, fileNodeTo.hasChildByName(nodeFileFromName));
				continue;
			} else {
				node = new FileNode(comparedNode);
				node.setName(nodeFileFromName);
			}
			
			for (FileNode nodeTo : fileNodeTo.getChildren()) {
				if (nodeTo.getName().equals(nodeFileFromName)) {
					inBooth = true;
					
					// If the Node One is not a leaf, then compare their children
					if(!nodeFrom.isLeaf()) {
						this.comparingFileNodes(node, compareStatusFrom, nodeFrom, nodeTo);
					}
					break;
				}
			}
			
			// Set Compared Status in From File Node
			if (!inBooth) {
				nodeFrom.setCompareStatus(compareStatusFrom);
				node.setCompareStatus(compareStatusFrom);
			} else {
				nodeFrom.setCompareStatus(CompareStatus.InBoth);
				node.setCompareStatus(CompareStatus.InBoth);
			}
			
			
			comparedNode.addChildNode(node);
		}		
		return comparedNode;
	}
	

	private void addChildNode(FileNode node) {
		this.children.add(node);
		
	}

	public FileNode hasChildByName(String name) {
		FileNode fileNode = null;
		
		// If doesn't have child, then return false;
		if(this.getChildren() == null || this.getChildren().isEmpty()) {
			return null;
		}
		
		for(FileNode child : this.getChildren()){
			if(child.getName().equals(name)){
				fileNode = child;
				break;
			}
		}
		
		return fileNode;
	}

	public FileNode commpareFileNodes(File fileFrom, File fileTo) {
		FileNode comparedNode = new FileNode(null);
		
		FileNode fileNodeFrom = new FileNode(fileFrom, null);
		fileNodeFrom.loadFileNodeChildren();
		FileNode fileNodeTo = new FileNode(fileTo, null);
		fileNodeTo.loadFileNodeChildren();
		
		comparedNode = this.comparingFileNodes(comparedNode, CompareStatus.InFrom, fileNodeFrom, fileNodeTo);
		comparedNode = this.comparingFileNodes(comparedNode, CompareStatus.InTo, fileNodeTo, fileNodeFrom);
	
		System.out.println("------------");
		return comparedNode;
	}
	
	public static void main(String[] args) {
		
		File fileFrom = new File("/mnt/tmp/From");
		File fileTo = new File("/mnt/tmp/To");
		
		FileNode fileNode = new FileNode(null);
		fileNode = fileNode.commpareFileNodes(fileFrom, fileTo);
		
		System.out.println("------");
	}
}
