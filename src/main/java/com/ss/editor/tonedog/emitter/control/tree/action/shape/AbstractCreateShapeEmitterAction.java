package com.ss.editor.tonedog.emitter.control.tree.action.shape;

import com.jme3.scene.Mesh;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.plugin.api.dialog.GenericFactoryDialog;
import com.ss.editor.plugin.api.property.PropertyDefinition;
import com.ss.editor.tonedog.emitter.control.operation.ChangeEmitterMeshOperation;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.rlib.common.util.VarTable;
import com.ss.rlib.common.util.array.Array;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;

/**
 * The action to switch an emitter shape of the {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr
 */
public abstract class AbstractCreateShapeEmitterAction extends AbstractNodeAction<ModelChangeConsumer> {

    public AbstractCreateShapeEmitterAction(@NotNull NodeTree<?> nodeTree, @NotNull TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.GEOMETRY_16;
    }

    @Override
    @FxThread
    protected void process() {
        super.process();
        var dialog = new GenericFactoryDialog(getPropertyDefinitions(), this::handleResult);
        dialog.setTitle(getDialogTitle());
        dialog.show();
    }

    /**
     * Get the dialog title.
     *
     * @return the dialog title.
     */
    @FxThread
    protected abstract @NotNull String getDialogTitle();

    /**
     * Handle the result from the dialog.
     *
     * @param vars the table with variables.
     */
    @FxThread
    private void handleResult(@NotNull VarTable vars) {

        var treeNode = getNode();
        var element = (ParticleEmitterNode) treeNode.getElement();
        var shape = createMesh(vars);

        getNodeTree().requireChangeConsumer()
                .execute(new ChangeEmitterMeshOperation(shape, element));
    }

    /**
     * Get a list of property definitions.
     *
     * @return the list of definitions.
     */
    @FxThread
    protected abstract @NotNull Array<PropertyDefinition> getPropertyDefinitions();

    /**
     * Create a mesh.
     *
     * @param vars the table with variables.
     * @return the created mesh.
     */
    @FxThread
    protected abstract @NotNull Mesh createMesh(@NotNull final VarTable vars);
}
