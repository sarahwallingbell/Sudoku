// The BinaryDiffConstraint class represents a binary difference constraint -- i.e.,
// it is a constraint specifying that two variables must take on a different value.
// You will notice that this class has already been written for you and is nothing
// more than a plain old data class. All of the fields are public and its only purpose
// is to package together the row and column indices for both variables.

package csp;

/**
 * A binary constraint that specifies that the value of 2 variables must be different.
 *
 * @author alchambers
 * @version spring2019
 */
public class BinaryDiffConstraint {
	public int row1, col1;
	public int row2, col2;

	/**
	 * Constructs a new binary difference constraint
	 * @param row1
	 * 				the row index of the first variable
	 * @param col1
	 * 				the column index of the first variable
	 * @param row2
	 * 				the row index of the second variable
	 * @param col2
	 * 				the column index of the second variable
	 */
	public BinaryDiffConstraint(int row1, int col1, int row2, int col2) {
		this.row1 = row1;
		this.col1 = col1;
		this.row2 = row2;
		this.col2 = col2;
	}
}
