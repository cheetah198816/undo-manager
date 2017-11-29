package undo.document.impl;

import undo.undomanager.UndoManager;
import undo.change.impl.DeleteChange;
import undo.change.impl.InsertChange;
import undo.document.Document;

/**
 * Created by chetan on 27.11.2017.
 */
public class TextDocument implements Document {

    private StringBuffer stringBuffer;

    private UndoManager undoManager;

    private int currentPosition;

    public TextDocument(final StringBuffer stringBuffer) {
        this.stringBuffer = stringBuffer;
    }

    @Override
    public void delete(int pos, String s) {
        if ((pos + s.length()) > stringBuffer.length()) {
            throw new IllegalStateException();
        }
        if (!stringBuffer.substring(pos, pos + s.length()).equals(s)) {
            throw new IllegalStateException();
        }
        stringBuffer.delete(pos, pos + s.length());
        currentPosition = pos;
        undoManager.registerChange(new DeleteChange(pos, s));
    }

    @Override
    public void insert(int pos, String s) {
        if (pos > stringBuffer.length()) {
            throw new IllegalStateException();
        }
        stringBuffer.insert(pos, s);
        currentPosition += s.length();
        undoManager.registerChange(new InsertChange(pos, s));
    }

    @Override
    public void setDot(int pos) {
        if (pos > stringBuffer.length()) {
            throw new IllegalStateException();
        }
        currentPosition = pos;
    }

    @Override
    public StringBuffer getContent() {
        return stringBuffer;
    }

    @Override
    public Integer getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void setUndoManager(UndoManager undoManager) {
        this.undoManager = undoManager;
    }
}
