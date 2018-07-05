package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import com.jme3.math.Vector3f;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.DestinationInfluencerControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.rlib.fx.control.input.FloatTextField;
import com.ss.rlib.fx.util.FxControlUtils;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.DestinationInfluencer;

/**
 * The implementation of the element for editing vector values and interpolation.
 *
 * @author JavaSaBr
 */
public class DestinationWeightInterpolationElement extends
        InterpolationElement<DestinationInfluencer, HBox, DestinationInfluencerControl> {

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

    /**
     * The weight field.
     */
    @NotNull
    private final FloatTextField weightField;

    public DestinationWeightInterpolationElement(@NotNull DestinationInfluencerControl control, int index) {
        super(control, index);
        this.xField = new FloatTextField();
        this.yField = new FloatTextField();
        this.zField = new FloatTextField();
        this.weightField = new FloatTextField();
    }

    @Override
    @FxThread
    public void createComponents() {

        xField.prefWidthProperty()
                .bind(editableControl.widthProperty().divide(0.25));

        yField.prefWidthProperty()
                .bind(editableControl.widthProperty().divide(0.25));

        zField.prefWidthProperty()
                .bind(editableControl.widthProperty().divide(0.25));

        weightField.prefWidthProperty()
                .bind(editableControl.widthProperty().divide(0.25));

        FxControlUtils.onValueChange(xField, this::applyDestination);
        FxControlUtils.onValueChange(yField, this::applyDestination);
        FxControlUtils.onValueChange(zField, this::applyDestination);
        FxControlUtils.onValueChange(weightField, this::applyWeight);

        FxUtils.addClass(xField, yField,
                CssClasses.PROPERTY_CONTROL_VECTOR_3F_FIELD,
                CssClasses.TRANSPARENT_TEXT_FIELD)
                .addClass(zField, weightField,
                        CssClasses.PROPERTY_CONTROL_VECTOR_3F_FIELD,
                        CssClasses.TRANSPARENT_TEXT_FIELD)
                .addClass(editableControl,
                        CssClasses.DEF_HBOX,
                        CssClasses.TEXT_INPUT_CONTAINER,
                        CssClasses.ABSTRACT_PARAM_CONTROL_SHORT_INPUT_CONTAINER);

        FxUtils.addChild(editableControl, xField, yField, zField, weightField);

        super.createComponents();
    }

    @Override
    @FxThread
    protected @NotNull HBox createEditableControl() {

        var container = new HBox();
        container.prefWidthProperty()
                .bind(widthProperty().multiply(0.5));

        return container;
    }

    @Override
    @FxThread
    protected @NotNull String getEditableTitle() {
        return "xyzw";
    }

    /**
     * Apply the new destination value.
     */
    @FxThread
    private void applyDestination() {

        if (isIgnoreListeners()) {
            return;
        }

        var x = xField.getValue();
        var y = yField.getValue();
        var z =zField.getValue();

        control.requestToChange(new Vector3f(x, y, z), getIndex());
    }

    /**
     * Apply the new weight value.
     */
    @FxThread
    private void applyWeight() {
        if (!isIgnoreListeners()) {
            control.requestToChange(weightField.getValue(), getIndex());
        }
    }

    @Override
    @FxThread
    protected void reloadImpl() {

        var influencer = control.getInfluencer();

        var destination = influencer.getDestination(getIndex());
        var weight = influencer.getWeight(getIndex());

        xField.setValue(destination.getX());
        xField.positionCaret(xField.getText().length());

        yField.setValue(destination.getY());
        yField.positionCaret(yField.getText().length());

        zField.setValue(destination.getZ());
        zField.positionCaret(zField.getText().length());

        weightField.setValue(weight);
        weightField.positionCaret(weightField.getText().length());

        super.reloadImpl();
    }
}
