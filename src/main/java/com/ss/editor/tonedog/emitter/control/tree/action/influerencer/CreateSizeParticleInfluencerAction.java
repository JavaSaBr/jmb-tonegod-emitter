package com.ss.editor.tonedog.emitter.control.tree.action.influerencer;

import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.Messages;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.influencers.ParticleInfluencer;
import tonegod.emitter.influencers.impl.SizeInfluencer;

/**
 * The action to create a {@link SizeInfluencer} for a {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr
 */
public class CreateSizeParticleInfluencerAction extends AbstractCreateParticleInfluencerAction {

    public CreateSizeParticleInfluencerAction(
            @NotNull NodeTree<ModelChangeConsumer> nodeTree,
            @NotNull TreeNode<?> node
    ) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.PARTICLE_INFLUENCER_SIZE;
    }

    @Override
    @FxThread
    protected @NotNull ParticleInfluencer createInfluencer() {
        return new SizeInfluencer();
    }
}
