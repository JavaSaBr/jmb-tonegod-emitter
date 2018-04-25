package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer;

import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.property.impl.IntArrayPropertyControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The implementation of the {@link IntArrayPropertyControl} to edit int array values in the {@link
 * ParticleInfluencer}*.
 *
 * @param <D> the type of an editing object.
 * @author JavaSaBr
 */
public class IntArrayParticleInfluencerPropertyControl<D extends ParticleInfluencer> extends
        IntArrayPropertyControl<ModelChangeConsumer, D> {

    public IntArrayParticleInfluencerPropertyControl(
            @Nullable int[] element,
            @NotNull String paramName,
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull Object parent
    ) {
        super(element, paramName, modelChangeConsumer, ParticleInfluencerPropertyControl.newChangeHandler(parent));
    }
}
