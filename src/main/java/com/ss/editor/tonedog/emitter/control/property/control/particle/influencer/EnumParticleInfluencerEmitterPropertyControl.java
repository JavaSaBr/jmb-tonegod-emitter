package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer;

import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.property.impl.EnumPropertyControl;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The implementation of the {@link EnumPropertyControl} to edit the {@link Enum} values.
 *
 * @param <D> the type of an editing object.
 * @param <T> the type of an editing property.
 * @author JavaSaBr
 */
public class EnumParticleInfluencerEmitterPropertyControl<D extends ParticleInfluencer, T extends Enum<T>> extends
        EnumPropertyControl<ModelChangeConsumer, D, T> {

    public EnumParticleInfluencerEmitterPropertyControl(
            @NotNull T element,
            @NotNull String paramName,
            @NotNull ModelChangeConsumer modelChangeConsumer,
            @NotNull T[] availableValues,
            @NotNull Object parent
    ) {
        super(element, paramName, modelChangeConsumer, availableValues,
                ParticleInfluencerPropertyControl.newChangeHandler(parent));
    }
}
