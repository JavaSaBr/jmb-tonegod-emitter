package com.ss.editor.tonedog.emitter.control.tree.node.influencer;

import static com.ss.rlib.common.util.ClassUtils.getConstructor;
import static com.ss.rlib.common.util.ClassUtils.newInstance;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.tonedog.emitter.control.tree.action.influerencer.*;
import com.ss.editor.tonedog.emitter.model.ParticleInfluencers;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.model.ModelNodeTree;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import com.ss.rlib.common.util.dictionary.DictionaryFactory;
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

    @NotNull
    private static final ObjectDictionary<Class<? extends ParticleInfluencer>, Constructor<? extends MenuItem>> CONSTRUCTORS =
            DictionaryFactory.newObjectDictionary();

    static {
        CONSTRUCTORS.put(AlphaInfluencer.class, getConstructor(CreateAlphaParticleInfluencerAction.class, NodeTree.class, TreeNode.class));
        CONSTRUCTORS.put(ColorInfluencer.class, getConstructor(CreateColorParticleInfluencerAction.class, NodeTree.class, TreeNode.class));
        CONSTRUCTORS.put(DestinationInfluencer.class, getConstructor(CreateDestinationParticleInfluencerAction.class, NodeTree.class, TreeNode.class));
        CONSTRUCTORS.put(GravityInfluencer.class, getConstructor(CreateGravityParticleInfluencerAction.class, NodeTree.class, TreeNode.class));
        CONSTRUCTORS.put(ImpulseInfluencer.class, getConstructor(CreateImpulseParticleInfluencerAction.class, NodeTree.class, TreeNode.class));
        CONSTRUCTORS.put(PhysicsInfluencer.class, getConstructor(CreatePhysicsParticleInfluencerAction.class, NodeTree.class, TreeNode.class));
        CONSTRUCTORS.put(RadialVelocityInfluencer.class, getConstructor(CreateRadialVelocityParticleInfluencerAction.class, NodeTree.class, TreeNode.class));
        CONSTRUCTORS.put(RotationInfluencer.class, getConstructor(CreateRotationParticleInfluencerAction.class, NodeTree.class, TreeNode.class));
        CONSTRUCTORS.put(SizeInfluencer.class, getConstructor(CreateSizeParticleInfluencerAction.class, NodeTree.class, TreeNode.class));
        CONSTRUCTORS.put(SpriteInfluencer.class, getConstructor(CreateSpriteParticleInfluencerAction.class, NodeTree.class, TreeNode.class));
    }

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
            createItems.add(newInstance(constructor, nodeTree, this));
        });

        items.add(createMenu);

        super.fillContextMenu(nodeTree, items);
    }

    @Override
    @FxThread
    public @NotNull Array<TreeNode<?>> getChildren(@NotNull NodeTree<?> nodeTree) {

        var result = ArrayFactory.<TreeNode<?>>newArray(TreeNode.class);

        getElement().getInfluencers()
                .forEach(influencer -> result.add(FACTORY_REGISTRY.createFor(influencer)));

        return result;
    }

    @Override
    @FxThread
    public boolean hasChildren(@NotNull final NodeTree<?> nodeTree) {
        return nodeTree instanceof ModelNodeTree;
    }
}
