package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import com.ss.editor.Messages;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.PluginCss;
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
import tonegod.emitter.influencers.InterpolatedParticleInfluencer;
import tonegod.emitter.interpolation.Interpolation;
import tonegod.emitter.interpolation.InterpolationManager;

/**
 * The implementation of the element for {@link AbstractInterpolationInfluencerControl} for editing something and
 * interpolation.
 *
 * @param <P> the interpolated particle influencer's type.
 * @param <E> the node's type.
 * @param <C> the control's type.
 * @author JavaSaBr
 */
public abstract class InterpolationElement<P extends InterpolatedParticleInfluencer, E extends Node,
        C extends AbstractInterpolationInfluencerControl<P>> extends HBox {

    protected static final StringConverter<Interpolation> STRING_CONVERTER = new StringConverter<>() {

        @Override
        public String toString(@NotNull Interpolation object) {
            return object.getName();
        }

        @Override
        public Interpolation fromString(@NotNull String string) {
            return null;
        }
    };

    protected static final ObservableList<Interpolation> INTERPOLATIONS;

    static {
        INTERPOLATIONS = FXCollections.observableArrayList();
        INTERPOLATIONS.addAll(InterpolationManager.getAvailable());
    }

    /**
     * The parent control.
     */
    @NotNull
    protected final C control;

    /**
     * The editable control.
     */
    @NotNull
    protected final E editableControl;

    /**
     * The interpolation chooser.
     */
    @NotNull
    protected final ComboBox<Interpolation> interpolationComboBox;

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
        this.editableControl = createEditableControl();
        this.interpolationComboBox = new ComboBox<>();

        FxUtils.addClass(this,
                CssClasses.DEF_HBOX,
                PluginCss.PROPERTY_CONTROL_INFLUENCER_ELEMENT);
    }

    /**
     * Create components.
     */
    @FxThread
    public void createComponents() {

        Label editableLabel = null;

        if (isNeedEditableLabel()) {

            editableLabel = new Label(getEditableTitle() + ":");
            editableLabel.prefWidthProperty().bind(widthProperty().multiply(0.2));

            FxUtils.addClass(editableLabel, CssClasses.ABSTRACT_PARAM_CONTROL_PARAM_NAME_SINGLE_ROW);
        }

        var interpolationLabel = new Label(Messages.MODEL_PROPERTY_INTERPOLATION + ":");
        interpolationLabel.prefWidthProperty().bind(widthProperty().multiply(0.25));

        interpolationComboBox.setEditable(false);
        interpolationComboBox.setConverter(STRING_CONVERTER);
        interpolationComboBox.getItems().setAll(INTERPOLATIONS);
        interpolationComboBox.prefWidthProperty()
                .bind(widthProperty().multiply(0.35));

        FxControlUtils.onSelectedItemChange(interpolationComboBox, this::apply);

        FxUtils.addClass(interpolationLabel, CssClasses.ABSTRACT_PARAM_CONTROL_PARAM_NAME_SINGLE_ROW)
                .addClass(interpolationComboBox, CssClasses.PROPERTY_CONTROL_COMBO_BOX);

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
        if (!isIgnoreListeners()) {
            control.requestToChange(newValue, index);
        }
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
     * Reload this element.
     */
    @FxThread
    public void reload() {
        setIgnoreListeners(true);
        try {
            reloadImpl();
        } finally {
            setIgnoreListeners(false);
        }
    }

    @FxThread
    protected void reloadImpl() {

        var influencer = control.getInfluencer();
        var newInterpolation = influencer.getInterpolation(getIndex());

        interpolationComboBox.getSelectionModel()
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
