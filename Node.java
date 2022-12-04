/*Ονοματεπώνυμο : Κουρκουτίδης Αθανάσιος & ΑΕΜ : 9673 & Tel : 6945346059 & Email AUTH : kourkoua@ece.auth.gr 
 
Ονοματεπώνυμο : Σταύρος Βασίλειος Μπουλιόπουλος & ΑΕΜ : 9671 & Tel : 6943081690 & E-mail AUTH : smpoulio@ece.auth.gr*/
import java.util.ArrayList;
public class Node {
	private Node parent;
	private ArrayList<Node> children;
	private int nodeDepth;
	private int[] nodeMove; // (x,y) & dice code
	private Board nodeBoard;
	private double nodeEvaluation;
	//Constructors

	public Node()
	{
		
	}
	public Node(Node parent,int n,int nodeDepth,int[] nodeMove,Board nodeBoard,double nodeEvaluation) //Parametric 
	{
	this.parent= parent;
	children = new ArrayList<Node>(n); //n=possibleMoves
	this.nodeDepth = nodeDepth;
	this.nodeMove = nodeMove;
	this.nodeBoard = nodeBoard;
	this.nodeEvaluation = nodeEvaluation;
	}
	public Node(Node node)
	{
		parent = node.parent;
		children = node.children;
		nodeDepth = node.nodeDepth;
		nodeMove = node.nodeMove;
		nodeBoard =node.nodeBoard;
		nodeEvaluation = node.nodeEvaluation;
	}

	//Getters & setters
	public Node getParent()
	{
		return parent;
	}
	public ArrayList<Node> getChildren()
	{
		return children;
	}
	public int[] getNodeMove()
	{
		return nodeMove;
	}
	public int getNodeDepth()
	{
		return nodeDepth;
	}
	public Board getNodeBoard()
	{
		return nodeBoard;
	}
	public double getNodeEvaluation()
	{
		return nodeEvaluation;
	}
	//////
	public void setParent(Node parent)
	{
		this.parent=parent;
	}
	public void setChildren(ArrayList<Node> children)
	{
		this.children = children;
	}
	public void setNodeMove(int[] nodeMove)
	{
		this.nodeMove = nodeMove;
	}
	public void setNodeDepth(int nodeDepth)
	{
		this.nodeDepth = nodeDepth;
	}
	public void setNodeBoard(Board nodeBoard)
	{
		this.nodeBoard = nodeBoard;
	}
	public void setNodeEvaluation(double nodeEvaluation)
	{
		this.nodeEvaluation = nodeEvaluation;
	}
}
