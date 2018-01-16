package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.AbstractInterpolationInfluencerControl;
import com.ss.editor.ui.css.CSSClasses;
import com.ss.rlib.ui.util.FXUtils;
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

    public InterpolationElement(@NotNull final C control, final int index) {
        this.control = control;
        this.index = index;
        createComponents();
        setIgnoreListeners(true);
        reload();
        setIgnoreListeners(false);
        FXUtils.addClassesTo(this, CSSClasses.DEF_HBOX, CSSClasses.ABSTRACT_PARAM_CONTROL_INFLUENCER_ELEMENT);
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

            FXUtils.addClassTo(editableLabel, CSSClasses.ABSTRACT_PARAM_CONTROL_PARAM_NAME_SINGLE_ROW);
        }

        editableControl = createEditableControl();

        final Label interpolationLabel = new Label(Messages.MODEL_PROPERTY_INTERPOLATION + ":");
        interpolationLabel.prefWidthProperty().bind(widthProperty().multiply(0.25));

        interpolationComboBox = new ComboBox<>();
        interpolationComboBox.setEditable(false);
        interpolationComboBox.prefWidthProperty().bind(widthProperty().multiply(0.35));
        interpolationComboBox.setConverter(STRING_CONVERTER);
        interpolationComboBox.getItems().setAll(INTERPOLATIONS);
        interpolationComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> processChange(newValue));

        if (editableLabel != null) {
            FXUtils.addToPane(editableLabel, this);
        }

        FXUtils.addToPane(editableControl, this);
        FXUtils.addToPane(interpolationLabel, this);
        FXUtils.addToPane(interpolationComboBox, this);

        FXUtils.addClassTo(interpolationLabel, CSSClasses.ABSTRACT_PARAM_CONTROL_PARAM_NAME_SINGLE_ROW);
        FXUtils.addClassTo(interpolationComboBox, CSSClasses.ABSTRACT_PARAM_CONTROL_COMBO_BOX);
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
     * Create editable control.
     *
     * @return the e
     */
    @FxThread
    protected abstract E createEditableControl();

    /**
     * Process change.
     *
     * @param newValue the new value
     */
    @FxThread
    protected void processChange(@NotNull final Interpolation newValue) {
        if (isIgnoreListeners()) return;
        final C control = getControl();
        control.requestToChange(newValue, index);
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
     * Is ignore listeners boolean.
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
     * Sets ignore listeners.
     *
     * @param ignoreListeners the flag for ignoring listeners.
     */
    @FxThread
    protected void setIgnoreListeners(final boolean ignoreListeners) {
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

        final C control = getControl();
        final P influencer = control.getInfluencer();

        final Interpolation newInterpolation = influencer.getInterpolation(getIndex());
        final ComboBox<Interpolation> interpolationComboBox = getInterpolationComboBox();
        interpolationComboBox.getSelectionModel().select(newInterpolation);
    }

    /**
     * Is need editable label boolean.
     *
     * @return the boolean
     */
    @FromAnyThread
    public boolean isNeedEditableLabel() {
        return true;
    }
}
