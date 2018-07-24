package com.ss.editor.tonedog.emitter.control.operation;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.annotation.JmeThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.model.undo.impl.AbstractEditorOperation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.ParticleInfluencer;

import java.util.function.BiConsumer;

/**
 * The implementation of the {@link AbstractEditorOperation} to edit {@link ParticleInfluencer}.
 *
 * @param <D> the type of a {@link ParticleInfluencer}
 * @param <T> the type of an edited value.
 * @author JavaSaBr
 */
public class ParticleInfluencerPropertyOperation<D extends ParticleInfluencer, T> extends
        AbstractEditorOperation<ModelChangeConsumer> {

    /**
     * The property name.
     */
    @NotNull
    private final String propertyName;

    /**
     * The particle influencer.
     */
    @NotNull
    private final D influencer;

    /**
     * The parent of the infuencer.
     */
    @NotNull
    private final Object parent;

    /**
     * The new value of the property.
     */
    @Nullable
    private final T newValue;

    /**
     * The old value of the property.
     */
    @Nullable
    private final T oldValue;

    /**
     * The handler for applying new value.
     */
    @Nullable
    private BiConsumer<D, T> applyHandler;

    public ParticleInfluencerPropertyOperation(
            @NotNull D influencer,
            @NotNull Object parent,
            @NotNull String propertyName,
            @Nullable T newValue,
            @Nullable T oldValue
    ) {
        this.parent = parent;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.influencer = influencer;
        this.propertyName = propertyName;
    }

    /**
     * Set the apply handler.
     *
     * @param applyHandler the handler.
     */
    public void setApplyHandler(@NotNull BiConsumer<D, T> applyHandler) {
        this.applyHandler = applyHandler;
    }

    @Override
    @JmeThread
    protected void redoInJme(@NotNull ModelChangeConsumer editor) {
        super.redoInJme(editor);
        apply(influencer, newValue);
    }

    @Override
    @JmeThread
    protected void undoInJme(@NotNull ModelChangeConsumer editor) {
        super.undoInJme(editor);
        apply(influencer, oldValue);
    }

    @Override
    @FxThread
    protected void endInFx(@NotNull ModelChangeConsumer editor) {
        super.endInFx(editor);
        editor.notifyFxChangeProperty(parent, influencer, propertyName);
    }

    /**
     * Apply new value of the property to the model.
     *
     * @param spatial the spatial.
     * @param value   the value.
     */
    protected void apply(@NotNull D spatial, @Nullable T value) {
        notNull(applyHandler).accept(spatial, value);
    }
}