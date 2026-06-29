package dev.lightclient.manager;

import dev.lightclient.hud.elements.ArmorHud;
import dev.lightclient.hud.elements.ComboCounterHud;
import dev.lightclient.hud.elements.CoordinatesHud;
import dev.lightclient.hud.elements.CpsHud;
import dev.lightclient.hud.elements.DirectionHud;
import dev.lightclient.hud.elements.FpsHud;
import dev.lightclient.hud.elements.InventoryHud;
import dev.lightclient.hud.elements.KeystrokesHud;
import dev.lightclient.hud.elements.PingHud;
import dev.lightclient.hud.elements.PotionHud;
import dev.lightclient.hud.elements.ReachHud;
import dev.lightclient.hud.elements.SessionStatsHud;
import dev.lightclient.hud.elements.SpeedHud;
import dev.lightclient.hud.elements.TargetHud;
import dev.lightclient.hud.elements.TpsHud;
import dev.lightclient.module.Category;
import dev.lightclient.module.Module;
import dev.lightclient.modules.combat.DamageTintModule;
import dev.lightclient.modules.combat.HitColorModule;
import dev.lightclient.modules.combat.HitParticlesModule;
import dev.lightclient.modules.cosmetics.AnimatedCapesModule;
import dev.lightclient.modules.cosmetics.CapesModule;
import dev.lightclient.modules.cosmetics.CrownsModule;
import dev.lightclient.modules.cosmetics.EmotesModule;
import dev.lightclient.modules.cosmetics.HalosModule;
import dev.lightclient.modules.cosmetics.KillEffectsModule;
import dev.lightclient.modules.cosmetics.TrailsModule;
import dev.lightclient.modules.cosmetics.WingsModule;
import dev.lightclient.modules.misc.AutoGGModule;
import dev.lightclient.modules.misc.AutoTipModule;
import dev.lightclient.modules.misc.DeathCoordsModule;
import dev.lightclient.modules.movement.SprintModule;
import dev.lightclient.modules.movement.ToggleSprintModule;
import dev.lightclient.modules.performance.AdaptiveFpsLimiterModule;
import dev.lightclient.modules.performance.ChunkOptimizationModule;
import dev.lightclient.modules.performance.DynamicFpsModule;
import dev.lightclient.modules.performance.EntityCullingModule;
import dev.lightclient.modules.performance.MemoryOptimizationModule;
import dev.lightclient.modules.performance.ParticleOptimizationModule;
import dev.lightclient.modules.performance.SmartAnimationCullingModule;
import dev.lightclient.modules.player.AutoRespawnModule;
import dev.lightclient.modules.player.FreelookModule;
import dev.lightclient.modules.player.PerspectiveModule;
import dev.lightclient.modules.render.AntiBlindModule;
import dev.lightclient.modules.render.BlockHitAnimationModule;
import dev.lightclient.modules.render.CrosshairModule;
import dev.lightclient.modules.render.FullbrightModule;
import dev.lightclient.modules.render.ItemPhysicsModule;
import dev.lightclient.modules.render.MotionBlurModule;
import dev.lightclient.modules.render.NoFovChangesModule;
import dev.lightclient.modules.render.OldAnimationsModule;
import dev.lightclient.modules.render.ViewBobbingModule;
import dev.lightclient.modules.render.ZoomModule;
import dev.lightclient.modules.utility.BetterChatModule;
import dev.lightclient.modules.utility.BetterTabModule;

import java.util.ArrayList;
import java.util.List;

/** Owns, instantiates and looks up every {@link Module} in the client. */
public final class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public void init() {
        // Combat
        register(new HitColorModule());
        register(new DamageTintModule());
        register(new HitParticlesModule());

        // Movement
        register(new ToggleSprintModule());
        register(new SprintModule());

        // Render
        register(new ZoomModule());
        register(new FullbrightModule());
        register(new CrosshairModule());
        register(new MotionBlurModule());
        register(new OldAnimationsModule());
        register(new BlockHitAnimationModule());
        register(new ItemPhysicsModule());
        register(new ViewBobbingModule());
        register(new NoFovChangesModule());
        register(new AntiBlindModule());

        // Player
        register(new PerspectiveModule());
        register(new FreelookModule());
        register(new AutoRespawnModule());

        // Utility
        register(new BetterChatModule());
        register(new BetterTabModule());

        // Misc
        register(new AutoGGModule());
        register(new AutoTipModule());
        register(new DeathCoordsModule());

        // Performance
        register(new DynamicFpsModule());
        register(new MemoryOptimizationModule());
        register(new AdaptiveFpsLimiterModule());
        register(new EntityCullingModule());
        register(new ChunkOptimizationModule());
        register(new ParticleOptimizationModule());
        register(new SmartAnimationCullingModule());

        // Cosmetics
        register(new CapesModule());
        register(new AnimatedCapesModule());
        register(new WingsModule());
        register(new CrownsModule());
        register(new HalosModule());
        register(new EmotesModule());
        register(new TrailsModule());
        register(new KillEffectsModule());

        // HUD elements
        FpsHud fps = register(new FpsHud());
        CpsHud cps = register(new CpsHud());
        register(new PingHud());
        CoordinatesHud coords = register(new CoordinatesHud());
        register(new DirectionHud());
        register(new SpeedHud());
        register(new TpsHud());
        register(new SessionStatsHud());
        KeystrokesHud keystrokes = register(new KeystrokesHud());
        register(new ArmorHud());
        register(new PotionHud());
        register(new InventoryHud());
        register(new TargetHud());
        register(new ReachHud());
        register(new ComboCounterHud());

        // Enable a sensible default HUD layout on first launch.
        fps.setEnabled(true);
        cps.setEnabled(true);
        coords.setEnabled(true);
        keystrokes.setEnabled(true);
    }

    public <T extends Module> T register(T module) {
        modules.add(module);
        return module;
    }

    public List<Module> getModules() {
        return modules;
    }

    public List<Module> getModules(Category category) {
        List<Module> result = new ArrayList<>();
        for (Module module : modules) {
            if (module.getCategory() == category) {
                result.add(module);
            }
        }
        return result;
    }

    public Module getModule(String name) {
        for (Module module : modules) {
            if (module.getName().equalsIgnoreCase(name)
                    || module.getName().replace(" ", "").equalsIgnoreCase(name.replace(" ", ""))) {
                return module;
            }
        }
        return null;
    }
}
