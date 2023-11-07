package ca.ulaval.glo2004.domaine.utils;

import java.util.Stack;

// TODO: undo/redo management should be completed.
class UndoRedoAction {
    private Runnable undoAction;
    private Runnable redoAction;

    public UndoRedoAction(Runnable undoAction, Runnable redoAction) {
        this.undoAction = undoAction;
        this.redoAction = redoAction;
    }

    public void undo() {
        undoAction.run();
    }

    public void redo() {
        redoAction.run();
    }
}

public class UndoRedoManager {
    private Stack<UndoRedoAction> undoStack = new Stack<>();
    private Stack<UndoRedoAction> redoStack = new Stack<>();

    public void addUndoRedoAction(Runnable undoAction, Runnable redoAction) {
        UndoRedoAction action = new UndoRedoAction(undoAction, redoAction);
        addUndoRedoAction(action);
    }

    public void addUndoRedoAction(UndoRedoAction action) {
        undoStack.push(action);
        redoStack.clear();
    }
    
    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }

        UndoRedoAction action = undoStack.pop();

        if (action == null) {
            return;
        }

        action.undo();
        redoStack.push(action);
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }

        UndoRedoAction action = redoStack.pop();

        if (action == null) {
            return;
        }

        action.redo();
        undoStack.push(action);
    }

    public int getUndoStackSize() {
        return undoStack.size();
    }

    public int getRedoStackSize() {
        return redoStack.size();
    }
}
