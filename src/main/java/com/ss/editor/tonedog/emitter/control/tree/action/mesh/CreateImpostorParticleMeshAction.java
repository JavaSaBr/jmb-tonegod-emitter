package com.ss.editor.tonedog.emitter.control.tree.action.mesh;

import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.geometry.ParticleGeometry;
import tonegod.emitter.particle.ParticleDataImpostorMesh;
import tonegod.emitter.particle.ParticleDataMeshInfo;

/**
 * The action to switch a particle mesh of the {@link ParticleGeometry} to {@link ParticleDataImpostorMesh}.
 *
 * @author JavaSaBr
 */
public class CreateImpostorParticleMeshAction extends AbstractCreateParticleMeshAction {

    public CreateImpostorParticleMeshAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.IMPOSTOR_16;
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_PARTICLE_EMITTER_PARTICLES_MESH_IMPOSTOR;
    }

    @Override
    protected @NotNull ParticleDataMeshInfo createMeshInfo() {
        return new ParticleDataMeshInfo(ParticleDataImpostorMesh.class, null);
    }
}
