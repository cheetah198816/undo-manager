package undo.change.impl;

import undo.change.Change;
import undo.document.Document;

/**
 * Created by chetan on 28.11.2017.
 */
public class InsertChange implements Change {

    private int position;

    private String string;

    public InsertChange(final int pos, final String s) {
        this.position = pos;
        this.string = s;
    }

    @Override
    public void apply(Document doc) {
        final StringBuffer stringBuffer = doc.getContent();
        int newPosition = 0;
        if (position > stringBuffer.length()) {
            throw new IllegalStateException();
        }
        stringBuffer.insert(position, string);
        newPosition = position + string.length();
        doc.setDot(newPosition);
    }

    @Override
    public void revert(Document doc) {
        final StringBuffer stringBuffer = doc.getContent();
        if ((position + string.length()) > stringBuffer.length()) {
            throw new IllegalStateException();
        }
        if (!stringBuffer.substring(position, position + string.length()).equals(string)) {
            throw new IllegalStateException();
        }
        stringBuffer.delete(position, position + string.length());
        doc.setDot(position);
    }
}
