package csp;
import java.util.LinkedList;

/**
* This class should contain a list of all 1,944 constraints
*/
public class Constraints {
  public static final int BOARD_SIZE = 9;
  private LinkedList<BinaryDiffConstraint> constraints; //list of all 1,944 constraints

  /**
  * Populates the constraints list with all 1,944 constraints.
  */
  public Constraints(){
    constraints = new LinkedList<BinaryDiffConstraint>();
    rowConstraints();
    colConstraints();
    boxConstraints();
  }

  /**
  * Populates the constraints list with all row constraints.
  */
  private void rowConstraints(){
    for(int i = 0; i < BOARD_SIZE; i++){ //for each row
      for (int j = 0; j < BOARD_SIZE; j++){ //for each variable in that row
        //create a BinaryDiffConstraint with every other variable in the row
        for(int k = 0; k < BOARD_SIZE; k++){
          if(j != k){ //don't contstrain a variable with itself
          int row1 = i;
          int col1 = j;
          int row2 = i;
          int col2 = k;
          BinaryDiffConstraint bdc = new BinaryDiffConstraint(row1, col1, row2, col2);
          constraints.add(bdc);
        }
      }
    }
  }
}

/**
* Populates the constraints list with all column constraints.
*/
private void colConstraints(){
  for(int i = 0; i < BOARD_SIZE; i++){ //for each col
    for (int j = 0; j < BOARD_SIZE; j++){ //for each variable in that col
      //create a BinaryDiffConstraint with every other variable in the col
      for(int k = 0; k < BOARD_SIZE; k++){
        if(j != k){ //don't contstrain a variable with itself
        int row1 = j;
        int col1 = i;
        int row2 = k;
        int col2 = i;
        BinaryDiffConstraint bdc = new BinaryDiffConstraint(row1, col1, row2, col2);
        constraints.add(bdc);
      }
    }
  }
}
}

/**
* Populates the constraints list with all box constraints.
*/
private void boxConstraints(){
  int boxSize = BOARD_SIZE/3;

  //for each box where the upper left variable is [g][h] (row==g col ==h)
  for(int g = 0; g < BOARD_SIZE; g += boxSize){
    for(int h = 0; h <BOARD_SIZE; h += boxSize){

      //for each item [i][j] in the box
      for(int i = g; i < g+boxSize; i++){
        for(int j = h; j < h+boxSize; j++){

          //compare it with every other item [k][l] in the box
          for(int k = g; k < g+boxSize; k++){
            for (int l = h; l < h+boxSize; l++){

              //if not the same item, add the constraint
              if(i != k || j != l){
                int row1 = i;
                int col1 = j;
                int row2 = k;
                int col2 = l;
                BinaryDiffConstraint bdc = new BinaryDiffConstraint(row1, col1, row2, col2);
                constraints.add(bdc);
              }
            }
          }
        }
      }
    }
  }
}

/**
* Returns the list of constraints
* @return the constraints LinkedList
*/
public LinkedList<BinaryDiffConstraint> getConstraints(){
  return constraints;
}

}
