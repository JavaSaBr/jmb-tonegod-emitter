package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer;

import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.property.impl.FloatPropertyControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The implementation of the {@link FloatPropertyControl} to edit float values in the {@link
 * ParticleInfluencer}*.
 *
 * @param <D> the type of an editing object.
 * @author JavaSaBr
 */
public class FloatParticleInfluencerPropertyControl<D extends ParticleInfluencer> extends
        FloatPropertyControl<ModelChangeConsumer, D> {

    public FloatParticleInfluencerPropertyControl(
            @Nullable Float element,
            @NotNull String paramName,
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull Object parent
    ) {
        super(element, paramName, modelChangeConsumer, ParticleInfluencerPropertyControl.newChangeHandler(parent));
    }
}
