import java.util.*;

public class Tree {
	private Node root;
	
	public Tree() {
		root = null;
	}
	
	public boolean insert(int x) {
		if(root == null) {
			root = new Node();
			root.insertKey(x);
			return true;
		}
		int size = size(root.keys.get(0));
		Node newRoot = root.insert(x);
		if(newRoot != null) {
		// assign new root if split node
			root = newRoot;
			return true;
		}
		else if(size(root.keys.get(0)) == size){
			return false;
		}
		return true;
	}
	
	public int size(int x) {
		Node n = search(x);
		if(n != null) {
			return search(x).size();
		}
		else {
			return 0;
		}
	}
	
	public Node search(int x) {
		return root.search(x);
	}
	
	class Node {
		ArrayList<Integer> keys;
		ArrayList<Node> children;
		
		Node() {
			keys = new ArrayList<Integer>();
			children = new ArrayList<Node>();
		}
		
		void insertKey(int key) {
			// insert key into sorted position
			int i = keys.size();
			while(i > 0 && key < keys.get(i - 1)) {
				i--;
			}
			keys.add(i, key);
		}
		
		void insertChild(Node child) {
			// insert child into sorted position
			int i = children.size();
			while(i > 0 && child.keys.get(0) < keys.get(i - 1)) {
				i--;
			}
			children.add(i, child);
		}
		
		Node search(int x) {
			if(keys.indexOf(x) != -1) {
				return this;
			}
			else if(children.size() < 1) {
				return null;
			}
			else {
				int i = keys.size();
				while(i > 0 && x < keys.get(i - 1)) {
					i--;
				}
				return children.get(i).search(x);
			}
		}
		
		int size() {
			int size = keys.size();
			for(Node child:children) {
				size += child.size();
			}
			return size;
		}
		
		Node insert(int x) {
			// return null if key is duplicate
			if(keys.indexOf(x) != -1) {
				return null;
			}
			// move down
			else {
				// insert key into node if leaf
				if(children.isEmpty()) {
					insertKey(x);
				}	
				// choose which child to move down to
				else {
					int i = keys.size();
					while(i > 0 && x < keys.get(i - 1)) {
						i--;
					}
					Node insertNode = children.get(i).insert(x);
					if(insertNode != null) {
						//remove emptied node, insert split key and add split children
						children.remove(i);
						insertKey(insertNode.keys.get(0));
						for(Node child:insertNode.children) {
							insertChild(child);
						}
					}
				}
				// split if node is full
				if(keys.size() > 2) {
					return split();
				}
				return null;
			}
		}
		
		Node split() {
			Node splitNode = new Node();
			splitNode.insertKey(keys.remove(1));
			int remainingKeys = keys.size();
			int remainingChildren = children.size();
			// split remaining keys into splitNode's children and distribute children into splitNode's children's children
			for(int i = 0; i < remainingKeys; i++) {
				Node newChild = new Node();
				newChild.insertKey(keys.remove(0));
				for(int j = 0; j < remainingChildren / remainingKeys; j++) {
					newChild.insertChild(children.remove(0));
				}
				splitNode.insertChild(newChild);
			}
			return splitNode;
		}
	}
}


