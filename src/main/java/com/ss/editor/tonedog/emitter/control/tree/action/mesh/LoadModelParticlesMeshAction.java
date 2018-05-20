package com.ss.editor.tonedog.emitter.control.tree.action.mesh;

import static com.ss.editor.util.EditorUtil.getAssetFile;
import static com.ss.rlib.common.util.ObjectUtils.notNull;
import com.jme3.asset.ModelKey;
import com.ss.editor.FileExtensions;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.operation.ChangeParticleMeshOperation;
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
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.particle.ParticleDataMeshInfo;
import tonegod.emitter.particle.ParticleDataTemplateMesh;

import java.nio.file.Path;
import java.util.function.Predicate;

/**
 * The action for switching the emitter shape of the {@link ParticleEmitterNode} to {@link ParticleDataTemplateMesh}.
 *
 * @author JavaSaBr
 */
public class LoadModelParticlesMeshAction extends AbstractNodeAction<ModelChangeConsumer> {

    private static final Predicate<Class<?>> ACTION_TESTER = type -> type == NewFileAction.class ||
            type == DeleteFileAction.class ||
            type == RenameFileAction.class;

    private static final Array<String> MODEL_EXTENSIONS = ArrayFactory.newArray(String.class);

    static {
        MODEL_EXTENSIONS.add(FileExtensions.JME_OBJECT);
    }

    public LoadModelParticlesMeshAction(@NotNull NodeTree<?> nodeTree, @NotNull TreeNode<?> node) {
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
        return Messages.MODEL_NODE_TREE_ACTION_PARTICLE_EMITTER_PARTICLES_MESH_MODEL;
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
    protected void processOpen(@NotNull Path file) {

        var assetFile = notNull(getAssetFile(file), "Not found asset file for " + file);
        var assetPath = EditorUtil.toAssetPath(assetFile);

        var modelKey = new ModelKey(assetPath);

        var assetManager = EditorUtil.getAssetManager();
        var loadedModel = assetManager.loadModel(modelKey);
        var geometry = NodeUtils.findGeometry(loadedModel);

        if (geometry == null) {
            LOGGER.warning(this, "not found a geometry in the model " + assetPath);
            return;
        }

        var emitterNode = (ParticleEmitterNode) getNode().getElement();
        var particleGeometry = emitterNode.getParticleGeometry();
        var meshInfo = new ParticleDataMeshInfo(ParticleDataTemplateMesh.class, geometry.getMesh());

        notNull(getNodeTree().getChangeConsumer())
                .execute(new ChangeParticleMeshOperation(meshInfo, particleGeometry));
    }
}
