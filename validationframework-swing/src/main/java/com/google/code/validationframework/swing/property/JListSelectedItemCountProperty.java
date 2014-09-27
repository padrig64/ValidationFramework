package com.google.code.validationframework.swing.property;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.property.AbstractReadableProperty;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Read-only property representing the number of selected items in a {@link JList}.
 */
public class JListSelectedItemCountProperty extends AbstractReadableProperty<Integer> implements Disposable {

    /**
     * Entity tracking changes of selection (and selection model).
     */
    private final SelectionAdapter selectionAdapter = new SelectionAdapter();

    /**
     * List whose selection count is to be tracked.
     */
    private JList list = null;

    /**
     * Current property value.
     */
    private int count = 0;

    /**
     * Constructor specifying the list whose selection count is represented by this property.
     *
     * @param list List whose selection count is to be tracked.
     */
    public JListSelectedItemCountProperty(JList list) {
        this.list = list;
        list.addPropertyChangeListener("selectionModel", selectionAdapter);
        list.getSelectionModel().addListSelectionListener(selectionAdapter);
        count = list.getSelectedIndices().length;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (list != null) {
            list.getSelectionModel().removeListSelectionListener(selectionAdapter);
            list.removePropertyChangeListener("selectionModel", selectionAdapter);
            list = null;
        }
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Integer getValue() {
        return count;
    }

    /**
     * Updates the value of this property based on the list's selection model and notify the listeners.
     */
    private void updateValue() {
        if (list != null) {
            int oldCount = this.count;
            this.count = list.getSelectedIndices().length;
            maybeNotifyListeners(oldCount, count);
        }
    }

    /**
     * Entity tracking changes of selection (and selection model).
     */
    private class SelectionAdapter implements ListSelectionListener, PropertyChangeListener {

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("selectionModel".equals(evt.getPropertyName())) {
                // Unregister from previous selection model
                if (evt.getOldValue() instanceof ListSelectionModel) {
                    ((ListSelectionModel) evt.getOldValue()).removeListSelectionListener(this);
                }

                // Register to new selection model
                if (evt.getNewValue() instanceof ListSelectionModel) {
                    ((ListSelectionModel) evt.getNewValue()).addListSelectionListener(this);

                    // Update value from new selection model
                    updateValue();
                }
            }
        }

        /**
         * @see ListSelectionListener#valueChanged(ListSelectionEvent)
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                updateValue();
            }
        }
    }
}
