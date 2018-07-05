package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control;

import com.jme3.math.Vector3f;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.InterpolationElement;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element.RotationInterpolationElement;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.RotationInfluencer;
import tonegod.emitter.interpolation.Interpolation;

/**
 * The control for editing sizes in the {@link RotationInfluencer}.
 *
 * @author JavaSaBr
 */
public class RotationInfluencerControl extends AbstractInterpolationInfluencerControl<RotationInfluencer> {

    public RotationInfluencerControl(
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull RotationInfluencer influencer,
            @NotNull Object parent
    ) {
        super(modelChangeConsumer, influencer, parent);
    }

    @Override
    @FxThread
    protected @NotNull String getControlTitle() {
        return Messages.MODEL_PROPERTY_ROTATION_INTERPOLATION;
    }

    @Override
    @FxThread
    protected int getMinElements() {
        return 1;
    }

    /**
     * Request to change.
     *
     * @param newValue the new value.
     * @param index    the index.
     */
    @FxThread
    public void requestToChange(@NotNull Vector3f newValue, int index) {

        var oldValue = influencer.getRotationSpeed(index);

        execute(newValue, oldValue, (rotationInfluencer, alpha) ->
                rotationInfluencer.updateRotationSpeed(alpha, index));
    }

    @Override
    @FxThread
    protected @NotNull InterpolationElement<?, ?, ?> createElement(int i) {
        return new RotationInterpolationElement(this, i);
    }

    @Override
    @FxThread
    protected void processAdd() {
        execute(true, false, (influencer, needAdd) -> {
            if (needAdd) {
                influencer.addRotationSpeed(Vector3f.UNIT_Z, Interpolation.LINEAR);
            } else {
                influencer.removeLast();
            }
        });
    }

    @Override
    @FxThread
    protected void processRemove() {

        var speeds = influencer.getRotationSpeeds();

        var speed = influencer.getRotationSpeed(speeds.size() - 1);
        var interpolation = influencer.getInterpolation(speeds.size() - 1);

        execute(true, false, (rotationInfluencer, needRemove) -> {
            if (needRemove) {
                rotationInfluencer.removeLast();
            } else {
                rotationInfluencer.addRotationSpeed(speed, interpolation);
            }
        });
    }
}
