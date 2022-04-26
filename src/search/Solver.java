package search;
import csp.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.Set;

/**
* Solves a sudoku puzzle
*/
public class Solver {

	public static final int BOARD_SIZE = 9;


	public static int[][] solve(int[][] original){

		//GENERATE THE CSP

		//generate variables
		Variable[][] variables = new Variable[BOARD_SIZE][BOARD_SIZE];
		for(int row = 0; row < BOARD_SIZE; row++){
			for (int col = 0; col < BOARD_SIZE; col++){
				if(original[row][col] != 0){
					//variable is given in original problem
					variables[row][col] = new Variable(original[row][col]);
				}
				else{
					//variable not given, so create variable with fill 1-9 domain
					variables[row][col] = new Variable();
				}
			}
		}

		//generate constraints
		Constraints constraints = new Constraints();


		//TRY TO SOLVE USING AC-3 ALGORITHM
		Variable[][] ac3VariableSolution = AC3(constraints, variables);

		//convert AC3 result to a node and check whether it is a solution
		Node root = new Node(ac3VariableSolution);
		if(root.isSolution()){
			//if AC3 solved the sudoku, return the solution.
			int[][] ac3Solution = varToInt(ac3VariableSolution);
			return ac3Solution;
		}

		//TRY TO SOLVE USING BACKTRACKING ALGORITHM
		Stack<Node> frontier = new Stack<Node>();
		Node backtrackingNodeSolution = backtracking(constraints, root, frontier);
		Variable[][] backtrackingVariableSolution = backtrackingNodeSolution.getVariables();
		int[][] backtrackingSolution = varToInt(backtrackingVariableSolution);
		if(backtrackingSolution != null){
			//sudoku was solved by backtracking!
			return backtrackingSolution;
		}

		//Backtracking with AC-3 unable to solve sudoku.
		return null;
	}




	/*
	* Runs the AC3 algorithm on the sudoku board to impose arc-consistency for
	* all input constraints. Returns the arc-consistent board, or null if arc-
	* consistency is not possible.
	* @param constraints the 1,944 constraints for sudoku boards
	* @param variables the original board given by the problem. Each element in
	*			the array is a Variable object which holds a domain.
	* @return An arc-consistent board, or null if arc-consistency isn't possible.
	*/
	private static Variable[][] AC3(Constraints constraints, Variable[][] variables){
		//copy the input board
		Variable[][] copyVariables = new Variable[BOARD_SIZE][BOARD_SIZE];
		for(int w = 0; w < BOARD_SIZE; w++){
			for (int q = 0; q < BOARD_SIZE; q++){
				ArrayList<Integer> oldDomain = variables[w][q].domain;
				copyVariables[w][q] = new Variable(oldDomain);
			}
		}

		LinkedList<BinaryDiffConstraint> queue = new LinkedList<BinaryDiffConstraint>();
		//Add all constraints to the queue.
		int constraintSize = constraints.getConstraints().size();

		for (int i = 0; i < constraintSize; i++){
			//Add twice: once in the form (Xi, Xj) and once in the form (Xj, Xi)
			BinaryDiffConstraint a = constraints.getConstraints().get(i);
			BinaryDiffConstraint b = new BinaryDiffConstraint(a.row2, a.col2, a.row1, a.col1);
			queue.add(a);
			queue.add(b);
		}

		while(!queue.isEmpty()){

			BinaryDiffConstraint bdc = queue.removeFirst();

			//Make bdc var1 arc-consistent with respect to bdc var2
			if (removeValues(bdc, copyVariables)){
				//if domain of variable 1 in bdc is empty, this CSP cannot be satisfied

				if(copyVariables[bdc.row1][bdc.col1].domain.isEmpty()){
					//return FAILURE
					return null;
				}

				//re-queue all incoming arcs bdc var1 (All bdc where var1 is var 2 position)
				for (int i = 0; i < constraintSize; i++){
					BinaryDiffConstraint c = constraints.getConstraints().get(i);
					if(c.row2 == bdc.row1 && c.col2 == bdc.col1){
						queue.add(c);
					}
				}

			}
		}
		return copyVariables;
	}


	/*
	* Removes elements from variable's domains to makes a given board arc-consistent
	* for a specified constraint.
	* @param bdc the constraint to enforce
	* @return true if the constraint can be enforced, false if it makes any of the domain sizes < 1
	*/
	private static boolean removeValues(BinaryDiffConstraint bdc, Variable[][] variables){
		boolean modified = false;
		boolean found = false;

		for (int i = 0; i < variables[bdc.row1][bdc.col1].domain.size(); i++){
			found = false;
			for (int j = 0; j < variables[bdc.row2][bdc.col2].domain.size(); j++){
				if(variables[bdc.row1][bdc.col1].domain.get(i) != variables[bdc.row2][bdc.col2].domain.get(j)){
					found = true;
					break;
				}
			}
			if(!found){
				//constraint is not satisfied, remove d1 from domain1
				variables[bdc.row1][bdc.col1].domain.remove(variables[bdc.row1][bdc.col1].domain.get(i));
				modified = true;
			}
		}

		return modified;
	}

	/*
	* Turns a board of variables into a board of ints if possible.
	* @param variables the board to be converted.
	* @return a board of ints, or null if any variables have a domain size != 1
	*/
	private static int[][] varToInt(Variable[][] variables){
		int[][] ret = new int[BOARD_SIZE][BOARD_SIZE];
		if(variables != null){
			for(int row = 0; row < BOARD_SIZE; row++){
				for(int col = 0; col < BOARD_SIZE; col++){
					if(variables[row][col].domain.size() != 1){
						//the domain is larger than one so it's not a solution
						int[][] failure = new int[BOARD_SIZE][BOARD_SIZE];
						return failure;
					}
					else{
						ret[row][col] = variables[row][col].getSingle();
					}
				}
			}
		}

		return ret;
	}


	/*
	* Run backtracking search to find board solution. Pick the variable on the board
	* with the smallest domain, and for each of its possible domain assignments,
	* assign arc-consistency and use Depth First Search (DFS) to find a board solution.
	* @param constraints the 1,944 constraints for sudoku boards
	* @param initialBoard the root board for DFS
	* @param frontier a stack to contain the nodes yet to be explored
	* @return a node with the solution to the sudoku.
	*/
	private static Node backtracking(Constraints constraints, Node initialBoard, Stack<Node> frontier){
		//create explored set and put initial board on frontier
		Set<Node> explored = new HashSet<Node>();
		frontier.push(initialBoard);

		//until the frontier is empty, pop a node from the stack to evaluate
		while(!frontier.isEmpty()){
			Node board = frontier.pop();

			//if the node has a solution board, return it
			if(board.isSolution()){
				return board;
			}

			explored.add(board);

			//make a copy of the variable board
			Variable[][] copyVariables = new Variable[BOARD_SIZE][BOARD_SIZE];
			for(int w = 0; w < BOARD_SIZE; w++){
				for (int q = 0; q < BOARD_SIZE; q++){
					ArrayList<Integer> oldDomain = board.variables[w][q].domain;
					copyVariables[w][q] = new Variable(oldDomain);
				}
			}

			//Assign arc consistency for board node using AC-3
			Variable[][] arcConsistent = AC3(constraints, copyVariables);

			//of arc consistency is possible, put all of the node's children on the frontier
			if(arcConsistent != null){

				// assign arcConsistent to Node's board
				board.setBoard(arcConsistent);

				//push children onto the stack
				int[] varCoords = minimumRemainingValue(board.getVariables());
				int varRow = varCoords[0];
				int varCol = varCoords[1];

				Node[] children = board.getChildren(varRow, varCol);
				int numChildren = children.length;

				//put all the children on the board
				for(int i = 0; i < numChildren; i++){
					frontier.push(children[i]);
				}
			}
		}
		return null;
	}



/*
 * Returns whether the explored set contains a specified node.
 * @param set to explore
 * @param node to search for
 * @return true if set contains node, otherwise false
 */
	private static boolean containsNode(Set<Node> set, Node node){
		Iterator<Node> itr = set.iterator();
		while(itr.hasNext()){
			if(itr.next().equalNode(node)){
				return false;
			}
		}
		return true;
	}


	/*
	* Fins the board variable with the smallest un fixed (greater than 1) domain size.
	* @param variables the baord of variables to search
	* @return an int array with the row and column of the chosen variable in the form
	*			index 1 = [row], index 2 = [column]
	*/
	private static int[] minimumRemainingValue(Variable[][] variables){
		int varRow = 0;
		int varCol = 0;

		//for any smaller domain sizes, update the returned minimum
		for(int row = 0; row < BOARD_SIZE; row++){
			for(int col = 0; col < BOARD_SIZE; col++){
				//if current var coords are of a fixed val and the [row][col] val is not
				//fixed, update the var coords
				if(variables[row][col].domain.size() > 1){
					if(variables[varRow][varCol].domain.size() <= 1){
						varRow = row;
						varCol = col;
					}
					//if the [row][col] val is not fixed, but smaller than the current
					//update the var coords
					else if(variables[row][col].domain.size() < variables[varRow][varCol].domain.size()){
						varRow = row;
						varCol = col;
					}
				}
			}
		}

		//return row/col coords of variable with smallest remaining domain size
		int[] retVal = new int[2];
		retVal[0] = varRow;
		retVal[1] = varCol;
		return retVal;
	}

}
