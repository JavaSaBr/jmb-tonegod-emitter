package com.ss.editor.tonedog.emitter.control.model.tree.action.shape;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.jme3.scene.Mesh;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ChangeConsumer;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.plugin.api.dialog.GenericFactoryDialog;
import com.ss.editor.plugin.api.property.PropertyDefinition;
import com.ss.editor.tonedog.emitter.control.model.tree.action.operation.ChangeEmitterMeshOperation;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.rlib.util.VarTable;
import com.ss.rlib.util.array.Array;
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

    public AbstractCreateShapeEmitterAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
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
        final GenericFactoryDialog dialog = new GenericFactoryDialog(getPropertyDefinitions(), this::handleResult);
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
    private void handleResult(@NotNull final VarTable vars) {

        final TreeNode<?> treeNode = getNode();
        final ParticleEmitterNode element = (ParticleEmitterNode) treeNode.getElement();
        final Mesh shape = createMesh(vars);

        final NodeTree<?> nodeTree = getNodeTree();
        final ChangeConsumer changeConsumer = notNull(nodeTree.getChangeConsumer());
        changeConsumer.execute(new ChangeEmitterMeshOperation(shape, element));
    }

    /**
     * Get a list of property definitions to create a mesh.
     *
     * @return the list of definitions.
     */
    @FxThread
    protected abstract @NotNull Array<PropertyDefinition> getPropertyDefinitions();

    /**
     * Create a mesh.
     *
     * @param vars the table with variables.
     * @return the mesh
     */
    @FxThread
    protected abstract @NotNull Mesh createMesh(@NotNull final VarTable vars);
}