package com.ss.editor.tonedog.emitter.control.property.builder;

import com.jme3.math.Vector2f;
import com.ss.editor.Messages;
import com.ss.editor.annotation.FromAnyThread;
import com.ss.editor.annotation.FxThread;
import com.ss.editor.model.undo.editor.ModelChangeConsumer;
import com.ss.editor.tonedog.emitter.control.property.control.particle.ParticleEmitterMaterialPropertyControl;
import com.ss.editor.tonedog.emitter.control.property.control.particle.ParticleEmitterSpriteCountModelPropertyControl;
import com.ss.editor.ui.control.property.builder.PropertyBuilder;
import com.ss.editor.ui.control.property.builder.impl.AbstractPropertyBuilder;
import com.ss.editor.ui.control.property.builder.impl.SpatialPropertyBuilder;
import com.ss.editor.ui.control.property.impl.*;
import com.ss.rlib.fx.util.FxUtils;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tonegod.emitter.BillboardMode;
import tonegod.emitter.EmissionPoint;
import tonegod.emitter.EmitterMesh.DirectionType;
import tonegod.emitter.ParticleEmitterNode;

import java.util.function.BiConsumer;

/**
 * The implementation of the {@link PropertyBuilder} to build property controls for {@link ParticleEmitterNode}.
 *
 * @author JavaSaBr
 */
public class ParticleEmitterPropertyBuilder extends AbstractPropertyBuilder<ModelChangeConsumer> {

    public static final int PRIORITY = SpatialPropertyBuilder.PRIORITY + 1;

    private static final DirectionType[] DIRECTION_TYPES = DirectionType.values();
    private static final EmissionPoint[] PARTICLE_EMISSION_POINTS = EmissionPoint.values();
    private static final BillboardMode[] BILLBOARD_MODES = BillboardMode.values();

    @FunctionalInterface
    private interface ChangeHandler<T> extends BiConsumer<ParticleEmitterNode, T> {

        @Override
        default void accept(ParticleEmitterNode emitter, T value) {
            acceptImpl(emitter, value);
            emitter.killAllParticles();
        }

        void acceptImpl(ParticleEmitterNode emitter, T value);
    }

    private static final ChangeHandler<Boolean> FOLLOW_EMITTER_HANDLER = ParticleEmitterNode::setParticlesFollowEmitter;
    private static final ChangeHandler<Boolean> VELOCITY_STRETCHING_HANDLER = ParticleEmitterNode::setVelocityStretching;
    private static final ChangeHandler<Boolean> RANDOM_EMISSION_POINT_HANDLER = ParticleEmitterNode::setRandomEmissionPoint;
    private static final ChangeHandler<Boolean> SEQUENTIAL_EMISSION_FACE_HANDLER = ParticleEmitterNode::setSequentialEmissionFace;
    private static final ChangeHandler<DirectionType> DIRECTION_TYPE_HANDLER = ParticleEmitterNode::setDirectionType;
    private static final ChangeHandler<Boolean> SEQUENTIAL_SKIP_PATTERN_HANDLER = ParticleEmitterNode::setSequentialSkipPattern;
    private static final ChangeHandler<EmissionPoint> EMISSION_POINT_HANDLER = ParticleEmitterNode::setEmissionPoint;
    private static final ChangeHandler<Float> EMISSIONS_PER_SECOND_HANDLER = ParticleEmitterNode::setEmissionsPerSecond;
    private static final ChangeHandler<Integer> PARTICLES_PER_EMISSION_HANDLER = ParticleEmitterNode::setParticlesPerEmission;
    private static final ChangeHandler<Vector2f> FORCE_MIN_MAX_HANDLER = ParticleEmitterNode::setForceMinMax;
    private static final ChangeHandler<Vector2f> LIFE_MIN_MAX_HANDLER = ParticleEmitterNode::setLifeMinMax;

    private static final PropertyBuilder INSTANCE = new ParticleEmitterPropertyBuilder();

    @FromAnyThread
    public static @NotNull PropertyBuilder getInstance() {
        return INSTANCE;
    }

    private ParticleEmitterPropertyBuilder() {
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

        if (!(object instanceof ParticleEmitterNode)) {
            return;
        }

        buildFor(container, changeConsumer, (ParticleEmitterNode) object);
        buildSplitLine(container);
    }

    @FxThread
    private void buildFor(
            @NotNull VBox container,
            @NotNull ModelChangeConsumer changeConsumer,
            @NotNull ParticleEmitterNode emitterNode
    ) {

        var particlesMaterial = emitterNode.getParticlesMaterial();

        var testEmitter = emitterNode.isEnabledTestEmitter();
        var testParticles = emitterNode.isEnabledTestParticles();
        var enabled = emitterNode.isEnabled();
        var randomEmissionPoint = emitterNode.isRandomEmissionPoint();
        var sequentialEmissionFace = emitterNode.isSequentialEmissionFace();
        var skipPattern = emitterNode.isSequentialSkipPattern();
        var particlesFollowEmitter = emitterNode.isParticlesFollowEmitter();
        var velocityStretching = emitterNode.isVelocityStretching();

        var maxParticles = emitterNode.getMaxParticles();
        var particlesPerEmission = emitterNode.getParticlesPerEmission();

        var emissionsPerSecond = emitterNode.getEmissionsPerSecond();
        var emitterLife = emitterNode.getEmitterLife();
        var emitterDelay = emitterNode.getEmitterDelay();
        var stretchFactor = emitterNode.getVelocityStretchFactor();

        var directionType = emitterNode.getDirectionType();
        var billboardMode = emitterNode.getBillboardMode();
        var emissionPoint = emitterNode.getEmissionPoint();

        var forceMinMax = emitterNode.getForceMinMax();
        var lifeMinMax = emitterNode.getLifeMinMax();
        var spriteCount = emitterNode.getSpriteCount();

        var testEmitterControl = new BooleanPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(testEmitter,
                Messages.MODEL_PROPERTY_IS_TEST_MODE, changeConsumer);

        testEmitterControl.setApplyHandler(ParticleEmitterNode::setEnabledTestEmitter);
        testEmitterControl.setSyncHandler(ParticleEmitterNode::isEnabledTestEmitter);
        testEmitterControl.setEditObject(emitterNode);

        var testParticlesControl = new BooleanPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(testParticles,
                Messages.MODEL_PROPERTY_IS_TEST_PARTICLES, changeConsumer);

        testParticlesControl.setApplyHandler(ParticleEmitterNode::setEnabledTestParticles);
        testParticlesControl.setSyncHandler(ParticleEmitterNode::isEnabledTestParticles);
        testParticlesControl.setEditObject(emitterNode);

        var enableControl = new BooleanPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(enabled,
                Messages.MODEL_PROPERTY_IS_ENABLED, changeConsumer);

        enableControl.setApplyHandler(ParticleEmitterNode::setEnabled);
        enableControl.setSyncHandler(ParticleEmitterNode::isEnabled);
        enableControl.setEditObject(emitterNode);

        var particlesFollowEmitControl = new BooleanPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(
                particlesFollowEmitter, Messages.MODEL_PROPERTY_IS_FOLLOW_EMITTER, changeConsumer);

        particlesFollowEmitControl.setApplyHandler(FOLLOW_EMITTER_HANDLER);
        particlesFollowEmitControl.setSyncHandler(ParticleEmitterNode::isParticlesFollowEmitter);
        particlesFollowEmitControl.setEditObject(emitterNode);

        var particlesStretchingControl = new BooleanPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(
                velocityStretching, Messages.MODEL_PROPERTY_STRETCHING, changeConsumer);

        particlesStretchingControl.setApplyHandler(VELOCITY_STRETCHING_HANDLER);
        particlesStretchingControl.setSyncHandler(ParticleEmitterNode::isVelocityStretching);
        particlesStretchingControl.setEditObject(emitterNode);

        var randomPointControl = new BooleanPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(
                randomEmissionPoint, Messages.MODEL_PROPERTY_IS_RANDOM_POINT, changeConsumer);

        randomPointControl.setApplyHandler(RANDOM_EMISSION_POINT_HANDLER);
        randomPointControl.setSyncHandler(ParticleEmitterNode::isRandomEmissionPoint);
        randomPointControl.setEditObject(emitterNode);

        var sequentialFaceControl = new BooleanPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(
                sequentialEmissionFace, Messages.MODEL_PROPERTY_IS_SEQUENTIAL_FACE, changeConsumer);

        sequentialFaceControl.setApplyHandler(SEQUENTIAL_EMISSION_FACE_HANDLER);
        sequentialFaceControl.setSyncHandler(ParticleEmitterNode::isSequentialEmissionFace);
        sequentialFaceControl.setEditObject(emitterNode);

        var skipPatternControl = new BooleanPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(skipPattern,
                Messages.MODEL_PROPERTY_IS_SKIP_PATTERN, changeConsumer);

        skipPatternControl.setApplyHandler(SEQUENTIAL_SKIP_PATTERN_HANDLER);
        skipPatternControl.setSyncHandler(ParticleEmitterNode::isSequentialSkipPattern);
        skipPatternControl.setEditObject(emitterNode);

        var directionTypeControl = new EnumPropertyControl<ModelChangeConsumer, ParticleEmitterNode, DirectionType>(
                directionType, Messages.MODEL_PROPERTY_DIRECTION_TYPE, changeConsumer, DIRECTION_TYPES);

        directionTypeControl.setApplyHandler(DIRECTION_TYPE_HANDLER);
        directionTypeControl.setSyncHandler(ParticleEmitterNode::getDirectionType);
        directionTypeControl.setEditObject(emitterNode);

        var emissionPointControl = new EnumPropertyControl<ModelChangeConsumer, ParticleEmitterNode, EmissionPoint>(
                emissionPoint, Messages.MODEL_PROPERTY_EMISSION_POINT, changeConsumer, PARTICLE_EMISSION_POINTS);

        emissionPointControl.setApplyHandler(EMISSION_POINT_HANDLER);
        emissionPointControl.setSyncHandler(ParticleEmitterNode::getEmissionPoint);
        emissionPointControl.setEditObject(emitterNode);

        var billboardModeControl = new EnumPropertyControl<ModelChangeConsumer, ParticleEmitterNode, BillboardMode>(
                billboardMode, Messages.MODEL_PROPERTY_BILLBOARD, changeConsumer, BILLBOARD_MODES);

        billboardModeControl.setApplyHandler(ParticleEmitterNode::setBillboardMode);
        billboardModeControl.setSyncHandler(ParticleEmitterNode::getBillboardMode);
        billboardModeControl.setEditObject(emitterNode);

        var maxParticlesControl = new IntegerPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(maxParticles,
                Messages.MODEL_PROPERTY_MAX_PARTICLES, changeConsumer);

        maxParticlesControl.setApplyHandler(ParticleEmitterNode::setMaxParticles);
        maxParticlesControl.setSyncHandler(ParticleEmitterNode::getMaxParticles);
        maxParticlesControl.setEditObject(emitterNode);

        var emissionPerSecControl = new FloatPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(
                emissionsPerSecond, Messages.MODEL_PROPERTY_EMISSION_PER_SECOND, changeConsumer);

        emissionPerSecControl.setApplyHandler(EMISSIONS_PER_SECOND_HANDLER);
        emissionPerSecControl.setSyncHandler(ParticleEmitterNode::getEmissionsPerSecond);
        emissionPerSecControl.setMinMax(0.1F, (float) Integer.MAX_VALUE);
        emissionPerSecControl.setScrollPower(3F);
        emissionPerSecControl.setEditObject(emitterNode);

        var particlesPerEmissionControl = new IntegerPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(
                particlesPerEmission, Messages.MODEL_PROPERTY_PARTICLES_PER_SECOND, changeConsumer);

        particlesPerEmissionControl.setApplyHandler(PARTICLES_PER_EMISSION_HANDLER);
        particlesPerEmissionControl.setSyncHandler(ParticleEmitterNode::getParticlesPerEmission);
        particlesPerEmissionControl.setEditObject(emitterNode);

        var emitterLifeControl = new FloatPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(emitterLife,
                Messages.MODEL_PROPERTY_EMITTER_LIFE, changeConsumer);

        emitterLifeControl.setApplyHandler(ParticleEmitterNode::setEmitterLife);
        emitterLifeControl.setSyncHandler(ParticleEmitterNode::getEmitterLife);
        emitterLifeControl.setEditObject(emitterNode);

        var emitterDelayControl = new FloatPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(emitterDelay,
                Messages.MODEL_PROPERTY_EMITTER_DELAY, changeConsumer);

        emitterDelayControl.setApplyHandler(ParticleEmitterNode::setEmitterDelay);
        emitterDelayControl.setSyncHandler(ParticleEmitterNode::getEmitterDelay);
        emitterDelayControl.setEditObject(emitterNode);

        var magnitudeControl = new FloatPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(stretchFactor,
                Messages.MODEL_PROPERTY_MAGNITUDE, changeConsumer);

        magnitudeControl.setApplyHandler(ParticleEmitterNode::setVelocityStretchFactor);
        magnitudeControl.setSyncHandler(ParticleEmitterNode::getVelocityStretchFactor);
        magnitudeControl.setEditObject(emitterNode);

        var materialControl = new ParticleEmitterMaterialPropertyControl(particlesMaterial,
                Messages.MODEL_PROPERTY_MATERIAL, changeConsumer);

        materialControl.setApplyHandler(ParticleEmitterNode::setParticlesMaterial);
        materialControl.setSyncHandler(ParticleEmitterNode::getParticlesMaterial);
        materialControl.setEditObject(emitterNode);

        var forceMinMaxControl = new MinMaxPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(forceMinMax,
                Messages.MODEL_PROPERTY_INITIAL_FORCE, changeConsumer);

        forceMinMaxControl.setApplyHandler(FORCE_MIN_MAX_HANDLER);
        forceMinMaxControl.setSyncHandler(ParticleEmitterNode::getForceMinMax);
        forceMinMaxControl.setEditObject(emitterNode);

        var lifeMinMaxControl = new MinMaxPropertyControl<ModelChangeConsumer, ParticleEmitterNode>(lifeMinMax,
                Messages.MODEL_PROPERTY_LIFE, changeConsumer);

        lifeMinMaxControl.setApplyHandler(LIFE_MIN_MAX_HANDLER);
        lifeMinMaxControl.setSyncHandler(ParticleEmitterNode::getLifeMinMax);
        lifeMinMaxControl.setEditObject(emitterNode);

        var spriteCountControl = new ParticleEmitterSpriteCountModelPropertyControl(spriteCount,
                Messages.MODEL_PROPERTY_SPRITE_COUNT, changeConsumer);

        spriteCountControl.setApplyHandler(ParticleEmitterNode::setSpriteCount);
        spriteCountControl.setSyncHandler(ParticleEmitterNode::getSpriteCount);
        spriteCountControl.setEditObject(emitterNode);

        FxUtils.addChild(container, enableControl, testEmitterControl, testParticlesControl, randomPointControl,
                sequentialFaceControl, skipPatternControl, particlesFollowEmitControl, particlesStretchingControl,
                directionTypeControl, emissionPointControl, billboardModeControl, maxParticlesControl, 
                emissionPerSecControl, particlesPerEmissionControl, emitterLifeControl, emitterDelayControl,
                magnitudeControl, spriteCountControl, forceMinMaxControl, lifeMinMaxControl);
        
        buildSplitLine(container);

        FxUtils.addChild(container, materialControl);
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }
}
