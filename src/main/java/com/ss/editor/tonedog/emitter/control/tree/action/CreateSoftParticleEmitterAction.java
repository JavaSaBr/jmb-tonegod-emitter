package com.ss.editor.tonedog.emitter.control.tree.action;

import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.PluginMessages;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.editor.util.EditorUtil;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.SoftParticleEmitterNode;

/**
 * The action for creating new {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr
 */
public class CreateSoftParticleEmitterAction extends CreateParticleEmitterAction {

    public CreateSoftParticleEmitterAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return PluginMessages.MODEL_NODE_TREE_ACTION_CREATE_SOFT_PARTICLE_EMITTER;
    }

    @Override
    @FxThread
    protected @NotNull ParticleEmitterNode createEmitterNode() {
        return new SoftParticleEmitterNode(EditorUtil.getAssetManager());
    }
}
