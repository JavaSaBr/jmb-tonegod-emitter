package com.ss.editor.tonedog.emitter.control.property.control.particle;

import com.jme3.material.Material;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.dialog.ParticlesAssetEditorDialog;
import com.ss.editor.ui.control.property.impl.MaterialPropertyControl;
import com.ss.editor.util.EditorUtil;
import org.jetbrains.annotations.NotNull;
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

    @Override
    @FxThread
    protected void chooseKey() {
        super.chooseKey();

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
        reloadImpl();
    }

    @Override
    @FxThread
    protected void openKey() {
        super.openKey();

        getPropertyValueOpt()
                .map(ParticlesMaterial::getMaterial)
                .map(Material::getKey)
                .ifPresent(EditorUtil::openInEditor);
    }

    @Override
    @FxThread
    protected void reloadImpl() {
        super.reloadImpl();

        keyLabel.setText(getPropertyValueOpt()
                .map(ParticlesMaterial::getMaterial)
                .map(Material::getKey)
                .map(assetKey -> EditorUtil.ifEmpty(assetKey, NO_MATERIAL))
                .orElse(NO_MATERIAL));
    }
}
