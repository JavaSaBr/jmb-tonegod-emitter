package com.ss.editor.tonedog.emitter.control.tree.action.mesh;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.operation.ChangeParticleMeshOperation;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.tree.node.TreeNode;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.geometry.ParticleGeometry;
import tonegod.emitter.particle.ParticleDataMeshInfo;

/**
 * The action to switch a particle mesh of the {@link ParticleGeometry} to another mesh.
 *
 * @author JavaSaBr
 */
public abstract class AbstractCreateParticleMeshAction extends AbstractNodeAction<ModelChangeConsumer> {

    AbstractCreateParticleMeshAction(@NotNull NodeTree<?> nodeTree, @NotNull TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.MESH_16;
    }

    @FxThread
    @Override
    protected void process() {
        super.process();

        var nodeTree = getNodeTree();

        var treeNode = getNode();
        var emitterNode = (ParticleEmitterNode) treeNode.getElement();
        var geometry = emitterNode.getParticleGeometry();
        var meshInfo = createMeshInfo();

        notNull(nodeTree.getChangeConsumer())
                .execute(new ChangeParticleMeshOperation(meshInfo, geometry));
    }

    /**
     * Create a particle data mesh info.
     *
     * @return the particle data mesh info.
     */
    protected abstract @NotNull ParticleDataMeshInfo createMeshInfo();
}
