package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import net.minecraft.world.entity.EntityAgeable;
import net.minecraft.world.entity.EntityLiving;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumCreatureType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.VillagerProfession;

public class Behaviors {

    private static final float STROLL_SPEED_MODIFIER = 0.4F;

    public Behaviors() {}

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityVillager>>> getCorePackage(VillagerProfession villagerprofession, float f) {
        return ImmutableList.of(Pair.of(0, new BehaviorSwim(0.8F)), Pair.of(0, new BehaviorInteractDoor()), Pair.of(0, new BehaviorLook(45, 90)), Pair.of(0, new BehaviorPanic()), Pair.of(0, new BehaviorWake()), Pair.of(0, new BehaviorBellAlert()), Pair.of(0, new BehaviorRaid()), Pair.of(0, new BehaviorPositionValidate(villagerprofession.heldJobSite(), MemoryModuleType.JOB_SITE)), Pair.of(0, new BehaviorPositionValidate(villagerprofession.acquirableJobSite(), MemoryModuleType.POTENTIAL_JOB_SITE)), Pair.of(1, new BehavorMove()), Pair.of(2, new BehaviorBetterJob(villagerprofession)), Pair.of(3, new BehaviorInteractPlayer(f)), new Pair[]{Pair.of(5, new BehaviorFindAdmirableItem<>(f, false, 4)), Pair.of(6, new BehaviorFindPosition(villagerprofession.acquirableJobSite(), MemoryModuleType.JOB_SITE, MemoryModuleType.POTENTIAL_JOB_SITE, true, Optional.empty())), Pair.of(7, new BehaviorPotentialJobSite(f)), Pair.of(8, new BehaviorLeaveJob(f)), Pair.of(10, new BehaviorFindPosition((holder) -> {
                    return holder.is(PoiTypes.HOME);
                }, MemoryModuleType.HOME, false, Optional.of((byte) 14))), Pair.of(10, new BehaviorFindPosition((holder) -> {
                    return holder.is(PoiTypes.MEETING);
                }, MemoryModuleType.MEETING_POINT, true, Optional.of((byte) 14))), Pair.of(10, new BehaviorCareer()), Pair.of(10, new BehaviorProfession())});
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityVillager>>> getWorkPackage(VillagerProfession villagerprofession, float f) {
        Object object;

        if (villagerprofession == VillagerProfession.FARMER) {
            object = new BehaviorWorkComposter();
        } else {
            object = new BehaviorWork();
        }

        return ImmutableList.of(getMinimalLookBehavior(), Pair.of(5, new BehaviorGateSingle<>(ImmutableList.of(Pair.of(object, 7), Pair.of(new BehaviorStrollPosition(MemoryModuleType.JOB_SITE, 0.4F, 4), 2), Pair.of(new BehaviorStrollPlace(MemoryModuleType.JOB_SITE, 0.4F, 1, 10), 5), Pair.of(new BehaviorStrollPlaceList(MemoryModuleType.SECONDARY_JOB_SITE, f, 1, 6, MemoryModuleType.JOB_SITE), 5), Pair.of(new BehaviorFarm(), villagerprofession == VillagerProfession.FARMER ? 2 : 5), Pair.of(new BehaviorBonemeal(), villagerprofession == VillagerProfession.FARMER ? 4 : 7)))), Pair.of(10, new BehaviorTradePlayer(400, 1600)), Pair.of(10, new BehaviorLookInteract(EntityTypes.PLAYER, 4)), Pair.of(2, new BehaviorWalkAwayBlock(MemoryModuleType.JOB_SITE, f, 9, 100, 1200)), Pair.of(3, new BehaviorVillageHeroGift(100)), Pair.of(99, new BehaviorSchedule()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityVillager>>> getPlayPackage(float f) {
        return ImmutableList.of(Pair.of(0, new BehavorMove(80, 120)), getFullLookBehavior(), Pair.of(5, new BehaviorPlay()), Pair.of(5, new BehaviorGateSingle<>(ImmutableMap.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES, MemoryStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(BehaviorInteract.of(EntityTypes.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 2), Pair.of(BehaviorInteract.of(EntityTypes.CAT, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 1), Pair.of(new BehaviorStrollRandom(f), 1), Pair.of(new BehaviorLookWalk(f, 2), 1), Pair.of(new BehaviorBedJump(f), 2), Pair.of(new BehaviorNop(20, 40), 2)))), Pair.of(99, new BehaviorSchedule()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityVillager>>> getRestPackage(VillagerProfession villagerprofession, float f) {
        return ImmutableList.of(Pair.of(2, new BehaviorWalkAwayBlock(MemoryModuleType.HOME, f, 1, 150, 1200)), Pair.of(3, new BehaviorPositionValidate((holder) -> {
            return holder.is(PoiTypes.HOME);
        }, MemoryModuleType.HOME)), Pair.of(3, new BehaviorSleep()), Pair.of(5, new BehaviorGateSingle<>(ImmutableMap.of(MemoryModuleType.HOME, MemoryStatus.VALUE_ABSENT), ImmutableList.of(Pair.of(new BehaviorWalkHome(f), 1), Pair.of(new BehaviorStrollInside(f), 4), Pair.of(new BehaviorNearestVillage(f, 4), 2), Pair.of(new BehaviorNop(20, 40), 2)))), getMinimalLookBehavior(), Pair.of(99, new BehaviorSchedule()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityVillager>>> getMeetPackage(VillagerProfession villagerprofession, float f) {
        return ImmutableList.of(Pair.of(2, new BehaviorGateSingle<>(ImmutableList.of(Pair.of(new BehaviorStrollPosition(MemoryModuleType.MEETING_POINT, 0.4F, 40), 2), Pair.of(new BehaviorBell(), 2)))), Pair.of(10, new BehaviorTradePlayer(400, 1600)), Pair.of(10, new BehaviorLookInteract(EntityTypes.PLAYER, 4)), Pair.of(2, new BehaviorWalkAwayBlock(MemoryModuleType.MEETING_POINT, f, 6, 100, 200)), Pair.of(3, new BehaviorVillageHeroGift(100)), Pair.of(3, new BehaviorPositionValidate((holder) -> {
            return holder.is(PoiTypes.MEETING);
        }, MemoryModuleType.MEETING_POINT)), Pair.of(3, new BehaviorGate<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), BehaviorGate.Order.ORDERED, BehaviorGate.Execution.RUN_ONE, ImmutableList.of(Pair.of(new BehaviorTradeVillager(), 1)))), getFullLookBehavior(), Pair.of(99, new BehaviorSchedule()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityVillager>>> getIdlePackage(VillagerProfession villagerprofession, float f) {
        return ImmutableList.of(Pair.of(2, new BehaviorGateSingle<>(ImmutableList.of(Pair.of(BehaviorInteract.of(EntityTypes.VILLAGER, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 2), Pair.of(new BehaviorInteract<>(EntityTypes.VILLAGER, 8, EntityAgeable::canBreed, EntityAgeable::canBreed, MemoryModuleType.BREED_TARGET, f, 2), 1), Pair.of(BehaviorInteract.of(EntityTypes.CAT, 8, MemoryModuleType.INTERACTION_TARGET, f, 2), 1), Pair.of(new BehaviorStrollRandom(f), 1), Pair.of(new BehaviorLookWalk(f, 2), 1), Pair.of(new BehaviorBedJump(f), 1), Pair.of(new BehaviorNop(30, 60), 1)))), Pair.of(3, new BehaviorVillageHeroGift(100)), Pair.of(3, new BehaviorLookInteract(EntityTypes.PLAYER, 4)), Pair.of(3, new BehaviorTradePlayer(400, 1600)), Pair.of(3, new BehaviorGate<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.INTERACTION_TARGET), BehaviorGate.Order.ORDERED, BehaviorGate.Execution.RUN_ONE, ImmutableList.of(Pair.of(new BehaviorTradeVillager(), 1)))), Pair.of(3, new BehaviorGate<>(ImmutableMap.of(), ImmutableSet.of(MemoryModuleType.BREED_TARGET), BehaviorGate.Order.ORDERED, BehaviorGate.Execution.RUN_ONE, ImmutableList.of(Pair.of(new BehaviorMakeLove(), 1)))), getFullLookBehavior(), Pair.of(99, new BehaviorSchedule()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityVillager>>> getPanicPackage(VillagerProfession villagerprofession, float f) {
        float f1 = f * 1.5F;

        return ImmutableList.of(Pair.of(0, new BehaviorCooldown()), Pair.of(1, BehaviorWalkAway.entity(MemoryModuleType.NEAREST_HOSTILE, f1, 6, false)), Pair.of(1, BehaviorWalkAway.entity(MemoryModuleType.HURT_BY_ENTITY, f1, 6, false)), Pair.of(3, new BehaviorStrollRandom(f1, 2, 2)), getMinimalLookBehavior());
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityVillager>>> getPreRaidPackage(VillagerProfession villagerprofession, float f) {
        return ImmutableList.of(Pair.of(0, new BehaviorBellRing()), Pair.of(0, new BehaviorGateSingle<>(ImmutableList.of(Pair.of(new BehaviorWalkAwayBlock(MemoryModuleType.MEETING_POINT, f * 1.5F, 2, 150, 200), 6), Pair.of(new BehaviorStrollRandom(f * 1.5F), 2)))), getMinimalLookBehavior(), Pair.of(99, new BehaviorRaidReset()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityVillager>>> getRaidPackage(VillagerProfession villagerprofession, float f) {
        return ImmutableList.of(Pair.of(0, new BehaviorGateSingle<>(ImmutableList.of(Pair.of(new BehaviorOutsideCelebrate(f), 5), Pair.of(new BehaviorVictory(f * 1.1F), 2)))), Pair.of(0, new BehaviorCelebrate(600, 600)), Pair.of(2, new BehaviorHomeRaid(24, f * 1.4F)), getMinimalLookBehavior(), Pair.of(99, new BehaviorRaidReset()));
    }

    public static ImmutableList<Pair<Integer, ? extends Behavior<? super EntityVillager>>> getHidePackage(VillagerProfession villagerprofession, float f) {
        boolean flag = true;

        return ImmutableList.of(Pair.of(0, new BehaviorHide(15, 3)), Pair.of(1, new BehaviorHome(32, f * 1.25F, 2)), getMinimalLookBehavior());
    }

    private static Pair<Integer, Behavior<EntityLiving>> getFullLookBehavior() {
        return Pair.of(5, new BehaviorGateSingle<>(ImmutableList.of(Pair.of(new BehaviorLookTarget(EntityTypes.CAT, 8.0F), 8), Pair.of(new BehaviorLookTarget(EntityTypes.VILLAGER, 8.0F), 2), Pair.of(new BehaviorLookTarget(EntityTypes.PLAYER, 8.0F), 2), Pair.of(new BehaviorLookTarget(EnumCreatureType.CREATURE, 8.0F), 1), Pair.of(new BehaviorLookTarget(EnumCreatureType.WATER_CREATURE, 8.0F), 1), Pair.of(new BehaviorLookTarget(EnumCreatureType.AXOLOTLS, 8.0F), 1), Pair.of(new BehaviorLookTarget(EnumCreatureType.UNDERGROUND_WATER_CREATURE, 8.0F), 1), Pair.of(new BehaviorLookTarget(EnumCreatureType.WATER_AMBIENT, 8.0F), 1), Pair.of(new BehaviorLookTarget(EnumCreatureType.MONSTER, 8.0F), 1), Pair.of(new BehaviorNop(30, 60), 2))));
    }

    private static Pair<Integer, Behavior<EntityLiving>> getMinimalLookBehavior() {
        return Pair.of(5, new BehaviorGateSingle<>(ImmutableList.of(Pair.of(new BehaviorLookTarget(EntityTypes.VILLAGER, 8.0F), 2), Pair.of(new BehaviorLookTarget(EntityTypes.PLAYER, 8.0F), 2), Pair.of(new BehaviorNop(30, 60), 8))));
    }
}
