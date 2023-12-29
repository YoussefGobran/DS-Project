/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class UndoRedo {
   private Stack<String> undoStack; //stack to hold the previous states to go back to it
    private Stack<String> redoStack; //stack to hold the next state to go to it after undoing 
    public UndoRedo(){
        undoStack=new Stack<>();
        redoStack=new Stack<>();
    } 
    
}