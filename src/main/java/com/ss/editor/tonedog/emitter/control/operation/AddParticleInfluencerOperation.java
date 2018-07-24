package com.ss.editor.tonedog.emitter.control.operation;

import com.ss.editor.annotation.FxThread;
import com.ss.editor.annotation.JmeThread;
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

    public AddParticleInfluencerOperation(@NotNull ParticleInfluencer influencer, @NotNull ParticleEmitterNode parent) {
        this.influencer = influencer;
        this.parent = parent;
    }

    @Override
    @JmeThread
    protected void redoInJme(@NotNull ModelChangeConsumer editor) {
        super.redoInJme(editor);
        parent.killAllParticles();
        parent.addInfluencer(influencer);
        parent.emitAllParticles();
    }

    @Override
    @FxThread
    protected void endRedoInFx(@NotNull ModelChangeConsumer editor) {
        super.endRedoInFx(editor);
        editor.notifyFxAddedChild(new ParticleInfluencers(parent), influencer, -1, true);
    }

    @Override
    @JmeThread
    protected void undoInJme(@NotNull ModelChangeConsumer editor) {
        super.undoInJme(editor);
        parent.killAllParticles();
        parent.removeInfluencer(influencer);
        parent.emitAllParticles();
    }

    @Override
    @FxThread
    protected void endUndoInFx(@NotNull ModelChangeConsumer editor) {
        super.endUndoInFx(editor);
        editor.notifyFxRemovedChild(new ParticleInfluencers(parent), influencer);
    }
}
