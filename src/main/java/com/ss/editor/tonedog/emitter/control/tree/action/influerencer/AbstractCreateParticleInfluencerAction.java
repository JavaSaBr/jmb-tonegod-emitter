package com.ss.editor.tonedog.emitter.control.tree.action.influerencer;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.operation.AddParticleInfluencerOperation;
import com.ss.editor.tonedog.emitter.model.ParticleInfluencers;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The action to create a {@link ParticleInfluencer} for a {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractCreateParticleInfluencerAction extends AbstractNodeAction<ModelChangeConsumer> {

    public AbstractCreateParticleInfluencerAction(
            @NotNull NodeTree<ModelChangeConsumer> nodeTree,
            @NotNull TreeNode<?> node
    ) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.INFLUENCER_16;
    }

    @Override
    @FxThread
    protected void process() {
        super.process();

        var element = (ParticleInfluencers) getNode().getElement();
        var emitterNode = element.getEmitterNode();
        var influencer = createInfluencer();

        notNull(getNodeTree().getChangeConsumer())
                .execute(new AddParticleInfluencerOperation(influencer, emitterNode));
    }

    /**
     * Create a particle influencer.
     *
     * @return the particle influencer.
     */
    @FxThread
    protected abstract @NotNull ParticleInfluencer createInfluencer();
}
