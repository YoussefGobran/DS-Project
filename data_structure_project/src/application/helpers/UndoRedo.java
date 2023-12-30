package application.helpers;

import java.util.Stack;

public class UndoRedo {
    private Stack<String> undoStack; // stack to hold the previous states to go back to it
    private Stack<String> redoStack; // stack to hold the next state to go to it after undoing

    public UndoRedo() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    // saves the state of the xml before any change
    public void saveState(String state) {
        undoStack.push(state); // push in the undo stack
        redoStack.clear(); // clear the redo stack

    }

    // undo new operations by going back to the previos state
    // function takes the current state of the xml as parameter and returns the
    // previos state
    // if the undo is done before any saving then the current state is returned
    public String undo(String state) {
        if (!undoStack.isEmpty()) {
            String temp = undoStack.pop();
            redoStack.push(state);
            return temp;
        }
        return state;
    }
	
	//redo previously undoed operations 
    //this function also takes the current state as input and returns the state which we undoed from
    //if no undos is done before the redo same state is returned 
    public String redo(String state){
        if(!redoStack.isEmpty()){
            String temp=redoStack.pop();
            undoStack.push(state);
            return temp;
        }
        return state;
    }

}