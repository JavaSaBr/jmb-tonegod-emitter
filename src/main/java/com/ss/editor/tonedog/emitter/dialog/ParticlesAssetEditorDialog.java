package com.ss.editor.tonedog.emitter.dialog;

import static com.ss.editor.util.EditorUtil.getAssetFile;
import static com.ss.editor.util.EditorUtil.toAssetPath;
import com.jme3.asset.MaterialKey;
import com.jme3.shader.VarType;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.ui.component.asset.tree.resource.ResourceElement;
import com.ss.editor.ui.css.CssClasses;
import com.ss.editor.ui.dialog.asset.file.AssetEditorDialog;
import com.ss.editor.util.EditorUtil;
import com.ss.rlib.fx.util.FxUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.material.ParticlesMaterial;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

/**
 * The implementation of the {@link AssetEditorDialog} to chose the {@link ParticlesMaterial} from asset.
 *
 * @author JavaSaBr
 */
public class ParticlesAssetEditorDialog extends AssetEditorDialog<ParticlesMaterial> {

    /**
     * The combo box with texture parameter name.
     */
    @NotNull
    private final ComboBox<String> textureParamNameComboBox;

    /**
     * The check box for applying the lighting transform.
     */
    @NotNull
    private final CheckBox applyLightingTransformCheckBox;

    public ParticlesAssetEditorDialog(@NotNull Consumer<ParticlesMaterial> consumer) {
        super(consumer);
        this.textureParamNameComboBox = new ComboBox<>();
        this.applyLightingTransformCheckBox = new CheckBox();
    }

    @Override
    protected @NotNull Region buildSecondPart(@NotNull HBox container) {

        var preview = super.buildSecondPart(container);

        var textureParamNameLabel = new Label(Messages.PARTICLE_ASSET_EDITOR_DIALOG_TEXTURE_PARAM_LABEL + ":");
        textureParamNameLabel.prefWidthProperty()
                .bind(container.widthProperty().multiply(0.25));

        var applyLightingTransformLabel = new Label(Messages.PARTICLE_ASSET_EDITOR_DIALOG_LIGHTING_TRANSFORM_LABEL + ":");
        applyLightingTransformLabel.prefWidthProperty()
                .bind(container.widthProperty().multiply(0.25));

        textureParamNameComboBox.prefWidthProperty()
                .bind(container.widthProperty().multiply(0.25));

        applyLightingTransformCheckBox.prefWidthProperty()
                .bind(container.widthProperty().multiply(0.25));

        var settingsContainer = new GridPane();
        settingsContainer.add(textureParamNameLabel, 0, 0);
        settingsContainer.add(textureParamNameComboBox, 1, 0);
        settingsContainer.add(applyLightingTransformLabel, 0, 1);
        settingsContainer.add(applyLightingTransformCheckBox, 1, 1);
        settingsContainer.add(preview, 0, 2, 2, 1);

        FxUtils.addClass(settingsContainer, CssClasses.DEF_GRID_PANE);

        return settingsContainer;
    }

    @Override
    @FxThread
    protected void processOpen(@NotNull ResourceElement element) {
        super.processOpen(element);

        boolean applyLightingTransform = applyLightingTransformCheckBox.isSelected();
        var textureParamName = textureParamNameComboBox.getValue();

        var assetManager = EditorUtil.getAssetManager();

        var file = element.getFile();
        var assetFile = getAssetFile(file);

        if (assetFile == null) {
            throw new RuntimeException("AssetFile can't be null.");
        }

        var material = assetManager.loadAsset(new MaterialKey(toAssetPath(assetFile)));
        var particlesMaterial = new ParticlesMaterial(material, textureParamName, applyLightingTransform);

        getConsumer().accept(particlesMaterial);
    }

    @Override
    @FxThread
    protected @NotNull ObservableBooleanValue buildAdditionalDisableCondition() {

        var itemProperty = textureParamNameComboBox
                .getSelectionModel()
                .selectedItemProperty();

        var parent = super.buildAdditionalDisableCondition();

        return Bindings.and(parent, itemProperty.isNull().or(itemProperty.isEqualTo("")));
    }

    @Override
    @FxThread
    protected void validate(@NotNull Label warningLabel, @Nullable ResourceElement element) {

        var items = textureParamNameComboBox.getItems();
        items.clear();

        final Path file = element == null ? null : element.getFile();

        if (file != null && !Files.isDirectory(file)) {

            var assetManager = EditorUtil.getAssetManager();
            var assetFile = getAssetFile(file);

            if (assetFile == null) {
                throw new RuntimeException("AssetFile can't be null.");
            }

            var materialKey = new MaterialKey(toAssetPath(assetFile));
            var material = assetManager.loadAsset(materialKey);
            var materialDef = material.getMaterialDef();

            var materialParams = materialDef.getMaterialParams();
            materialParams.stream()
                    .filter(param -> param.getVarType() == VarType.Texture2D)
                    .filter(matParam -> material.getTextureParam(matParam.getName()) != null)
                    .forEach(filtered -> items.add(filtered.getName()));

            var selectionModel = textureParamNameComboBox.getSelectionModel();

            if (!items.isEmpty()) {
                selectionModel.select(0);
            } else {
                selectionModel.select(null);
            }
        }

        super.validate(warningLabel, element);
    }
}
