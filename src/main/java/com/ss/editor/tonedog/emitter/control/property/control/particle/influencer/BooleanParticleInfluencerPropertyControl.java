package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer;

import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.property.impl.BooleanPropertyControl;
import com.ss.editor.ui.control.property.impl.FloatPropertyControl;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The implementation of the {@link FloatPropertyControl} to edit boolean values in the {@link
 * ParticleInfluencer}*.
 *
 * @param <D> the type of an editing object.
 * @author JavaSaBr
 */
public class BooleanParticleInfluencerPropertyControl<D extends ParticleInfluencer> extends
        BooleanPropertyControl<ModelChangeConsumer, D> {

    public BooleanParticleInfluencerPropertyControl(
            @NotNull Boolean element,
            @NotNull String paramName,
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull Object parent
    ) {
        super(element, paramName, modelChangeConsumer, ParticleInfluencerPropertyControl.newChangeHandler(parent));
    }
}
