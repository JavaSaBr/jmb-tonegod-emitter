package com.ss.editor.tonedog.emitter.control.operation;

import com.jme3.scene.Mesh;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.annotation.JmeThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.model.undo.impl.AbstractEditorOperation;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.ParticleEmitterNode;

/**
 * The implementation of the {@link AbstractEditorOperation} for changing a shape in the {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr.
 */
public class ChangeEmitterMeshOperation extends AbstractEditorOperation<ModelChangeConsumer> {

    /**
     * The emitter node.
     */
    @NotNull
    private final ParticleEmitterNode emitterNode;

    /**
     * The prev shape.
     */
    @NotNull
    private volatile Mesh prevShape;

    public ChangeEmitterMeshOperation(@NotNull Mesh newShape, @NotNull ParticleEmitterNode emitterNode) {
        this.prevShape = newShape;
        this.emitterNode = emitterNode;
    }

    @Override
    @JmeThread
    protected void redoInJme(@NotNull ModelChangeConsumer editor) {
        super.redoInJme(editor);
        switchShape();
    }

    @Override
    @JmeThread
    protected void undoInJme(@NotNull ModelChangeConsumer editor) {
        super.undoInJme(editor);
        switchShape();
    }

    @FxThread
    @Override
    protected void endInFx(@NotNull ModelChangeConsumer editor) {
        super.endInFx(editor);
        var emitterMesh = emitterNode.getEmitterShape();
        editor.notifyFxReplaced(emitterNode, emitterMesh, emitterMesh, true, true);
    }

    @JmeThread
    private void switchShape() {
        var emitterMesh = emitterNode.getEmitterShape();
        var newShape = prevShape;
        prevShape = emitterMesh.getMesh();
        emitterNode.changeEmitterShapeMesh(newShape);
    }
}
