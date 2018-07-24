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
 * The implementation of the {@link AbstractEditorOperation} to remove a {@link ParticleInfluencer} from a {@link
 * ParticleEmitterNode}*.
 *
 * @author JavaSaBr.
 */
public class RemoveParticleInfluencerOperation extends AbstractEditorOperation<ModelChangeConsumer> {

    /**
     * The influencer to remove.
     */
    @NotNull
    private final ParticleInfluencer influencer;

    /**
     * The parent element.
     */
    @NotNull
    private final ParticleEmitterNode emitterNode;

    /**
     * The index of position in the influencers.
     */
    private final int childIndex;

    public RemoveParticleInfluencerOperation(
            @NotNull ParticleInfluencer influencer,
            @NotNull ParticleEmitterNode emitterNode,
            int childIndex
    ) {
        this.influencer = influencer;
        this.emitterNode = emitterNode;
        this.childIndex = childIndex;
    }

    @Override
    @JmeThread
    protected void redoInJme(@NotNull ModelChangeConsumer editor) {
        super.redoInJme(editor);
        emitterNode.killAllParticles();
        emitterNode.removeInfluencer(influencer);
        emitterNode.emitAllParticles();
    }

    @Override
    @FxThread
    protected void endRedoInFx(@NotNull ModelChangeConsumer editor) {
        super.endRedoInFx(editor);
        editor.notifyFxRemovedChild(new ParticleInfluencers(emitterNode), influencer);
    }

    @Override
    @JmeThread
    protected void undoInJme(@NotNull ModelChangeConsumer editor) {
        super.undoInJme(editor);
        emitterNode.killAllParticles();
        emitterNode.addInfluencer(influencer, childIndex);
        emitterNode.emitAllParticles();
    }

    @Override
    @FxThread
    protected void endUndoInFx(@NotNull ModelChangeConsumer editor) {
        super.endUndoInFx(editor);
        editor.notifyFxAddedChild(new ParticleInfluencers(emitterNode), influencer, childIndex, false);
    }
}
