package com.ss.editor.tonedog.emitter.control.tree.action.shape;

import static com.ss.editor.util.EditorUtil.getAssetFile;
import static com.ss.editor.util.EditorUtil.toAssetPath;
import static com.ss.rlib.util.ObjectUtils.notNull;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.ss.editor.FileExtensions;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.operation.ChangeEmitterMeshOperation;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.component.asset.tree.context.menu.action.DeleteFileAction;
import com.ss.editor.ui.component.asset.tree.context.menu.action.NewFileAction;
import com.ss.editor.ui.component.asset.tree.context.menu.action.RenameFileAction;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.action.AbstractNodeAction;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.editor.ui.util.UiUtils;
import com.ss.editor.util.EditorUtil;
import com.ss.editor.util.NodeUtils;
import com.ss.rlib.util.array.Array;
import com.ss.rlib.util.array.ArrayFactory;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;

import java.nio.file.Path;
import java.util.function.Predicate;

/**
 * The action for switching the emitter shape of the {@link ParticleEmitterNode} to {@link Spatial}.
 *
 * @author JavaSaBr
 */
public class LoadModelShapeEmitterAction extends AbstractNodeAction<ModelChangeConsumer> {

    private static final Predicate<Class<?>> ACTION_TESTER = type -> type == NewFileAction.class ||
            type == DeleteFileAction.class ||
            type == RenameFileAction.class;

    private static final Array<String> MODEL_EXTENSIONS = ArrayFactory.newArray(String.class);

    static {
        MODEL_EXTENSIONS.add(FileExtensions.JME_OBJECT);
    }

    public LoadModelShapeEmitterAction(@NotNull final NodeTree<?> nodeTree, @NotNull final TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.OPEN_FILE_16;
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_PARTICLE_EMITTER_MODEL_SHAPE;
    }

    @Override
    @FxThread
    protected void process() {
        UiUtils.openFileAssetDialog(this::processOpen, MODEL_EXTENSIONS, ACTION_TESTER);
    }

    /**
     * The process of opening file.
     *
     * @param file the file
     */
    protected void processOpen(@NotNull final Path file) {

        final NodeTree<ModelChangeConsumer> nodeTree = getNodeTree();
        final ModelChangeConsumer changeConsumer = notNull(nodeTree.getChangeConsumer());

        final Path assetFile = notNull(getAssetFile(file), "Not found asset file for " + file);
        final String assetPath = toAssetPath(assetFile);

        final AssetManager assetManager = EditorUtil.getAssetManager();
        final Spatial loadedModel = assetManager.loadModel(assetPath);
        final Geometry geometry = NodeUtils.findGeometry(loadedModel);

        if (geometry == null) {
            LOGGER.warning(this, "not found a geometry in the model " + assetPath);
            return;
        }

        final TreeNode<?> treeNode = getNode();
        final ParticleEmitterNode element = (ParticleEmitterNode) treeNode.getElement();

        changeConsumer.execute(new ChangeEmitterMeshOperation(geometry.getMesh(), element));
    }
}
