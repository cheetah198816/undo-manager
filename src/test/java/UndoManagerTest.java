import org.junit.Before;
import org.junit.Test;
import undo.document.Document;
import undo.document.impl.TextDocument;
import undo.undomanager.UndoManager;
import undo.undomgrfactory.UndoManagerFactory;
import undo.undomgrfactory.impl.UndoManagerFactoryImpl;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by chetan on 28.11.2017.
 */
public class UndoManagerTest {

    private Document document;

    private UndoManager undoManager;

    private UndoManagerFactory undoManagerFactory;

    @Before
    public void init() {
        document = new TextDocument(new StringBuffer());
        undoManagerFactory = new UndoManagerFactoryImpl();
        undoManager = undoManagerFactory.createUndoManager(document, 3);
        document.setUndoManager(undoManager);
    }

    @Test
    public void test_document_insert() {
        document.insert(document.getCurrentPosition(), "ab");
        document.insert(document.getCurrentPosition(), "ab");
        document.setDot(4);
        document.insert(document.getCurrentPosition(), "cd");

        assertThat(document.getContent()).contains("ababcd");
    }

    @Test(expected = IllegalStateException.class)
    public void test_document_insert_illegal_state() {
        document.insert(document.getCurrentPosition(), "ab");
        document.insert(document.getCurrentPosition(), "ab");
        document.setDot(5);
        document.insert(document.getCurrentPosition(), "cd");
    }

    @Test(expected = IllegalStateException.class)
    public void test_document_insert_illegal_state_wring_position() {
        document.insert(document.getCurrentPosition(), "ab");
        document.insert(document.getCurrentPosition(), "ab");
        document.insert(5, "cd");
    }

    @Test(expected = IllegalStateException.class)
    public void test_document_delete_illegal_state() {
        document.insert(document.getCurrentPosition(), "ab");
        document.insert(document.getCurrentPosition(), "ab");
        document.setDot(0);
        document.delete(document.getCurrentPosition(), "ac");
    }

    @Test(expected = IllegalStateException.class)
    public void test_document_delete_illegal_state_wrong_position() {
        document.insert(document.getCurrentPosition(), "ab");
        document.insert(document.getCurrentPosition(), "ab");
        document.setDot(3);
        document.delete(document.getCurrentPosition(), "ab");
    }

    @Test
    public void test_document_delete() {
        document.insert(document.getCurrentPosition(), "ab");
        document.insert(document.getCurrentPosition(), "ab");
        document.setDot(2);
        document.delete(document.getCurrentPosition(), "ab");

        assertThat(document.getContent()).contains("ab");
    }

    @Test(expected = IllegalStateException.class)
    public void test_document_delete_illegal_state_explicit_wrong_position() {
        document.insert(document.getCurrentPosition(), "ab");
        document.insert(document.getCurrentPosition(), "ab");
        document.delete(5, "ab");
    }

    @Test
    public void can_undo_if_no_changes_made() {
        boolean result = undoManager.canUndo();

        assertThat(result).isFalse();
    }

    @Test
    public void test_can_undo_if_changes_made() {
        document.insert(document.getCurrentPosition(), "ab");
        boolean result = undoManager.canUndo();

        assertThat(result).isTrue();
    }

    @Test
    public void test_can_undo_if_no_changes_made() {
        boolean result = undoManager.canUndo();

        assertThat(result).isFalse();
    }

    @Test
    public void test_can_redo_if_no_changes_made() {
        boolean result = undoManager.canRedo();

        assertThat(result).isFalse();
    }

    @Test
    public void test_undo_redo() {
        document.insert(document.getCurrentPosition(), "ab");
        document.insert(document.getCurrentPosition(), "cd");
        document.insert(document.getCurrentPosition(), "ef");
        document.setDot(0);
        document.delete(document.getCurrentPosition(), "ab");

        undoManager.undo();
        undoManager.undo();
        undoManager.undo();

        undoManager.redo();

        assertThat(document.getContent()).contains("abcd");
    }

    @Test
    public void test_undo_redo_another_test() {
        document.insert(document.getCurrentPosition(), "ab");
        document.setDot(0);
        document.delete(document.getCurrentPosition(), "ab");
        document.insert(document.getCurrentPosition(), "cd");
        document.setDot(0);
        document.delete(document.getCurrentPosition(), "cd");
        document.insert(document.getCurrentPosition(), "ef");

        undoManager.undo();
        undoManager.undo();
        undoManager.undo();

        undoManager.redo();
        undoManager.redo();
        undoManager.redo();

        assertThat(document.getContent()).contains("ef");
    }
}
