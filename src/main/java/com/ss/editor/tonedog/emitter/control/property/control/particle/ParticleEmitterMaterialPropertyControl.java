package com.ss.editor.tonedog.emitter.control.property.control.particle;

import com.jme3.material.Material;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.dialog.ParticlesAssetEditorDialog;
import com.ss.editor.ui.control.property.impl.MaterialPropertyControl;
import com.ss.editor.util.EditorUtil;
import javafx.event.ActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;
import tonegod.emitter.material.ParticlesMaterial;

/**
 * The implementation of the {@link MaterialPropertyControl} to edit the {@link Material} of the {@link
 * ParticleEmitterNode}*.
 *
 * @author JavaSaBr
 */
public class ParticleEmitterMaterialPropertyControl extends
        MaterialPropertyControl<ModelChangeConsumer, ParticleEmitterNode, ParticlesMaterial> {

    public ParticleEmitterMaterialPropertyControl(
            @NotNull ParticlesMaterial element,
            @NotNull String paramName,
            @NotNull ModelChangeConsumer modelChangeConsumer
    ) {
        super(element, paramName, modelChangeConsumer);
    }

    /**
     * Show a dialog to choose another material.
     */
    @FxThread
    protected void processChange() {
        var dialog = new ParticlesAssetEditorDialog(this::addMaterial);
        dialog.setExtensionFilter(MATERIAL_EXTENSIONS);
        dialog.show();
    }

    /**
     * Add the mew material.
     */
    @FxThread
    private void addMaterial(@NotNull ParticlesMaterial particlesMaterial) {
        changed(particlesMaterial, getPropertyValue());
    }

    @Override
    protected void openToEdit(@Nullable ActionEvent event) {
        super.openToEdit(event);

        var element = getPropertyValue();
        if (element == null) {
            return;
        }

        var material = element.getMaterial();

        EditorUtil.openInEditor(material.getKey());
    }

    @Override
    @FxThread
    protected void reload() {

        var element = getPropertyValue();
        if (element == null) {
            return;
        }

        var material = element.getMaterial();
        var key = material.getKey();

        getMaterialLabel().setText(EditorUtil.isEmpty(key) ? NO_MATERIAL : key.getName());
    }
}
