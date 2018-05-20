package com.ss.editor.tonedog.emitter;

import static com.ss.editor.util.NodeUtils.findParent;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Spatial;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.annotation.JmeThread;
import com.ss.editor.part3d.editor.impl.scene.AbstractSceneEditor3DPart;
import com.ss.editor.plugin.EditorPlugin;
import com.ss.editor.plugin.api.RenderFilterExtension;
import com.ss.editor.tonedog.emitter.control.property.builder.ParticleEmitterPropertyBuilder;
import com.ss.editor.tonedog.emitter.control.property.builder.ParticleInfluencerPropertyBuilder;
import com.ss.editor.tonedog.emitter.control.tree.ParticlesTreeNodeFactory;
import com.ss.editor.tonedog.emitter.control.tree.action.CreateParticleEmitterAction;
import com.ss.editor.tonedog.emitter.control.tree.action.CreateSoftParticleEmitterAction;
import com.ss.editor.tonedog.emitter.model.ParticleInfluencers;
import com.ss.editor.ui.control.model.ModelPropertyEditor;
import com.ss.editor.ui.control.property.builder.PropertyBuilderRegistry;
import com.ss.editor.ui.control.property.builder.impl.GeometryPropertyBuilder;
import com.ss.editor.ui.control.tree.action.impl.particle.emitter.ResetParticleEmittersAction;
import com.ss.editor.ui.control.tree.node.factory.TreeNodeFactoryRegistry;
import com.ss.editor.ui.control.tree.node.impl.spatial.NodeTreeNode;
import com.ss.editor.ui.control.tree.node.impl.spatial.SpatialTreeNode;
import com.ss.editor.util.NodeUtils;
import com.ss.rlib.common.plugin.PluginContainer;
import com.ss.rlib.common.plugin.PluginSystem;
import com.ss.rlib.common.plugin.annotation.PluginDescription;
import com.ss.rlib.common.util.FileUtils;
import com.ss.rlib.common.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.filter.TonegodTranslucentBucketFilter;
import tonegod.emitter.geometry.ParticleGeometry;
import tonegod.emitter.node.ParticleNode;

import java.net.URL;

/**
 * The implementation of an editor plugin.
 *
 * @author JavaSaBr
 */
@PluginDescription(
        id = "com.ss.editor.tonegod.emitter",
        version = "1.1.1",
        minAppVersion = "1.8.0",
        name = "Tonegod.Emitter Support",
        description = "Provides integration with the library 'tonegod.emitter'."
)
public class TonegodEmitterEditorPlugin extends EditorPlugin {

    private static final Class<TonegodEmitterEditorPlugin> CLASS = TonegodEmitterEditorPlugin.class;

    @NotNull
    private static final String GRADLE_DEPENDENCIES;

    @NotNull
    private static final String MAVEN_DEPENDENCIES;


    static {
        GRADLE_DEPENDENCIES = FileUtils.read(CLASS.getResourceAsStream("/com/ss/editor/tonegod/emitter/dependency/gradle.html"));
        MAVEN_DEPENDENCIES = FileUtils.read(CLASS.getResourceAsStream("/com/ss/editor/tonegod/emitter/dependency/maven.html"));
    }

    public TonegodEmitterEditorPlugin(@NotNull PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    @JmeThread
    public void onAfterCreateJmeContext(@NotNull PluginSystem pluginSystem) {
        super.onAfterCreateJmeContext(pluginSystem);

        // add the selection finder to handle selection events in 3D editors
        AbstractSceneEditor3DPart.registerSelectionFinder(object -> {
            if (object instanceof ParticleGeometry) {
                return findParent((Spatial) object, ParticleEmitterNode.class::isInstance);
            } else return null;
        });

        // register the filter to process tonegod soft particles
        var softParticlesFilter = new TonegodTranslucentBucketFilter(true);
        var filterExtension = RenderFilterExtension.getInstance();
        filterExtension.register(softParticlesFilter);
        filterExtension.setOnRefresh(softParticlesFilter, TonegodTranslucentBucketFilter::refresh);
    }

    @Override
    @FxThread
    public void onAfterCreateJavaFxContext(@NotNull PluginSystem pluginSystem) {
        super.onAfterCreateJavaFxContext(pluginSystem);

        // register disabling editing materials for tonegod particle geometries
        GeometryPropertyBuilder.registerCanEditMaterialChecker(geometry -> !(geometry instanceof ParticleGeometry));

        // register the checker to prevent editing of toneogd particle emitter nodes
        // if it was linked to a scene using AssetLinkNode
        ModelPropertyEditor.registerCanEditChecker((object, parent) -> {
            if (parent instanceof ParticleInfluencers) {
                var emitterNode = ((ParticleInfluencers) parent).getEmitterNode();
                var linkNode = findParent(emitterNode, AssetLinkNode.class::isInstance);
                return linkNode == null;
            }
            return true;
        });

        // register the checker to detect some cases when we need
        // to update properties of tonegod particle emitter nodes
        ModelPropertyEditor.registerIsNeedUpdateChecker((currentObject, object) -> {
            if (currentObject instanceof ParticleNode && object instanceof ParticleEmitterNode) {
                var parent = findParent((Spatial) currentObject, ParticleEmitterNode.class::isInstance);
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
    public void register(@NotNull PropertyBuilderRegistry registry) {
        super.register(registry);
        registry.register(ParticleEmitterPropertyBuilder.getInstance());
        registry.register(ParticleInfluencerPropertyBuilder.getInstance());
    }

    @Override
    @FromAnyThread
    public void register(@NotNull TreeNodeFactoryRegistry registry) {
        super.register(registry);
        registry.register(ParticlesTreeNodeFactory.getInstance());
    }

    @Override
    @FromAnyThread
    public @Nullable String getUsedGradleDependencies() {
        return GRADLE_DEPENDENCIES;
    }

    @Override
    @FromAnyThread
    public @Nullable String getUsedMavenDependencies() {
        return MAVEN_DEPENDENCIES;
    }

    @Override
    @FromAnyThread
    public @Nullable URL getHomePageUrl() {
        return Utils.get("https://github.com/JavaSaBr/tonegodemitter", URL::new);
    }
}
