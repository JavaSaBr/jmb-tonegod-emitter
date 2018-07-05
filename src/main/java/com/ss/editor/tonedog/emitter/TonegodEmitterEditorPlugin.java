package com.ss.editor.tonedog.emitter;

import static com.ss.editor.util.NodeUtils.findParent;
import com.jme3.post.Filter;
import com.jme3.scene.AssetLinkNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.ss.editor.annotation.BackgroundThread;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.annotation.JmeThread;
import com.ss.editor.part3d.editor.impl.scene.AbstractSceneEditor3dPart;
import com.ss.editor.part3d.editor.impl.scene.AbstractSceneEditor3dPart.SelectionFinder;
import com.ss.editor.plugin.EditorPlugin;
import com.ss.editor.plugin.api.RenderFilterRegistry;
import com.ss.editor.plugin.api.RenderFilterRegistry.FilterExtension;
import com.ss.editor.tonedog.emitter.control.property.builder.ParticleEmitterPropertyBuilder;
import com.ss.editor.tonedog.emitter.control.property.builder.ParticleInfluencerPropertyBuilder;
import com.ss.editor.tonedog.emitter.control.tree.ParticlesTreeNodeFactory;
import com.ss.editor.tonedog.emitter.control.tree.action.CreateParticleEmitterAction;
import com.ss.editor.tonedog.emitter.control.tree.action.CreateSoftParticleEmitterAction;
import com.ss.editor.tonedog.emitter.model.ParticleInfluencers;
import com.ss.editor.ui.control.model.ModelPropertyEditor;
import com.ss.editor.ui.control.model.ModelPropertyEditor.CanEditChecker;
import com.ss.editor.ui.control.model.ModelPropertyEditor.IsNeedUpdateChecker;
import com.ss.editor.ui.control.property.builder.PropertyBuilder;
import com.ss.editor.ui.control.property.builder.PropertyBuilderRegistry;
import com.ss.editor.ui.control.property.builder.impl.GeometryPropertyBuilder;
import com.ss.editor.ui.control.property.builder.impl.GeometryPropertyBuilder.CanEditMaterialChecker;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.action.AbstractNodeAction.AdditionalAction;
import com.ss.editor.ui.control.tree.action.impl.particle.emitter.ResetParticleEmittersAction;
import com.ss.editor.ui.control.tree.node.factory.TreeNodeFactory;
import com.ss.editor.ui.control.tree.node.factory.TreeNodeFactoryRegistry;
import com.ss.editor.ui.control.tree.node.impl.spatial.NodeTreeNode;
import com.ss.editor.ui.control.tree.node.impl.spatial.NodeTreeNode.ParticleEmitterFinder;
import com.ss.editor.ui.control.tree.node.impl.spatial.SpatialTreeNode;
import com.ss.editor.ui.control.tree.node.impl.spatial.SpatialTreeNode.ActionFactory;
import com.ss.editor.ui.css.CssRegistry;
import com.ss.editor.util.NodeUtils;
import com.ss.rlib.common.plugin.PluginContainer;
import com.ss.rlib.common.plugin.annotation.PluginDescription;
import com.ss.rlib.common.plugin.extension.ExtensionPointManager;
import com.ss.rlib.common.util.FileUtils;
import com.ss.rlib.common.util.Utils;
import javafx.scene.control.MenuItem;
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
        version = "1.1.4",
        minAppVersion = "1.9.0",
        name = "Tonegod.Emitter Support",
        description = "Provides integration with the library 'tonegod.emitter'."
)
public class TonegodEmitterEditorPlugin extends EditorPlugin {

    private static final Class<TonegodEmitterEditorPlugin> CLASS = TonegodEmitterEditorPlugin.class;

    private static final String GRADLE_DEPENDENCIES =
            FileUtils.readFromClasspath(CLASS, "/com/ss/editor/tonegod/emitter/dependency/gradle.html");

    private static final String MAVEN_DEPENDENCIES =
            FileUtils.readFromClasspath(CLASS, "/com/ss/editor/tonegod/emitter/dependency/maven.html");

    @FxThread
    private static boolean canEditProperties(@Nullable Object object, @Nullable Object parent) {

        if (parent instanceof ParticleInfluencers) {
            var emitterNode = ((ParticleInfluencers) parent).getEmitterNode();
            var linkNode = findParent(emitterNode, AssetLinkNode.class::isInstance);
            return linkNode == null;
        }

        return true;
    }

    @FxThread
    private static @Nullable MenuItem createSoftParticleEmitter(
            @NotNull SpatialTreeNode<?> node,
            @NotNull NodeTree<?> nodeTree
    ) {
        if (node instanceof NodeTreeNode) {
            return new CreateSoftParticleEmitterAction(nodeTree, node);
        }
        return null;
    }

    @FxThread
    private static @Nullable MenuItem createParticleEmitter(
            @NotNull SpatialTreeNode<?> node,
            @NotNull NodeTree<?> nodeTree
    ) {
        if (node instanceof NodeTreeNode) {
            return new CreateParticleEmitterAction(nodeTree, node);
        }
        return null;
    }

    @FxThread
    private static boolean isNeedUpdateProperties(@Nullable Object currentObject, @Nullable Object object) {

        if (currentObject instanceof ParticleNode && object instanceof ParticleEmitterNode) {
            var parent = findParent((Spatial) currentObject, ParticleEmitterNode.class::isInstance);
            return parent == object;
        }

        return false;
    }

    @JmeThread
    private static @Nullable Spatial findParticleEmitter(@NotNull Object object) {
        if (object instanceof ParticleGeometry) {
            return NodeUtils.findParent((Spatial) object, ParticleEmitterNode.class::isInstance);
        } else {
            return null;
        }
    }

    public TonegodEmitterEditorPlugin(@NotNull PluginContainer pluginContainer) {
        super(pluginContainer);
    }

    @Override
    @BackgroundThread
    public void register(@NotNull ExtensionPointManager manager) {
        super.register(manager);

        manager.<PropertyBuilder>getExtensionPoint(PropertyBuilderRegistry.EP_BUILDERS)
                .register(ParticleEmitterPropertyBuilder.getInstance())
                .register(ParticleInfluencerPropertyBuilder.getInstance());

        manager.<SelectionFinder>getExtensionPoint(AbstractSceneEditor3dPart.EP_SELECTION_FINDER)
                .register(TonegodEmitterEditorPlugin::findParticleEmitter);

        manager.<TreeNodeFactory>getExtensionPoint(TreeNodeFactoryRegistry.EP_FACTORIES)
                .register(ParticlesTreeNodeFactory.getInstance());

        // register disabling editing materials for tonegod particle geometries
        manager.<CanEditMaterialChecker>getExtensionPoint(GeometryPropertyBuilder.EP_CAN_EDIT_MATERIAL_CHECKERS)
                .register(geometry -> !(geometry instanceof ParticleGeometry));

        // register the checker to prevent editing of toneogd particle emitter nodes
        // if it was linked to a scene using AssetLinkNode
        manager.<CanEditChecker>getExtensionPoint(ModelPropertyEditor.EP_CAN_EDIT_CHECKERS)
                .register(TonegodEmitterEditorPlugin::canEditProperties);

        // register the checker to detect some cases when we need
        // to update properties of tonegod particle emitter nodes
        manager.<IsNeedUpdateChecker>getExtensionPoint(ModelPropertyEditor.EP_NEED_UPDATE_CHECKERS)
                .register(TonegodEmitterEditorPlugin::isNeedUpdateProperties);


        // register the additional finder to detect tonegod particle emitter nodes inside a node
        manager.<ParticleEmitterFinder>getExtensionPoint(NodeTreeNode.EP_PARTICLE_EMITTER_FILTERS)
                .register(node -> NodeUtils.findSpatial(node, ParticleEmitterNode.class::isInstance) != null);

        // register the action to reset tonegod particle emitter nodes
        manager.<AdditionalAction<Node>>getExtensionPoint(ResetParticleEmittersAction.EP_ADDITIONAL_ACTIONS)
                .register(node -> () -> NodeUtils.visitSpatial(node, ParticleEmitterNode.class, ParticleEmitterNode::reset));


        // register additional creation actions
        manager.<ActionFactory>getExtensionPoint(SpatialTreeNode.EP_CREATION_ACTION_FACTORIES)
                .register(TonegodEmitterEditorPlugin::createParticleEmitter)
                .register(TonegodEmitterEditorPlugin::createSoftParticleEmitter);

        // register the filter to process tonegod soft particles
        var softParticlesFilter = new TonegodTranslucentBucketFilter(true);
        var filterExtension = new FilterExtension() {

            @Override
            @FromAnyThread
            public @NotNull Filter getFilter() {
                return softParticlesFilter;
            }

            @Override
            @JmeThread
            public void refresh() {
                softParticlesFilter.refresh();
            }
        };

        manager.addExtension(RenderFilterRegistry.EP_FILTERS, filterExtension);
    }

    @Override
    @BackgroundThread
    public void register(@NotNull CssRegistry registry) {
        super.register(registry);
        registry.register("com/ss/editor/tonegod/emitter/plugin.css", getClass().getClassLoader());
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
