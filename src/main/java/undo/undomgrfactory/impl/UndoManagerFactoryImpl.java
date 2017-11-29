package undo.undomgrfactory.impl;

import undo.document.Document;
import undo.undomanager.UndoManager;
import undo.undomanager.impl.UndoManagerImpl;
import undo.undomgrfactory.UndoManagerFactory;

/**
 * Created by chetan on 28.11.2017.
 */
public class UndoManagerFactoryImpl implements UndoManagerFactory {

    @Override
    public UndoManager createUndoManager(Document doc, int bufferSize) {
        return new UndoManagerImpl(doc, bufferSize);
    }
}
