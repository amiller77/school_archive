
public class Computer extends Player{
	private static int cols = 7;
	private static int rows = 6;
	
	public Computer(int player, Connect4Model model) {
		this.player = player;
		this.model = model;
	}
	
	/**
	 * GET MOVE
	 * first checks out necessary moves to win or lose in this round
	 * "defensive" -> will try to block the human if it will lose otherwise
	 * then tries to build sequences of 3 and 2, if it can't win or doesn't need to defend
	 */	
	public void move() {
		// first check out necessary moves to win or lose in this round
		// "defensive" -> will try to block the human if it will lose otherwise
		// then tries to build sequences of 3 and 2, if it can't win or doesn't need to defend
		Integer c = null; // column
		for (int i = 3; i>0; i--) {
			boolean defensive;
			if (i==3) {
				defensive = true;
			} else {
				defensive = false;
			}
			if (tryMove(c = AIcheckRows(i,defensive))) {
				// opt for row move
				makeMove(c);
				return;
			} else if (tryMove(c=AIcheckColumns(i,defensive))) {
				// opt for column move
				makeMove(c);
				return;
			} else if(tryMove(c=AIcheckUpwardsDiagonal(i,defensive))) {
				// opt for upwards diagonal move
				makeMove(c);
				return;
			} else if (tryMove(c=AIcheckDownwardsDiagonal(i,defensive))) {
				// opt for downwards diagonal move
				makeMove(c);
				return;
			}
		
		}
		// if no sequence of 2 or 3 is possible, make random move
		boolean flag = false;
		while (!flag) {
			c = (int) Math.floor(Math.random()*cols);
			flag = tryMove(c);
		}
		makeMove(c);
	}

	// TRY MOVE
	// attempts a move, if invalid, returns false
	private boolean tryMove(Integer c) {
		if (c != null) {
			try {
				validateMove(c);
			} catch (ColumnFullException e) {
				return false;
			}
			return true;
		} else {
			return false;
		}	
	}
	

	// **************************************************************
	// ******************* MOVE LOGISTICS ****************
	
	/**
	 * Validates a players move and throws the corresponding error if it does not meet the criteria
	 * being an integer from 0 to 6.
	 * @param colNum Move to be validated
	 * @throws IllegalMoveException
	 * @throws ColumnFullException
	 */
	public void validateMove(int col) throws ColumnFullException{
		int[][] board = model.getBoard();
		if(board[0][col] != 0) {
			throw new ColumnFullException(col + " ");
		}
	}
	
	
	// **************************************************************
	// ******************* AI BOARD CHECKING METHODS ****************
	
	/**
	 * AI CHECK ROWS
	 * @param lengthToCheckFor length of sequence being verified as parameter
	 * @param defensive is the AI playing defensively on this move?
	 * @return location of required row move or null if none needed
	 */
	private Integer AIcheckRows(int lengthToCheckFor, boolean defensive) {
		int[][] board = model.getBoard();
		for (int r = 0; r<rows; r++) {
			int humanCounter = 0;
			int computerCounter = 0;
			for (int c = 0; c<cols; c++) {
				if (board[r][c]==1) {
					computerCounter++;
					humanCounter=0;
				} else if (board[r][c]==2) {
					humanCounter++;
					computerCounter=0;
				} else {
					humanCounter=0;
					computerCounter=0;
				}
				
				// if defensive and human could win || computer has required sequence
				if ( (defensive && (humanCounter == 3)) || (computerCounter == lengthToCheckFor) ){
					Integer goodMove = rowMoveValidator(board,r,c,lengthToCheckFor);
					if (goodMove == null) {
						continue;
					} else {
						return goodMove;
					}
				}
			}
		}
		return null;
	}	
	
	
	/**
	 * ROW MOVE VALIDATOR
	 * if we found a candidate for a good row move, we check it in this method and return the good move to 
	 * make or null if no a good move right now
	 * @param board
	 * @param r
	 * @param c
	 * @param lengthToCheckFor
	 * @return
	 */
	private Integer rowMoveValidator (int[][] board, int r, int c, int lengthToCheckFor) {
		//System.out.println("");
		//System.out.println("row check.");
		//System.out.println("length to look for: "+lengthToCheckFor);
		// find total whitespace surrounding our row:
		int whiteSpaceCounter = 0;
		
		// RIGHT CHECK: check that the sequence is valid to extend to 4 to right
		//System.out.println("check to right");
		boolean rightSequenceFree = true;
		// start at next tile
		for (int i = 1;i<=4-lengthToCheckFor;i++) {
			int activeColumn = c+i;
			//System.out.println("checking: c+i= "+activeColumn);
			// if we hit non-empty token in boundaries -> uh oh:
			if (activeColumn < board[r].length && board[r][activeColumn]!=0 ) {
				//System.out.println("problem found.");
				rightSequenceFree=false;
				break;
				
			} // otherwise, if in boundaries, then whitespace encountered
			else if (activeColumn<board[r].length){
				whiteSpaceCounter++;
			}
		}

		// LEFT CHECK: check that the sequence is valid to extend to 4 to left
		//System.out.println("check to left");
		boolean leftSequenceFree = true;
		// make sure we start behind items we know to be in the row
		int k = lengthToCheckFor;
		for (int i = 1;i<=4-lengthToCheckFor; i++) {
			int activeColumn = c-k;
			//System.out.println("checking: c-k= "+activeColumn);
			// if we hit non-empty token in boundaries -> uh oh:
			if (activeColumn >= 0 && board[r][activeColumn]!=0) {
				//System.out.println("problem found.");
				leftSequenceFree= false;
				break;
			} // otherwise, if in boundaries, then whitespace encountered
			else if ( activeColumn>=0) {
				whiteSpaceCounter++;
			}
			k++;
		}
		
		// MAKE DECISION BASED ON RIGHT AND LEFT CHECKS
		//System.out.println("whitespace counter: "+whiteSpaceCounter);
		// if sequence open and available whitespace sufficient and move within bounds:
		// (right version)
		if (rightSequenceFree && whiteSpaceCounter >= 4-lengthToCheckFor && c+1<board[0].length) {
			// make move, but only if the space beneath it is the bottom of the board or already occupied
			if (r+1>=board.length || board[r+1][c+1]!=0) {
				return c+1;
			}
		} //(left version)
		if (leftSequenceFree && whiteSpaceCounter >= 4-lengthToCheckFor && c-lengthToCheckFor>=0 ){
			// make move, but only if the space beneath it is the bottom of the board or already occupied
			if (r+1>=board.length || board[r+1][c-lengthToCheckFor]!=0) {
				return c-lengthToCheckFor;
			}
		}
		return null;
	}
	
	
	/**
	 * AI CHECK COLUMNS
	 * @param lengthToCheckFor length of sequence being verified as parameter
	 * @return location of required row move or null if none needed
	 */
	private Integer AIcheckColumns(int lengthToCheckFor,boolean defensive) {
		int[][] board = model.getBoard();
		for (int c = 0; c<cols; c++) {
			int humanCounter = 0;
			int computerCounter = 0;
			for (int r = 0; r<rows; r++) {
				if (board[r][c]==1) {
					computerCounter++;
					humanCounter=0;
				} else if (board[r][c]==2) {
					humanCounter++;
					computerCounter=0;
				} else {
					humanCounter=0;
					computerCounter=0;
				}
				if ((defensive && humanCounter == 3) || computerCounter == lengthToCheckFor) {
					//System.out.println("column check");
					Integer goodMove = columnMoveValidator(board,r,c,lengthToCheckFor);
					if (goodMove==null) {
						continue;
					} else {
						return goodMove;
					}
				}
				
			}
		}
		return null;
	}
	
	/**
	 * COLUMN MOVE VALIDATOR
	 * if we found a candidate for a good column move, we check it in this method and return the good move to 
	 * make or null if no a good move right now
	 * @param board
	 * @param r
	 * @param c
	 * @param lengthToCheckFor
	 * @return
	 */
	private Integer columnMoveValidator(int[][] board, int r, int c, int lengthToCheckFor) {
		//System.out.println("r= "+r);
		// keep track of whitespace above
		int whiteSpaceCounter = 0;
		// iterate back through the rows, starting at lengthToCheckFor above r
		for (int row = r-lengthToCheckFor; row>=0; row--) {
			// if we hit a non-white tile, bad move
			//System.out.println("row: "+row);
			if (board[row][c]!=0) {
				//System.out.println("uh oh -> hit non-white tile");
				return null;
			} else {
				whiteSpaceCounter++;
			}
		}
		// if we have sufficient whitespace to get a sequence of 4, make the move, else don't
		if (whiteSpaceCounter>=4-lengthToCheckFor) {
			return c;
		} else {
			//System.out.println("not enough whitespace: whitespace = "+whiteSpaceCounter);
			return null;
		}
	}

	
	/**
	 * AI CHECK UPWARDS DIAGONAL
	 * @param lengthToCheckFor length of sequence being verified as parameter
	 * @return location of necessary move or null if none necessary
	 */
	private Integer AIcheckUpwardsDiagonal(int lengthToCheckFor,boolean defensive) {
		int[][] board = model.getBoard();
		int humanScore;
		int computerScore;
		// check down the left side of the board
		// lowest row is index 3, move down the rows
		for (int r = 3; r<rows; r++) {
			// record the scores
			humanScore = 0;
			computerScore = 0;
			// move up across the diagonal
			int j = 0;
			for (int k = r; k>=0; k--) {
				if (board[k][j]==1) {
					computerScore++;
					humanScore=0;
				} else if (board[k][j]==2) {
					humanScore++;
					computerScore=0;
				} else {
					humanScore=0;
					computerScore=0;
				}
				
				// CHECK FOR MOVE TRIGGERS
				// if defensive and human could win || computer has required sequence:
				if ( (defensive && (humanScore == 3)) || (computerScore == lengthToCheckFor) ){
					Integer goodMove = upwardsDiagonalValidator(board,k,j,lengthToCheckFor);
					if (goodMove!=null) {
						return goodMove;
					}
				} 
				
				j++;
				// max cols: 6
				if (j>cols) {
					break;
				}
			}
		}
		// now check across the bottom of the board
		// lowest column index is 1, max is 3
		for (int c = 1; c<4; c++) {
			// record the scores
			humanScore = 0;
			computerScore = 0;
			// move up across the diagonal
			int i = c;
			for (int m = 5; m>=0; m--) {
				if (board[m][i]==1) {
					computerScore++;
					humanScore=0;
				} else if (board[m][i]==2) {
					humanScore++;
					computerScore=0;
				} else {
					humanScore=0;
					computerScore=0;
				}
				
				// CHECK FOR MOVE TRIGGERS
				// if defensive and human could win || computer has required sequence:
				if ( (defensive && (humanScore == 3)) || (computerScore == lengthToCheckFor) ){
					Integer goodMove = upwardsDiagonalValidator(board,m,i,lengthToCheckFor);
					if (goodMove != null) {
						return goodMove;
					}
				}
				
				i++;
				// max cols: 6
				if (i>6) {
					break;
				}
			}
		}
		return null;
	}

	/**
	 * UPWARDS DIAGONAL VALIDATOR
	 * if we found a candidate for a good move, we check it in this method and return the good move to 
	 * make or null if no a good move right now
	 * @param board
	 * @param r
	 * @param c
	 * @param lengthToCheckFor
	 * @return
	 */
	private Integer upwardsDiagonalValidator(int[][] board, int r, int c, int lengthToCheckFor) {
		// whitespace counter:
		int whitespaceCounter = 0;
		
		// CHECK RIGHT
		// check if the sequence is valid to extend to 4 to right:
		boolean rightSequenceFree = true;
		for (int i = 1;i<=4-lengthToCheckFor;i++) {
			int activeColumn = c+i;
			int activeRow = r-i;
			// if coordinates valid:
			if (activeColumn < board[r].length && activeRow>=0) {
				// if whitespace, iterate whitespace counter and continue:
				if (board[activeRow][activeColumn]==0) {
					whitespaceCounter++;
					continue;
				} // if not whitespace, then sequence not free:
				else {
					rightSequenceFree = false;
				}
			} // if coordinates invalid, break
			else {
				// if invalid coordinates on first iteration, not actually free
				if (i == 1) {
					rightSequenceFree = false;
				}
				break;
			}
		}
		
		// CHECK LEFT
		// check if the sequence is valid to extend to 4 to left:
		boolean leftSequenceFree = true;
		for (int i = lengthToCheckFor;i<4;i++) {
			int activeColumn = c-i;
			int activeRow = r+i;
			// if coordinates valid: 
			if (activeColumn >= 0 && activeRow<board.length) {
				// if whitespace, iterate whitespace counter and continue:
				if (board[activeRow][activeColumn]==0) {
					whitespaceCounter++;
					continue;
				} // if not whitespace, then sequence not free:
				else {
					leftSequenceFree = false;
				}
			} // if coordinates invalid, break
			else {
				// if invalid coordinates on first iteration, not actually free
				if (i == lengthToCheckFor) {
					leftSequenceFree = false;
				}
				break;
			}
		}
		
		// MAKE DECISION BASED ON RIGHT AND LEFT CHECKS
		// if sequence open and available whitespace sufficient and move within bounds:
		//(right version)
		if (rightSequenceFree && whitespaceCounter >= 4-lengthToCheckFor && c+1>=0 && c+1<board[r].length) {
			// also make sure the tile beneath placement isn't whitespace
			if (board[r][c+1]!=0) {
				return c+1;
			}
		}
		//(left version)
		if (leftSequenceFree && whitespaceCounter >= 4-lengthToCheckFor && c-lengthToCheckFor >=0 && c-lengthToCheckFor <board[r].length) {
			// also make sure the tile beneath placement isn't whitespace
			if (r+lengthToCheckFor+1<board.length) {
				if (board[r+lengthToCheckFor+1][c-lengthToCheckFor]!=0) {
					return c-lengthToCheckFor;
				}
				// if bottom row, nothing to worry about
			} else {
				return c-lengthToCheckFor;
			}
			
		}
		
		return null;
	}
	
	
	/**
	 * AI CHECK DOWNWARDS DIAGONAL
	 * @param lengthToCheckFor length of sequence being verified as parameter
	 * @return location of necessary move or null if none necessary
	 */
	private Integer AIcheckDownwardsDiagonal(int lengthToCheckFor,boolean defensive) {
		int[][] board = model.getBoard();
		int humanScore;
		int computerScore;
		// move up left side; max row is 2
		for (int r = 2; r>=0; r--) {
			// record the scores
			humanScore = 0;
			computerScore = 0;
			// move down across the diagonal
			int j = 0;
			for (int k=r; k<rows;k++) {
				if (board[k][j] == 1) {
					computerScore++;
					humanScore=0;
				} else if (board[k][j]==2) {
					humanScore++;
					computerScore=0;
				} else {
					humanScore=0;
					computerScore=0;
				}
				
				// CHECK FOR MOVE TRIGGERS
				// if defensive and human could win || computer has required sequence: 
				if ( (defensive && (humanScore == 3)) || (computerScore == lengthToCheckFor) ) {
					Integer goodMove = downwardsDiagonalValidator(board,k,j,lengthToCheckFor);
					if (goodMove != null) {
						return goodMove;
					}
				} 
				
				j++;
				// max cols: 7
				if (j>cols-1) {
					break;
				}
			}
		}
		// now check across the top of the board
		// lowest column index is 1, max is 3
		for (int c = 1; c<4; c++) {
			// record the scores
			humanScore = 0;
			computerScore = 0;
			// move down across the diagonal
			int i = c;
			for (int m = 0; m<rows; m++) {
				if (board[m][i]==1) {
					computerScore++;
					humanScore=0;
				} else if (board[m][i]==2) {
					humanScore++;
					computerScore=0;
				} else {
					humanScore=0;
					computerScore=0;
				}
				
				// CHECK FOR MOVE TRIGGERS
				// if defensive and human could win || computer has required sequence: 
				if ( (defensive && (humanScore == 3)) || (computerScore == lengthToCheckFor) ) {
					Integer goodMove = downwardsDiagonalValidator(board,m,i,lengthToCheckFor);
					if (goodMove != null) {
						return goodMove;
					}
				} 
				
				i++;
				if (i>cols -1) {
					break;
				}
			}
		}
		return null;	
	}
	
	/**
	 * DOWNWARDS DIAGONAL VALIDATOR
	 * if we found a candidate for a good move, we check it in this method and return the good move to 
	 * make or null if no a good move right now
	 * @param board
	 * @param r
	 * @param c
	 * @param lengthToCheckFor
	 * @return
	 */
	private Integer downwardsDiagonalValidator(int[][]board, int r, int c, int lengthToCheckFor) {
		// whitespace counter:
		int whitespaceCounter = 0;
		
		// CHECK RIGHT
		// check if the sequence is valid to extend to 4 to right:
		boolean rightSequenceFree = true;
		for (int i=1;i<=4-lengthToCheckFor;i++) {
			int activeRow = r+i;
			int activeColumn=c+i;
			// if coordinates valid:
			if (activeColumn<board[r].length && activeRow<board.length) {
				// if whitespace, iterate whitespace counter and continue:
				if (board[activeRow][activeColumn] == 0) {
					whitespaceCounter++;
					continue;
				} // if not whitespace, then sequence not free:
				else {
					rightSequenceFree = false;
				}	
			} // if coordinates invalid, break
			else {
				// if invalid coordinates on first iteration, not actually free
				if (i == 1) {
					rightSequenceFree = false;
				}
				break;
			}
		}
	
		// CHECK LEFT
		// check if the sequence is valid to extend to 4 to left:
		boolean leftSequenceFree = true;
		for (int i=lengthToCheckFor;i<4;i++) {
			int activeColumn = c-i;
			int activeRow = r-i;
			// if coordinates valid:
			if (activeColumn>=0 && activeRow>=0) {
				// if whitespace, iterate whitespace counter and continue:
				if (board[activeColumn][activeRow]==0) {
					whitespaceCounter++;
					continue;
				} // if not whitespace, then sequence not free:
				else {
					leftSequenceFree= false;
				}
			} // if coordinates invalid, break
			else {
				// if invalid coordinates on first iteration, not actually free
				if (i == lengthToCheckFor) {
					leftSequenceFree = false;
				}
				break;
			}
		}
		
		// MAKE DECISION BASED ON RIGHT AND LEFT CHECKS
		// if sequence open and available whitespace sufficient and move within bounds:
		// (right version)
		if (rightSequenceFree && whitespaceCounter>=4-lengthToCheckFor && c+1 >= 0 && c+1 <board[r].length) {
			// also make sure the tile beneath placement isn't whitespace
			// if bottom row, nothing to worry about
			if (r+1>=board.length) {
				return c+1;
			} else if (board[r+1][c+1] !=0) {
				return c+1;
			}
		}
		//(left version)
		if (leftSequenceFree && whitespaceCounter>=4-lengthToCheckFor && c-lengthToCheckFor >=0 && r-lengthToCheckFor >= 0) {
			// also make sure the tile beneath placement isn't whitespace
			if (board[r-lengthToCheckFor+1][c-lengthToCheckFor] != 0) {
				return c-lengthToCheckFor;
			}
		}
		return null;
	}
}
