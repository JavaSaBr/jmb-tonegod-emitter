package com.ss.editor.tonedog.emitter.control.tree.action.influerencer;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.tree.node.influencer.ParticleInfluencerTreeNode;
import com.ss.editor.tonedog.emitter.control.tree.node.influencer.ParticleInfluencersTreeNode;
import com.ss.editor.tonedog.emitter.control.operation.RemoveParticleInfluencerOperation;
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

import java.util.List;

/**
 * The action for to remove the {@link ParticleInfluencer} from the {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr
 */
public class RemoveParticleInfluencerAction extends AbstractNodeAction<ModelChangeConsumer> {

    public RemoveParticleInfluencerAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.REMOVE_12;
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_REMOVE;
    }

    @Override
    @FxThread
    protected void process() {

        final ParticleInfluencerTreeNode node = (ParticleInfluencerTreeNode) getNode();
        final ParticleInfluencersTreeNode parent = (ParticleInfluencersTreeNode) notNull(node.getParent());
        final ParticleInfluencers particleInfluencers = notNull(parent.getElement());

        final ParticleInfluencer influencer = node.getElement();
        final ParticleEmitterNode emitterNode = particleInfluencers.getEmitterNode();
        final List<ParticleInfluencer> influencers = emitterNode.getInfluencers();
        final int childIndex = influencers.indexOf(influencer);

        final NodeTree<ModelChangeConsumer> nodeTree = getNodeTree();
        final ModelChangeConsumer changeConsumer = notNull(nodeTree.getChangeConsumer());
        changeConsumer.execute(new RemoveParticleInfluencerOperation(influencer, emitterNode, childIndex));
    }
}
