package com.ss.editor.tonedog.emitter.control.tree.node.influencer;

import com.ss.editor.Messages;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.tree.action.influerencer.*;
import com.ss.editor.tonedog.emitter.model.ParticleInfluencers;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.ModelNodeTree;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.rlib.common.util.ClassUtils;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.dictionary.ObjectDictionary;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.ParticleInfluencer;
import tonegod.emitter.influencers.impl.*;

import java.lang.reflect.Constructor;

/**
 * The implementation of the {@link TreeNode} for representing the {@link ParticleInfluencers} in the editor.
 *
 * @author JavaSaBr
 */
public class ParticleInfluencersTreeNode extends TreeNode<ParticleInfluencers> {

    private static final ObjectDictionary<Class<? extends ParticleInfluencer>, Constructor<? extends MenuItem>> CONSTRUCTORS = ObjectDictionary.of(
            AlphaInfluencer.class, ClassUtils.requireConstructor(CreateAlphaParticleInfluencerAction.class, NodeTree.class, TreeNode.class),
            ColorInfluencer.class, ClassUtils.requireConstructor(CreateColorParticleInfluencerAction.class, NodeTree.class, TreeNode.class),
            DestinationInfluencer.class, ClassUtils.requireConstructor(CreateDestinationParticleInfluencerAction.class, NodeTree.class, TreeNode.class),
            GravityInfluencer.class, ClassUtils.requireConstructor(CreateGravityParticleInfluencerAction.class, NodeTree.class, TreeNode.class),
            ImpulseInfluencer.class, ClassUtils.requireConstructor(CreateImpulseParticleInfluencerAction.class, NodeTree.class, TreeNode.class),
            PhysicsInfluencer.class, ClassUtils.requireConstructor(CreatePhysicsParticleInfluencerAction.class, NodeTree.class, TreeNode.class),
            RadialVelocityInfluencer.class, ClassUtils.requireConstructor(CreateRadialVelocityParticleInfluencerAction.class, NodeTree.class, TreeNode.class),
            RotationInfluencer.class, ClassUtils.requireConstructor(CreateRotationParticleInfluencerAction.class, NodeTree.class, TreeNode.class),
            SizeInfluencer.class, ClassUtils.requireConstructor(CreateSizeParticleInfluencerAction.class, NodeTree.class, TreeNode.class),
            SpriteInfluencer.class, ClassUtils.requireConstructor(CreateSpriteParticleInfluencerAction.class, NodeTree.class, TreeNode.class)
    );

    public ParticleInfluencersTreeNode(@NotNull ParticleInfluencers element, long objectId) {
        super(element, objectId);
    }

    @Override
    @FromAnyThread
    public @NotNull String getName() {
        return Messages.MODEL_FILE_EDITOR_NODE_PARTICLE_EMITTER_INFLUENCERS;
    }

    @Override
    @FxThread
    public @Nullable Image getIcon() {
        return Icons.NODE_16;
    }

    @Override
    @FxThread
    public void fillContextMenu(@NotNull NodeTree<?> nodeTree, @NotNull ObservableList<MenuItem> items) {

        var element = getElement();
        var emitterNode = element.getEmitterNode();

        var createMenu = new Menu(Messages.MODEL_NODE_TREE_ACTION_CREATE, new ImageView(Icons.ADD_12));
        var createItems = createMenu.getItems();

        CONSTRUCTORS.forEach((type, constructor) -> {
            if (emitterNode.getInfluencer(type) != null) return;
            createItems.add(ClassUtils.newInstance(constructor, nodeTree, this));
        });

        items.add(createMenu);

        super.fillContextMenu(nodeTree, items);
    }

    @Override
    @FxThread
    public @NotNull Array<TreeNode<?>> getChildren(@NotNull NodeTree<?> nodeTree) {

        var result = Array.<TreeNode<?>>ofType(TreeNode.class);

        getElement().getInfluencers()
                .forEach(influencer -> result.add(FACTORY_REGISTRY.createFor(influencer)));

        return result;
    }

    @Override
    @FxThread
    public boolean hasChildren(@NotNull NodeTree<?> nodeTree) {
        return nodeTree instanceof ModelNodeTree;
    }
}
