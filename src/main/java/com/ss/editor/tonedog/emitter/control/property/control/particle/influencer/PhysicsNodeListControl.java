package com.ss.editor.tonedog.emitter.control.property.control.particle.influencer;

import static com.ss.rlib.util.ObjectUtils.notNull;
import com.jme3.renderer.queue.GeometryList;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.operation.ParticleInfluencerPropertyOperation;
import com.ss.editor.ui.Icons;
import com.ss.editor.ui.control.UpdatableControl;
import com.ss.editor.ui.css.CssClasses;
import com.ss.editor.ui.dialog.geometry.GeometrySelectorDialog;
import com.ss.editor.ui.util.DynamicIconSupport;
import com.ss.editor.ui.util.UiUtils;
import com.ss.rlib.ui.util.FXUtils;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.impl.PhysicsInfluencer;

import java.util.function.BiConsumer;

/**
 * The control for editing geometry list in the {@link PhysicsInfluencer}.
 *
 * @author JavaSaBr
 */
public class PhysicsNodeListControl extends VBox implements UpdatableControl {

    /**
     * The consumer of changes.
     */
    @NotNull
    private final ModelChangeConsumer modelChangeConsumer;

    /**
     * The physics influencer.
     */
    @NotNull
    private final PhysicsInfluencer influencer;

    /**
     * The parent.
     */
    @NotNull
    private final Object parent;

    /**
     * The element container.
     */
    @Nullable
    private VBox elementContainer;

    public PhysicsNodeListControl(@NotNull final ModelChangeConsumer modelChangeConsumer,
                                  @NotNull final PhysicsInfluencer influencer,
                                  @NotNull final Object parent) {
        this.modelChangeConsumer = modelChangeConsumer;
        this.parent = parent;
        this.influencer = influencer;
        createControls();
        FXUtils.addClassesTo(this, CssClasses.DEF_VBOX, CssClasses.ABSTRACT_PARAM_CONTROL_INFLUENCER,
                CssClasses.PHYSICS_NODE_LIST_CONTROL);
    }

    /**
     * Create controls.
     */
    @FxThread
    private void createControls() {

        final Label propertyNameLabel = new Label(getControlTitle() + ":");

        elementContainer = new VBox();

        final Button addButton = new Button();
        addButton.setGraphic(new ImageView(Icons.ADD_16));
        addButton.setOnAction(event -> processAdd());

        final Button removeButton = new Button();
        removeButton.setGraphic(new ImageView(Icons.REMOVE_12));
        removeButton.setOnAction(event -> processRemove());
        removeButton.setDisable(true);

        final HBox buttonContainer = new HBox(addButton, removeButton);

        final ObservableList<Node> children = elementContainer.getChildren();
        children.addListener((ListChangeListener<Node>) c -> removeButton.setDisable(children.size() < (getMinElements() + 1)));

        FXUtils.addToPane(propertyNameLabel, this);
        FXUtils.addToPane(elementContainer, this);
        FXUtils.addToPane(buttonContainer, this);

        FXUtils.addClassTo(propertyNameLabel, CssClasses.ABSTRACT_PARAM_CONTROL_PARAM_NAME_SINGLE_ROW);
        FXUtils.addClassTo(addButton, CssClasses.BUTTON_WITHOUT_RIGHT_BORDER);
        FXUtils.addClassTo(removeButton, CssClasses.BUTTON_WITHOUT_LEFT_BORDER);
        FXUtils.addClassTo(buttonContainer, CssClasses.DEF_HBOX);
        FXUtils.addClassTo(elementContainer, CssClasses.DEF_VBOX);

        DynamicIconSupport.addSupport(addButton, removeButton);
    }

    /**
     * Get the count of minimum elements.
     *
     * @return the count of minimum elements.
     */
    @FromAnyThread
    protected int getMinElements() {
        return 0;
    }

    /**
     * Gets control title.
     *
     * @return the control title
     */
    @FromAnyThread
    protected @NotNull String getControlTitle() {
        return Messages.MODEL_PROPERTY_GEOMETRY_LIST;
    }

    /**
     * Gets influencer.
     *
     * @return the influencer.
     */
    @FromAnyThread
    public @NotNull PhysicsInfluencer getInfluencer() {
        return influencer;
    }

    /**
     * Gets element container.
     *
     * @return the element container.
     */
    @FromAnyThread
    protected @NotNull VBox getElementContainer() {
        return notNull(elementContainer);
    }

    /**
     * Gets model change consumer.
     *
     * @return the consumer of changes.
     */
    @FromAnyThread
    protected @NotNull ModelChangeConsumer getModelChangeConsumer() {
        return modelChangeConsumer;
    }

    /**
     * Reload this control.
     */
    @FxThread
    public void reload() {

        final PhysicsInfluencer influencer = getInfluencer();
        final VBox root = getElementContainer();
        final ObservableList<Node> children = root.getChildren();

        if (isNeedRebuild(influencer, children.size())) {
            UiUtils.clear(root);
            fillControl(influencer, root);
        }
    }

    /**
     * Is need rebuild boolean.
     *
     * @param influencer   the influencer
     * @param currentCount the current count
     * @return the boolean
     */
    @FxThread
    protected boolean isNeedRebuild(@NotNull final PhysicsInfluencer influencer, final int currentCount) {
        return influencer.getGeometries().size() != currentCount;
    }

    /**
     * Fill this control.
     *
     * @param influencer the influencer
     * @param root       the root
     */
    @FxThread
    protected void fillControl(@NotNull final PhysicsInfluencer influencer, @NotNull final VBox root) {

        final GeometryList geometries = influencer.getGeometries();

        for (int i = 0, length = geometries.size(); i < length; i++) {

            final Geometry geometry = geometries.get(i);
            final Label label = new Label(Messages.MODEL_PROPERTY_GEOMETRY + ": " + geometry.getName());
            label.prefWidthProperty().bind(widthProperty());

            FXUtils.addToPane(label, root);
        }
    }

    /**
     * Handle removing last interpolation.
     */
    @FromAnyThread
    protected void processRemove() {

        final PhysicsInfluencer influencer = getInfluencer();
        final GeometryList geometries = influencer.getGeometries();

        final Geometry geometry = geometries.get(geometries.size() - 1);

        execute(true, false, (physicsInfluencer, needRemove) -> {
            if (needRemove) {
                physicsInfluencer.removeLast();
            } else {
                physicsInfluencer.addCollidable(geometry);
            }
        });
    }

    /**
     * Handle adding new interpolation.
     */
    @FxThread
    protected void processAdd() {

        final ModelChangeConsumer modelChangeConsumer = getModelChangeConsumer();
        final Spatial model = modelChangeConsumer.getCurrentModel();

        final GeometrySelectorDialog dialog = new GeometrySelectorDialog(model, this::processAdd);
        dialog.show();
    }

    /**
     * Handle adding new interpolation.
     *
     * @param geometry the selected geometry.
     */
    @FromAnyThread
    protected void processAdd(@NotNull final Geometry geometry) {
        execute(true, false, (physicsInfluencer, needAdd) -> {
            if (needAdd) {
                physicsInfluencer.addCollidable(geometry);
            } else {
                physicsInfluencer.removeLast();
            }
        });
    }

    /**
     * Execute change operation.
     *
     * @param <T>          the type of value.
     * @param newValue     the new value.
     * @param oldValue     the old value.
     * @param applyHandler the apply handler.
     */
    @FromAnyThread
    protected <T> void execute(@Nullable final T newValue, @Nullable final T oldValue,
                               @NotNull final BiConsumer<PhysicsInfluencer, T> applyHandler) {

        final ParticleInfluencerPropertyOperation<PhysicsInfluencer, T> operation =
                new ParticleInfluencerPropertyOperation<>(influencer, parent, getPropertyName(), newValue, oldValue);
        operation.setApplyHandler(applyHandler);

        final ModelChangeConsumer modelChangeConsumer = getModelChangeConsumer();
        modelChangeConsumer.execute(operation);
    }

    /**
     * Gets property name.
     *
     * @return the property name
     */
    @FromAnyThread
    protected @NotNull String getPropertyName() {
        return getControlTitle();
    }

    @Override
    @FxThread
    public void sync() {
        reload();
    }
}
