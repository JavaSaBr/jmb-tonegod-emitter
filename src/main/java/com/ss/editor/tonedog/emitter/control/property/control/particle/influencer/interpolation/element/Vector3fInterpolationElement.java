package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.jme3.math.Vector3f;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.AbstractInterpolationInfluencerControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.rlib.fx.control.input.FloatTextField;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.InterpolatedParticleInfluencer;

/**
 * The implementation of the element for editing vector values and interpolation.
 *
 * @param <P> the type parameter
 * @param <C> the type parameter
 * @author JavaSaBr
 */
public class Vector3fInterpolationElement<P extends InterpolatedParticleInfluencer, C extends AbstractInterpolationInfluencerControl<P>>
        extends InterpolationElement<P, Parent, C> {

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

    public Vector3fInterpolationElement(@NotNull C control, int index) {
        super(control, index);
    }

    @Override
    @FxThread
    protected @NotNull Parent createEditableControl() {

        var container = new HBox();
        container.prefWidthProperty()
                .bind(widthProperty().multiply(0.4));

        xField = new FloatTextField();
        xField.setMinMax(getMinValue(), getMaxValue());
        xField.prefWidthProperty()
                .bind(container.widthProperty().multiply(0.33));

        yField = new FloatTextField();
        yField.setMinMax(getMinValue(), getMaxValue());
        yField.prefWidthProperty()
                .bind(container.widthProperty().multiply(0.33));

        zField = new FloatTextField();
        zField.setMinMax(getMinValue(), getMaxValue());
        zField.prefWidthProperty()
                .bind(container.widthProperty().multiply(0.33));

        xField.addChangeListener((observable, oldValue, newValue) -> processChange());
        yField.addChangeListener((observable, oldValue, newValue) -> processChange());
        zField.addChangeListener((observable, oldValue, newValue) -> processChange());

        FxUtils.addClass(xField, yField, zField,
                        CssClasses.ABSTRACT_PARAM_CONTROL_VECTOR3F_FIELD,
                        CssClasses.TRANSPARENT_TEXT_FIELD)
                .addClass(container,
                        CssClasses.DEF_HBOX,
                        CssClasses.TEXT_INPUT_CONTAINER,
                        CssClasses.ABSTRACT_PARAM_CONTROL_SHORT_INPUT_CONTAINER);

        FxUtils.addChild(container, xField, yField, zField);

        return container;
    }

    @Override
    @FxThread
    protected @NotNull String getEditableTitle() {
        return "xyz";
    }

    /**
     * Gets min value.
     *
     * @return the min available value.
     */
    @FxThread
    protected float getMinValue() {
        return Integer.MIN_VALUE;
    }

    /**
     * Gets max value.
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

        var x = getXField().getValue();
        var y = getYField().getValue();
        var z = getZField().getValue();

        requestToChange(x, y, z);
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
     * Request to change the vector value.
     *
     * @param x the x.
     * @param y the y.
     * @param z the z.
     */
    @FxThread
    protected void requestToChange(float x, float y, float z) {
    }

    /**
     * Reload this element.
     */
    @FxThread
    public void reload() {

        var influencer = getControl().getInfluencer();
        var value = getValue(influencer);

        var xField = getXField();
        xField.setValue(value.getX());
        xField.positionCaret(xField.getText().length());

        var yField = getYField();
        yField.setValue(value.getY());
        yField.positionCaret(yField.getText().length());

        var zField = getZField();
        zField.setValue(value.getZ());
        zField.positionCaret(zField.getText().length());

        super.reload();
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
