package com.ss.editor.tonedog.emitter.control.tree.action.shape;

import static com.ss.editor.extension.property.EditablePropertyType.FLOAT;
import static com.ss.editor.extension.property.EditablePropertyType.INTEGER;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Dome;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.plugin.api.property.PropertyDefinition;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.rlib.common.util.VarTable;
import com.ss.rlib.common.util.array.Array;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;

/**
 * The action to switch an emitter shape of the {@link ParticleEmitterNode} to a {@link Dome}.
 *
 * @author JavaSaBr
 */
public class CreateDomeShapeEmitterAction extends AbstractCreateShapeEmitterAction {

    private static final String PROPERTY_PLANES = "planes";
    private static final String PROPERTY_RADIAL_SAMPLES = "radialSamples";
    private static final String PROPERTY_RADIUS = "radius";

    public CreateDomeShapeEmitterAction(@NotNull NodeTree<?> nodeTree, @NotNull TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.DOME_16;
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_PARTICLE_EMITTER_DOME_SHAPE;
    }

    @Override
    @FxThread
    protected @NotNull Array<PropertyDefinition> getPropertyDefinitions() {

        var definitions = Array.ofType(PropertyDefinition.class);
        definitions.add(new PropertyDefinition(INTEGER, Messages.MODEL_PROPERTY_PLANES, PROPERTY_PLANES, 10));
        definitions.add(new PropertyDefinition(INTEGER, Messages.MODEL_PROPERTY_RADIAL_SAMPLES, PROPERTY_RADIAL_SAMPLES, 10));
        definitions.add(new PropertyDefinition(FLOAT, Messages.MODEL_PROPERTY_RADIUS, PROPERTY_RADIUS, 1F));

        return definitions;
    }

    @Override
    @FxThread
    protected @NotNull String getDialogTitle() {
        return Messages.CREATE_PARTICLE_EMITTER_DOME_SHAPE_DIALOG_TITLE;
    }

    @Override
    @FxThread
    protected @NotNull Mesh createMesh(@NotNull VarTable vars) {
        var planes = vars.getInteger(PROPERTY_PLANES);
        var radialSamples = vars.getInteger(PROPERTY_RADIAL_SAMPLES);
        var radius = vars.getFloat(PROPERTY_RADIUS);
        return new Dome(planes, radialSamples, radius);
    }
}
