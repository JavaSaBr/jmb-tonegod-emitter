package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import com.jme3.math.Vector3f;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.RotationInfluencerControl;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.RotationInfluencer;

/**
 * The implementation of the element for {@link RotationInfluencer} for editing speeds and interpolation.
 *
 * @author JavaSaBr
 */
public class RotationInterpolationElement extends
        Vector3fInterpolationElement<RotationInfluencer, RotationInfluencerControl> {

    public RotationInterpolationElement(@NotNull RotationInfluencerControl control, int index) {
        super(control, index);
    }

    @Override
    @FxThread
    protected @NotNull String getEditableTitle() {
        return Messages.MODEL_PROPERTY_SPEED;
    }

    @Override
    @FxThread
    protected void requestToChange(float x, float y, float z) {
        getControl().requestToChange(new Vector3f(x, y, z), getIndex());
    }

    @Override
    @FxThread
    protected @NotNull Vector3f getValue(@NotNull RotationInfluencer influencer) {
        return influencer.getRotationSpeed(getIndex());
    }
}
