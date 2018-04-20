package com.ss.editor.tonedog.emitter.control.operation;

import static com.ss.editor.util.NodeUtils.findParent;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.annotation.JmeThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.model.undo.impl.AbstractEditorOperation;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.geometry.ParticleGeometry;
import tonegod.emitter.particle.ParticleDataMeshInfo;

/**
 * The implementation of the {@link AbstractEditorOperation} for changing a particle mesh in the {@link
 * ParticleGeometry}*.
 *
 * @author JavaSaBr.
 */
public class ChangeParticleMeshOperation extends AbstractEditorOperation<ModelChangeConsumer> {

    /**
     * The geometry.
     */
    @NotNull
    private final ParticleGeometry geometry;

    /**
     * The prev shape.
     */
    @NotNull
    private volatile ParticleDataMeshInfo prevInfo;

    public ChangeParticleMeshOperation(@NotNull ParticleDataMeshInfo newInfo, @NotNull ParticleGeometry geometry) {
        this.prevInfo = newInfo;
        this.geometry = geometry;
    }

    @Override
    @FxThread
    protected void redoImpl(@NotNull ModelChangeConsumer editor) {
        EXECUTOR_MANAGER.addJmeTask(() -> switchInfo(editor));
    }

    @JmeThread
    private void switchInfo(@NotNull ModelChangeConsumer editor) {

        ParticleEmitterNode emitterNode = findParent(geometry, ParticleEmitterNode.class::isInstance);
        if (emitterNode == null) {
            return;
        }

        var newInfo = prevInfo;
        prevInfo = emitterNode.getParticleMeshType();
        emitterNode.changeParticleMeshType(newInfo);
    }

    @Override
    @FxThread
    protected void undoImpl(@NotNull ModelChangeConsumer editor) {
        EXECUTOR_MANAGER.addJmeTask(() -> switchInfo(editor));
    }
}
