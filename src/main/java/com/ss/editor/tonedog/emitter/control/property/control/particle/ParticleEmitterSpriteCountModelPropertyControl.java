package com.ss.editor.tonedog.emitter.control.property.control.particle;

import com.jme3.math.Vector2f;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.property.impl.Vector2fPropertyControl;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.ParticleEmitterNode;

/**
 * The implementation of the {@link Vector2fPropertyControl} to edit sprite count of the {@link
 * ParticleEmitterNode}*.
 *
 * @author JavaSaBr
 */
public class ParticleEmitterSpriteCountModelPropertyControl extends
        Vector2fPropertyControl<ModelChangeConsumer, ParticleEmitterNode> {

    public ParticleEmitterSpriteCountModelPropertyControl(
            @NotNull Vector2f element,
            @NotNull String paramName,
            @NotNull ModelChangeConsumer modelChangeConsumer
    ) {
        super(element, paramName, modelChangeConsumer);
        setMinMax(1, Integer.MAX_VALUE);
    }
}
