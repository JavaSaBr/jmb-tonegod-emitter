package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.AbstractInterpolationInfluencerControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.rlib.fx.util.FxControlUtils;
import com.ss.rlib.fx.util.FxUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.InterpolatedParticleInfluencer;
import tonegod.emitter.interpolation.Interpolation;
import tonegod.emitter.interpolation.InterpolationManager;

/**
 * The implementation of the element for {@link AbstractInterpolationInfluencerControl} for editing something and
 * interpolation.
 *
 * @param <P> the type parameter
 * @param <E> the type parameter
 * @param <C> the type parameter
 * @author JavaSaBr
 */
public abstract class InterpolationElement<P extends InterpolatedParticleInfluencer, E extends Node,
        C extends AbstractInterpolationInfluencerControl<P>> extends HBox {

    /**
     * The constant STRING_CONVERTER.
     */
    @NotNull
    protected static final StringConverter<Interpolation> STRING_CONVERTER = new StringConverter<Interpolation>() {

        @Override
        public String toString(final Interpolation object) {
            return object.getName();
        }

        @Override
        public Interpolation fromString(final String string) {
            return null;
        }
    };

    /**
     * The constant INTERPOLATIONS.
     */
    @NotNull
    protected static final ObservableList<Interpolation> INTERPOLATIONS;

    static {
        INTERPOLATIONS = FXCollections.observableArrayList();
        INTERPOLATIONS.addAll(InterpolationManager.getAvailable());
    }

    /**
     * The parent control.
     */
    @NotNull
    private final C control;

    /**
     * The editable control.
     */
    @Nullable
    protected E editableControl;

    /**
     * The interpolation chooser.
     */
    @Nullable
    protected ComboBox<Interpolation> interpolationComboBox;

    /**
     * The index.
     */
    private final int index;

    /**
     * The flag for ignoring listeners.
     */
    private boolean ignoreListeners;

    public InterpolationElement(@NotNull C control, int index) {
        this.control = control;
        this.index = index;
        createComponents();
        setIgnoreListeners(true);
        reload();
        setIgnoreListeners(false);
        FxUtils.addClass(this, CssClasses.DEF_HBOX, CssClasses.ABSTRACT_PARAM_CONTROL_INFLUENCER_ELEMENT);
    }

    /**
     * Create components.
     */
    @FxThread
    protected void createComponents() {

        Label editableLabel = null;

        if (isNeedEditableLabel()) {

            editableLabel = new Label(getEditableTitle() + ":");
            editableLabel.prefWidthProperty().bind(widthProperty().multiply(0.2));

            FxUtils.addClass(editableLabel, CssClasses.ABSTRACT_PARAM_CONTROL_PARAM_NAME_SINGLE_ROW);
        }

        editableControl = createEditableControl();

        var interpolationLabel = new Label(Messages.MODEL_PROPERTY_INTERPOLATION + ":");
        interpolationLabel.prefWidthProperty().bind(widthProperty().multiply(0.25));

        interpolationComboBox = new ComboBox<>();
        interpolationComboBox.setEditable(false);
        interpolationComboBox.setConverter(STRING_CONVERTER);
        interpolationComboBox.getItems().setAll(INTERPOLATIONS);
        interpolationComboBox.prefWidthProperty()
                .bind(widthProperty().multiply(0.35));

        FxControlUtils.onSelectedItemChange(interpolationComboBox, this::apply);

        FxUtils.addClass(interpolationLabel, CssClasses.ABSTRACT_PARAM_CONTROL_PARAM_NAME_SINGLE_ROW)
                .addClass(interpolationComboBox, CssClasses.ABSTRACT_PARAM_CONTROL_COMBO_BOX);

        if (editableLabel != null) {
            FxUtils.addChild(this, editableLabel);
        }

        FxUtils.addChild(this, editableControl, interpolationLabel, interpolationComboBox);
    }

    /**
     * Get the editable title.
     *
     * @return the editable title
     */
    @FxThread
    protected @NotNull String getEditableTitle() {
        throw new UnsupportedOperationException();
    }

    /**
     * Create an editable control.
     *
     * @return the created editable control.
     */
    @FxThread
    protected abstract @NotNull E createEditableControl();

    /**
     * Apply the new value.
     *
     * @param newValue the new value.
     */
    @FxThread
    protected void apply(@NotNull Interpolation newValue) {
        if (isIgnoreListeners()) {
            getControl().requestToChange(newValue, index);
        }
    }

    /**
     * Get the control.
     *
     * @return the control
     */
    @FromAnyThread
    protected @NotNull C getControl() {
        return control;
    }

    /**
     * Return true if listeners is ignored.
     *
     * @return true if listeners is ignored.
     */
    @FxThread
    protected boolean isIgnoreListeners() {
        return ignoreListeners;
    }

    /**
     * Get the index.
     *
     * @return the index.
     */
    @FromAnyThread
    protected int getIndex() {
        return index;
    }

    /**
     * Set true if need to ignore listeners.
     *
     * @param ignoreListeners true if need to ignore listeners.
     */
    @FxThread
    protected void setIgnoreListeners(boolean ignoreListeners) {
        this.ignoreListeners = ignoreListeners;
    }

    /**
     * Get the editable control.
     *
     * @return the editable control.
     */
    @FxThread
    protected @NotNull E getEditableControl() {
        return notNull(editableControl);
    }

    /**
     * Get the interpolation combo box.
     *
     * @return the interpolation combo box.
     */
    @FxThread
    protected @NotNull ComboBox<Interpolation> getInterpolationComboBox() {
        return notNull(interpolationComboBox);
    }

    /**
     * Reload this element.
     */
    @FxThread
    public void reload() {

        var control = getControl();
        var influencer = control.getInfluencer();
        var newInterpolation = influencer.getInterpolation(getIndex());

        getInterpolationComboBox().getSelectionModel()
                .select(newInterpolation);
    }

    /**
     * Return true if need to have an editable label.
     *
     * @return true if need to have an editable label.
     */
    @FromAnyThread
    protected boolean isNeedEditableLabel() {
        return true;
    }
}
