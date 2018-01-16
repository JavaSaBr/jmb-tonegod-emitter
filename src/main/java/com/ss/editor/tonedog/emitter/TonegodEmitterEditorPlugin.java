package com.ss.editor.tonedog.emitter;

import static com.ss.editor.util.NodeUtils.findParent;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Spatial;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.plugin.EditorPlugin;
import com.ss.editor.plugin.api.RenderFilterExtension;
import com.ss.editor.state.editor.impl.scene.AbstractSceneEditor3DState;
import com.ss.editor.tonedog.emitter.control.model.property.builder.ParticleEmitterPropertyBuilder;
import com.ss.editor.tonedog.emitter.control.model.property.builder.ParticleInfluencerPropertyBuilder;
import com.ss.editor.tonedog.emitter.control.model.tree.action.CreateParticleEmitterAction;
import com.ss.editor.tonedog.emitter.control.model.tree.action.CreateSoftParticleEmitterAction;
import com.ss.editor.tonedog.emitter.control.tree.ParticlesTreeNodeFactory;
import com.ss.editor.tonedog.emitter.model.node.particles.ParticleInfluencers;
import com.ss.editor.ui.control.model.node.spatial.NodeTreeNode;
import com.ss.editor.ui.control.model.node.spatial.SpatialTreeNode;
import com.ss.editor.ui.control.model.property.ModelPropertyEditor;
import com.ss.editor.ui.control.model.property.builder.impl.GeometryPropertyBuilder;
import com.ss.editor.ui.control.model.tree.action.particle.emitter.ResetParticleEmittersAction;
import com.ss.editor.ui.control.property.builder.PropertyBuilderRegistry;
import com.ss.editor.ui.control.tree.node.TreeNodeFactoryRegistry;
import com.ss.editor.util.NodeUtils;
import com.ss.rlib.plugin.PluginContainer;
import com.ss.rlib.plugin.PluginSystem;
import com.ss.rlib.plugin.annotation.PluginDescription;
import org.jetbrains.annotations.NotNull;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.filter.TonegodTranslucentBucketFilter;
import tonegod.emitter.geometry.ParticleGeometry;
import tonegod.emitter.node.ParticleNode;

/**
 * The implementation of an editor plugin.
 *
 * @author JavaSaBr
 */
@PluginDescription(
        id = "com.ss.editor.tonegod.emitter",
        version = "1.0.0",
        minAppVersion = "1.5.1",
        name = "Tonegod.Emitter Support",
        description = "Provides integration with the library 'tonegod.emitter'."
)
public class TonegodEmitterEditorPlugin extends EditorPlugin {

    public TonegodEmitterEditorPlugin(@NotNull final PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    public void onAfterCreateJmeContext(@NotNull final PluginSystem pluginSystem) {
        super.onAfterCreateJmeContext(pluginSystem);

        // add the selection finder to handle selection events in 3D editors
        AbstractSceneEditor3DState.registerSelectionFinder(object -> {
            if (object instanceof ParticleGeometry) {
                return findParent((Spatial) object, ParticleEmitterNode.class::isInstance);
            } else return null;
        });

        // register the filter to process tonegod soft particles
        final TonegodTranslucentBucketFilter softParticlesFilter = new TonegodTranslucentBucketFilter(true);
        final RenderFilterExtension filterExtension = RenderFilterExtension.getInstance();
        filterExtension.register(softParticlesFilter);
        filterExtension.setOnRefresh(softParticlesFilter, TonegodTranslucentBucketFilter::refresh);
    }

    @Override
    public void onAfterCreateJavaFxContext(@NotNull final PluginSystem pluginSystem) {
        super.onAfterCreateJavaFxContext(pluginSystem);

        // register disabling editing materials for tonegod particle geometries
        GeometryPropertyBuilder.registerCanEditMaterialChecker(geometry -> !(geometry instanceof ParticleGeometry));

        // register the checker to prevent editing of toneogd particle emitter nodes if it was linked to a scene using AssetLinkNode
        ModelPropertyEditor.registerCanEditChecker((object, parent) -> {
            if (parent instanceof ParticleInfluencers) {
                final ParticleEmitterNode emitterNode = ((ParticleInfluencers) parent).getEmitterNode();
                final Object linkNode = findParent(emitterNode, AssetLinkNode.class::isInstance);
                return linkNode == null;
            }
            return true;
        });

        // register the checker to detect some cases when we need to update properties of tonegod particle emitter nodes
        ModelPropertyEditor.registerIsNeedUpdateChecker((currentObject, object) -> {
            if (currentObject instanceof ParticleNode && object instanceof ParticleEmitterNode) {
                final Object parent = findParent((Spatial) currentObject, ParticleEmitterNode.class::isInstance);
                return parent == object;
            }
            return false;
        });

        // register the additional finder to detect tonegod particle emitter nodes inside a node
        NodeTreeNode.registerParticleEmitterFinder(node ->
                NodeUtils.findSpatial(node, ParticleEmitterNode.class::isInstance) != null);

        // register the action to reset tonegod particle emitter nodes
        ResetParticleEmittersAction.registerAdditionalAction(node ->
                () -> NodeUtils.visitSpatial(node, ParticleEmitterNode.class, ParticleEmitterNode::reset));

        // register additional creation actions
        SpatialTreeNode.registerCreationAction((node, nodeTree) -> {
            if (node instanceof NodeTreeNode) {
                return new CreateParticleEmitterAction(nodeTree, node);
            }
            return null;
        });
        SpatialTreeNode.registerCreationAction((node, nodeTree) -> {
            if (node instanceof NodeTreeNode) {
                return new CreateSoftParticleEmitterAction(nodeTree, node);
            }
            return null;
        });
    }

    @Override
    @FromAnyThread
    public void register(@NotNull final PropertyBuilderRegistry registry) {
        super.register(registry);
        registry.register(ParticleEmitterPropertyBuilder.getInstance());
        registry.register(ParticleInfluencerPropertyBuilder.getInstance());
    }

    @Override
    @FromAnyThread
    public void register(@NotNull final TreeNodeFactoryRegistry registry) {
        super.register(registry);
        registry.register(ParticlesTreeNodeFactory.getInstance());
    }
}
