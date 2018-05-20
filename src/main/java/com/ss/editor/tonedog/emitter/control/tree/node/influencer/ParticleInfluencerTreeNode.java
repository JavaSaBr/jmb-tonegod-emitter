package com.ss.editor.tonedog.emitter.control.tree.node.influencer;

import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.tree.action.influerencer.RemoveParticleInfluencerAction;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import javafx.collections.ObservableList;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.ParticleInfluencer;

/**
 * The implementation of the {@link TreeNode} for representing the {@link ParticleInfluencer} in the editor.
 *
 * @author JavaSaBr
 */
public class ParticleInfluencerTreeNode extends TreeNode<ParticleInfluencer> {

    public ParticleInfluencerTreeNode(@NotNull ParticleInfluencer element, long objectId) {
        super(element, objectId);
    }

    @Override
    @FxThread
    public @Nullable Image getIcon() {
        return Icons.INFLUENCER_16;
    }

    @Override
    @FxThread
    public void fillContextMenu(@NotNull NodeTree<?> nodeTree, @NotNull ObservableList<MenuItem> items) {
        items.add(new RemoveParticleInfluencerAction(nodeTree, this));
        super.fillContextMenu(nodeTree, items);
    }

    @Override
    @FromAnyThread
    public @NotNull String getName() {
        return getElement().getName();
    }
}
