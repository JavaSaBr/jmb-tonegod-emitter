package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.element;

import com.jme3.math.Vector3f;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.SizeInfluencerControl;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.impl.SizeInfluencer;

/**
 * The implementation of the element for {@link SizeInfluencer} for editing size and interpolation.
 *
 * @author JavaSaBr
 */
public class SizeInterpolationElement extends Vector3fInterpolationElement<SizeInfluencer, SizeInfluencerControl> {

    public SizeInterpolationElement(@NotNull SizeInfluencerControl control, int index) {
        super(control, index);
    }

    @Override
    @FxThread
    protected @NotNull String getEditableTitle() {
        return Messages.MODEL_PROPERTY_SIZE;
    }

    @Override
    @FxThread
    protected float getMaxValue() {
        return 1F;
    }

    @Override
    @FxThread
    protected float getMinValue() {
        return 0F;
    }

    @Override
    @FxThread
    protected void requestToChange(float x, float y, float z) {
        control.requestToChange(new Vector3f(x, y, z), getIndex());
    }

    @Override
    @FxThread
    protected @NotNull Vector3f getValue(@NotNull SizeInfluencer influencer) {
        return influencer.getSize(getIndex());
    }
}
