package com.ss.editor.tonedog.emitter.control.tree.action.mesh;

import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ChangeConsumer;
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

    AbstractCreateParticleMeshAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
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

        final NodeTree<?> nodeTree = getNodeTree();

        final TreeNode<?> treeNode = getNode();
        final ParticleEmitterNode emitterNode = (ParticleEmitterNode) treeNode.getElement();
        final ParticleGeometry geometry = emitterNode.getParticleGeometry();
        final ParticleDataMeshInfo meshInfo = createMeshInfo();

        final ChangeConsumer changeConsumer = notNull(nodeTree.getChangeConsumer());
        changeConsumer.execute(new ChangeParticleMeshOperation(meshInfo, geometry));
    }

    /**
     * Create mesh info particle data mesh info.
     *
     * @return the particle data mesh info
     */
    protected abstract @NotNull ParticleDataMeshInfo createMeshInfo();
}
