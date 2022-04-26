package csp;
import java.util.ArrayList;

/**
* A variable in a CSP that takes on an integer value between 1 and 9.
* The value of the variable may be specified by the original problem.
*/
public class Variable {
  public ArrayList<Integer> domain; //list of entire domain of Variable

  /*
  * Constructor for creating a variable with the domain {1-9}.
  * @param row the variable's row number
  * @param col the variable's column number
  */
  public Variable(){
    domain = new ArrayList<Integer>();
    domain.add(1);
    domain.add(2);
    domain.add(3);
    domain.add(4);
    domain.add(5);
    domain.add(6);
    domain.add(7);
    domain.add(8);
    domain.add(9);

  }

  /*
  * Constructor for creating a variable with the domain specified by the input ArrayList.
  * @param inputDomain the domain set this variable to.
  */
  public Variable(ArrayList<Integer> inputDomain){
    domain = new ArrayList<Integer>();
    for(int i = 0; i < inputDomain.size(); i++){
      domain.add(inputDomain.get(i));
    }
  }

  /*
  * Constructor for creating a variable with a domain of a single given value.
  * @param v the known domain value
  */
  public Variable(int v){
    domain = new ArrayList<Integer>();
    domain.add(v);
  }


  /*
  * Gets the only value from the domain.
  * @return the int domain value
  */
  public int getSingle(){
    return domain.get(0);
  }


  /*
  * Determines if the domain of this variable is equal to that of another Variable
  * @param other another variable to compare this one to.
  * @return true if Values have the same domain, otherwise false. 
  */
  public boolean equalDomain(Variable other){
    if(domain.size() != other.domain.size()){
      return false;
    }
    for(int i = 0; i < domain.size(); i++){
      if(domain.get(i) != other.domain.get(i)){
        return false;
      }
    }
    return true;
  }

}
