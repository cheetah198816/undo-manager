package undo.undomanager.impl;

import undo.change.Change;
import undo.document.Document;
import undo.undomanager.UndoManager;

import java.util.Stack;

/**
 * Created by chetan on 28.11.2017.
 */
public class UndoManagerImpl implements UndoManager {

    private Stack<Change> undoStack = new Stack<>();

    private Stack<Change> redoStack = new Stack<>();

    private Document document;

    private Integer bufferSize;

    public UndoManagerImpl(final Document document, final int bufferSize) {
        this.document = document;
        this.bufferSize = bufferSize;
    }

    @Override
    public void registerChange(Change change) {
        if (undoStack.size() < bufferSize) {
            undoStack.push(change);
        } else {
            undoStack.remove(0);
            undoStack.push(change);
        }
    }

    @Override
    public boolean canUndo() {
        if (!undoStack.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void undo() {
        if (!canUndo()) {
            throw new IllegalStateException();
        }
        final Change change = undoStack.pop();
        change.revert(document);
        redoStack.push(change);
    }

    @Override
    public boolean canRedo() {
        if (!redoStack.isEmpty()) {
            return true;
        }
        return false;
    }

    @Override
    public void redo() {
        if (!canRedo()) {
            throw new IllegalStateException();
        }
        final Change change = redoStack.pop();
        change.apply(document);
    }
}
