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
import tonegod.emitter.particle.ParticleDataMeshInfo;
import tonegod.emitter.particle.ParticleDataTriMesh;

/**
 * The action to switch a particle mesh of the {@link ParticleGeometry} to {@link ParticleDataTriMesh}.
 *
 * @author JavaSaBr
 */
public class CreateQuadParticleMeshAction extends AbstractCreateParticleMeshAction {

    public CreateQuadParticleMeshAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.QUAD_16;
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_PARTICLE_EMITTER_PARTICLES_MESH_QUAD;
    }

    @Override
    protected @NotNull ParticleDataMeshInfo createMeshInfo() {
        return new ParticleDataMeshInfo(ParticleDataTriMesh.class, null);
    }
}
