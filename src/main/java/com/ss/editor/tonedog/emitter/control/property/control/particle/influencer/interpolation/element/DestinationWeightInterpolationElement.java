package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.jme3.math.Vector3f;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.DestinationInfluencerControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.rlib.fx.control.input.FloatTextField;
import com.ss.rlib.fx.util.FXUtils;
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
public class DestinationWeightInterpolationElement extends InterpolationElement<DestinationInfluencer, Parent, DestinationInfluencerControl> {

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

    public DestinationWeightInterpolationElement(@NotNull final DestinationInfluencerControl control, final int index) {
        super(control, index);
    }

    @Override
    @FxThread
    protected @NotNull Parent createEditableControl() {

        final HBox container = new HBox();
        container.prefWidthProperty().bind(widthProperty().multiply(0.5));

        xField = new FloatTextField();
        xField.prefWidthProperty().bind(container.widthProperty().divide(0.25));
        xField.addChangeListener((observable, oldValue, newValue) -> processDestinationChange());

        yField = new FloatTextField();
        yField.prefWidthProperty().bind(container.widthProperty().divide(0.25));
        yField.addChangeListener((observable, oldValue, newValue) -> processDestinationChange());

        zField = new FloatTextField();
        zField.prefWidthProperty().bind(container.widthProperty().divide(0.25));
        zField.addChangeListener((observable, oldValue, newValue) -> processDestinationChange());

        weightField = new FloatTextField();
        weightField.prefWidthProperty().bind(container.widthProperty().divide(0.25));
        weightField.addChangeListener((observable, oldValue, newValue) -> processWeightChange());

        FXUtils.addToPane(xField, container);
        FXUtils.addToPane(yField, container);
        FXUtils.addToPane(zField, container);
        FXUtils.addToPane(weightField, container);

        FXUtils.addClassesTo(xField, yField, zField, weightField, CssClasses.ABSTRACT_PARAM_CONTROL_VECTOR3F_FIELD,
                CssClasses.TRANSPARENT_TEXT_FIELD);
        FXUtils.addClassesTo(container, CssClasses.DEF_HBOX, CssClasses.TEXT_INPUT_CONTAINER,
                CssClasses.ABSTRACT_PARAM_CONTROL_SHORT_INPUT_CONTAINER);

        return container;
    }

    @Override
    @FxThread
    protected @NotNull String getEditableTitle() {
        return "xyzw";
    }

    /**
     * Handle changing destination value.
     */
    @FxThread
    private void processDestinationChange() {
        if (isIgnoreListeners()) return;

        final float x = getXField().getValue();
        final float y = getYField().getValue();
        final float z = getZField().getValue();

        final DestinationInfluencerControl control = getControl();
        control.requestToChange(new Vector3f(x, y, z), getIndex());
    }

    /**
     * Handle changing weight value.
     */
    @FxThread
    private void processWeightChange() {
        if (isIgnoreListeners()) return;

        final float weight = weightField.getValue();

        final DestinationInfluencerControl control = getControl();
        control.requestToChange(weight, getIndex());
    }

    /**
     * @return the field X.
     */
    @FxThread
    private @NotNull FloatTextField getXField() {
        return notNull(xField);
    }

    /**
     * @return the field Y.
     */
    @FxThread
    private @NotNull FloatTextField getYField() {
        return notNull(yField);
    }

    /**
     * @return the field Z.
     */
    @FxThread
    private @NotNull FloatTextField getZField() {
        return notNull(zField);
    }

    /**
     * @return the weight field.
     */
    @FxThread
    private @NotNull FloatTextField getWeightField() {
        return notNull(weightField);
    }

    @Override
    @FxThread
    public void reload() {

        final DestinationInfluencerControl control = getControl();
        final DestinationInfluencer influencer = control.getInfluencer();

        final Vector3f destination = influencer.getDestination(getIndex());
        final Float weight = influencer.getWeight(getIndex());

        final FloatTextField xField = getXField();
        xField.setValue(destination.getX());
        xField.positionCaret(xField.getText().length());

        final FloatTextField yField = getYField();
        yField.setValue(destination.getY());
        yField.positionCaret(yField.getText().length());

        final FloatTextField zField = getZField();
        zField.setValue(destination.getZ());
        zField.positionCaret(zField.getText().length());

        final FloatTextField weightField = getWeightField();
        weightField.setValue(weight);
        weightField.positionCaret(weightField.getText().length());

        super.reload();
    }
}
