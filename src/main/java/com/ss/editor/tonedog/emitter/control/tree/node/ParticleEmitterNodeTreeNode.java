package com.ss.editor.tonedog.emitter.control.tree.node;

import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.tree.action.mesh.CreateImpostorParticleMeshAction;
import com.ss.editor.tonedog.emitter.control.tree.action.mesh.CreatePointParticleMeshAction;
import com.ss.editor.tonedog.emitter.control.tree.action.mesh.CreateQuadParticleMeshAction;
import com.ss.editor.tonedog.emitter.control.tree.action.mesh.LoadModelParticlesMeshAction;
import com.ss.editor.tonedog.emitter.control.tree.action.shape.*;
import com.ss.editor.tonedog.emitter.model.ParticleInfluencers;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.ModelNodeTree;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.editor.ui.control.tree.node.impl.spatial.NodeTreeNode;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;

import java.util.Optional;

/**
 * The implementation of the {@link NodeTreeNode} to represent the {@link ParticleEmitterNode} in the editor.
 *
 * @author JavaSaBr
 */
public class ParticleEmitterNodeTreeNode extends NodeTreeNode<ParticleEmitterNode> {

    public ParticleEmitterNodeTreeNode(@NotNull ParticleEmitterNode element, long objectId) {
        super(element, objectId);
    }

    @Override
    @FxThread
    public @Nullable Image getIcon() {
        return Icons.PARTICLES_16;
    }

    @Override
    @FxThread
    protected @NotNull Optional<Menu> createToolMenu(@NotNull NodeTree<?> nodeTree) {
        return Optional.empty();
    }

    @Override
    @FxThread
    protected @NotNull Optional<Menu> createCreationMenu(@NotNull NodeTree<?> nodeTree) {
        return Optional.empty();
    }

    @Override
    @FxThread
    public void fillContextMenu(@NotNull NodeTree<?> nodeTree, @NotNull ObservableList<MenuItem> items) {

        if (!(nodeTree instanceof ModelNodeTree)) {
            return;
        }

        var changeMeshMenu = new Menu(Messages.MODEL_NODE_TREE_ACTION_PARTICLE_EMITTER_CHANGE_PARTICLES_MESH,
                new ImageView(Icons.MESH_16));

        var subItems = changeMeshMenu.getItems();
        subItems.add(new CreateQuadParticleMeshAction(nodeTree, this));
        subItems.add(new CreatePointParticleMeshAction(nodeTree, this));
        subItems.add(new CreateImpostorParticleMeshAction(nodeTree, this));
        subItems.add(new LoadModelParticlesMeshAction(nodeTree, this));

        var jmePrimitivesMenu = new Menu(Messages.MODEL_NODE_TREE_ACTION_CREATE_PRIMITIVE,
                new ImageView(Icons.GEOMETRY_16));

        var primitivesItems = jmePrimitivesMenu.getItems();
        primitivesItems.add(new CreateBoxShapeEmitterAction(nodeTree, this));
        primitivesItems.add(new CreateCylinderShapeEmitterAction(nodeTree, this));
        primitivesItems.add(new CreateDomeShapeEmitterAction(nodeTree, this));
        primitivesItems.add(new CreateQuadShapeEmitterAction(nodeTree, this));
        primitivesItems.add(new CreateSphereShapeEmitterAction(nodeTree, this));
        primitivesItems.add(new CreateTorusShapeEmitterAction(nodeTree, this));

        var changeShapeMenu = new Menu(Messages.MODEL_NODE_TREE_ACTION_PARTICLE_EMITTER_CHANGE_SHAPE,
                new ImageView(Icons.GEOMETRY_16));

        changeShapeMenu.getItems()
                .addAll(new CreateTriangleShapeEmitterAction(nodeTree, this), jmePrimitivesMenu,
                        new LoadModelShapeEmitterAction(nodeTree, this));

        items.add(changeShapeMenu);
        items.add(changeMeshMenu);

        super.fillContextMenu(nodeTree, items);
    }

    @Override
    @FxThread
    public @NotNull Array<TreeNode<?>> getChildren(@NotNull NodeTree<?> nodeTree) {

        var element = getElement();
        var emitterShape = element.getEmitterShape();

        var children = ArrayFactory.<TreeNode<?>>newArray(TreeNode.class);
        children.add(FACTORY_REGISTRY.createFor(new ParticleInfluencers(element)));
        children.add(FACTORY_REGISTRY.createFor(emitterShape.getMesh()));

        return children;
    }
}
