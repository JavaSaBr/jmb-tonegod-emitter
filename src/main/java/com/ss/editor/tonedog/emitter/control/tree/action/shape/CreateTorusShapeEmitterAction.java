package com.ss.editor.tonedog.emitter.control.tree.action.shape;

import static com.ss.editor.extension.property.EditablePropertyType.FLOAT;
import static com.ss.editor.extension.property.EditablePropertyType.INTEGER;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Torus;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.plugin.api.property.PropertyDefinition;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.tree.NodeTree;
import com.ss.editor.ui.control.tree.node.TreeNode;
import com.ss.rlib.common.util.VarTable;
import com.ss.rlib.common.util.array.Array;
import com.ss.rlib.common.util.array.ArrayFactory;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.ParticleEmitterNode;

/**
 * The action to switch an emitter shape of the {@link ParticleEmitterNode} to a {@link Torus}.
 *
 * @author JavaSaBr
 */
public class CreateTorusShapeEmitterAction extends AbstractCreateShapeEmitterAction {

    @NotNull
    private static final String PROPERTY_CIRCLE_SAMPLES = "circleSamples";

    @NotNull
    private static final String PROPERTY_RADIAL_SAMPLES = "radialSamples";

    @NotNull
    private static final String PROPERTY_INNER_RADIUS = "innerRadius";

    @NotNull
    private static final String PROPERTY_OUTER_RADIUS = "outerRadius";

    public CreateTorusShapeEmitterAction(@NotNull NodeTree<?> nodeTree, @NotNull TreeNode<?> node) {
        super(nodeTree, node);
    }

    @Override
    @FxThread
    protected @Nullable Image getIcon() {
        return Icons.TORUS_16;
    }

    @Override
    @FxThread
    protected @NotNull String getName() {
        return Messages.MODEL_NODE_TREE_ACTION_PARTICLE_EMITTER_TORUS_SHAPE;
    }

    @Override
    @FxThread
    protected @NotNull Array<PropertyDefinition> getPropertyDefinitions() {

        var definitions = ArrayFactory.<PropertyDefinition>newArray(PropertyDefinition.class);
        definitions.add(new PropertyDefinition(INTEGER, Messages.MODEL_PROPERTY_CIRCLE_SAMPLES, PROPERTY_CIRCLE_SAMPLES, 10));
        definitions.add(new PropertyDefinition(INTEGER, Messages.MODEL_PROPERTY_RADIAL_SAMPLES, PROPERTY_RADIAL_SAMPLES, 10));
        definitions.add(new PropertyDefinition(FLOAT, Messages.MODEL_PROPERTY_INNER_RADIUS, PROPERTY_INNER_RADIUS, 0.1F));
        definitions.add(new PropertyDefinition(FLOAT, Messages.MODEL_PROPERTY_OUTER_RADIUS, PROPERTY_OUTER_RADIUS, 1F));

        return definitions;
    }

    @Override
    @FxThread
    protected @NotNull String getDialogTitle() {
        return Messages.CREATE_PARTICLE_EMITTER_TORUS_SHAPE_DIALOG_TITLE;
    }

    @Override
    @FxThread
    protected @NotNull Mesh createMesh(@NotNull VarTable vars) {
        var circleSamples = vars.getInteger(PROPERTY_CIRCLE_SAMPLES);
        var radialSamples = vars.getInteger(PROPERTY_RADIAL_SAMPLES);
        var innerRadius = vars.getFloat(PROPERTY_INNER_RADIUS);
        var outerRadius = vars.getFloat(PROPERTY_OUTER_RADIUS);
        return new Torus(circleSamples, radialSamples, innerRadius, outerRadius);
    }
}
