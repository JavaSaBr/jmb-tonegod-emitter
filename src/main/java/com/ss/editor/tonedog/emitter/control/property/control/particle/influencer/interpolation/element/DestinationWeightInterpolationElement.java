package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.jme3.math.Vector3f;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.DestinationInfluencerControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.rlib.fx.control.input.FloatTextField;
import com.ss.rlib.fx.util.FxControlUtils;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.impl.DestinationInfluencer;

/**
 * The implementation of the element for editing vector values and interpolation.
 *
 * @author JavaSaBr
 */
public class DestinationWeightInterpolationElement extends
        InterpolationElement<DestinationInfluencer, Parent, DestinationInfluencerControl> {

    /**
     * The field X.
     */
    @Nullable
    private FloatTextField xField;

    /**
     * The field Y.
     */
    @Nullable
    private FloatTextField yField;

    /**
     * The field Z.
     */
    @Nullable
    private FloatTextField zField;

    /**
     * The weight field.
     */
    @Nullable
    private FloatTextField weightField;

    public DestinationWeightInterpolationElement(@NotNull DestinationInfluencerControl control, int index) {
        super(control, index);
    }

    @Override
    @FxThread
    protected @NotNull Parent createEditableControl() {

        var container = new HBox();
        container.prefWidthProperty().bind(widthProperty().multiply(0.5));

        xField = new FloatTextField();
        xField.prefWidthProperty()
                .bind(container.widthProperty().divide(0.25));

        yField = new FloatTextField();
        yField.prefWidthProperty()
                .bind(container.widthProperty().divide(0.25));

        zField = new FloatTextField();
        zField.prefWidthProperty()
                .bind(container.widthProperty().divide(0.25));

        weightField = new FloatTextField();
        weightField.prefWidthProperty()
                .bind(container.widthProperty().divide(0.25));

        FxControlUtils.onValueChange(xField, this::applyDestination);
        FxControlUtils.onValueChange(yField, this::applyDestination);
        FxControlUtils.onValueChange(zField, this::applyDestination);
        FxControlUtils.onValueChange(weightField, this::applyWeight);

        FxUtils.addClass(xField, yField,
                        CssClasses.ABSTRACT_PARAM_CONTROL_VECTOR3F_FIELD,
                        CssClasses.TRANSPARENT_TEXT_FIELD)
                .addClass(zField, weightField,
                        CssClasses.ABSTRACT_PARAM_CONTROL_VECTOR3F_FIELD,
                        CssClasses.TRANSPARENT_TEXT_FIELD)
                .addClass(container,
                        CssClasses.DEF_HBOX,
                        CssClasses.TEXT_INPUT_CONTAINER,
                        CssClasses.ABSTRACT_PARAM_CONTROL_SHORT_INPUT_CONTAINER);

        FxUtils.addChild(container, xField, yField, zField, weightField);

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

        var x = getXField().getValue();
        var y = getYField().getValue();
        var z = getZField().getValue();

        getControl().requestToChange(new Vector3f(x, y, z), getIndex());
    }

    /**
     * Apply the new weight value.
     */
    @FxThread
    private void applyWeight() {

        if (isIgnoreListeners()) {
            return;
        }

        var weight = weightField.getValue();

        getControl().requestToChange(weight, getIndex());
    }

    /**
     * Get the field X.
     *
     * @return the field X.
     */
    @FxThread
    private @NotNull FloatTextField getXField() {
        return notNull(xField);
    }

    /**
     * Get the field Y.
     *
     * @return the field Y.
     */
    @FxThread
    private @NotNull FloatTextField getYField() {
        return notNull(yField);
    }

    /**
     * Get the field Z.
     *
     * @return the field Z.
     */
    @FxThread
    private @NotNull FloatTextField getZField() {
        return notNull(zField);
    }

    /**
     * Get the weight field.
     *
     * @return the weight field.
     */
    @FxThread
    private @NotNull FloatTextField getWeightField() {
        return notNull(weightField);
    }

    @Override
    @FxThread
    public void reload() {

        var influencer = getControl().getInfluencer();

        var destination = influencer.getDestination(getIndex());
        var weight = influencer.getWeight(getIndex());

        var xField = getXField();
        xField.setValue(destination.getX());
        xField.positionCaret(xField.getText().length());

        var yField = getYField();
        yField.setValue(destination.getY());
        yField.positionCaret(yField.getText().length());

        var zField = getZField();
        zField.setValue(destination.getZ());
        zField.positionCaret(zField.getText().length());

        var weightField = getWeightField();
        weightField.setValue(weight);
        weightField.positionCaret(weightField.getText().length());

        super.reload();
    }
}
