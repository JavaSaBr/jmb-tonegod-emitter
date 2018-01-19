package com.ss.editor.tonedog.emitter.control.operation;

import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.model.undo.impl.AbstractEditorOperation;
import com.ss.editor.tonedog.emitter.model.ParticleInfluencers;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The implementation of the {@link AbstractEditorOperation} to add a {@link ParticleInfluencer} to a {@link
 * ParticleEmitterNode}*.
 *
 * @author JavaSaBr.
 */
public class AddParticleInfluencerOperation extends AbstractEditorOperation<ModelChangeConsumer> {

    /**
     * The influencer.
     */
    @NotNull
    private final ParticleInfluencer influencer;

    /**
     * The parent.
     */
    @NotNull
    private final ParticleEmitterNode parent;

    public AddParticleInfluencerOperation(@NotNull final ParticleInfluencer influencer,
                                          @NotNull final ParticleEmitterNode parent) {
        this.influencer = influencer;
        this.parent = parent;
    }

    @Override
    @FxThread
    protected void redoImpl(@NotNull final ModelChangeConsumer editor) {
        EXECUTOR_MANAGER.addJmeTask(() -> {

            parent.killAllParticles();
            parent.addInfluencer(influencer);
            parent.emitAllParticles();

            EXECUTOR_MANAGER.addFxTask(() -> editor.notifyFxAddedChild(new ParticleInfluencers(parent),
                    influencer, -1, true));
        });
    }

    @Override
    @FxThread
    protected void undoImpl(@NotNull final ModelChangeConsumer editor) {
        EXECUTOR_MANAGER.addJmeTask(() -> {

            parent.killAllParticles();
            parent.removeInfluencer(influencer);
            parent.emitAllParticles();

            EXECUTOR_MANAGER.addFxTask(() -> editor.notifyFxRemovedChild(new ParticleInfluencers(parent), influencer));
        });
    }
}
