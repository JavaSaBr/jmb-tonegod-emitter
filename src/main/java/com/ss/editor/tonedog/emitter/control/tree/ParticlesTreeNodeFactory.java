package com.ss.editor.tonedog.emitter.control.tree;

import static com.ss.rlib.common.util.ClassUtils.unsafeCast;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.tree.node.ParticleEmitterNodeTreeNode;
import com.ss.editor.tonedog.emitter.control.tree.node.influencer.ParticleInfluencerTreeNode;
import com.ss.editor.tonedog.emitter.control.tree.node.influencer.ParticleInfluencersTreeNode;
import com.ss.editor.tonedog.emitter.model.ParticleInfluencers;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.editor.ui.control.tree.node.factory.TreeNodeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The implementation of a tree node factory to make toneg0d nodes.
 *
 * @author JavaSaBr
 */
public class ParticlesTreeNodeFactory implements TreeNodeFactory {

    @NotNull
    private static final ParticlesTreeNodeFactory INSTANCE = new ParticlesTreeNodeFactory();

    public static @NotNull ParticlesTreeNodeFactory getInstance() {
        return INSTANCE;
    }

    @Override
    @FxThread
    public <T, V extends TreeNode<T>> @Nullable V createFor(@Nullable final T element, final long objectId) {

        if (element instanceof ParticleEmitterNode) {
            return unsafeCast(new ParticleEmitterNodeTreeNode((ParticleEmitterNode) element, objectId));
        } else if (element instanceof ParticleInfluencers) {
            return unsafeCast(new ParticleInfluencersTreeNode((ParticleInfluencers) element, objectId));
        } else if (element instanceof ParticleInfluencer) {
            return unsafeCast(new ParticleInfluencerTreeNode((ParticleInfluencer) element, objectId));
        }

        return null;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
