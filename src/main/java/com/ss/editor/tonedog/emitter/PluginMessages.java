package com.ss.editor.tonedog.emitter;

import static com.ss.editor.plugin.api.messages.MessagesPluginFactory.getResourceBundle;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 * The class with localised all plugin messages.
 *
 * @author JavaSaBr
 */
public interface PluginMessages {

    @NotNull ResourceBundle RESOURCE_BUNDLE = getResourceBundle(TonegodEmitterEditorPlugin.class,
            "com/ss/editor/tonegod/emitter/messages/messages");

    @NotNull String MODEL_NODE_TREE_ACTION_CREATE_PARTICLE_EMITTER =
            RESOURCE_BUNDLE.getString("ModelNodeTreeActionCreateParticleEmitter");

    @NotNull String MODEL_NODE_TREE_ACTION_CREATE_SOFT_PARTICLE_EMITTER =
            RESOURCE_BUNDLE.getString("ModelNodeTreeActionCreateSoftParticleEmitter");
}
