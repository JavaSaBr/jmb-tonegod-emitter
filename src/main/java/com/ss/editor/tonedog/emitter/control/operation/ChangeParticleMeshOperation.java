package com.ss.editor.tonedog.emitter.control.operation;

import com.ss.editor.annotation.JmeThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.model.undo.impl.AbstractEditorOperation;
import com.ss.editor.util.NodeUtils;
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
    @JmeThread
    protected void redoInJme(@NotNull ModelChangeConsumer editor) {
        super.redoInJme(editor);
        switchInfo();
    }

    @Override
    @JmeThread
    protected void undoInJme(@NotNull ModelChangeConsumer editor) {
        super.undoInJme(editor);
        switchInfo();
    }

    @JmeThread
    private void switchInfo() {

        var emitterNode = NodeUtils.<ParticleEmitterNode>findParent(geometry, ParticleEmitterNode.class::isInstance);
        if (emitterNode == null) {
            return;
        }

        var newInfo = prevInfo;
        prevInfo = emitterNode.getParticleMeshType();
        emitterNode.changeParticleMeshType(newInfo);
    }
}
