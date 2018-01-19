package com.ss.editor.tonedog.emitter.control.tree.node;

import com.ss.editor.annotation.FxThread;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.tree.node.impl.spatial.NodeTreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.node.ParticleNode;

/**
 * The implementation of the {@link NodeTreeNode} for representing the {@link ParticleNode} in the editor.
 *
 * @author JavaSaBr
 */
public class ParticleNodeTreeNode extends NodeTreeNode<ParticleNode> {

    public ParticleNodeTreeNode(final ParticleNode element, final long objectId) {
        super(element, objectId);
    }

    @Override
    @FxThread
    public @Nullable Image getIcon() {
        return Icons.NODE_16;
    }

    @Override
    @FxThread
    public boolean canRemove() {
        return false;
    }

    @Override
    @FxThread
    public boolean canMove() {
        return false;
    }

    @Override
    @FxThread
    public boolean canEditName() {
        return false;
    }
}
