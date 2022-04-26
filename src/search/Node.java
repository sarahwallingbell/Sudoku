package search;
import csp.*;
import java.util.ArrayList;

public class Node {

	public static final int BOARD_SIZE = 9;

	public Variable[][] variables; //the board configuration
	private Node parent;
	//the row and column of the changed variable that led from the parent to this node.
	private int row;
	private int col;

	/*
	 * Create a root node with the input board and no Parent
	 * @param var a board configuration
	 */
	public Node(Variable[][] var){
		variables = var;
		parent = null;
		row = 0;
		col = 0;
	}

	/*
	 * Creates a node with the input parent, board, and changed variable.
	 * @param parent the parent to this node
	 * @param row the row of the changed variable between this and the parent node
	 * @param col the col of the changed variable between this and the parent node
	 * @param newVariables the board for this node
	 */
	public Node(Node parent, int row, int col, Variable[][] newVariables){
		variables = newVariables;
		this.parent = parent;
		this.row = row;
		this.col = col;
	}


	/*
	 * Creates child nodes for each element in the domain of the parent Board
	 * at the indicated row/col.
	 * @param row the row of the variable to change
	 * @param col the col of the variable to change
	 * @return an array of the child nodes.
	 */
	public Node[] getChildren(int row, int col){
		//for the variable indicated by input row/col, for each value in its domain
		//generate a node with the updated board.

		//get the variable that should be changed
		Variable var = variables[row][col];

		//get the domain of the variable that should be Changed
		ArrayList<Integer> varDomain = var.domain;

		//get the number of children to create (length of domain)
		int numChildren = var.domain.size();

		//create an array of nodes to hold all of the childre.
		Node[] children = new Node[numChildren];

		for(int i = 0; i < numChildren; i++){
			//clone the board
			Variable[][] newVariables = new Variable[BOARD_SIZE][BOARD_SIZE];
			for(int w = 0; w < BOARD_SIZE; w++){
			 for (int q = 0; q < BOARD_SIZE; q++){
				 ArrayList<Integer> oldDomain = variables[w][q].domain;
				 newVariables[w][q] = new Variable(oldDomain);
			 }
			}

			//create the new child and add it to the children array
			int d = variables[row][col].domain.get(i);
			Variable newVar = new Variable(d);
			newVariables[row][col] = newVar;
			children[i] = new Node(this, row, col, newVariables);

		}

		return children;
	}

	/*
	 * Determines if variables global variable is a solution. That is, do all of
	 * the variables in it have a domain size of 1.
	 * @return true if the board is a solution (all variables have domain size of 1)
	 */
	public boolean isSolution(){
		for(int row = 0; row < BOARD_SIZE; row++){
			for(int col = 0; col < BOARD_SIZE; col++){
				//if the domain is not 1 for all variables, a solution has not been found
				if(variables[row][col].domain.size() != 1){
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * return the board.
	 * @return variables board
	 */
	public Variable[][] getVariables(){
		return variables;
	}

	/*
	 * change the Node's variables to the input board.
	 * @param board the new board configuration
	 */
	public void setBoard(Variable[][] board){
		for(int i = 0; i < BOARD_SIZE; i++){
			for(int j = 0; j <BOARD_SIZE; j++){
				variables[i][j] = board[i][j];
			}
		}
	}


	/*
	 * Determines if too nodes are equal.
	 * @param other the other node to compare this one to
	 * @return true if all variables in the node's board have the same domain
	 */
	public boolean equalNode(Node other){
		System.out.println("Node other: " + other);
		for (int row = 0; row < BOARD_SIZE; row++){
			for (int col = 0; col < BOARD_SIZE; col++){
				System.out.println("!variables["+row+"][" +col+ "].equalDomain(other.variables[" +row +"]["+col+"]");
				if(!variables[row][col].equalDomain(other.variables[row][col])){
					return false;
				}
			}
		}
		return true;
	}


}
