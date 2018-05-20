package com.ss.editor.tonedog.emitter.control.property.builder;

import com.ss.editor.Messages;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.*;
import com.ss.editor.tonedog.emitter.control.property.control.particle.influencer.interpolation.control.*;
import com.ss.editor.ui.control.property.builder.PropertyBuilder;
import com.ss.editor.ui.control.property.builder.impl.AbstractPropertyBuilder;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.influencers.InterpolatedParticleInfluencer;
import tonegod.emitter.influencers.ParticleInfluencer;
import tonegod.emitter.influencers.impl.*;
import tonegod.emitter.influencers.impl.GravityInfluencer.GravityAlignment;
import tonegod.emitter.influencers.impl.PhysicsInfluencer.CollisionReaction;
import tonegod.emitter.influencers.impl.RadialVelocityInfluencer.RadialPullAlignment;
import tonegod.emitter.influencers.impl.RadialVelocityInfluencer.RadialPullCenter;
import tonegod.emitter.influencers.impl.RadialVelocityInfluencer.RadialUpAlignment;

/**
 * The implementation of the {@link PropertyBuilder} to build property controls for {@link ParticleInfluencer}.
 *
 * @author JavaSaBr
 */
public class ParticleInfluencerPropertyBuilder extends AbstractPropertyBuilder<ModelChangeConsumer> {

    private static final GravityAlignment[] GRAVITY_ALIGNMENTS = GravityAlignment.values();
    private static final CollisionReaction[] COLLISION_REACTIONS = CollisionReaction.values();
    private static final RadialPullCenter[] RADIAL_PULL_CENTERS = RadialPullCenter.values();
    private static final RadialPullAlignment[] RADIAL_PULL_ALIGNMENTS = RadialPullAlignment.values();
    private static final RadialUpAlignment[] RADIAL_UP_ALIGNMENTS = RadialUpAlignment.values();

    private static final PropertyBuilder INSTANCE = new ParticleInfluencerPropertyBuilder();

    @FxThread
    public static @NotNull PropertyBuilder getInstance() {
        return INSTANCE;
    }

    private ParticleInfluencerPropertyBuilder() {
        super(ModelChangeConsumer.class);
    }

    @Override
    @FxThread
    protected void buildForImpl(
            @NotNull Object object,
            @Nullable Object parent,
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer
    ) {

        if (!(object instanceof ParticleInfluencer) || parent == null) {
            return;
        }

        if (object instanceof AlphaInfluencer) {
            createControls(container, changeConsumer, (AlphaInfluencer) object, parent);
        } else if (object instanceof ColorInfluencer) {
            createControls(container, changeConsumer, (ColorInfluencer) object, parent);
        } else if (object instanceof SizeInfluencer) {
            createControls(container, changeConsumer, (SizeInfluencer) object, parent);
        } else if (object instanceof DestinationInfluencer) {
            createControls(container, changeConsumer, (DestinationInfluencer) object, parent);
        } else if (object instanceof ImpulseInfluencer) {
            createControls(container, changeConsumer, (ImpulseInfluencer) object, parent);
        } else if (object instanceof GravityInfluencer) {
            createControls(container, changeConsumer, (GravityInfluencer) object, parent);
        } else if (object instanceof RadialVelocityInfluencer) {
            createControls(container, changeConsumer, (RadialVelocityInfluencer) object, parent);
        } else if (object instanceof RotationInfluencer) {
            createControls(container, changeConsumer, (RotationInfluencer) object, parent);
        } else if (object instanceof PhysicsInfluencer) {
            createControls(container, changeConsumer, (PhysicsInfluencer) object, parent);
        } else if (object instanceof SpriteInfluencer) {
            createControls(container, changeConsumer, (SpriteInfluencer) object, parent);
        }

        if (object instanceof InterpolatedParticleInfluencer) {
            createControls(container, changeConsumer, (InterpolatedParticleInfluencer) object, parent);
        }
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull InterpolatedParticleInfluencer influencer,
            @NotNull Object parent
    ) {

        var fixedDuration = influencer.getFixedDuration();

        var fixedDurationControl = new FloatParticleInfluencerPropertyControl<InterpolatedParticleInfluencer>(
                fixedDuration, Messages.MODEL_PROPERTY_FIXED_DURATION, changeConsumer, parent);

        fixedDurationControl.setSyncHandler(InterpolatedParticleInfluencer::getFixedDuration);
        fixedDurationControl.setApplyHandler(InterpolatedParticleInfluencer::setFixedDuration);
        fixedDurationControl.setEditObject(influencer);

        FxUtils.addChild(container, fixedDurationControl);
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull AlphaInfluencer influencer,
            @NotNull Object parent
    ) {

        var influencerControl = new AlphaInfluencerControl(changeConsumer, influencer, parent);
        influencerControl.reload();

        FxUtils.addChild(container, influencerControl);

        buildSplitLine(container);
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull ColorInfluencer influencer,
            @NotNull Object parent
    ) {

        var randomStartColor = influencer.isRandomStartColor();

        var colorControl = new ColorInfluencerControl(changeConsumer, influencer, parent);
        colorControl.reload();

        var randomStartColorControl = new BooleanParticleInfluencerPropertyControl<ColorInfluencer>(randomStartColor,
                Messages.MODEL_PROPERTY_IS_RANDOM_START_COLOR, changeConsumer, parent);

        randomStartColorControl.setSyncHandler(ColorInfluencer::isRandomStartColor);
        randomStartColorControl.setApplyHandler(ColorInfluencer::setRandomStartColor);
        randomStartColorControl.setEditObject(influencer);

        FxUtils.addChild(container, colorControl);

        buildSplitLine(container);

        FxUtils.addChild(container, randomStartColorControl);
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull SizeInfluencer influencer,
            @NotNull Object parent
    ) {

        var randomSize = influencer.isRandomSize();
        var randomSizeTolerance = influencer.getRandomSizeTolerance();

        var sizeControl = new SizeInfluencerControl(changeConsumer, influencer, parent);
        sizeControl.reload();

        var randomStartSizeControl = new BooleanParticleInfluencerPropertyControl<SizeInfluencer>(randomSize,
                Messages.MODEL_PROPERTY_IS_RANDOM_START_SIZE, changeConsumer, parent);

        randomStartSizeControl.setSyncHandler(SizeInfluencer::isRandomSize);
        randomStartSizeControl.setApplyHandler(SizeInfluencer::setRandomSize);
        randomStartSizeControl.setEditObject(influencer);

        var sizeVariationToleranceControl = new FloatParticleInfluencerPropertyControl<SizeInfluencer>(
                randomSizeTolerance, Messages.MODEL_PROPERTY_SIZE_VARIATION_FACTOR, changeConsumer, parent);

        sizeVariationToleranceControl.setSyncHandler(SizeInfluencer::getRandomSizeTolerance);
        sizeVariationToleranceControl.setApplyHandler(SizeInfluencer::setRandomSizeTolerance);
        sizeVariationToleranceControl.setEditObject(influencer);

        FxUtils.addChild(container, sizeControl);

        buildSplitLine(container);

        FxUtils.addChild(container, randomStartSizeControl, sizeVariationToleranceControl);
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull SpriteInfluencer influencer,
            @NotNull Object parent
    ) {

        var frameSequence = influencer.getFrameSequence();
        var fixedDuration = influencer.getFixedDuration();
        var animate = influencer.isAnimate();
        var randomStartImage = influencer.isRandomStartImage();

        var frameSequenceControl = new IntArrayParticleInfluencerPropertyControl<SpriteInfluencer>(frameSequence,
                Messages.MODEL_PROPERTY_FRAME_SEQUENCE, changeConsumer, parent);

        frameSequenceControl.setSyncHandler(SpriteInfluencer::getFrameSequence);
        frameSequenceControl.setApplyHandler(SpriteInfluencer::setFrameSequence);
        frameSequenceControl.setEditObject(influencer);

        var randomStartImageControl = new BooleanParticleInfluencerPropertyControl<SpriteInfluencer>(randomStartImage,
                Messages.MODEL_PROPERTY_IS_RANDOM_START_IMAGE, changeConsumer, parent);

        randomStartImageControl.setSyncHandler(SpriteInfluencer::isRandomStartImage);
        randomStartImageControl.setApplyHandler(SpriteInfluencer::setRandomStartImage);
        randomStartImageControl.setEditObject(influencer);

        var animateControl = new BooleanParticleInfluencerPropertyControl<SpriteInfluencer>(animate,
                Messages.MODEL_PROPERTY_IS_ANIMATE, changeConsumer, parent);

        animateControl.setSyncHandler(SpriteInfluencer::isAnimate);
        animateControl.setApplyHandler(SpriteInfluencer::setAnimate);
        animateControl.setEditObject(influencer);

        var fixedDurationControl = new FloatParticleInfluencerPropertyControl<SpriteInfluencer>(fixedDuration,
                Messages.MODEL_PROPERTY_FIXED_DURATION, changeConsumer, parent);

        fixedDurationControl.setSyncHandler(SpriteInfluencer::getFixedDuration);
        fixedDurationControl.setApplyHandler(SpriteInfluencer::setFixedDuration);
        fixedDurationControl.setEditObject(influencer);

        FxUtils.addChild(container, frameSequenceControl, randomStartImageControl,
                animateControl, fixedDurationControl);
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull DestinationInfluencer influencer,
            @NotNull Object parent
    ) {

        var randomStartDestination = influencer.isRandomStartDestination();

        var influencerControl = new DestinationInfluencerControl(changeConsumer, influencer, parent);
        influencerControl.reload();

        var randomStartDestinationControl = new BooleanParticleInfluencerPropertyControl<DestinationInfluencer>(
                randomStartDestination, Messages.MODEL_PROPERTY_IS_RANDOM_START_DESTINATION, changeConsumer, parent);

        randomStartDestinationControl.setSyncHandler(DestinationInfluencer::isRandomStartDestination);
        randomStartDestinationControl.setApplyHandler(DestinationInfluencer::setRandomStartDestination);
        randomStartDestinationControl.setEditObject(influencer);

        FxUtils.addChild(container, influencerControl);

        buildSplitLine(container);

        FxUtils.addChild(container, randomStartDestinationControl);
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull ImpulseInfluencer influencer,
            @NotNull Object parent
    ) {

        var chance = influencer.getChance();
        var strength = influencer.getStrength();
        var magnitude = influencer.getMagnitude();

        var chanceControl = new FloatParticleInfluencerPropertyControl<ImpulseInfluencer>(chance,
                Messages.MODEL_PROPERTY_CHANCE, changeConsumer, parent);

        chanceControl.setSyncHandler(ImpulseInfluencer::getChance);
        chanceControl.setApplyHandler(ImpulseInfluencer::setChance);
        chanceControl.setEditObject(influencer);

        var strengthControl = new FloatParticleInfluencerPropertyControl<ImpulseInfluencer>(strength,
                Messages.MODEL_PROPERTY_STRENGTH, changeConsumer, parent);

        strengthControl.setSyncHandler(ImpulseInfluencer::getStrength);
        strengthControl.setApplyHandler(ImpulseInfluencer::setStrength);
        strengthControl.setEditObject(influencer);

        var magnitudeControl = new FloatParticleInfluencerPropertyControl<ImpulseInfluencer>(magnitude,
                Messages.MODEL_PROPERTY_MAGNITUDE, changeConsumer, parent);

        magnitudeControl.setSyncHandler(ImpulseInfluencer::getMagnitude);
        magnitudeControl.setApplyHandler(ImpulseInfluencer::setMagnitude);
        magnitudeControl.setEditObject(influencer);

        FxUtils.addChild(container, chanceControl, strengthControl, magnitudeControl);
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull GravityInfluencer influencer,
            @NotNull Object parent
    ) {

        var gravity = influencer.getGravity().clone();
        var alignment = influencer.getAlignment();
        var magnitude = influencer.getMagnitude();

        var gravityControl = new Vector3fParticleInfluencerPropertyControl<GravityInfluencer>(gravity,
                Messages.MODEL_PROPERTY_GRAVITY, changeConsumer, parent);

        gravityControl.setSyncHandler(GravityInfluencer::getGravity);
        gravityControl.setApplyHandler(GravityInfluencer::setGravity);
        gravityControl.setEditObject(influencer);

        var gravityAlignmentControl = new EnumParticleInfluencerEmitterPropertyControl<GravityInfluencer, GravityAlignment>(
                alignment, Messages.MODEL_PROPERTY_ALIGNMENT, changeConsumer, GRAVITY_ALIGNMENTS, parent);

        gravityAlignmentControl.setSyncHandler(GravityInfluencer::getAlignment);
        gravityAlignmentControl.setApplyHandler(GravityInfluencer::setAlignment);
        gravityAlignmentControl.setEditObject(influencer);

        var magnitudeControl = new FloatParticleInfluencerPropertyControl<GravityInfluencer>(magnitude,
                Messages.MODEL_PROPERTY_MAGNITUDE, changeConsumer, parent);

        magnitudeControl.setSyncHandler(GravityInfluencer::getMagnitude);
        magnitudeControl.setApplyHandler(GravityInfluencer::setMagnitude);
        magnitudeControl.setEditObject(influencer);

        FxUtils.addChild(container, gravityAlignmentControl, magnitudeControl, gravityControl);
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull PhysicsInfluencer influencer,
            @NotNull Object parent
    ) {

        var collisionReaction = influencer.getCollisionReaction();
        var restitution = influencer.getRestitution();

        var nodeListControl = new PhysicsNodeListControl(changeConsumer, influencer, parent);
        nodeListControl.reload();

        var reactionControl = new EnumParticleInfluencerEmitterPropertyControl<PhysicsInfluencer, CollisionReaction>(
                collisionReaction, Messages.MODEL_PROPERTY_REACTION, changeConsumer, COLLISION_REACTIONS, parent);

        reactionControl.setSyncHandler(PhysicsInfluencer::getCollisionReaction);
        reactionControl.setApplyHandler(PhysicsInfluencer::setCollisionReaction);
        reactionControl.setEditObject(influencer);

        var restitutionControl = new FloatParticleInfluencerPropertyControl<PhysicsInfluencer>(restitution,
                Messages.MODEL_PROPERTY_RESTITUTION, changeConsumer, parent);

        restitutionControl.setSyncHandler(PhysicsInfluencer::getRestitution);
        restitutionControl.setApplyHandler(PhysicsInfluencer::setRestitution);
        restitutionControl.setEditObject(influencer);

        FxUtils.addChild(container, nodeListControl);

        buildSplitLine(container);

        FxUtils.addChild(container, reactionControl, restitutionControl);
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull RadialVelocityInfluencer influencer,
            @NotNull Object parent
    ) {

        var pullCenter = influencer.getRadialPullCenter();
        var pullAlignment = influencer.getRadialPullAlignment();
        var upAlignment = influencer.getRadialUpAlignment();
        var tangentForce = influencer.getTangentForce();
        var radialPull = influencer.getRadialPull();
        var randomDirection = influencer.isRandomDirection();

        var randomDirectionControl = new BooleanParticleInfluencerPropertyControl<RadialVelocityInfluencer>(
                randomDirection, Messages.MODEL_PROPERTY_IS_RANDOM_DIRECTION, changeConsumer, parent);

        randomDirectionControl.setSyncHandler(RadialVelocityInfluencer::isRandomDirection);
        randomDirectionControl.setApplyHandler(RadialVelocityInfluencer::setRandomDirection);
        randomDirectionControl.setEditObject(influencer);

        var pullCenterControl = new EnumParticleInfluencerEmitterPropertyControl<RadialVelocityInfluencer, RadialPullCenter>(
                pullCenter, Messages.MODEL_PROPERTY_PULL_CENTER, changeConsumer, RADIAL_PULL_CENTERS, parent);

        pullCenterControl.setSyncHandler(RadialVelocityInfluencer::getRadialPullCenter);
        pullCenterControl.setApplyHandler(RadialVelocityInfluencer::setRadialPullCenter);
        pullCenterControl.setEditObject(influencer);

        var pullAlignmentControl = new EnumParticleInfluencerEmitterPropertyControl<RadialVelocityInfluencer, RadialPullAlignment>(
                pullAlignment, Messages.MODEL_PROPERTY_PULL_ALIGNMENT, changeConsumer, RADIAL_PULL_ALIGNMENTS, parent);

        pullAlignmentControl.setSyncHandler(RadialVelocityInfluencer::getRadialPullAlignment);
        pullAlignmentControl.setApplyHandler(RadialVelocityInfluencer::setRadialPullAlignment);
        pullAlignmentControl.setEditObject(influencer);

        var upAlignmentControl = new EnumParticleInfluencerEmitterPropertyControl<RadialVelocityInfluencer, RadialUpAlignment>(
                upAlignment, Messages.MODEL_PROPERTY_UP_ALIGNMENT, changeConsumer, RADIAL_UP_ALIGNMENTS, parent);

        upAlignmentControl.setSyncHandler(RadialVelocityInfluencer::getRadialUpAlignment);
        upAlignmentControl.setApplyHandler(RadialVelocityInfluencer::setRadialUpAlignment);
        upAlignmentControl.setEditObject(influencer);

        var radialPullControl = new FloatParticleInfluencerPropertyControl<RadialVelocityInfluencer>(radialPull,
                Messages.MODEL_PROPERTY_RADIAL_PULL, changeConsumer, parent);

        radialPullControl.setSyncHandler(RadialVelocityInfluencer::getRadialPull);
        radialPullControl.setApplyHandler(RadialVelocityInfluencer::setRadialPull);
        radialPullControl.setEditObject(influencer);

        var tangentForceControl = new FloatParticleInfluencerPropertyControl<RadialVelocityInfluencer>(tangentForce,
                Messages.MODEL_PROPERTY_TANGENT_FORCE, changeConsumer, parent);

        tangentForceControl.setSyncHandler(RadialVelocityInfluencer::getTangentForce);
        tangentForceControl.setApplyHandler(RadialVelocityInfluencer::setTangentForce);
        tangentForceControl.setEditObject(influencer);

        FxUtils.addChild(container, randomDirectionControl, pullCenterControl, pullAlignmentControl,
                upAlignmentControl, radialPullControl, tangentForceControl);
    }

    /**
     * Create controls.
     *
     * @param container      the container.
     * @param changeConsumer the change consumer.
     * @param influencer     the influencer.
     * @param parent         the parent.
     */
    @FxThread
    private void createControls(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull RotationInfluencer influencer,
            @NotNull Object parent
    ) {

        var randomDirection = influencer.isRandomDirection();
        var randomSpeed = influencer.isRandomSpeed();
        var randomStartRotationX = influencer.isRandomStartRotationX();
        var randomStartRotationY = influencer.isRandomStartRotationY();
        var randomStartRotationZ = influencer.isRandomStartRotationZ();

        var influencerControl = new RotationInfluencerControl(changeConsumer, influencer, parent);
        influencerControl.reload();

        var randomDirectionControl = new BooleanParticleInfluencerPropertyControl<RotationInfluencer>(randomDirection,
                Messages.MODEL_PROPERTY_IS_RANDOM_DIRECTION, changeConsumer, parent);

        randomDirectionControl.setSyncHandler(RotationInfluencer::isRandomDirection);
        randomDirectionControl.setApplyHandler(RotationInfluencer::setRandomDirection);
        randomDirectionControl.setEditObject(influencer);

        var randomSpeedControl = new BooleanParticleInfluencerPropertyControl<RotationInfluencer>(randomSpeed,
                Messages.MODEL_PROPERTY_IS_RANDOM_SPEED, changeConsumer, parent);

        randomSpeedControl.setSyncHandler(RotationInfluencer::isRandomSpeed);
        randomSpeedControl.setApplyHandler(RotationInfluencer::setRandomSpeed);
        randomSpeedControl.setEditObject(influencer);

        var randomStartRotationXControl = new BooleanParticleInfluencerPropertyControl<RotationInfluencer>(
                randomStartRotationX, Messages.MODEL_PROPERTY_IS_START_RANDOM_ROTATION_X + " X", changeConsumer, parent);

        randomStartRotationXControl.setSyncHandler(RotationInfluencer::isRandomStartRotationX);
        randomStartRotationXControl.setApplyHandler(RotationInfluencer::setRandomStartRotationX);
        randomStartRotationXControl.setEditObject(influencer);

        var randomStartRotationYControl = new BooleanParticleInfluencerPropertyControl<RotationInfluencer>(
                randomStartRotationY, Messages.MODEL_PROPERTY_IS_START_RANDOM_ROTATION_X + " Y", changeConsumer, parent);

        randomStartRotationYControl.setSyncHandler(RotationInfluencer::isRandomStartRotationY);
        randomStartRotationYControl.setApplyHandler(RotationInfluencer::setRandomStartRotationY);
        randomStartRotationYControl.setEditObject(influencer);

        var randomStartRotationZControl = new BooleanParticleInfluencerPropertyControl<RotationInfluencer>(
                randomStartRotationZ, Messages.MODEL_PROPERTY_IS_START_RANDOM_ROTATION_X + " Z", changeConsumer, parent);

        randomStartRotationZControl.setSyncHandler(RotationInfluencer::isRandomStartRotationZ);
        randomStartRotationZControl.setApplyHandler(RotationInfluencer::setRandomStartRotationZ);
        randomStartRotationZControl.setEditObject(influencer);

        FxUtils.addChild(container, influencerControl);

        buildSplitLine(container);

        FxUtils.addChild(container, randomDirectionControl, randomSpeedControl, randomStartRotationXControl,
                randomStartRotationYControl, randomStartRotationZControl);
    }
}
