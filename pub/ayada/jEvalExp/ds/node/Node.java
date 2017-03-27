package pub.ayada.jEvalExp.ds.node;

import java.io.Serializable;
import java.util.ArrayList;

import pub.ayada.jEvalExp.ds.token.Token;

public class Node implements Cloneable, Serializable  {
	private static final long serialVersionUID = -8133744603326481356L;

	private NodeTypeEnum nodeType_ini;
	private NodeTypeEnum nodeType_res;
	private Token<?> data_ini;
	private Token<?> data_res;
	private ArrayList<Node> children;
	private Node parent;

	public <E> Node(Token<?> Data,NodeTypeEnum NodeTyp) {
		setNodeType(NodeTyp);
		setData(Data);
		this.children = new ArrayList<Node>(2);
		this.parent = null; 
		clearResInfo();
	}

	public <E> Node(NodeTypeEnum nodeTyp, Token<?> Data, Node... Children) {
		setNodeType(nodeTyp);
		this.parent = null;
		setData(Data);
		for (Node c : Children)
			this.children.add(c);
		this.children.trimToSize();
		clearResInfo();
	}

	public Token<?> getData() {
		return (this.data_res == null ) ? this.data_ini : this.data_res;			
	}

	public void setData(Token<?> data) {
			this.data_ini = data;
	}
    public void updData(Token<?> data){
    	this.data_res = data;
    }
	 
	public NodeTypeEnum getNodeType() {
		return (this.nodeType_res == null ) ?  this.nodeType_ini : this.nodeType_res;
	}

	public void setNodeType(NodeTypeEnum nodeTyp) {
		this.nodeType_ini = nodeTyp;
	}
	public void updNodeType(NodeTypeEnum nodeTyp) {
		this.nodeType_res = nodeTyp;
	}
	
	public void updNode(Token<?> token, NodeTypeEnum nodeTyp) {
		updNodeType(nodeTyp);
		updData(token);
	}

	public ArrayList<Node> getChildren() {
		return this.children;
	}
	public Node getLeftChild() {
		return (this.children.isEmpty())? null : this.children.get(0);
	}

	public void addRightChild(Node Child) {
		this.children.add(Child);
		this.children.trimToSize();
	}
	public void addLeftChild(Node Child) {
		this.children.add(0,Child);
		this.children.trimToSize();
	}	

	public Node getRightChild() {
		return this.children.get(this.children.size() - 1);
	}

	public Node getParent() {
		return this.parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public boolean isLeafNode() {
		return (this.children.isEmpty());
	}

	public boolean isRootNode() {
		return (getParent() == null);
	}

	public void clearResInfo() {
		this.data_res = null;
		this.nodeType_res = null;
	}
	
	@Override
	public Node clone() throws CloneNotSupportedException {
 		Node c  = new Node(this.data_ini.clone(),NodeTypeEnum.copy(this.nodeType_ini));		
		if (this.children != null && this.children.size() >0 ) {
			c.children = new ArrayList<Node>(this.children.size());
			for (int i=0 ; i < this.children.size(); i++) {
				c.children.add(this.children.get(i));
				c.children.get(i).setParent(c);
			}	
			for (int i=0 ; i < c.children.size(); i++) {
				c.children.set(i,c.children.get(i).clone());
			}	
		}
	    return c ;
	}

	
/*	public static void main(String[] args) throws CloneNotSupportedException {

		Node o = new Node(new Token<String>(TokenTypeEnum.STR, "Original"), NodeTypeEnum.LITERAL);		
		Node c = o.clone();
		c = new Node(new Token<Double>(TokenTypeEnum.DEC, new Double(10.0)), NodeTypeEnum.LITERAL);
		o.setParent(c);
		System.out.println("Clone Before:" + c.getData().getValue());
		c.setData(new Token<String>(TokenTypeEnum.STR, "Clone"));
		System.out.println("Clone After:" + c.getData().getValue());
		System.out.println("Original After:" + o.getData().getValue());
	}*/
	

}
