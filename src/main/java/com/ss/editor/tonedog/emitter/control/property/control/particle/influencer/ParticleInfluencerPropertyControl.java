package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer;

import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.operation.ParticleInfluencerPropertyOperation;
import com.ss.editor.ui.component.editor.impl.model.ModelFileEditor;
import com.ss.editor.ui.control.property.PropertyControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The base implementation of the property control for the {@link ModelFileEditor}.
 *
 * @param <D> the type of an editing object.
 * @param <T> the type of an editing property.
 * @author JavaSaBr
 */
public class ParticleInfluencerPropertyControl<D extends ParticleInfluencer, T> extends
        PropertyControl<ModelChangeConsumer, D, T> {

    /**
     * Create a new change handler.
     *
     * @param <D> the type of an editing object.
     * @param <T> the type of an editing property.
     * @param parent the parent.
     * @return the new change handler.
     */
    public static <D extends ParticleInfluencer, T> @NotNull ChangeHandler<ModelChangeConsumer, D, T> newChangeHandler(
            @NotNull Object parent
    ) {
        return (changeConsumer, object, propName, newValue, oldValue, handler) -> {

            var operation = new ParticleInfluencerPropertyOperation<D, T>(object, parent, propName, newValue, oldValue);
            operation.setApplyHandler(handler);

            changeConsumer.execute(operation);
        };
    }

    public ParticleInfluencerPropertyControl(
            @Nullable T propertyValue,
            @NotNull String propertyName,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull Object parent
    ) {
        super(propertyValue, propertyName, changeConsumer, newChangeHandler(parent));
    }
}
