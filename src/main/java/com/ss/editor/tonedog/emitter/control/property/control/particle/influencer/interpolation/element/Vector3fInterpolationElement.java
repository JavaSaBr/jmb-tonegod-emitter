package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import com.jme3.math.Vector3f;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.AbstractInterpolationInfluencerControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.rlib.fx.control.input.FloatTextField;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.InterpolatedParticleInfluencer;

/**
 * The implementation of the element for editing vector values and interpolation.
 *
 * @param <P> the type parameter
 * @param <C> the type parameter
 * @author JavaSaBr
 */
public class Vector3fInterpolationElement<P extends InterpolatedParticleInfluencer, C extends AbstractInterpolationInfluencerControl<P>>
        extends InterpolationElement<P, HBox, C> {

    /**
     * The field X.
     */
    @NotNull
    private final FloatTextField xField;

    /**
     * The field Y.
     */
    @NotNull
    private final FloatTextField yField;

    /**
     * The field Z.
     */
    @NotNull
    private final FloatTextField zField;

    public Vector3fInterpolationElement(@NotNull C control, int index) {
        super(control, index);
        this.xField = new FloatTextField();
        this.yField = new FloatTextField();
        this.zField = new FloatTextField();
    }

    @Override
    public void createComponents() {

        xField.setMinMax(getMinValue(), getMaxValue());
        xField.prefWidthProperty()
                .bind(editableControl.widthProperty().multiply(0.33));

        yField.setMinMax(getMinValue(), getMaxValue());
        yField.prefWidthProperty()
                .bind(editableControl.widthProperty().multiply(0.33));

        zField.setMinMax(getMinValue(), getMaxValue());
        zField.prefWidthProperty()
                .bind(editableControl.widthProperty().multiply(0.33));

        xField.addChangeListener((observable, oldValue, newValue) -> processChange());
        yField.addChangeListener((observable, oldValue, newValue) -> processChange());
        zField.addChangeListener((observable, oldValue, newValue) -> processChange());

        FxUtils.addClass(xField, yField, zField,
                CssClasses.PROPERTY_CONTROL_VECTOR_3F_FIELD,
                CssClasses.TRANSPARENT_TEXT_FIELD)
                .addClass(editableControl,
                        CssClasses.DEF_HBOX,
                        CssClasses.TEXT_INPUT_CONTAINER,
                        CssClasses.ABSTRACT_PARAM_CONTROL_SHORT_INPUT_CONTAINER);

        FxUtils.addChild(editableControl, xField, yField, zField);

        super.createComponents();
    }

    @Override
    @FxThread
    protected @NotNull HBox createEditableControl() {

        var container = new HBox();
        container.prefWidthProperty()
                .bind(widthProperty().multiply(0.4));

        return container;
    }

    @Override
    @FxThread
    protected @NotNull String getEditableTitle() {
        return "xyz";
    }

    /**
     * Get the min value.
     *
     * @return the min available value.
     */
    @FxThread
    protected float getMinValue() {
        return Integer.MIN_VALUE;
    }

    /**
     * Get the max value.
     *
     * @return the max available value.
     */
    @FxThread
    protected float getMaxValue() {
        return Integer.MAX_VALUE;
    }

    /**
     * Handle changing vector value.
     */
    @FxThread
    private void processChange() {

        if (isIgnoreListeners()) {
            return;
        }

        var x = xField.getValue();
        var y = yField.getValue();
        var z = xField.getValue();

        requestToChange(x, y, z);
    }

    /**
     * Request to change the vector value.
     *
     * @param x the x.
     * @param y the y.
     * @param z the z.
     */
    @FxThread
    protected void requestToChange(float x, float y, float z) {
    }

    @Override
    @FxThread
    protected void reloadImpl() {

        var influencer = control.getInfluencer();
        var value = getValue(influencer);

        xField.setValue(value.getX());
        xField.positionCaret(xField.getText().length());

        yField.setValue(value.getY());
        yField.positionCaret(yField.getText().length());

        zField.setValue(value.getZ());
        zField.positionCaret(zField.getText().length());

        super.reloadImpl();
    }

    /**
     * Get a vector value from the influencer.
     *
     * @param influencer the influencer.
     * @return the value.
     */
    @FxThread
    protected @NotNull Vector3f getValue(@NotNull P influencer) {
        throw new UnsupportedOperationException();
    }
}
