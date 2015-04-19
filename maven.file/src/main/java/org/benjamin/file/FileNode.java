package org.benjamin.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileNode {
	private FileNode parent;
	protected List<FileNode> children = new ArrayList<FileNode>();
	protected File file;
	protected BigInteger size;
	private String name;
	protected CompareStatus compareStatus;
	protected SizeCompareStatus compareSizeStatus;
	private int depth;
	
	protected FileWriter writer;

	public FileNode(FileNode parent) {
		super();
		this.parent = parent;
		
		// Set the depth
		if(parent == null) {
			this.depth = 0;
		} else {
			this.depth = parent.depth + 1;
		}
		
		 try {
			writer = this.getFileWriter("E:/ShareVM/5.5/git2svn.log");
		} catch (IOException e) {
		}
	}
	
	public FileNode(File file, FileNode parent) {
		this(parent);
		this.file = file;
		if(file.isDirectory()) {
			this.size = FileUtils.sizeOfDirectoryAsBigInteger(file);
		} else {
			this.size = BigInteger.valueOf(FileUtils.sizeOf(file));
		}
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
	
	public BigInteger getSize() {
		return size;
	}

	public void setSize(BigInteger size) {
		this.size = size;
	}

	public SizeCompareStatus getCompareSizeStatus() {
		return compareSizeStatus;
	}

	public void setCompareSizeStatus(SizeCompareStatus compareSizeStatus) {
		this.compareSizeStatus = compareSizeStatus;
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
		this.loadFileNodeChildren(Integer.MAX_VALUE);
		
/*		children = new ArrayList<FileNode>();
		if(!this.isLeaf()) { 
			File[] files = file.listFiles();
			for(File file : files){
				FileNode childFileNode = new FileNode(file, this);
				System.out.println(childFileNode.getName() + " - " + childFileNode.getDepth() + " - " + childFileNode.getCompareStatus());
				children.add(childFileNode);
				childFileNode.loadFileNodeChildren();
			}
		}*/
	}
	
	public void printFileNodeChildren(){

		
		if(this.getCompareStatus() != CompareStatus.InBoth || this.getCompareSizeStatus() != SizeCompareStatus.EQUAL) {
//			for(int i=0; i<this.getDepth(); i++) {
//				System.out.print("----");
//			}
			if(this.getDepth() !=0) {
				try {
					System.out.println(this.getFile().getAbsolutePath().replace("\\", "/") + " - " + this.getName() + " - " + this.getDepth() + " - " + this.getCompareStatus() + " - " + this.size + " - " + this.compareSizeStatus);
					
					writer.write(this.getFile().getAbsolutePath().replace("\\", "/") + " - " + this.getName() + " - " + this.getDepth() + " - " + this.getCompareStatus() + " - " + this.size + " - " + this.compareSizeStatus + "\n");
					writer.flush();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(this.getName() + " - " + this.getDepth() + " - " + this.getCompareStatus() + " - " + this.size + " - " + this.compareSizeStatus);
					try {
						try{
							writer.write(this.getParent().getFile().getAbsolutePath());
							writer.flush();
						}
						catch(Exception ee) {
							
						}
						writer.write(this.getName() + " - " + this.getDepth() + " - " + this.getCompareStatus() + " - " + this.size + " - " + this.compareSizeStatus + "\n");
						writer.flush();
						writer.write("--- EE ---\n");
						writer.flush();
					} catch (IOException e1) {
					}
					
					System.out.println("-- EE --");
				}
			}
			
		}
//		System.out.println(this.getName() + " - " + this.getDepth() + " - " + this.getCompareStatus() + " - " + this.size + " - " + this.compareSizeStatus);
		for(FileNode child : this.getChildren()){
			child.printFileNodeChildren();
		}
	}
	
	public void loadFileNodeChildren(int depth){
		children = new ArrayList<FileNode>();
//		System.out.println("this.depth - " + this.getDepth() + ", depth - " + depth);
		if(!this.isLeaf() && this.getDepth()<=depth) { 
			File[] files = file.listFiles();
			for(File file : files){
				FileNode childFileNode = new FileNode(file, this);
				System.out.println(childFileNode.getName() + " - " + childFileNode.getDepth() + " - " + childFileNode.getCompareStatus() + " - " + childFileNode.size);
				children.add(childFileNode);
				childFileNode.loadFileNodeChildren(depth);
			}
		}
	}
	
	public FileNode comparingFileNodes(FileNode comparedNode, CompareStatus compareStatusFrom, FileNode fileNodeFrom, FileNode fileNodeTo) {
		
		for (FileNode nodeFrom : fileNodeFrom.getChildren()) {
			boolean inBooth = false;
			String nodeFileFromName = nodeFrom.getName();
//			System.out.println("Node From Name - " + nodeFileFromName);
			
			if(nodeFileFromName.contains("r81376")){
				System.out.println(nodeFileFromName);
			}
			
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
				String nodeFileToName = nodeTo.getName();
				if(nodeFileToName.contains("SNAP")) {
					nodeFileToName = nodeFileToName.split("_")[0];
					nodeFileFromName = nodeFileFromName.split("_")[0];
				} else{
					nodeFileFromName = nodeFrom.getName();
				}
				
				if(nodeFileToName.contains("r81376")){
					System.out.println(nodeFileFromName);
				}
				
				if (nodeFileToName.equals(nodeFileFromName)) {
//					sizeCompareStatus = nodeFrom.size.compareTo(nodeTo.size) == 0;
					if(compareStatusFrom.equals(CompareStatus.InFrom)) {
						node.setCompareSizeStatus(SizeCompareStatus.getSizeCompareStatus(nodeFrom.getSize().compareTo(nodeTo.getSize())));
					} else {
						node.setCompareSizeStatus(SizeCompareStatus.getSizeCompareStatus(-nodeFrom.getSize().compareTo(nodeTo.getSize())));
					}
					
					node.setSize(nodeFrom.getSize());
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
				
				node.setFile(nodeFrom.getFile());
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

	/**
	 * @param fileFrom	The FileNode to be compared
	 * @param fileTo	The FileNoe to compare
	 * @param depth		The depth to compare the FileNode
	 * @return FileNode	The compared result will be stored as FIle Node
	 * 
	 * Load the FileNodes by depth, compare all the info in the loaded FileNodes
	 */
	public FileNode commpareFileNodes(File fileFrom, File fileTo, int depth) {
		FileNode comparedNode = new FileNode(null);
		
		FileNode fileNodeFrom = new FileNode(fileFrom, null);
		fileNodeFrom.loadFileNodeChildren(depth);
		FileNode fileNodeTo = new FileNode(fileTo, null);
		fileNodeTo.loadFileNodeChildren(depth);
		
		comparedNode = this.comparingFileNodes(comparedNode, CompareStatus.InFrom, fileNodeFrom, fileNodeTo);
		comparedNode = this.comparingFileNodes(comparedNode, CompareStatus.InTo, fileNodeTo, fileNodeFrom);
	
		System.out.println("------------");
		return comparedNode;
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
	
	private FileWriter getFileWriter(String filePath) throws IOException{
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		return new FileWriter(file.getAbsoluteFile(),true);
	}
	
	public static void main(String[] args) {
	}
}
