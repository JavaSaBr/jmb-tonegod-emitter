package com.ss.editor.tonedog.emitter.control.tree.action.influerencer;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.operation.RemoveParticleInfluencerOperation;
import com.ss.editor.tonedog.emitter.control.tree.node.influencer.ParticleInfluencerTreeNode;
import com.ss.editor.tonedog.emitter.control.tree.node.influencer.ParticleInfluencersTreeNode;
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
 * The action for to remove the {@link ParticleInfluencer} from the {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr
 */
public class RemoveParticleInfluencerAction extends AbstractNodeAction<ModelChangeConsumer> {

    public RemoveParticleInfluencerAction(@NotNull NodeTree<?> nodeTree, @NotNull TreeNode<?> node) {
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

        var node = (ParticleInfluencerTreeNode) getNode();
        var parent = (ParticleInfluencersTreeNode) notNull(node.getParent());
        var particleInfluencers = notNull(parent.getElement());

        var influencer = node.getElement();
        var emitterNode = particleInfluencers.getEmitterNode();
        var influencers = emitterNode.getInfluencers();
        var childIndex = influencers.indexOf(influencer);

        notNull(getNodeTree().getChangeConsumer())
                .execute(new RemoveParticleInfluencerOperation(influencer, emitterNode, childIndex));
    }
}
